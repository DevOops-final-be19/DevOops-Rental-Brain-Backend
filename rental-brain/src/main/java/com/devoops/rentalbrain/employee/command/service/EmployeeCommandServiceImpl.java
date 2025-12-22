package com.devoops.rentalbrain.employee.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.employee.command.dto.*;
import com.devoops.rentalbrain.employee.command.entity.EmpPosition;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import com.devoops.rentalbrain.employee.command.entity.EmployeeAuth;
import com.devoops.rentalbrain.employee.command.entity.LoginHistory;
import com.devoops.rentalbrain.employee.command.repository.EmpPositionCommandRepository;
import com.devoops.rentalbrain.employee.command.repository.EmployeeAuthCommandRepository;
import com.devoops.rentalbrain.employee.command.repository.EmployeeCommandRepository;
import com.devoops.rentalbrain.employee.command.repository.LoginHistoryCommandRepository;
import com.devoops.rentalbrain.employee.query.service.EmployeeQueryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@Slf4j
public class EmployeeCommandServiceImpl implements EmployeeCommandService {
    private final EmployeeCommandRepository employeeCommandRepository;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final Environment env;
    private final EmployeeAuthCommandRepository employeeAuthCommandRepository;
    private final LoginHistoryCommandRepository loginHistoryCommandRepository;
    private final EmpPositionCommandRepository empPositionCommandRepository;
    private final CodeGenerator codeGenerator;

    public EmployeeCommandServiceImpl(EmployeeCommandRepository employeeCommandRepository,
                                      EmployeeQueryServiceImpl employeeQueryServiceImpl,
                                      RedisTemplate<String, String> redisTemplate,
                                      Environment env,
                                      EmployeeAuthCommandRepository employeeAuthCommandRepository,
                                      LoginHistoryCommandRepository loginHistoryCommandRepository,
                                      EmpPositionCommandRepository empPositionCommandRepository,
                                      CodeGenerator codeGenerator) {
        this.employeeCommandRepository = employeeCommandRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.employeeQueryService = employeeQueryServiceImpl;
        this.redisTemplate = redisTemplate;
        this.env = env;
        this.loginHistoryCommandRepository = loginHistoryCommandRepository;
        this.employeeAuthCommandRepository = employeeAuthCommandRepository;
        this.empPositionCommandRepository = empPositionCommandRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeCommandRepository.findByEmpId(username);

        // 존재하지 않는 아이디
        if (employee == null) throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");

        // 정지된 회원
        if (employee.getStatus() == 'Q') throw new LockedException("퇴사한 직원입니다.");

        // 5회 이상 로그인 시도


        // 회원 권한 꺼내기
        List<GrantedAuthority> grantedAuthorities = employeeQueryService.getUserAuth(employee.getId());


        // 커스텀한 User 객체 이용(원래는 안티 패턴)
        UserImpl userImpl = new UserImpl(employee.getEmpId(), employee.getPwd(), grantedAuthorities);
        userImpl.setUserInfo(new UserDetailInfoDTO(
                        employee.getId(),
                        employee.getEmployeeCode(),
                        employee.getEmpId(),
                        employee.getName(),
                        employee.getPhone(),
                        employee.getEmail(),
                        employee.getAddr(),
                        employee.getBirthday(),
                        employee.getGender(),
                        employee.getStatus(),
                        employee.getDept(),
                        employee.getHireDate(),
                        employee.getResignDate(),
                        employee.getPositionId()
                )
        );

        return userImpl;
        // 사용자의 id,pw,권한,하위 정보들을 provider로 전송
//        return new User(employee.getEmpId(), employee.getPwd(), true, true, true, true, grantedAuthorities);
    }

    @Override
    @Transactional
    public void signup(SignUpDTO signUpDTO) {
        if(employeeCommandRepository.existsByEmpIdOrEmail(signUpDTO.getEmpId(),signUpDTO.getEmail())){
            throw new RuntimeException("이미 존재하는 아이디 혹은 이메일 번호 입니다.");
        }
        Employee employee = new Employee();
        employee.setEmployeeCode(codeGenerator.generate(CodeType.EMPLOYEE));
        employee.setEmpId(signUpDTO.getEmpId());
        employee.setPwd(bCryptPasswordEncoder.encode(signUpDTO.getPwd()));
        employee.setName(signUpDTO.getName());
        employee.setPhone(signUpDTO.getPhone());
        employee.setEmail(signUpDTO.getEmail());
        employee.setAddr(signUpDTO.getAddr());
        employee.setBirthday(signUpDTO.getBirthday());
        employee.setGender(signUpDTO.getGender());
        employee.setStatus('W');
        employee.setDept(signUpDTO.getDept());
        employee.setHireDate(signUpDTO.getHireDate());
        employee.setPositionId(signUpDTO.getPositionId());
//        LocalDateTime now = LocalDateTime.now();
//        employee.setSign_up_date(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        log.info("회원가입 사원 정보: {}", employee);
        employeeCommandRepository.save(employee);
    }

