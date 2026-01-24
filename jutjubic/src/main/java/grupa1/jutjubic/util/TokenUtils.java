package grupa1.jutjubic.util;

import grupa1.jutjubic.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenUtils {
    @Value("jutjubic")
    private String APP_NAME;

    @Value("${jwt.secret}")
    public String SECRET_KEY;

    // 3h
    @Value("10800000")
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private static final String AUDIENCE_WEB = "web";

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        System.out.println("Secret Key Length: " + keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email, Long id) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(APP_NAME)
                .setSubject(email)
                .setAudience(AUDIENCE_WEB)
                .setIssuedAt(new Date())
                .claim("key", id)
                .setExpiration(generateExpirationDate())
                .signWith(getSigningKey(), SIGNATURE_ALGORITHM)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);

        if  (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String getEmailFromToken(String token) {
        String email;

        try {
            final Claims claims = getAllClaimsFromToken(token);
            email = claims.getSubject();
        }  catch (ExpiredJwtException ex) {
            throw ex;
        }  catch (Exception e) {
            email = null;
        }
        return email;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            expiration = null;
        }

        return expiration;
    }

    public Long getIdFromToken(String token) {
        Long id;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            id = claims.get("key", Long.class);
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String username = getEmailFromToken(token);
        final Date issuedAt = getIssuedAtDateFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);

        return (username != null && username.equals(user.getEmail()) && issuedAt.before(expiration));
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }
}
