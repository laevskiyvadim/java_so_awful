package org.example.avtodiller.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.avtodiller.models.UserModel;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JWTUtils {
    private final String jwtSecret = "mySuperSecretKey12345678901234567890123456789012";
    private final long validityInMilliseconds = 3600000;
    private final Key secretKey;

    public JWTUtils() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public  String generateToken(UserModel user)
    {
        String subject = new Gson().toJson(user);
        Instant now = Instant.now();


        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(validityInMilliseconds, ChronoUnit.MILLIS)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean validateToken(String token)
    {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            System.out.println("JWT error");
        }

        return false;
    }

    public String tokenData(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Cookie findCookie(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if ("token".equals(cookie.getName()))
                {
                    return cookie;
                }
            }
        }
        return null;
    }
}
