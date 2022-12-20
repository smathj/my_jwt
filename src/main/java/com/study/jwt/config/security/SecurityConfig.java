package com.study.jwt.config.security;

import com.study.jwt.config.jwt.JwtAuthenticationFilter;
import com.study.jwt.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final UserDetailsService userDetailsService;

    @Bean
    public JwtTokenProvider jwtTokenProvider(UserDetailsService userDetailsService) {
//        return new JwtTokenProvider(this.userDetailsService);
        return new JwtTokenProvider(userDetailsService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtTokenProvider(userDetailsService));
    }



    // * Bean
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {


        http
            .httpBasic().disable()
            .csrf().disable()
                .addFilterBefore(jwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
            ;

        return http.build();
    }










}
