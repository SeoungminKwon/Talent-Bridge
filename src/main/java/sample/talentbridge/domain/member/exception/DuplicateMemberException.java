package sample.talentbridge.domain.member.exception;

public class DuplicateMemberException extends RuntimeException{

    public DuplicateMemberException() {
        super(MemberErrorMessage.DUPLICATE_USERNAME.getMessage());
    }

    public DuplicateMemberException(String message) {
        super(message);
    }
}
