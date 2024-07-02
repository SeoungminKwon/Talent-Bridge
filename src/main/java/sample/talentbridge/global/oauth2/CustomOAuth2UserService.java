package sample.talentbridge.global.oauth2;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sample.talentbridge.domain.member.entity.Member;
import sample.talentbridge.domain.member.repository.MemberRepository;
import sample.talentbridge.global.oauth2.dto.GoogleResponse;
import sample.talentbridge.global.oauth2.dto.NaverResponse;
import sample.talentbridge.global.oauth2.dto.OAuth2UserDto;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        } else {
            throw  new OAuth2AuthenticationException("지원안하는 OAuth2 Provider ");
        }
        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디 값을 만듬 => 중복 될 수 있기 때문
        String username = oAuth2Response.getProvider()+ " " + oAuth2Response.getProviderId();
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        if (optionalMember.isEmpty()) {
            // 신규 OAuht2 회원가입
            Member newMember = Member.builder()
                                       .username(username)
                                       .name(oAuth2Response.getName())
                                       .role("ROLE_USER")
                                       .build();
            memberRepository.save(newMember);
        } else{
            // 기존 정보 업데이트
            Member member = optionalMember.get();
            member.updateName(oAuth2Response.getName());
            member.updateEmail(oAuth2Response.getEmail());
            memberRepository.save(member);
        }

        OAuth2UserDto oAuth2UserDto = new OAuth2UserDto();
        oAuth2UserDto.setUsername(username);
        oAuth2UserDto.setName(oAuth2Response.getName());
        oAuth2UserDto.setRole("ROLE_USER");

        return new CustomOAuth2User(oAuth2UserDto);
    }
}
