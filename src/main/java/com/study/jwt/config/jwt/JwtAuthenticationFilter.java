package com.study.jwt.config.jwt;//package com.forward.payment.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// * JWT 인증 필터
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(servletRequest);   // 헤더에서 토큰 추출
        log.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

        log.info("[doFilterInternal] token 값 유효성 체크 시작");
        if (token != null && jwtTokenProvider.validateToken(token)) {   // 토큰 유효성 체크
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);  // 토큰정보로 회원정보 조회

            log.info("시큐리티 컨텍스트에 담습니다");
//            SecurityContextHolder.getContext().setAuthentication(authentication);   // 시큐리티 컨텍스트에 저장

            // 로그인 한 사람 아이디 꺼낼떄는 이걸 씀
            // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            log.info("[doFilterInternal] token 값 유효성 체크 완료");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
