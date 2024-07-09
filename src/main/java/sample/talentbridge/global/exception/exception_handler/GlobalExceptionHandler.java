package sample.talentbridge.global.exception.exception_handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sample.talentbridge.domain.member.exception.DuplicateMemberException;
import sample.talentbridge.global.exception.exception.dto.ErrorResponse;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(OAuth2AuthenticationException exception) {

        log.error("OAuth2 인증 예외 : {}", exception.getMessage());
        ErrorResponse response = new ErrorResponse("OAuth2_Authentication_Error", "OAuth2인증 처리중 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicateMemberException exception) {

        log.error("중복 아이디 존재 예외 : {}", exception.getMessage());
        ErrorResponse response = new ErrorResponse("DuplicateMember_Error", "중복되는 회원이 존재합니다.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
