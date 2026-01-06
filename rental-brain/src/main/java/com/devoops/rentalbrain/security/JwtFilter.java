package com.devoops.rentalbrain.security;

import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtFilter(JwtUtil jwtUtil,
                     RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        //  JWT 검사 제외 경로
        if (
                path.equals("/login") ||
                path.equals("/auth/validate") ||
                path.equals("/health") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/api-docs") ||
                path.startsWith("/v3/api-docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 Authorization Token 추출
        String authorizationHeader = request.getHeader("Authorization");
        log.info("source - {}",getClientIp(request));
        // 토큰 존재하는지 검사
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("토큰 인증 실패");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(7);

        // 토큰 유효성 검사
        try {
            jwtUtil.validateToken(accessToken);

            // 토큰에 있는 권한 추출
            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            // 토큰 서버측 검사(redis)
            try {
                if (redisTemplate.opsForValue().get("BL:" + accessToken) != null) {
                    log.info("유효하지 않은 JWT Token(BL)");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                log.info("오류, {}", e.getMessage());
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserImpl user = (UserImpl) authentication.getPrincipal();
            log.info("user - {}",user.getUsername());

        } catch (ExpiredJwtException e) {
            log.info("세션 만료");

            // 프론트로 엑세스 토큰 만료 메세지 전송
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"access token expired\"}");
            return;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("유효하지 않은 JWT Token(값이 없음)");
            return;
        } catch (UnsupportedJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("지원하지 않는 JWT Token");
            return;
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("토큰의 클레임이 없음");
            return;
        }

        filterChain.doFilter(request, response);
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
