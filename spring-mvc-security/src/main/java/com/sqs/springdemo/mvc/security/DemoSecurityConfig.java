package com.sqs.springdemo.mvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    @Bean
    InMemoryUserDetailsManager userDetailsManager() {

	// @formatter:off
	UserDetails john = User.builder()
		.username("john")
		.password("{noop}test123")
		.roles("EMPLOYEE")
		.build();
	
	UserDetails mary = User.builder()
		.username("mary")
		.password("{noop}test123")
		.roles("EMPLOYEE", "MANAGER")
		.build();
	
	UserDetails susan = User.builder()
		.username("susan")
		.password("{noop}test123")
		.roles("EMPLOYEE", "MANAGER", "ADMIN")
		.build();
	// @formatter:on

	return new InMemoryUserDetailsManager(john, mary, susan);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	// @formatter:off
	http.authorizeHttpRequests(configurer -> configurer.anyRequest().authenticated())
		.formLogin(form -> form
			.loginPage("/showMyLoginPage")
			.loginProcessingUrl("/authenticateTheUser")
			.permitAll())
		.logout(logout -> logout.permitAll());
	// @formatter:on

	return http.build();
    }
}
