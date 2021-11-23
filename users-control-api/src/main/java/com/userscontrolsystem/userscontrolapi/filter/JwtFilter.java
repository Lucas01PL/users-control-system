package com.userscontrolsystem.userscontrolapi.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.userscontrolsystem.userscontrolapi.service.MyUserDetailsService;
import com.userscontrolsystem.userscontrolapi.service.UserService;
import com.userscontrolsystem.userscontrolapi.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	public static final String BEARER_PREFIX = "Bearer ";
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
    private RedisTemplate<String, String> redis;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		String token = null;
		String login = null;
		
		if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
			token = authorizationHeader.substring(7);

			try {
				login = jwtUtil.getLoginFromToken(token);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
			
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(login);
        	
//        	user = userService.findByLogin(login);
            
            if (jwtUtil.validateToken(token)) {
            	
            	String loginFromRedis = this.redis.opsForValue().get(token);
//                if (loginFromRedis != null && user.getLogin().equals(loginFromRedis)) {
                if (loginFromRedis != null && userDetails.getUsername().equals(loginFromRedis)) {
	                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
	                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        
       	filterChain.doFilter(request, response);
	}
}