package com.devoops.rentalbrain.security;

import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final Key refreshKey;
    private final Environment env;
    private final EmployeeCommandService employeeCommandService;

    public JwtUtil(@Value("${token.access_secret}")String key,
                   @Value("${token.refresh_secret}")String refreshToken,
                   EmployeeCommandService employeeCommandService,
                   Environment env){
        byte[] keyBytes = Decoders.BASE64.decode(key);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshToken);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.employeeCommandService = employeeCommandService;
        this.env = env;
    }

    public void validateToken(String token) throws io.jsonwebtoken.security.SecurityException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Authentication getAuthentication(String token) {
        // 토큰에 있는 claims 추출
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        // 토큰에 들어있던 emp_id로 유효성 검증
        UserDetails userDetails = employeeCommandService.loadUserByUsername(claims.getSubject());
//        log.info("userDetails: {}",userDetails.getUsername());
//        log.info("userDetails: {}",userDetails.getAuthorities());


        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String validateRefreshToken(String refreshToken) throws ExpiredJwtException,Exception{
        Claims claims = Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken).getBody();
        return claims.getSubject();
    }

    public String getNewAccessToken(String expiredToken, String id){
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("auth", getAuthoritiesFromExpiredToken(expiredToken));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + Long.parseLong(env.getProperty(("token.access_expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String getNewRefreshToken(String id){
        return Jwts.builder()
                .setSubject(id)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + Long.parseLong(env.getProperty(("token.refresh_expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, refreshKey)
                .compact();
    }

    public Claims getExpiredTokenClaims(String token) {
        try {
            // 정상 파싱 시도 (만료되면 예외 발생)
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 경우 여기서 claim 접근 가능
            return e.getClaims();
        }
    }

    public List<String> getAuthoritiesFromExpiredToken(String token) {
        Claims claims = getExpiredTokenClaims(token);
        return claims.get("roles", List.class);
    }


}
