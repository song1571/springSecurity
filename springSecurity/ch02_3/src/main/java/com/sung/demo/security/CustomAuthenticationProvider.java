package com.sung.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j // Lombok을 사용하여 Logger를 생성
@Component // Spring의 Bean으로 등록하여 DI로 주입 가능하게 함
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * 인증 처리 메서드
     * 사용자가 제공한 인증 정보(username과 password)를 검증하여 유효한 경우 인증 토큰을 반환함.
     * 
     * @param authentication - 사용자가 입력한 자격 증명(Username과 Password)
     * @return - 인증된 사용자 정보를 포함하는 UsernamePasswordAuthenticationToken 객체
     * @throws AuthenticationException - 인증 실패 시 예외를 발생시킴
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // 입력된 사용자 이름
        log.info("What is principal :" + authentication.getPrincipal()); // Principal 객체를 로그로 출력
        String password = String.valueOf(authentication.getCredentials()); // 입력된 비밀번호

        // 사용자 이름과 비밀번호가 일치하는지 확인
        if("john".equals(username) && "1234".equals(password)) {
            // 인증 성공 시 UsernamePasswordAuthenticationToken 객체를 생성해 반환
            return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList());
        } else {
            // 인증 실패 시 예외를 발생
            throw new AuthenticationCredentialsNotFoundException("Not Match Error");
        }
    }

    /**
     * 인증 제공자가 특정 유형의 인증 객체를 지원하는지 여부를 확인
     * 
     * @param authentication - 확인할 인증 클래스 유형
     * @return - 이 인증 제공자가 UsernamePasswordAuthenticationToken을 지원하는 경우 true 반환
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

