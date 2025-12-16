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

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String,String> redisTemplate;

    public JwtFilter(JwtUtil jwtUtil,
                     RedisTemplate<String,String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals("/auth/validate")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 Authorization Token 추출
        String authorizationHeader = request.getHeader("Authorization");

        log.info("Authorization header: {}", authorizationHeader);

        // 토큰 존재하는지 검사
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            log.info("토큰 인증 실패");
            filterChain.doFilter(request,response);
            return;
        }

        String accessToken = authorizationHeader.substring(7);
        log.info("엑세스 토큰: {}", accessToken);

        // 토큰 유효성 검사
        try{
            jwtUtil.validateToken(accessToken);

            // 토큰에 있는 권한 추출
            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            log.info("authentication 내용 : {}",authentication.toString());
            // 토큰 서버측 검사(redis), 나중에 넣음
            try{
            if(redisTemplate.opsForValue().get("BL:"+accessToken) != null){
                log.info("유효하지 않은 JWT Token(BL)");
                filterChain.doFilter(request,response);
                return;
            }
            }catch (Exception e){
                log.info("오류, {}",e.getMessage());
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch(ExpiredJwtException e){
            log.info("세션 만료");

            // 프론트로 엑세스 토큰 만료 메세지 전송
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"access token expired\"}");
            return;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("유효하지 않은 JWT Token(값이 없음)");
        }  catch(UnsupportedJwtException e){
            log.info("지원하지 않는 JWT Token");
        } catch(IllegalArgumentException e){
            log.info("토큰의 클레임이 없음");
        }

        filterChain.doFilter(request,response);
    }
}
