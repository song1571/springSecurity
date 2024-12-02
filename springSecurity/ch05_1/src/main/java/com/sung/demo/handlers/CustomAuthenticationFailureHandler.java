package com.sung.demo.handlers;

// 필요한 라이브러리 임포트
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

// @Component 애노테이션을 통해 스프링 컨텍스트에 빈으로 등록합니다.
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    // 인증 실패 시 호출되는 메서드입니다.
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        try {
            // 인증 실패 시 "/error" 페이지로 리다이렉트합니다.
            response.sendRedirect("/error");

            // 응답 헤더에 실패 시각을 "failed"라는 헤더로 추가합니다.
            response.setHeader("failed", LocalDateTime.now().toString());
        } catch (IOException ex) {
            // IOException 발생 시 런타임 예외로 래핑하여 다시 던집니다.
            throw new RuntimeException(ex);
        }
    }
}