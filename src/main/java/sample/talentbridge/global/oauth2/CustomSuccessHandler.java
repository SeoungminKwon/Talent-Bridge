package sample.talentbridge.global.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sample.talentbridge.global.jwt.JwtConstants;
import sample.talentbridge.global.jwt.JwtUtil;

@Component
@RequiredArgsConstructor
// SimpleUrlAuthenticationSuccessHandler => Spring Security 인증 성공시 특정 URL로 리다이렉트하는 역할을 하는 핸들러
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    @Value("${custom.site.frontUrl}")
    private String frontUrl;

    @Value("${custom.cookie.secure}")
    private boolean cookieSecure;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2UserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customOAuth2UserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        jwtUtil.createAccessAndRefresh(username, role,
                JwtConstants.ACCESS_TOKEN_EXPIRATION, JwtConstants.REFRESH_TOKEN_EXPIRATION, response);

        response.sendRedirect(frontUrl);
    }
}



