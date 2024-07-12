package sample.talentbridge.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Map<String, String> parsedRequestMap = parseLoginRequest(request);
        String username = parsedRequestMap.get("username");
        String password = parsedRequestMap.get("password");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    // 검증 성공시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        GrantedAuthority auth = authorities.iterator().next();
        String role = auth.getAuthority();

        jwtUtil.createAccessAndRefresh(username, role,
                JwtConstants.ACCESS_TOKEN_EXPIRATION, JwtConstants.REFRESH_TOKEN_EXPIRATION, response);
    }

    // 검증 실패시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }

    private Map<String, String> parseLoginRequest(HttpServletRequest request) {
        try(BufferedReader reader = request.getReader()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, Map.class);

        } catch (IOException e) {
            throw new BadCredentialsException("인증과정중 본문을 읽지 못했습니다.");
        }
    }
}
