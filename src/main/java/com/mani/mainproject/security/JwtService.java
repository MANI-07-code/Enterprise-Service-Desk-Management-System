package com.mani.mainproject.security;

import com.mani.mainproject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mySecretKeyForAuthenticationInEHDMSProject123456";

        private SecretKey getSigningKey(){
            return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        }

        public String generateToken(User user){

            return Jwts.builder()
                    .subject(user.getEmail())
                    .claim("role",user.getRole())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+1000L*60*60*24))
                    .signWith(getSigningKey(),Jwts.SIG.HS256)
                    .compact();
        }

    private Claims extractClaims(String token){

            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
                 }

    public String extractEmail(String token){

        return extractClaims(token)
                .getSubject();
            }

    public boolean isTokenExpired(String token){
            return extractClaims(token)
                    .getExpiration().before(new Date());
         }

         public  boolean validateToken(String token,User user){
            return extractEmail(token).equals(user.getEmail()) &&
                    !isTokenExpired(token);
         }
}


