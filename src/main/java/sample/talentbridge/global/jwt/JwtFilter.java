package sample.talentbridge.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sample.talentbridge.domain.member.entity.Member;

@RequiredArgsConstructor
// OncePerRequestFilter => DispatcherServlet 앞단에서 실행되어서 중복실행을 막음
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = request.getHeader("access");
        // 토큰이 없는 경우
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 만료 검증
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // accessToken인지 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }



        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Member tmpMember = Member.builder()
                                      .username(username)
                                      .password("tempPassword")
                                      .role(role)
                                      .build();

        //권한 처리
        PrincipalDetails principalDetails = new PrincipalDetails(tmpMember);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                principalDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
