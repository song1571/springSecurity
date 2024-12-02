package com.sung.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // RESTful API로 사용되는 Controller로 설정
public class HelloController {

    /**
     * 인증된 사용자가 /hello 경로에 GET 요청을 보내면 "Hello"라는 문자열을 반환
     * 
     * @return - "Hello" 문자열
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
