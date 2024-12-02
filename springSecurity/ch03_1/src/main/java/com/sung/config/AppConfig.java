package com.sung.config;

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

import com.sung.security.CustomAuthenticationProvider;

@Configuration
public class AppConfig {
	

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
//		http.authenticationProvider(cap);
		http.authorizeHttpRequests(
				c -> c.anyRequest().authenticated()
		);
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		String usersByUsernameQuery = 
					"select username, password, enabled from spring.users where username = ?";
		String authsByUserQuery =
					"select username, authority from spring.authorities where username = ?";
		var userDetailsManager = new JdbcUserDetailsManager(dataSource);
		userDetailsManager.setUsersByUsernameQuery(usersByUsernameQuery);
		userDetailsManager.setAuthoritiesByUsernameQuery(authsByUserQuery);
		return userDetailsManager;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}