    @Override
    public void logout(LogoutDTO logoutDTO, String token) {
        token = token.substring(7);
        try {
            redisTemplate.opsForValue().set("BL:" + token, logoutDTO.getEmpId(), Long.parseLong(env.getProperty("token.access_expiration_time")), TimeUnit.MILLISECONDS);
            redisTemplate.delete("RT:" + logoutDTO.getEmpId());
            log.info("redis 저장,로그아웃 완료");
        } catch (Exception e) {
            log.info("오류 - {}",e.getMessage());
        }
    }

    @Override
    @Transactional
    public void modifyAuth(EmployeeAuthDTO employeeAuthDTO) {
        // db에 있는 현재 요청 보낸 사용자 직책 조회
        UserImpl user = (UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getId() == employeeAuthDTO.getEmp_id()){
            throw new RuntimeException("자기 자신의 권한 수정은 불가능합니다.");
        }
        List<EmployeeAuth> verifyAuthList = employeeAuthCommandRepository.findByEmpId(user.getId());
        if(verifyAuthList.stream().noneMatch(auth -> auth.getAuthId()==26L)){
            throw new RuntimeException("관리자 외 사용자의 권한 수정은 불가능합니다.");
        }

        // db에 있는 직책별 권한 조회
        List<EmployeeAuth> employeeAuthList = employeeAuthCommandRepository.findByEmpId(employeeAuthDTO.getEmp_id());
        Map<Long, EmployeeAuth> employeeAuthMap = employeeAuthList.stream()
                .collect(Collectors.toMap(EmployeeAuth::getAuthId, EmployeeAuth->EmployeeAuth));

        // 수정된 권한이 있으면 저장
        for (Long authId : employeeAuthDTO.getAuth_id()) {
            if (employeeAuthMap.get(authId) == null) {
                EmployeeAuth employeeAuth = new EmployeeAuth();
                employeeAuth.setAuthId(authId);
                employeeAuth.setEmpId(employeeAuthDTO.getEmp_id());
                employeeAuthCommandRepository.save(employeeAuth);
            } else {
                employeeAuthMap.remove(authId);
            }
        }

        // 수정되지 않고 남아있는 권한은 삭제
        for (Map.Entry<Long, EmployeeAuth> entry : employeeAuthMap.entrySet()) {
            employeeAuthCommandRepository.delete(entry.getValue());
        }
    }

