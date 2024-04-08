package utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");

    public static String generateToken(String permission, int expirationTime) {
        return Jwts.builder()
                .setSubject(permission)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String token) {
        Jws<Claims> jwt;
        try {
            jwt = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return null;
        }
        return jwt.getBody();
    }

    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}