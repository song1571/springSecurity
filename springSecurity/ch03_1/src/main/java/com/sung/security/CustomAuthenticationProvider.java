package com.sung.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j // Lombok을 사용하여 Logger를 생성
@Component // Spring의 Bean으로 등록하여 DI로 주입 가능하게 함
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private UserDetailsService uds;
	
	public CustomAuthenticationProvider(UserDetailsService uds) {
		this.uds = uds;
	}

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // 입력된 사용자 이름
        log.info("What is principal :" + authentication.getPrincipal()); // Principal 객체를 로그로 출력
        String password = String.valueOf(authentication.getCredentials()); // 입력된 비밀번호
        
        UserDetails ud = uds.loadUserByUsername(username);
        // 사용자 이름과 비밀번호가 일치하는지 확인
        if (ud.getUsername().equals(username) && ud.getPassword().equals(password)) {
        	return new UsernamePasswordAuthenticationToken(username,
        								password, Arrays.asList());
        } else {
            // 인증 실패 시 예외를 발생
            throw new AuthenticationCredentialsNotFoundException("Not Match Error");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
