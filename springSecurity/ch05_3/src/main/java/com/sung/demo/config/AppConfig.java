package com.sung.demo.config;

// 필요한 클래스들을 임포트합니다. 이 중에서 주요 기능을 하는 커스텀 필터, 핸들러, 인터셉터 등이 포함됩니다.
import com.sung.demo.filters.AuthenticationLoggingFilter;
import com.sung.demo.filters.RequestValidationFilter;
import com.sung.demo.handlers.CustomAuthenticationFailureHandler;
import com.sung.demo.handlers.CustomAuthenticationSuccessHandler;
import com.sung.demo.interceptor.CustomInterceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configuration을 나타내는 @Configuration 애노테이션을 추가해 Spring에서 이 클래스를 설정 클래스로 인식하도록 합니다.
// EnableAsync 애노테이션을 추가해 비동기 처리를 활성화합니다.
@Configuration
@EnableAsync
public class AppConfig implements WebMvcConfigurer {

    // 커스텀 인터셉터 및 성공/실패 핸들러를 주입받습니다.
	private final CustomInterceptor customInterceptor;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    // 생성자를 통해 위에서 정의한 핸들러들과 인터셉터를 초기화합니다.
    public AppConfig(CustomInterceptor customInterceptor,
                     CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                     CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.customInterceptor = customInterceptor;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    // InitializingBean은 초기화 시점을 맞추기 위해 사용되며, 보안 컨텍스트가
    // 자식 스레드에서도 공유되도록 전략을 설정합니다.
    @Bean
    public InitializingBean initializingBean() {
        return () -> SecurityContextHolder
                .setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    // WebMvcConfigurer 인터페이스를 구현하여 addInterceptors 메서드를 오버라이드합니다.
    // CustomInterceptor를 /hello 경로에 적용하도록 설정합니다.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor)
                .addPathPatterns("/hello"); // 특정 경로에 대해 인터셉터 적용
    }

    // InMemoryUserDetailsManager를 사용하여 메모리 내에 사용자 정보를 저장하는 빈을 생성합니다.
    @Bean
    public UserDetailsService uds() {
    	
    	// 사용자 이름이 "john"이고 패스워드가 "12345"인 사용자 객체를 생성합니다.
        var user = User.withUsername("john")
                .password("12345")
                .authorities("read") // 권한은 "read"로 설정합니다.
                .build();
    	
        var uds = new InMemoryUserDetailsManager(user);

        // 주석 처리된 코드: InMemoryUserDetailsManager를 통해 기본 암호 인코더와 함께 두 번째 사용자를 추가하는 예입니다.
        // 기본 사용자 생성 예제:
        // uds.createUser(
        //         User.withDefaultPasswordEncoder()
        //                 .username("john")
        //                 .password("12345")
        //                 .authorities("read")
        //                 .build()
        // );
        // 새로운 사용자 생성 예제:
        // uds.createUser(
        //         User.withDefaultPasswordEncoder()
        //                 .username("bill")
        //                 .password("12345")
        //                 .authorities("write")
        //                 .build()
        // );

        return uds;
    }

    // HttpSecurity 객체를 설정하여 SecurityFilterChain 빈을 생성합니다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // formLogin 메서드를 사용하여 폼 로그인 기능을 설정합니다.
        // 로그인 성공 시 authenticationSuccessHandler가 동작하고,
        // 로그인 실패 시 authenticationFailureHandler가 동작하도록 지정합니다.
        http.formLogin(c ->
                c.successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
        );

        // 주석 처리된 코드: httpBasic 인증 방식을 기본 설정으로 활성화할 수 있습니다.
        // 주석을 해제하면 Basic Authentication이 활성화됩니다.
        // http.httpBasic(Customizer.withDefaults());

        // 주석 처리된 코드: CustomEntryPoint를 사용하여 인증되지 않은 사용자에 대한 예외를 처리할 수 있습니다.
        // 예외 처리에 CustomEntryPoint를 설정하여 비인증 요청을 처리하는 코드입니다.
        // http.exceptionHandling()
        //        .authenticationEntryPoint(new CustomEntryPoint());

        // 모든 요청을 인증이 필요하도록 설정합니다. 모든 요청이 authenticated() 메서드를 통해 인증을 요구합니다.
        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());

        return http.build(); // 설정을 적용한 SecurityFilterChain을 빌드하여 반환합니다.
    }

    // PasswordEncoder 빈을 생성하여 패스워드 인코딩 방법을 설정합니다.
    // NoOpPasswordEncoder를 사용하여 암호화를 하지 않고 비밀번호를 평문으로 저장하게 설정합니다.
    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .anyRequest().authenticated() // 모든 요청에 인증 요구
//            .and()
//            .exceptionHandling()
//                .authenticationEntryPoint(new CustomEntryPoint()); // CustomEntryPoint 설정
//
//        return http.build();
//    }
    
    
    
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}