package sample.talentbridge.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.talentbridge.domain.address.entity.Address;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    private Address address;

    private String username;

    private String password;

    private String name;

    private String nickname;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String phoneNumber;

    private int age;

    private String role;

    public Member updateName(String newName){
        if(newName == null && !newName.isBlank()){
            this.name = newName;
        }
        return this;
    }

    public Member updateEmail(String email) {
        this.email = email;
        return this;
    }


}
