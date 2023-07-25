package com.roadmaker.member.authentication;

import com.roadmaker.member.dto.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final Key key;
    private final long TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    //access 토큰, refresh 토큰 생성
    /* generateToken 메소드는 인증 객체를 기반으로 JWT 토큰을 생성
     이 메소드는 인증 객체에서 사용자의 이름과 권한 목록을 가져와 JWT 토큰의 payload 에 저장
      또한, 토큰의 만료 시간을 설정하고, 서명 알고리즘으로 HS256을 사용하여 토큰에 서명합니다 */
    public TokenInfo generateToken(Authentication authentication) {
        //권한 가지고 오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        //access token 생성
        Date accessTokenExpiresIn = new Date(now + TOKEN_VALID_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();

        //refresh token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    // 복호화된 정보 꺼내기
    /* getAuthentication 메소드는 전달된 JWT 토큰에서 인증 객체를 가져온다.
    이 메소드는 토큰을 복호화하여 클레임을 가져오고, 클레임에서 사용자의 이름과 권한 목록을 추출하고
    이 정보를 기반으로 UserDetails 객체를 생성하고, 이 객체를 사용하여 Authentication 객체를 반환 */
    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰");
        }

        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetail 객체를 만들어 authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /* 메소드는 전달된 JWT 토큰의 유효성을 검사한다.
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token) 코드를 사용하여 토큰을 파싱하고,
    이 과정에서 발생하는 예외를 처리하여 토큰의 유효성을 검사한다. */
    public boolean validationToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key)
                    .build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
