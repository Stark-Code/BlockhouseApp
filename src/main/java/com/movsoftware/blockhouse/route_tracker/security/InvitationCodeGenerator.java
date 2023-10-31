package com.movsoftware.blockhouse.route_tracker.security;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class InvitationCodeGenerator {
    private static final byte[] SECRET_KEY = "MySuperSecretKey1234567890Key1234567890".getBytes();
    private static final long EXPIRATION_TIME = 72 * 60 * 60 * 1000; // 72 hours in milliseconds

    public static String generateInvitationCode(String email, String organizationId) {
        // Define the claims for the invitation JWT
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("organizationId", organizationId);
        claims.put("permissions", Collections.singletonList("register"));

        // Calculate the expiration date/time
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        // Generate the JWT
        Key key = Keys.hmacShaKeyFor(SECRET_KEY);
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();

        return jwt;
    }

    public static Jws<Claims> decodeInvitationCode(String jwt) {
        // Decode and verify the JWT
        Key key = Keys.hmacShaKeyFor(SECRET_KEY);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);
    }
}
