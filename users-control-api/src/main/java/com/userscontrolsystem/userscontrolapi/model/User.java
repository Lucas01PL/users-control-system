package com.userscontrolsystem.userscontrolapi.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Data
@Entity
@Table(name = "user", schema = "public")
public class User implements Serializable {
	
	private static final long serialVersionUID = 6645503096132060184L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
	@SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
	private Long id;
	
	@NonNull
	private String name;
	
	@NotNull
	private String login;
	
	@NotNull
	private String password;
	
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	private LocalDateTime updatedDate;
	
	@NotNull
	private String email;
	
	@NotNull
	private Boolean isAdmin;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;
	
	public User(String name, String login, String email, Boolean isAdmin, String password) {
		this.name = name;
		this.login = login;
		this.email = email;
		this.password = password;
		this.isAdmin = isAdmin;
	}

}
