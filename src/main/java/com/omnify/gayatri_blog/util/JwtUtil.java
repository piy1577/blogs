package com.omnify.gayatri_blog.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(Long id) {
        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long extractId(String token) {
        String id =Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
        return Long.parseLong(id);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
