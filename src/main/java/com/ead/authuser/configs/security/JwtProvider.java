package com.ead.authuser.configs.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MalformedKeyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    Logger logger = LogManager.getLogger(this.getClass());

    @Value("${ead.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${ead.auth.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwt(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSdecretKey())
                .compact();
    }

    private SecretKey getSdecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsernameJwt(String token) {
        return Jwts.parser()
                .verifyWith(getSdecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    public boolean validateJwt(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSdecretKey())
                    .build()
                    .parseSignedClaims(authToken);
        } catch (SecurityException e) {
           logger.error("invalid jwt signature: {}", e.getMessage());
        } catch (MalformedKeyException e) {
            logger.error("invalid jwt token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("jwt token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("jwt is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("jwt claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
