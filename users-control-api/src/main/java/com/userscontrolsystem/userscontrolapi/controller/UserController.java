package com.userscontrolsystem.userscontrolapi.controller;

import java.util.List;
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

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@ApiOperation(value = "Endpoint to list all User", response = UserResponse[].class)
	@ApiResponses(value = {
	     @ApiResponse(code = 200, message = "Ok"),
	     @ApiResponse(code = 400, message = "Bad Request"),
	     @ApiResponse(code = 401, message = "Not authorized"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyWF0IjoxNjM3Nzk1MjU4fQ.2_uzMC2ZbNOf6uWbK8zv")
	})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> findAll() {
		return ResponseEntity.ok(userService.findAll().stream()
														.map(user -> userService.convertUserToUserResponse(user))
														.collect(Collectors.toList()));
	}
	
	@ApiOperation(value = "Endpoint to find a User by your id", response = UserResponse.class)
	@ApiResponses(value = {
	     @ApiResponse(code = 200, message = "Ok"),
	     @ApiResponse(code = 400, message = "Bad Request"),
	     @ApiResponse(code = 401, message = "Not authorized"),
	     @ApiResponse(code = 404, message = "The resource not found"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyWF0IjoxNjM3Nzk1MjU4fQ.2_uzMC2ZbNOf6uWbK8zv")
	})
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
		User user = userService.findById(id);
		UserResponse userResponse = userService.convertUserToUserResponse(user);
		return ResponseEntity.ok(userResponse);
	}
	
	@ApiOperation(value = "Endpoint to add a User.", response = UserResponse.class)
	@ApiResponses(value = {
	     @ApiResponse(code = 201, message = "Created"),
	     @ApiResponse(code = 400, message = "Bad Request"),
	     @ApiResponse(code = 401, message = "Not authorized"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyWF0IjoxNjM3Nzk1MjU4fQ.2_uzMC2ZbNOf6uWbK8zv")
	})
	@PostMapping
	public ResponseEntity<Void> add(@Valid @RequestBody UserAddRequest request) {
		User userAdd = userService.prepareSave(request);
		userService.save(userAdd);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@ApiOperation(value = "Endpoint to edit a User by your id", response = UserResponse.class)
	@ApiResponses(value = {
	     @ApiResponse(code = 200, message = "Ok"),
	     @ApiResponse(code = 400, message = "Bad Request"),
	     @ApiResponse(code = 401, message = "Not authorized"),
	     @ApiResponse(code = 404, message = "The resource not found"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyWF0IjoxNjM3Nzk1MjU4fQ.2_uzMC2ZbNOf6uWbK8zv")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Void> edit(@PathVariable Long id, @Valid @RequestBody UserEditRequest request) {
		User userEdit = userService.prepareEdit(request, id);
		userService.save(userEdit);
		return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "Delete the User by your id")
	@ApiResponses(value = {
	     @ApiResponse(code = 200, message = "Ok"),
	     @ApiResponse(code = 400, message = "Bad Request"),
	     @ApiResponse(code = 401, message = "Not authorized"),
	     @ApiResponse(code = 404, message = "The resource not found"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyWF0IjoxNjM3Nzk1MjU4fQ.2_uzMC2ZbNOf6uWbK8zv")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.deleteById(userService.findById(id));
		return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "Reset password of the User by your id")
	@ApiResponses(value = {
	     @ApiResponse(code = 200, message = "Ok"),
	     @ApiResponse(code = 400, message = "Bad Request"),
	     @ApiResponse(code = 401, message = "Not authorized"),
	     @ApiResponse(code = 404, message = "The resource not found"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyWF0IjoxNjM3Nzk1MjU4fQ.2_uzMC2ZbNOf6uWbK8zv")
	})
	@PostMapping("{id}/password/reset")
	public ResponseEntity<UserResponse> resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request, HttpServletRequest httpRequest) {
		
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
		
		return ResponseEntity.ok(userService.convertUserToUserResponse(userFindById));
	}
	
	@ApiOperation(value = "Endpoint to create a Default Admin User", response = UserResponse.class)
	@ApiResponses(value = {
	     @ApiResponse(code = 200, message = "Ok"),
	     @ApiResponse(code = 404, message = "The resource not found"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@PostMapping("/default")
	public ResponseEntity<UserResponse> createUserDefault() {
		userService.save(userService.getUserAdminDefault());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
}