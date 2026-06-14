package com.cambofreelance.webbackend.utils;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {

  @Value("${authentication.jwtSecret}")
  private String jwtSecret;

  @Value("${authentication.jwtExpiration}")
  private int jwtExpirationMs;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateJwtToken(String userId, Date date) {
    return generateTokenFromUserId(userId, date);
  }

  public String generateTokenFromUserId(String userId, Date date) {
    return Jwts.builder()
        .setSubject(userId)
        .setIssuedAt(date)
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey())
        .compact();
  }

  public String generateJwtToken(UserEntity userEntity, Date date, String deviceId) {
    return generateTokenFromUser(userEntity, date, deviceId);
  }

  public String generateTokenFromUser(UserEntity userEntity, Date date, String deviceId) {
    return Jwts.builder()
        .setSubject(userEntity.getUserId())
        .claim(Constants.APPLICATION_TYPE, userEntity.getApplicationId())
        .claim(Constants.USER_TYPE, userEntity.getUserType())
        .claim(Constants.USER_ID, userEntity.getUserId())
        .claim(Constants.DEVICE_ID,deviceId)
        .claim(Constants.CLIENT_USER_NAME,userEntity.getUsername())
        .setIssuedAt(date)
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey())
        .compact();
  }

  public String getUserIdFromJwtToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(authToken);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
