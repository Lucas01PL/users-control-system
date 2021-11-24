package com.userscontrolsystem.userscontrolapi.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userscontrolsystem.userscontrolapi.dto.ResetPasswordRequest;
import com.userscontrolsystem.userscontrolapi.dto.UserAddRequest;
import com.userscontrolsystem.userscontrolapi.dto.UserEditRequest;
import com.userscontrolsystem.userscontrolapi.dto.UserResponse;
import com.userscontrolsystem.userscontrolapi.exception.UserNotAdministratorException;
import com.userscontrolsystem.userscontrolapi.filter.JwtFilter;
import com.userscontrolsystem.userscontrolapi.model.User;
import com.userscontrolsystem.userscontrolapi.service.UserService;
import com.userscontrolsystem.userscontrolapi.util.JwtUtil;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(userService.findAll().stream()
														.map(user -> new UserResponse(user.getId(),
																						user.getName(), 
																						user.getLogin(), 
																						user.getCreatedDate(), 
																						user.getUpdatedDate(), 
																						user.getEmail(), 
																						user.getIsAdmin()))
														.collect(Collectors.toList()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody UserAddRequest request) {
		User userAdd = userService.prepareSave(request);
		userService.save(userAdd);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> edit(@PathVariable Long id, @Valid @RequestBody UserEditRequest request) {
		User userEdit = userService.prepareEdit(request, id);
		userService.save(userEdit);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		userService.deleteById(userService.findById(id));
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("{id}/password/reset")
	public ResponseEntity<?> resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request, HttpServletRequest httpRequest) {
		
		String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (authorizationHeader != null && authorizationHeader.startsWith(JwtFilter.BEARER_PREFIX)) {
			
			String token = authorizationHeader.substring(7);

			String login = jwtUtil.getLoginFromToken(token);
		
			User userRequest = userService.findByLogin(login);
	    	if (userRequest == null || !userRequest.getIsAdmin()) {
	    		throw new UserNotAdministratorException();
	    	}
		}
		
       	User userFindById = userService.findById(id);
		userFindById.setPassword(userService.encodePassword(request.getNewPassword()));
		userService.save(userFindById);
		
		return ResponseEntity.ok(userFindById);
	}
	
}