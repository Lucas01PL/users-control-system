package com.userscontrolsystem.userscontrolapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userscontrolsystem.userscontrolapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByName(String name);

	User findByLogin(String login);
	
	List<User> findByEmail(String email);

	List<User> findByIsAdmin(Boolean isAdmin);

}
