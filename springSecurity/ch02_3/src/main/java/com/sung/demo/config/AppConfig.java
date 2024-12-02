package com.sung.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.sung.demo.security.CustomAuthenticationProvider;

@Configuration // Spring 설정 클래스로 등록
public class AppConfig {

    private final CustomAuthenticationProvider authenticationProvider;

    /**
     * CustomAuthenticationProvider를 생성자 주입을 통해 DI로 주입받음
     * 
     * @param authenticationProvider - 사용자 정의 인증 제공자
     */
    public AppConfig(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Spring Security 설정을 관리하는 SecurityFilterChain Bean
     * - Basic 인증을 활성화
     * - CustomAuthenticationProvider를 설정에 추가하여 사용자 정의 인증 로직을 사용
     * - 모든 요청에 대해 인증을 요구함
     * 
     * @param http - HttpSecurity 객체로, 각 요청의 보안 정책을 정의
     * @return - 설정이 완료된 SecurityFilterChain 객체
     * @throws Exception - 설정 중 예외 발생 시 예외를 던짐
     */
    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .httpBasic(Customizer.withDefaults()) // 기본 HTTP Basic 인증을 활성화
            .authenticationProvider(authenticationProvider) // 사용자 정의 인증 제공자 설정
            .authorizeHttpRequests(
                c -> c.anyRequest().authenticated() // 모든 요청에 대해 인증 필요
            );
        return http.build(); // 구성된 SecurityFilterChain을 반환
    }
}
