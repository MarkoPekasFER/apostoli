package com.apostoli.UnluckyApp.security;

import com.apostoli.UnluckyApp.model.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {



    private static final String ISSUER = "APOSTOLI";

    private static final String SECRET =
            "fe02ea3834d740c3fff3e5e35ac1a1b1c43309719f1ffd63ae15923ec8902e1d7e0302af2751322ffa8e10f1b7c32a2974bcf15f0b659b1f6c55b0d9ff0a7244";



    public String generateToken(String username, List<Role> role) {


        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(username)
                .claim("username", username)
                .claim("roles", role.stream().map(roles -> roles.getName().name()).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24 Hours
                .signWith(getSigningKey())
                .compact();

    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public boolean validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();


        try {
           return ISSUER.equals(claims.getIssuer()) && claims.getIssuedAt().before(new Date());
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT is expired or invalid issuer.");
        }
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }




}
