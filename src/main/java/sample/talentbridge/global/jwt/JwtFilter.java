package sample.talentbridge.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sample.talentbridge.global.oauth2.CustomOAuth2User;
import sample.talentbridge.global.oauth2.dto.OAuth2UserDto;

@RequiredArgsConstructor
// OncePerRequestFilter => DispatcherServlet 앞단에서 실행되어서 중복실행을 막음
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        //헤더 검증
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 만료 시간 검증
        if (jwtUtil.isExpired(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(authorization);
        String role = jwtUtil.getRole(authorization);

        OAuth2UserDto userDto = new OAuth2UserDto();
        userDto.setUsername(username);
        userDto.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
