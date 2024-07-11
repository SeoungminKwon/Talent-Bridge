package sample.talentbridge.domain.member.exception;

import lombok.Getter;

@Getter
public enum MemberErrorMessage {
    DUPLICATE_USERNAME("중복되는 아이디가 존재합니다."),
    NOTFOUND_USER("존재하는 사용자가 없습니다."),
    //TODO 에러코드 고려해보기
    ;

    private final String message;

    MemberErrorMessage(String message) {
        this.message = message;
    }
}
