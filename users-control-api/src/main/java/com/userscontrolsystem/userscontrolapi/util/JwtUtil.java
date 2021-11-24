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
	
	private static final long serialVersionUID = -2412348405114290465L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	private static final String SECRET = "secret@2021";
	
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims)
								.setSubject(subject)
								.setIssuedAt(new Date(System.currentTimeMillis()))
								.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
								.signWith(SignatureAlgorithm.HS512, SECRET)
								.compact();
	}
	
	public String generateToken(String login) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, login);
	}

	public String getLoginFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
	
	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody();
	}

}