package itmo.blps.elearningplatform.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import itmo.blps.elearningplatform.ELearningPlatformConfig;
import itmo.blps.elearningplatform.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final ELearningPlatformConfig eLearningPlatformConfig;

    public String generateToken(User user) {
        Map<String, Object> extraClaims = Map.of(
                "id", user.getId(),
                "role", user.getRole().name(),
                "enabled", user.isEnabled()
        );
        return buildToken(extraClaims, user, eLearningPlatformConfig.security().jwt().expiration());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public User parseUser(String token) {
        Claims claims = extractAllClaims(token);

        if (claims.getExpiration().before(new Date())) {
            throw new JwtException("JWT has expired");
        }

        return User.builder()
                .id(claims.get("id", Integer.class))
                .username(claims.getSubject())
                .role(User.Role.valueOf(claims.get("role", String.class)))
                .enabled(claims.get("enabled", Boolean.class))
                .build();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(eLearningPlatformConfig.security().jwt().secretKey()));
    }
}