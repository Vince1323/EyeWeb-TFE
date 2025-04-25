package com.jorami.eyeapp.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean validToken(String token);

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimResolver);

    String generateToken(UserDetails userDetails);

    SimpleGrantedAuthority getAuthoritie(String token);

}
