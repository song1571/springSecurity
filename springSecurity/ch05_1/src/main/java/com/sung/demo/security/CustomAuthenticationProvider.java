package com.sung.demo.security;

// 필요한 라이브러리 임포트
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// @Component 애노테이션을 사용하여 이 클래스를 스프링 컨텍스트에 빈으로 등록합니다.
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // 사용자 정보를 로드하기 위해 UserDetailsService와 비밀번호 검증을 위한 PasswordEncoder를 사용합니다.
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // 생성자 주입을 통해 UserDetailsService와 PasswordEncoder를 초기화합니다.
    public CustomAuthenticationProvider(UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // authenticate 메서드는 사용자의 인증 정보를 검증합니다.
    @Override
    public Authentication authenticate(Authentication authentication) {
        // 입력된 사용자 이름과 비밀번호를 가져옵니다.
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // UserDetailsService를 사용하여 사용자 이름에 해당하는 사용자 정보를 로드합니다.
        UserDetails u = userDetailsService.loadUserByUsername(username);

        // 입력된 비밀번호와 저장된 비밀번호를 비교하여 인증을 검증합니다.
        if (passwordEncoder.matches(password, u.getPassword())) {
            // 비밀번호가 일치하면 UsernamePasswordAuthenticationToken을 생성하여 인증된 토큰을 반환합니다.
            return new UsernamePasswordAuthenticationToken(username, password, u.getAuthorities());
        } else {
            // 비밀번호가 일치하지 않으면 BadCredentialsException을 발생시킵니다.
            throw new BadCredentialsException("Something went wrong!");
        }
    }

    // supports 메서드는 이 AuthenticationProvider가 특정 인증 유형을 지원하는지 여부를 반환합니다.
    @Override
    public boolean supports(Class<?> authenticationType) {
        // UsernamePasswordAuthenticationToken 인증 유형만 지원합니다.
        return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
    }
}
