package com.userscontrolsystem.userscontrolapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.userscontrolsystem.userscontrolapi.config.JwtAuthenticationEntryPoint;
import com.userscontrolsystem.userscontrolapi.filter.JwtFilter;
import com.userscontrolsystem.userscontrolapi.service.MyUserDetailsService;

@CrossOrigin(origins = "*")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	private String[] WHITELIST_URL = {"/hello/",
									  "/authenticate/*"};
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.cors()
			.and()
			.csrf().disable()
			.headers()
			.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "*"))
  			.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS"))
  			.addHeaderWriter(new StaticHeadersWriter("Access-Control-Max-Age", "3600"))
  			.addHeaderWriter(new StaticHeadersWriter("Access-Control-Expose-Headers", "Authorization"))
  			.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "true"))
  			.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization"))
			.and()
  			.httpBasic()
			.and()
			.headers().frameOptions().disable()
			.and()
			.authorizeRequests()
			.antMatchers(WHITELIST_URL).permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	/*
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	*/
	
	@Bean
	public AuthenticationManager getAuthenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
}
