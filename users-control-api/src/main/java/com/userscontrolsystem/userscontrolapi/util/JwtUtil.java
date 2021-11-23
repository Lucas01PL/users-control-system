package com.userscontrolsystem.userscontrolapi.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil implements Serializable {
	
	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	private static final String SECRET = "secret@2021";
	
	//Cria o token e devine tempo de expiração pra ele
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims)
								.setSubject(subject)
								.setIssuedAt(new Date(System.currentTimeMillis()))
								.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
								.signWith(SignatureAlgorithm.HS512, SECRET)
								.compact();
	}
	
	//gera token para user
	public String generateToken(String login) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, login);
	}

	//retorna o username do token jwt 
	public String getLoginFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/*
	//valida o token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String loginToken = getLoginFromToken(token);
		return (userDetails.getUsername().equals(loginToken) && !isTokenExpired(token));
	}
	*/
	
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
	
	//retorna expiration date do token jwt 
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//para retornar qualquer informação do token nos iremos precisar da secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody();
	}

}