package sample.talentbridge.global.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final sample.talentbridge.global.oauth2.dto.OAuth2UserDto OAuth2UserDto;

    @Override
    public Map<String, Object> getAttributes() {
        return null; // 획일화 하기 힘듬
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return OAuth2UserDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return OAuth2UserDto.getName();
    }

    public String getUsername() {
        return OAuth2UserDto.getUsername();
    }
}
