package sample.talentbridge.global.exception.exception_handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.talentbridge.global.exception.exception.dto.ErrorResponse;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(OAuth2AuthenticationException exception) {

        log.error("OAuth2 인증 예외 : {}", exception.getMessage());
        ErrorResponse response = new ErrorResponse("OAuth2_Authentication_Error", "OAuth2인증 처리중 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
