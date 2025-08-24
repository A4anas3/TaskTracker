package com.corperate.TaskTracker.Service.JwtService;

import com.corperate.TaskTracker.DTO.JwtDto.JwtDto;
import com.corperate.TaskTracker.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    private  String SECRET_KEY ;
    public JwtService(){
        SECRET_KEY=getSECRET_KEY();
    }

    public  String getSECRET_KEY(){
        try {
            KeyGenerator generator= KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey=generator.generateKey();
            System.out.println("secret key y h "+secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
    public String generateToken(JwtDto user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(3, ChronoUnit.HOURS);
        Map<String, Object> claims = new HashMap<>();
         claims.put("id",user.getId());
        claims.put("role",user.getRole().name());
        claims.put("email",user.getEmail());
        claims.put("sub", user.getEmail());            // subject
        claims.put("iat", now.getEpochSecond());       // issued at (seconds since epoch)
        claims.put("exp", expiry.getEpochSecond());
        return Jwts.builder()
                .claims(claims)
                .signWith(getKey())
                .compact();
        }
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
       return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims=extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {

        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
