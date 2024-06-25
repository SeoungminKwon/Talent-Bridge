package sample.talentbridge.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import sample.talentbridge.domain.address.entity.Address;

@Entity
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

    private String phoneNumber;

    private int age;
}
