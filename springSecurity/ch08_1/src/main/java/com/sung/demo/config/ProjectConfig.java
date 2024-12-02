package com.sung.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();

        var user1 = User.withUsername("john")
                .password("12345")
                .roles("ADMIN")
                .build();

        var user2 = User.withUsername("jane")
                .password("12345")
                .roles("MANAGER")
                .build();

        manager.createUser(user1);
        manager.createUser(user2);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

//    @SuppressWarnings("removal")
//	@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .httpBasic()
//            .and()
//            .authorizeRequests()
//                .requestMatchers("/hello").hasRole("ADMIN")
//                .requestMatchers("/ciao").hasRole("MANAGER")
//                .anyRequest().permitAll();
//                // .anyRequest().denyAll();
//                // .anyRequest().authenticated();
//
//        return http.build();
//    }
    
//    @SuppressWarnings("removal")
//	@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .httpBasic()
//            .and()
//            .authorizeRequests()
//                .requestMatchers("/a/b/**").authenticated()
//                .anyRequest().permitAll()
//            .and()
//            .csrf().disable();
//
//        return http.build();
//    }
    
//    @SuppressWarnings("deprecation")
//	@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .httpBasic()
//            .and()
//            .authorizeRequests()
//                .requestMatchers("/product/{code:^[0-9]*$}").permitAll()
//                .anyRequest().denyAll();
//
//        return http.build();
//    }
    

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .httpBasic()
//            .and()
//            .authorizeRequests()
//                .requestMatchers(".*/(us|uk|ca)+/(en|fr).*").authenticated()
//                .anyRequest().hasAuthority("premium");
//
//        return http.build();
//    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
            .authorizeRequests()
                .requestMatchers("/email/{email:.*(.+@.+\\.com)}").permitAll()
                .anyRequest().denyAll();

        return http.build();
    }
    
}