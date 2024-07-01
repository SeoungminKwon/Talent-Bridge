package sample.talentbridge.global.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserDto {

    private String role;
    private String name;
    private String username;
}