    @Override
    @Transactional
    public void saveLoginHistory(Long id, String ipAddress, char y) {
        LoginHistory loginHistory = new LoginHistory();
        LocalDateTime now = LocalDateTime.now();
        loginHistory.setLoginSuccessDate(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        loginHistory.setLoginIsSucceed(y);
        loginHistory.setLoginIp(ipAddress);
        loginHistory.setEmpId(id);
        loginHistoryCommandRepository.save(loginHistory);
    }

    @Override
    @Transactional
    public void modifyEmpInfo(EmployeeInfoModifyDTO employeeInfoModifyDTO) {
        UserImpl user = (UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getEmpId().equals(employeeInfoModifyDTO.getEmpId())) {
            throw new NullPointerException("잘못된 접근입니다.");
        }
        Employee employee = employeeCommandRepository.findByEmpId(employeeInfoModifyDTO.getEmpId());

        if(employeeCommandRepository.existsByEmail(employeeInfoModifyDTO.getEmail())) {
            if(!employee.getEmpId().equals(employeeInfoModifyDTO.getEmpId())) {
                throw new RuntimeException("이미 존재하는 이메일입니다.");
            }
        }
        modifyInfo(employee,employeeInfoModifyDTO);
    }

    @Override
    @Transactional
    public void modifyEmpInfoByAdmin(EmployeeInfoModifyByAdminDTO employeeInfoModifyByAdminDTO) {
        UserImpl user = (UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EmployeeAuth> verifyAuthList = employeeAuthCommandRepository.findByEmpId(user.getId());
        if(verifyAuthList.stream().noneMatch(auth -> auth.getAuthId()==26L)){
            throw new RuntimeException("관리자 외 사용자의 권한 수정은 불가능합니다.");
        }

        Employee employee = employeeCommandRepository.findByEmpId(employeeInfoModifyByAdminDTO.getEmpId());
        if(employeeCommandRepository.existsByEmail(employeeInfoModifyByAdminDTO.getEmail())) {
            if(!employeeInfoModifyByAdminDTO.getEmail().equals(employee.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
            }
        }
        modifyInfoByAdmin(employee,employeeInfoModifyByAdminDTO);
    }

    @Override
    @Transactional
    public void modifyEmpPwd(EmployeePasswordModifyDTO employeePasswordModifyDTO) {
        UserImpl user = (UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.getEmpId().equals(employeePasswordModifyDTO.getEmpId())) {
            throw new RuntimeException("잘못된 접근입니다.");
        }

        Employee employee = employeeCommandRepository.findByEmpId(employeePasswordModifyDTO.getEmpId());

        if(!bCryptPasswordEncoder.matches(employeePasswordModifyDTO.getPwd(),employee.getPwd())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        employee.setPwd(bCryptPasswordEncoder.encode(employeePasswordModifyDTO.getModifiedPwd()));
    }

    private void modifyInfoByAdmin(Employee employee, EmployeeInfoModifyByAdminDTO employeeInfoModifyByAdminDTO) {
        if(!bCryptPasswordEncoder.encode(employeeInfoModifyByAdminDTO.getPwd()).equals(employee.getPwd())) {
            employee.setPwd(bCryptPasswordEncoder.encode(employeeInfoModifyByAdminDTO.getPwd()));
        }
        if(!employeeInfoModifyByAdminDTO.getName().equals(employee.getName())) {
            employee.setName(employeeInfoModifyByAdminDTO.getName());
        }
        if(!employeeInfoModifyByAdminDTO.getPhone().equals(employee.getPhone())) {
            employee.setPhone(employeeInfoModifyByAdminDTO.getPhone());
        }
        if(!employeeInfoModifyByAdminDTO.getEmail().equals(employee.getEmail())) {
            employee.setEmail(employeeInfoModifyByAdminDTO.getEmail());
        }
        if(!employeeInfoModifyByAdminDTO.getAddr().equals(employee.getAddr())) {
            employee.setAddr(employeeInfoModifyByAdminDTO.getAddr());
        }
        if(!employeeInfoModifyByAdminDTO.getBirthday().equals(employee.getBirthday())) {
            employee.setBirthday(employeeInfoModifyByAdminDTO.getBirthday());
        }
        if(!employeeInfoModifyByAdminDTO.getGender().equals(employee.getGender())) {
            employee.setGender(employeeInfoModifyByAdminDTO.getGender());
        }
        if(!employeeInfoModifyByAdminDTO.getStatus().equals(employee.getStatus())) {
            employee.setStatus(employeeInfoModifyByAdminDTO.getStatus());
        }
        if(!employeeInfoModifyByAdminDTO.getDept().equals(employee.getDept())) {
            employee.setDept(employeeInfoModifyByAdminDTO.getDept());
        }
        if(!employeeInfoModifyByAdminDTO.getHireDate().equals(employee.getHireDate())) {
            employee.setHireDate(employeeInfoModifyByAdminDTO.getHireDate());
        }
        if(employeeInfoModifyByAdminDTO.getResignDate()!=null&&!employeeInfoModifyByAdminDTO.getResignDate().equals(employee.getResignDate())) {
            employee.setResignDate(employeeInfoModifyByAdminDTO.getResignDate());
        }
        if(!employeeInfoModifyByAdminDTO.getPositionId().equals(employee.getPositionId())) {
            employee.setPositionId(employeeInfoModifyByAdminDTO.getPositionId());
        }
    }

    private void modifyInfo(Employee employee, EmployeeInfoModifyDTO employeeInfoModifyDTO) {
        if(!employeeInfoModifyDTO.getName().equals(employee.getName())) {
            employee.setName(employeeInfoModifyDTO.getName());
        }
        if(!employeeInfoModifyDTO.getPhone().equals(employee.getPhone())) {
            employee.setPhone(employeeInfoModifyDTO.getPhone());
        }
        if(!employeeInfoModifyDTO.getEmail().equals(employee.getEmail())) {
            employee.setEmail(employeeInfoModifyDTO.getEmail());
        }
        if(!employeeInfoModifyDTO.getAddr().equals(employee.getAddr())) {
            employee.setAddr(employeeInfoModifyDTO.getAddr());
        }
    }
}
