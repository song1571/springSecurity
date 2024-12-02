package com.sung.demo.filters;

// 필요한 라이브러리 임포트
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// RequestValidationFilter는 Filter 인터페이스를 구현하여, 요청을 검증하는 필터로 사용됩니다.
public class RequestValidationFilter implements Filter {

    // 요청이 필터를 거칠 때마다 실행되는 메서드입니다.
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        
        // ServletRequest와 ServletResponse를 HttpServletRequest와 HttpServletResponse로 캐스팅합니다.
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        // 요청 헤더에서 "Request-Id"라는 헤더 값을 가져옵니다.
        String requestId = httpRequest.getHeader("Request-Id");

        // Request-Id가 없거나 비어있으면 400 Bad Request 상태 코드로 응답을 설정하고 필터 체인을 종료합니다.
        if (requestId == null || requestId.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Request-Id가 존재하는 경우 다음 필터 또는 요청 처리로 넘겨줍니다.
        filterChain.doFilter(request, response);
    }
}