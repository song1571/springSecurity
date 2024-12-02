package com.sung.demo.interceptor;

// 필요한 라이브러리 임포트
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component 애노테이션을 통해 이 인터셉터를 스프링 컨텍스트에 빈으로 등록합니다.
@Component
public class CustomInterceptor implements HandlerInterceptor {

    // 핸들러 메서드 호출 전에 실행되는 메서드입니다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청을 처리하는 스레드를 출력합니다.
        System.out.println("현재 스레드:" + Thread.currentThread());

        // 핸들러 메서드 호출 전 로직을 실행하고, 요청 URI를 콘솔에 출력합니다.
        System.out.println("핸들러 메서드 호출 전: " + request.getRequestURI());

        // true를 반환하면 다음 단계(핸들러 메서드 실행)로 진행합니다.
        // false를 반환하면 요청 처리가 중단됩니다.
        return true;
    }

    // 핸들러 메서드가 호출된 후 실행되는 메서드입니다.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 핸들러 메서드 실행 후 후처리 로직을 수행할 수 있습니다.
        // 현재 주석 처리된 코드로, 핸들러 정보가 콘솔에 출력됩니다.
        // System.out.println("컨트롤러 메서드 실행 후 후처리: " + handler.toString());
    }
}
