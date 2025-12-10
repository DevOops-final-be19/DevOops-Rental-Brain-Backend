package com.devoops.rentalbrain.employee.command.service;

import com.devoops.rentalbrain.employee.command.dto.LogoutDTO;
import com.devoops.rentalbrain.employee.command.dto.SignUpDTO;
import com.devoops.rentalbrain.employee.command.dto.UserDetailInfoDTO;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import com.devoops.rentalbrain.employee.command.repository.EmployeeCommandRepository;
import com.devoops.rentalbrain.employee.query.service.EmployeeQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeCommandServiceImpl implements EmployeeCommandService {
    private final EmployeeCommandRepository employeeCommandRepository;
    private final EmployeeQueryService employeeQueryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final Environment env;

    public EmployeeCommandServiceImpl(EmployeeCommandRepository employeeCommandRepository,
                                      EmployeeQueryService employeeQueryService,
                                      RedisTemplate<String, String> redisTemplate,
                                      Environment env) {
        this.employeeCommandRepository = employeeCommandRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.employeeQueryService = employeeQueryService;
        this.redisTemplate = redisTemplate;
        this.env = env;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeCommandRepository.findByEmpId(username);

        // 존재하지 않는 아이디
        if (employee == null) throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");

        // 정지된 회원
//        if (member.getUser_state() == 'S') throw new LockedException("정지된 회원입니다.");

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
}
