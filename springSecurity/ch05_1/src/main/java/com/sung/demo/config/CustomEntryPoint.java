package com.sung.demo.config;

// 필요한 라이브러리를 임포트합니다.
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

// AuthenticationEntryPoint 인터페이스를 구현하여 인증되지 않은 사용자가 접근 시 처리하는 커스텀 엔트리 포인트를 생성합니다.
public class CustomEntryPoint implements AuthenticationEntryPoint {

    // commence 메서드는 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출됩니다.
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        // 응답 헤더에 "message"라는 커스텀 헤더와 "Luke, I am your father"라는 메시지를 추가합니다.
        response.addHeader("message", "Luke, I am your father");

        // HttpStatus.UNAUTHORIZED (401) 상태 코드를 클라이언트에 전송합니다.
        // sendError는 지정한 상태 코드와 메시지로 응답을 설정하여 인증되지 않은 접근임을 클라이언트에 알립니다.
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
