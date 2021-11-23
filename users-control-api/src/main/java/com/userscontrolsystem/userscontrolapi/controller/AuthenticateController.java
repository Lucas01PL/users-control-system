package com.userscontrolsystem.userscontrolapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userscontrolsystem.userscontrolapi.dto.AuthenticateRequest;
import com.userscontrolsystem.userscontrolapi.dto.AuthenticateResponse;
import com.userscontrolsystem.userscontrolapi.model.User;
import com.userscontrolsystem.userscontrolapi.service.UserService;
import com.userscontrolsystem.userscontrolapi.util.JwtUtil;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
    private RedisTemplate<String, String> redis;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticateRequest request) {
		
		System.out.println(userService.encodePassword(request.getPassword()));
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), userService.encodePassword(request.getPassword())));
			
			User user = userService.findByLogin(request.getLogin());
			String jwt = jwtUtil.generateToken(user.getLogin());
			redis.opsForValue().set(jwt, user.getLogin());
			AuthenticateResponse response = new AuthenticateResponse();
			response.setJwt(jwt);
			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}