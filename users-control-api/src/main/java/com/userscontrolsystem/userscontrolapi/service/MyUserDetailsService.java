package com.userscontrolsystem.userscontrolapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.userscontrolsystem.userscontrolapi.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		User user = userService.findByLogin(login);
		if (user == null)
            throw new UsernameNotFoundException("No user found with login: " + login);

		return new org.springframework.security.core.userdetails.User(user.getLogin(), userService.encodePassword(user.getPassword()), List.of());
	}
	
	

}
