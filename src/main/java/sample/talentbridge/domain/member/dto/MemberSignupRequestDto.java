package sample.talentbridge.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSignupRequestDto {

    @NotBlank(message = "아이디는 필수로 입력해주세요.")
    @Size(min = 4, max = 20, message = "아이디는 4 ~ 20자 사이여야 합니다.")
    private String username;

    @NotBlank(message = "패스워드를 필수로 입력해주세요.")
    //TODO 패스워드 검증 로직 결정 필요
    private String password;

    @NotBlank(message = "이름은 필수로 입력해주세요.")
    private String name;

    @NotBlank(message = "이름은 필수로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "이메일은 필수로 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "핸드폰번호는 필수로 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "나이는 필수로 입력해주세요.")
    private int age;
}
