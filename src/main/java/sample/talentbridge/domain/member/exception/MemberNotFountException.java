package sample.talentbridge.domain.member.exception;

public class MemberNotFountException extends RuntimeException {
    public MemberNotFountException() {
        super(MemberErrorMessage.NOTFOUND_USER.getMessage());
    }

    public MemberNotFountException(String message) {
        super(message);
    }
}
