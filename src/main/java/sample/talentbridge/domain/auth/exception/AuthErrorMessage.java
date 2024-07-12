package sample.talentbridge.domain.auth.exception;

import lombok.Getter;

@Getter
public enum AuthErrorMessage {
    REFRESH_TOKEN_NULL("RefreshToken의 값이 Null입니다."),
    REFRESH_TOKEN_VERIFICATION_FAILED("RefreshToken 검증 실패 했습니다."),
    REFRESH_TOKEN_EXPIRED("RefreshToken 이 만료되었습니다."),
    ;

    private final String message;

    AuthErrorMessage(String message) {
        this.message = message;
    }
}
