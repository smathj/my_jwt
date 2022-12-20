package com.study.jwt.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    // Spring Security에서 제공하는 빈 주입
    private final UserDetailsService userDetailsService;




    @Value("${springboot.jwt.secret}")
    private String secretKey;

//    private final long tokenTime = 1000L * 60 * 60;   // 1시간

    @PostConstruct
    protected void init() {
        log.info("[init 메서드 실행] JwtTokenProvider에서 secretKey 초기화 시작 !!");
        System.out.println("[인코딩 전] secretKey = " + secretKey);

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));

        System.out.println("[인코딩 후] secretKey = " + secretKey);
        log.info("[init 메서드 실행] JwtTokenProvider에서 secretKey 초기화 종료 !!");
    }


    public String createToken(String userId, String role) {
        log.info("[createToken 메서드 실행] 토근 생성 시작");
        Claims claims = Jwts.claims().setSubject(userId);  // 등록된 클레임
        claims.put("role", role);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);    // 1분 뒤
        Date expireDate = cal.getTime();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + tokenTime))
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret 값 세팅
                .compact();

//        System.out.println("token = " + token);

        log.info("[createToken 메서드 실행] 토근 생성 종료");

        return token;

    }


    // * 토큰 체크
    public boolean validateToken(String token) {
        log.info("[토큰 유효성 검사] 시작");
        try {


        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);  // * <- 에러위치
        System.out.println("claims.getHeader() = " + claims.getHeader());
        System.out.println("claims.getBody() = " + claims.getBody());
        System.out.println("claims.getBody().get(\"role\") = " + claims.getBody().get("role"));
        System.out.println("claims.getBody().getExpiration() = " + claims.getBody().getExpiration());
        System.out.println("claims.getBody().getSubject() = " + claims.getBody().getSubject());
        System.out.println("claims.getSignature() = " + claims.getSignature());
        System.out.println("claims.getClass() = " + claims.getClass());

        log.info("[토큰 유효성 검사] 종료");

        System.out.println("claims.getBody().getExpiration().after(new Date()) = " + claims.getBody().getExpiration().after(new Date()));

        return claims.getBody().getExpiration().after(new Date());
        } catch(Exception e) {
            e.printStackTrace();
            log.info("[토큰 유효성 검사] 토큰 만료");
            return false;
        }

    }

    // ! JWT 토큰에서 회원 구별 정보 추출
    public String getUsername(String token) {
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    // ! 토큰을 이용한 정보 조회
    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));   // id 꺼내옴, subject에 id넣었음
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}",
                userDetails.getUsername());

        // 시큐리티 컨텍스트 홀더에 넣기위해서
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        System.out.println("authToken.getPrincipal() = " + authToken.getPrincipal());
        System.out.println("authToken.getName() = " + authToken.getName());
        System.out.println("authToken.getDetails() = " + authToken.getDetails());
        System.out.println("authToken.getCredentials() = " + authToken.getCredentials());

        return authToken;
    }

    // ! HTTP Request Header 에 설정된 토큰 값을 가져옴
    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        log.info(request.getHeader("X-AUTH-TOKEN"));
        return request.getHeader("X-AUTH-TOKEN");
    }
}
