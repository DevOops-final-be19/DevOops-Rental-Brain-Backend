package com.devoops.rentalbrain.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@Slf4j
public class RefreshAuthController {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final Environment env;

    RefreshAuthController(JwtUtil jwtUtil,
                          RedisTemplate<String, String> redisTemplate,
                          Environment env) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.env = env;
    }

    @PostMapping("/validate")
    private ResponseEntity<?> refreshTokenValidate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = request.getHeader("Authorization");
//        String refreshToken = request.getHeader("Refresh-Token");

        // 쿠키 추출
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("No cookies");
        }

        // 리프레쉬 토큰 추출
        Cookie refreshCookie = Arrays.stream(cookies)
                .filter(c -> {
                    log.info(c.getName());
                    return "refreshToken".equals(c.getName());
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No refresh token"));
        String refreshToken = refreshCookie.getValue();


        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            log.info("엑세스 토큰이 없습니다.");

            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("엑세스 토큰이 없습니다.");
        }
        if (refreshToken == null) {
            log.info("리프레쉬 토큰이 없습니다.");

            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("리프레쉬 토큰이 없습니다.");
        }

        accessToken = accessToken.substring(7);
        String sub = null;

        // 리프레쉬 토큰 검증
        try {
            sub = jwtUtil.validateRefreshToken(refreshToken);
        } catch (ExpiredJwtException e) {
            log.info("만료된 리프레쉬 토큰입니다.");
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("만료된 리프레쉬 토큰입니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String storedAccessToken = (String) redisTemplate.opsForHash().get("RT:" + sub, "Access-Token");
        String storedRefreshToken = (String) redisTemplate.opsForHash().get("RT:" + sub, "Refresh-Token");

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken) || !storedAccessToken.equals(accessToken)) {
            log.info("검증되지 않은 리프레쉬 토큰입니다.");
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("검증되지 않은 리프레쉬 토큰입니다.");
        }

        String newAccessToken = jwtUtil.getNewAccessToken(accessToken, sub);
        String newRefreshToken = jwtUtil.getNewRefreshToken(sub);

        try {
            // 리프레쉬 토큰 쿠키 저장
            Cookie cookie = new Cookie("refreshToken", newRefreshToken);
            cookie.setValue(newRefreshToken);
            cookie.setHttpOnly(true);
//            cookie.setSecure(true);   // https 일때만 전송
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);

            response.addCookie(cookie);
            // BL지정
            redisTemplate.opsForValue().set("BL:" + accessToken, sub, Long.parseLong(env.getProperty("token.access_expiration_time")), TimeUnit.MILLISECONDS);
            // redis 저장
            redisTemplate.opsForHash().putAll("RT:" + sub, Map.of(
                    "Refresh-Token", newRefreshToken,
                    "Access-Token", newAccessToken));
            redisTemplate.expire("RT:" + sub, Long.parseLong(env.getProperty("token.refresh_expiration_time")), TimeUnit.MILLISECONDS);
            log.info("redis 저장 완료");
        } catch (Exception e) {
            log.info("redis 오류!");
        }

        return ResponseEntity.ok().body(Map.of("accessToken", newAccessToken));
    }

}
