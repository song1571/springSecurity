package com.sung.demo.controller;

// 필요한 라이브러리 임포트
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.*;

// RestController로 설정하여 REST API 요청을 처리합니다.
@RestController
public class HelloController {

    // /moring 엔드포인트로 요청이 들어오면 "/hello/dmove"로 요청을 forward 합니다.
    @GetMapping("/moring")
    public void hello(HttpServletRequest request, HttpServletResponse response,
                      Authentication a) throws ServletException, IOException {
        // "/hello/dmove"로 요청을 포워드하여 다른 엔드포인트로 리디렉션합니다.
        request.getRequestDispatcher("/hello/dmove")
               .forward(request, response);
    }

    // /hello/dmove 경로에 대해 요청을 처리하는 메서드
    @GetMapping("/hello/dmove")
    public ResponseEntity<String> handlerForwardedRequest() {
        // 포워딩 후 최종 목적지로 "this is final destination after forwarding" 메시지를 반환합니다.
        return ResponseEntity.ok("this is final destination after forwarding");
    }

    // /hello 엔드포인트에 대해 비동기 요청을 처리하는 메서드
    @GetMapping("/hello")
    @Async // 이 메서드는 비동기적으로 실행되어 호출 시 별도의 스레드에서 수행됩니다.
    public CompletableFuture<String> hello() {
        System.out.println("Start hello() method in thread: " + Thread.currentThread().getName());
        System.out.println("현재 스레드:" + Thread.currentThread());

        // 현재 SecurityContext에서 인증 정보를 가져옵니다.
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication a = context.getAuthentication();
        String name = a.getName(); // 인증된 사용자의 이름을 가져옵니다.

        // 3초 지연을 추가하여 비동기 작업의 예를 보여줍니다.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("End hello() method in thread: " + Thread.currentThread().getName());

        // CompletableFuture를 통해 비동기 결과를 반환합니다.
        return CompletableFuture.completedFuture(name);
    }

    // Future를 반환하는 hello 메서드 (위의 메서드와 오버로딩 관계)
    public Future<String> hello(Authentication a) {
        // 현재 SecurityContext에서 인증 정보를 가져옵니다.
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication aa = context.getAuthentication();
        String name = a.getName();

        // AsyncResult를 사용해 비동기 결과를 반환합니다.
        return new AsyncResult<>(name);
    }

    // /ciao 엔드포인트로 요청이 들어올 때 호출되는 메서드
    @GetMapping("/ciao")
    public String ciao(Authentication a) throws Exception {
        Thread t1 = Thread.currentThread(); // 현재 스레드를 t1 변수에 저장

        // Callable을 사용해 비동기 작업을 정의합니다.
        Callable<String> task = () -> {
            Thread t2 = Thread.currentThread(); // 비동기 작업을 수행하는 스레드를 t2 변수에 저장
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication aa = context.getAuthentication();
            return context.getAuthentication().getName(); // 인증된 사용자의 이름을 반환합니다.
        };

        // ExecutorService를 사용하여 새로운 스레드 풀을 생성합니다.
        ExecutorService e = Executors.newCachedThreadPool();
        try {
            // DelegatingSecurityContextCallable을 사용하여 SecurityContext가 전파되도록 합니다.
            var contextTask = new DelegatingSecurityContextCallable<>(task);

            // 비동기 작업을 제출하고 결과를 반환합니다.
            return "Ciao, " + e.submit(contextTask).get() + "!";
        } finally {
            // ExecutorService를 종료하여 자원을 해제합니다.
            e.shutdown();
        }
    }
}
