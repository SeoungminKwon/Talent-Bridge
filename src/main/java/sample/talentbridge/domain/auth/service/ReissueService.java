package sample.talentbridge.domain.auth.service;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.talentbridge.domain.auth.exception.AuthErrorMessage;
import sample.talentbridge.domain.auth.exception.InvalidTokenException;
import sample.talentbridge.global.jwt.JwtConstants;
import sample.talentbridge.global.jwt.JwtUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;

    public void verifyJwtToken(String token) {

        if (token == null) {
            throw new InvalidTokenException(AuthErrorMessage.REFRESH_TOKEN_NULL.getMessage());
        }

        // 토큰 만료 검증
        checkExpiration(token);
        // 카테고리 검증
        checkCategory(token);
    }

    public String createJWTToken(String strategy, String token) {
        if (strategy.equals("access")) {
            return createAccessToken(token);
        }
        return createRefreshToken(token);
    }

    private void checkExpiration(String token) {
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(AuthErrorMessage.REFRESH_TOKEN_EXPIRED.getMessage());
        }
    }

    private void checkCategory(String token) {
        String category = jwtUtil.getCategory(token);
        if (!category.equals("refresh")) {
            throw new InvalidTokenException(AuthErrorMessage.REFRESH_TOKEN_NOT_MATCH.getMessage());
        }
    }


    private String createRefreshToken(String token) {
        return jwtUtil.createJwt("refresh",
                jwtUtil.getUsername(token),
                jwtUtil.getRole(token),
                JwtConstants.REFRESH_TOKEN_EXPIRATION);
    }
    private String createAccessToken(String token) {
        return jwtUtil.createJwt("access",
                jwtUtil.getUsername(token),
                jwtUtil.getRole(token),
                JwtConstants.ACCESS_TOKEN_EXPIRATION);
    }
}
