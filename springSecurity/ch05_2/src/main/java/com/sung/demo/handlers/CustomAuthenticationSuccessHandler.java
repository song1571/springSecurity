package com.sung.demo.handlers;

// 필요한 라이브러리 임포트
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

// @Component 애노테이션을 사용하여 이 클래스를 스프링 컨텍스트에 빈으로 등록합니다.
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // 인증 성공 시 호출되는 메서드입니다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 인증된 사용자의 권한 목록을 가져옵니다.
        var authorities = authentication.getAuthorities();

        // "read" 권한을 가진 사용자가 있는지 필터링하여 Optional로 반환합니다.
        @SuppressWarnings("unchecked")
        Optional<GrantedAuthority> auth = (Optional<GrantedAuthority>) authorities.stream()
                .filter(a -> a.getAuthority().equals("read"))
                .findFirst();

        // "read" 권한이 있는 사용자는 "/home" 경로로 리다이렉트합니다.
        if (auth.isPresent()) {
            response.sendRedirect("/home");
        } else {
            // "read" 권한이 없는 사용자는 "/error" 경로로 리다이렉트합니다.
            response.sendRedirect("/error");
        }
    }
}
