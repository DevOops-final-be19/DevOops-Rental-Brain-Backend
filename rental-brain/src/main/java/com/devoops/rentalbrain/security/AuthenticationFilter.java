package com.devoops.rentalbrain.security;


import com.devoops.rentalbrain.employee.command.dto.LoginDTO;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment env;
    private final EmployeeCommandService employeeCommandService;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Environment env,
                                EmployeeCommandService employeeCommandService,
                                RedisTemplate<String, String> redisTemplate) {
        // authenticationManager를 인지시킴
        super(authenticationManager);
        this.env = env;
        this.employeeCommandService = employeeCommandService;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDTO credential = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getEmpId(), credential.getPwd(), new ArrayList<>()));
        } catch (IOException e) {
            log.info("attemptAuthentication 에서 오류");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 성공한 사용자의 id
        String id = authResult.getName();
        log.info(id);

        // 사용자의 권한 목록
        List<String> roles = authResult.getAuthorities().stream()
//                .map(role->role.getAuthority())
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        log.info("List<String> 형태로 뽑아낸 로그인 한 회원의 권한들 : {}", roles);

        // JWT 토큰 발행
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("auth", roles);

        // 엑세스 토큰
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + Long.parseLong(env.getProperty(("token.access_expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.access_secret"))
                .compact();
        log.info("accessToken - {}",accessToken);

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        response.addHeader("access-token", accessToken);


        // 리프레쉬 토큰
        String refreshToken = Jwts.builder()
                .setSubject(id)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + Long.parseLong(env.getProperty(("token.refresh_expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.refresh_secret"))
                .compact();
        try {
            // HttpOnly 헤더
            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
//            cookie.setSecure(true);   // https 일때만 전송
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);

            response.addCookie(cookie);

            // redis 저장
            redisTemplate.opsForHash().putAll("RT:" + id, Map.of(
                    "Refresh-Token", refreshToken,
                    "Access-Token", accessToken));
            redisTemplate.expire("RT:" + id, Long.parseLong(env.getProperty("token.refresh_expiration_time")), TimeUnit.MILLISECONDS);

            log.info("redis 저장 완료");
        } catch (Exception e) {
            log.info("오류 - {}", e.getMessage());
        }


        // 성공 객체 반환
        UserImpl user = (UserImpl) authResult.getPrincipal();
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("success", "로그인 성공");
        responseBody.put("id", user.getId());
        responseBody.put("employeeCode", user.getEmployeeCode());
        responseBody.put("empId", user.getEmpId());
        responseBody.put("name", user.getName());
        responseBody.put("phone", user.getPhone());
        responseBody.put("email", user.getEmail());
        responseBody.put("addr", user.getAddr());
        responseBody.put("gender", user.getGender());
        responseBody.put("status", user.getStatus());
        responseBody.put("dept", user.getDept());
        responseBody.put("hireDate", user.getHireDate());
        responseBody.put("resignDate", user.getResignDate());
        responseBody.put("positionId", user.getPositionId());
        responseBody.put("roles", roles);
        responseBody.put("accessToken", accessToken);

        // Jackson으로 JSON 변환
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(responseBody);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        // 로그인 이력 저장
        String ipAddress = getClientIp(request);
        employeeCommandService.saveLoginHistory(user.getId(), ipAddress, 'Y');
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 인증 실패 : {}", failed.toString());
        // 인증 실패 응답
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        if (failed instanceof BadCredentialsException) {
            // 로그인 이력 저장
            response.getWriter().write("{\"error\": \"" + failed.getMessage().split(",")[0] + "\"}");
            Long id = Long.parseLong(failed.getMessage().split(",")[1]);
            String ipAddress = getClientIp(request);
            employeeCommandService.saveLoginHistory(id, ipAddress, 'N');      // 실패했을때 비밀번호 불일치 Exception 일 시 실패한 id 값을 이력에 저장
        } else {
            response.getWriter().write("{\"error\": \"" + failed.getMessage() + "\"}");
        }
//        super.unsuccessfulAuthentication(request, response, failed);

    }

    public static String getClientIp(HttpServletRequest request) throws UnknownHostException {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // fallback
        }

        // X-Forwarded-For는 콤마로 여러 IP가 들어올 수 있음 → 첫 번째가 클라이언트
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.getHostAddress();
        }

        return ip;
    }
}
