package com.sqs.springdemo.mvc.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {

	JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

	jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT user_id, pw, active FROM members WHERE user_id=?");

	jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id, role FROM roles WHERE user_id=?");

	return jdbcUserDetailsManager;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	// @formatter:off
	http.authorizeHttpRequests(configurer -> configurer
			.requestMatchers("/").hasRole("EMPLOYEE")
			.requestMatchers("/leaders/**").hasRole("MANAGER")
			.requestMatchers("/systems/**").hasRole("ADMIN")
			.anyRequest().authenticated())
		.formLogin(form -> form
			.loginPage("/showMyLoginPage")
			.loginProcessingUrl("/authenticateTheUser")
			.permitAll())
		.logout(logout -> logout.permitAll())
		.exceptionHandling(configurer -> configurer
			.accessDeniedPage("/access-denied"));
	// @formatter:on

	return http.build();
    }

    /*
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
    */
}
