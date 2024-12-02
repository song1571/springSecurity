package com.sung.demo.filters;

// 필요한 라이브러리 임포트
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// 이 클래스를 Spring의 컴포넌트로 등록하여 스프링 컨텍스트에 빈으로 등록합니다.
@Component
// @Slf4j 애노테이션을 사용하여 로깅 기능을 추가합니다.
@Slf4j
public class AuthenticationLoggingFilter extends OncePerRequestFilter {

    // 요청이 필터를 거칠 때마다 실행되는 메서드입니다.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 "Request-Id"라는 헤더 값을 가져옵니다.
        String requestId = request.getHeader("Request-Id");

        // 로그에 인증이 성공한 요청과 해당 요청 ID를 기록합니다.
        log.info("Successfully authenticated request with id " + requestId);

        // 필터 체인을 통해 다음 필터 또는 요청 처리로 넘겨줍니다.
        filterChain.doFilter(request, response);
    }
}