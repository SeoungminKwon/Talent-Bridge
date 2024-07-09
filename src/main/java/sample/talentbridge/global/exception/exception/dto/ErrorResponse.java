package sample.talentbridge.global.exception.exception.dto;

public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public static ErrorResponse of(String error, String message) {
        return new ErrorResponse(error, message);
    }
}
