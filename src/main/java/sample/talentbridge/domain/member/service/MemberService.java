package sample.talentbridge.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sample.talentbridge.domain.member.dto.MemberSignupRequestDto;
import sample.talentbridge.domain.member.entity.Member;
import sample.talentbridge.domain.member.exception.DuplicateMemberException;
import sample.talentbridge.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public Member createMember(MemberSignupRequestDto signupDto) {
        validateUsername(signupDto.getUsername());
        validateNickname(signupDto.getNickname());
        Member member = Member.builder()
                                .username(signupDto.getUsername())
                                .password(passwordEncoder.encode(signupDto.getPassword()))
                                .name(signupDto.getName())
                                .nickname(signupDto.getNickname())
                                .email(signupDto.getEmail())
                                .age(signupDto.getAge())
                                .phoneNumber(signupDto.getPhoneNumber())
                                .build();

        return memberRepository.save(member);
    }

    // 중복 ID 검증
    public void validateUsername(String username) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new DuplicateMemberException();
        }
    }

    // 중복 닉네임 검증
    public void validateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateMemberException();
        }
    }


}
