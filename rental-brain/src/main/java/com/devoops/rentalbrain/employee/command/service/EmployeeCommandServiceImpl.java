package com.devoops.rentalbrain.employee.command.service;

import com.devoops.rentalbrain.employee.command.dto.*;
import com.devoops.rentalbrain.employee.command.entity.EmpPositionAuth;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import com.devoops.rentalbrain.employee.command.entity.LoginHistory;
import com.devoops.rentalbrain.employee.command.repository.EmpPositionAuthCommandRepository;
import com.devoops.rentalbrain.employee.command.repository.EmployeeCommandRepository;
import com.devoops.rentalbrain.employee.command.repository.LoginHistoryCommandRepository;
import com.devoops.rentalbrain.employee.query.service.EmployeeQueryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeCommandServiceImpl implements EmployeeCommandService {
    private final EmployeeCommandRepository employeeCommandRepository;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final Environment env;
    private final EmpPositionAuthCommandRepository empPositionAuthCommandRepository;
    private final LoginHistoryCommandRepository loginHistoryCommandRepository;

    public EmployeeCommandServiceImpl(EmployeeCommandRepository employeeCommandRepository,
                                      EmployeeQueryServiceImpl employeeQueryServiceImpl,
                                      RedisTemplate<String, String> redisTemplate,
                                      Environment env,
                                      EmpPositionAuthCommandRepository empPositionAuthCommandRepository,
                                      LoginHistoryCommandRepository loginHistoryCommandRepository) {
        this.employeeCommandRepository = employeeCommandRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.employeeQueryService = employeeQueryServiceImpl;
        this.redisTemplate = redisTemplate;
        this.env = env;
        this.empPositionAuthCommandRepository = empPositionAuthCommandRepository;
        this.loginHistoryCommandRepository = loginHistoryCommandRepository;
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
        List<GrantedAuthority> grantedAuthorities = employeeQueryService.getUserAuth(employee.getId(), employee.getPositionId());


        // 커스텀한 User 객체 이용
        UserImpl userImpl = new UserImpl(employee.getEmpId(), employee.getPwd(), grantedAuthorities);
        userImpl.setUserInfo(new UserDetailInfoDTO(
                        employee.getId(),
                        employee.getEmpId(),
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
        Employee employee = new Employee();
        employee.setEmpId(signUpDTO.getEmpId());
        employee.setPwd(bCryptPasswordEncoder.encode(signUpDTO.getPwd()));
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
        log.info("회원가입 사원 정보: {}",employee);
        employeeCommandRepository.save(employee);
    }

    @Override
    public void logout(LogoutDTO logoutDTO,String token) {
        token = token.substring(7);
        try {
            redisTemplate.opsForValue().set("BL:" + token, logoutDTO.getEmpId(), Long.parseLong(env.getProperty("token.access_expiration_time")), TimeUnit.MILLISECONDS);
            redisTemplate.delete("RT:" + logoutDTO.getEmpId());
            log.info("redis 저장완료");
        }
        catch (Exception e){
            log.info("redis 오류!");
        }
    }

    @Override
    @Transactional
    public void modifyAuth(List<EmpPositionAuthDTO> empPositionAuthDTO) {
        // db에 있는 직책별 권한 조회
        List<EmpPositionAuth> empPositionAuthList = empPositionAuthCommandRepository.findAll();
        Map<String, EmpPositionAuth> empPositionAuths = empPositionAuthList.stream()
                .collect(Collectors.toMap(v->v.getPositionId() + "-" + v.getAuthId(), v -> v));

        for(EmpPositionAuthDTO empPositionAuthElement : empPositionAuthDTO){
            if(empPositionAuths.get(empPositionAuthElement.getPositionId() + "-" + empPositionAuthElement.getAuthId()) == null) {
                // 존재하지 않는 positionId + authId 일 때
                EmpPositionAuth empPositionAuth = new EmpPositionAuth();

                empPositionAuth.setPositionId(empPositionAuthElement.getPositionId());
                empPositionAuth.setAuthId(empPositionAuthElement.getAuthId());
                empPositionAuthCommandRepository.save(empPositionAuth);
            }else{
                // 이미 존재하는 positionId + authId 일 때
                empPositionAuths.remove(empPositionAuthElement.getPositionId() + "-" + empPositionAuthElement.getAuthId());
            }
        }

        // 남아있는 positionId + authId는 db 에서 삭제
        empPositionAuthCommandRepository.deleteAll(empPositionAuths.values());
        log.info("권한 수정 완료");
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
}
