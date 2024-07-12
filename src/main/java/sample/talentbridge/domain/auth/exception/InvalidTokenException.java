package sample.talentbridge.domain.auth.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(AuthErrorMessage.REFRESH_TOKEN_VERIFICATION_FAILED.getMessage());
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
