package sample.talentbridge.global.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component

public class JwtUtil {

    private SecretKey secretKey;

    public JwtUtil(@Value("${custom.jwt.secret-key}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                       .get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                       .get("role", String.class);
    }

    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }


    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
                       .before(new Date());
    }

    public String createJwt(String category, String username, String role, Long expiredMs) {
        return Jwts.builder()
                       .claim("category", category)
                       .claim("username", username)
                       .claim("role", role)
                       .issuedAt(new Date(System.currentTimeMillis()))
                       .expiration(new Date(System.currentTimeMillis() + expiredMs))
                       .signWith(secretKey)
                       .compact();
    }

    public void createAccessAndRefresh(String username, String role, Long accessExpiredMs,Long refreshExpiredMs, HttpServletResponse response) {
        String access = createJwt("access", username, role, accessExpiredMs);
        String refresh = createJwt("refresh", username, role, refreshExpiredMs);

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }

}
