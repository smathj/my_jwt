package com.study.jwt;

import com.study.jwt.config.jwt.JwtTokenProvider;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰 생성")
    public void createToken() {
        String userId = "김성준";
        String userPassword = "1234";
        String role = "admin";

        InnerUserDto userDto = new InnerUserDto(userId, userPassword, role);
        String token = jwtTokenProvider.createToken(userId, role);
        System.out.println("token = " + token);
    }

    @Test
    @DisplayName("토큰 검증")
    public void validateToken() {
        String userId = "김성준";
        String userPassword = "1234";
        String role = "admin";

        InnerUserDto userDto = new InnerUserDto(userId, userPassword, role);
        String token = jwtTokenProvider.createToken(userId, role);
        System.out.println("token = " + token);

        boolean result = jwtTokenProvider.validateToken(token);
//        boolean result = jwtTokenProvider.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3MzUxMmFjNi0xOGU0LTQ5MDItYWRmNi01NGM0ZTliMjZhYWQiLCJyb2xlIjoiYWRtaW4iLCJpYXQiOjE2NzE0Mjc1NTIsImV4cCI6MTY3MTQyNzYxMn0.ZL3xRxuDYt4nPaSM98uuah7nu4juS6H3EMNaQ_oJl0Q");
        System.out.println("result = " + result);
    }


    @Test
    @DisplayName("토큰으로 시큐리티 객체만들기")
    public void createSecurityUserObj() {
        String userId = "sjkim";
        String userPassword = "1234";
        String role = "admin";

        InnerUserDto userDto = new InnerUserDto(userId, userPassword, role);
        String token = jwtTokenProvider.createToken(userId, role);
        System.out.println("token = " + token);

//        boolean result = jwtTokenProvider.validateToken(token);
//        boolean result = jwtTokenProvider.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3MzUxMmFjNi0xOGU0LTQ5MDItYWRmNi01NGM0ZTliMjZhYWQiLCJyb2xlIjoiYWRtaW4iLCJpYXQiOjE2NzE0Mjc1NTIsImV4cCI6MTY3MTQyNzYxMn0.ZL3xRxuDYt4nPaSM98uuah7nu4juS6H3EMNaQ_oJl0Q");
        jwtTokenProvider.getAuthentication(token);
    }





    @Getter
    @Setter
    public static class InnerUserDto {
        private String userId;
        private String userPassword;
        private String role;

        public InnerUserDto() {
        }

        public InnerUserDto(String userId, String userPassword, String role) {
            this.userId = userId;
            this.userPassword = userPassword;
            this.role = role;
        }
    }

}