package com.devoops.rentalbrain.security;

import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurity {
    private Environment env;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtUtil jwtUtil;
    private final EmployeeCommandService employeeCommandService;
    private final RedisTemplate<String,String> redisTemplate;

    @Autowired
    public WebSecurity(Environment env,
                       JwtAuthenticationProvider jwtAuthenticationProvider,
                       JwtUtil jwtUtil,
                       EmployeeCommandService employeeCommandService,
                       RedisTemplate<String,String> redisTemplate) {
        this.env = env;     // JWT Token의 payload에 만료시간 만들다가 추가
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtUtil = jwtUtil;
        this.employeeCommandService = employeeCommandService;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        /* 설명. Spring Security 모듈 추가 후 default 로그인 페이지 제거 및 인가 설정 */
        http
                .cors(withDefaults())   // CORS 처리
                .csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(authz ->
                                   authz
                                           .requestMatchers("/**").permitAll()        // 개발시에는 주석 풀고 진행
                                        .requestMatchers(HttpMethod.GET, "/health").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/auth/validate").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/emp/admin/auth/modify").hasAuthority("ADMIN_MANAGE")
                                        .anyRequest()
                                        .authenticated()
                )
                /* 설명. Session 방식이 아닌 JWT Token 방식으로 인증된 회원(Authentication)을 Local Thread로 저장하겠다. */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // authenticationFilter를 추가하는 과정
        http.addFilter(new AuthenticationFilter(authenticationManager(),env,employeeCommandService,redisTemplate));

        // JwtFilter를 통한 토큰 검증 필터 추가
        http.addFilterBefore(new JwtFilter(jwtUtil,redisTemplate),UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
