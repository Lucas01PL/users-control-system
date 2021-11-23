package com.userscontrolsystem.userscontrolapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "role", schema = "public")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 6752612252347961104L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
	@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", allocationSize = 1)
	private Long id;
	
	@NotNull
	private String name;

	@Override
	public String getAuthority() {
		return this.name;
	}
}