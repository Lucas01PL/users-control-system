package com.userscontrolsystem.userscontrolapi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.userscontrolsystem.userscontrolapi.dto.UserAddRequest;
import com.userscontrolsystem.userscontrolapi.dto.UserEditRequest;
import com.userscontrolsystem.userscontrolapi.dto.UserResponse;
import com.userscontrolsystem.userscontrolapi.exception.BusinessException;
import com.userscontrolsystem.userscontrolapi.model.User;
import com.userscontrolsystem.userscontrolapi.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public User findByUsername(String name) {
		return userRepository.findByName(name);
	}
	
	public List<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User save(User user) {
		
		Boolean isEmailExist = findByEmail(user.getEmail()).stream().anyMatch(userExist -> !userExist.equals(user));
		if (isEmailExist)
			throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
		
		
		User userFindByLogin = findByLogin(user.getLogin());
		if (userFindByLogin != null && !userFindByLogin.equals(user))
			throw new BusinessException("Já existe um usuário cadastrado com este login.");
		
		return userRepository.save(user);
	}

	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Usuário não encontrado."));
		
	}
	
	public void deleteById(User user) {
		userRepository.delete(user);
	}

	public User findByLogin(String login) {
		return userRepository.findByLogin(login);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User prepareSave(UserAddRequest request) {
		ModelMapper modelMapper = new ModelMapper();
		User userAdd = new User();
		modelMapper.map(request, userAdd);
		userAdd.setPassword(passwordEncoder.encode(request.getPassword()));
		return userAdd;
	}
	
	public User prepareEdit(UserEditRequest request, Long id) {
		User userFindById = findById(id);
		ModelMapper modelMapper = new ModelMapper();
		User userEdit = modelMapper.map(request, User.class);
		userEdit.setId(userFindById.getId());
		userEdit.setPassword(userFindById.getPassword());
		userEdit.setCreatedDate(userFindById.getCreatedDate());
		return userEdit;
	}

	public String encodePassword(String password) {
		//TODO Implementar encode password.
		return password;
	}
	
	public List<User> findByIsAdmin(Boolean isAdmin) {
		return userRepository.findByIsAdmin(isAdmin);
	}
	
	public UserResponse convertUserToUserResponse(User user) {
		return new UserResponse(user.getId(),
								user.getName(), 
								user.getLogin(), 
								user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SSS")), 
								user.getUpdatedDate() != null ? user.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SSS")) : "", 
								user.getEmail(), 
								user.getIsAdmin());
	}

	public User getUserAdminDefault() {
		User user = new User();
		user.setCreatedDate(LocalDateTime.now());
		user.setEmail("admin@admin.com");
		user.setIsAdmin(Boolean.TRUE);
		user.setLogin("admin");
		user.setName("admin");
		user.setPassword(encodePassword("admin"));
		System.out.println("usuario criado");
		return user;
	}
		
}
