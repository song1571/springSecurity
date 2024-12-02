package com.sung.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppConfig {

		// Spring Boot3에서 사용하는 SecurityFilterChain 설정
		@Bean
		SecurityFilterChain configure(HttpSecurity http) throws Exception {
			
			// 기본 HTTP Basic 인증 사용
			http.httpBasic(Customizer.withDefaults());
			
			// 모든 요청에 대해 인증 요구
			http.authorizeHttpRequests(
					c -> c.anyRequest().authenticated()
			);
			
			return http.build();
		}
		
		@Bean
		UserDetailsService userDetailsService() {
			
			var user = User.withUsername("john")
					.password("12345") 
					.authorities("read")
					.build();
			
			return new InMemoryUserDetailsManager(user);
		}
		
		@Bean
		PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}
	}
