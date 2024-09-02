package csw.practice.security.auth.component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간 (60분)
    private final long TOKEN_TIME = 60 * 60 * 1000L;

    private final Key accessKey;
    private final Key refreshKey;
    private final long accessValidityInMilliseconds;
    private final long refreshValidityInMilliseconds;

    public JwtUtil(
            @Value("${jwt.secret.access}") String accessSecret,
            @Value("${jwt.secret.refresh}") String refreshSecret,
            @Value("${jwt.expiredIn.access}") long accessValidityInSeconds,
            @Value("${jwt.expiredIn.refresh}") long refreshValidityInSeconds) {
        this.accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
        this.accessValidityInMilliseconds = accessValidityInSeconds * 1000;
        this.refreshValidityInMilliseconds = refreshValidityInSeconds * 1000;
    }

    // JwtAuthenticationFilter의 successfulAuthentication()에서 사용됨 (로그인 성공 시)
    // 액세스 토큰 생성
    public String createAccessToken(Long id, List<String> roles) {
        return createToken(id, roles, accessValidityInMilliseconds, accessKey);
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(Long id, List<String> roles) {
        return createToken(id, roles, refreshValidityInMilliseconds, refreshKey);
    }

    // 토큰 생성 공통 로직
    private String createToken(Long id, List<String> roles, long validityInMilliseconds, Key key) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put(AUTHORIZATION_KEY, roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // HTTP 요청 헤더에서 JWT 추출 메서드
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);  // "Bearer " 접두사 제거 후 토큰 반환
        }
        return null;
    }

    // 토큰 검증 (액세스 토큰)
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessKey);
    }

    // 토큰 검증 (리프레시 토큰)
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey);
    }

    // 공통 토큰 검증 로직
    private boolean validateToken(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;  // 유효하지 않은 경우 false 반환
    }

    // 토큰에서 사용자 ID 추출 (액세스 토큰)
    public Long getUserIdFromAccessToken(String token) {
        return getUserIdFromToken(token, accessKey);
    }

    // 토큰에서 사용자 ID 추출 (리프레시 토큰)
    public Long getUserIdFromRefreshToken(String token) {
        return getUserIdFromToken(token, refreshKey);
    }

    // 공통 사용자 ID 추출 로직
    private Long getUserIdFromToken(String token, Key key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    // 토큰에서 사용자 권한 추출 (액세스 토큰)
    public List<String> getRolesFromAccessToken(String token) {
        return getRolesFromToken(token, accessKey);
    }

    // 토큰에서 사용자 권한 추출 (리프레시 토큰)
    public List<String> getRolesFromRefreshToken(String token) {
        return getRolesFromToken(token, refreshKey);
    }

    // 공통 권한 추출 로직
    private List<String> getRolesFromToken(String token, Key key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return (List<String>) claims.get(AUTHORIZATION_KEY);
    }
}
