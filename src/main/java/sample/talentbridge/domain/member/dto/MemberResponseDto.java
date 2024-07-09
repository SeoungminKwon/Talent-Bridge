package sample.talentbridge.domain.member.dto;

import lombok.Getter;
import sample.talentbridge.domain.member.entity.Member;

@Getter
public class MemberResponseDto {

    private String username;
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;
    private int age;

    public MemberResponseDto(Member member) {
        this.username = member.getUsername();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.age = member.getAge();
    }
}
