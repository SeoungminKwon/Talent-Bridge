package sample.talentbridge.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sample.talentbridge.domain.member.entity.Member;
import sample.talentbridge.domain.member.exception.MemberNotFountException;
import sample.talentbridge.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
// OncePerRequestFilter => DispatcherServlet 앞단에서 실행되어서 중복실행을 막음
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        //헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 제거
        String token = authorization.split(" ")[1];

        // 만료 시간 검증
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(authorization);

        if (username != null) {
            Member findMember = memberRepository.findByUsername(username)
                                    .orElseThrow(() -> {
                                        throw new MemberNotFountException();
                                    });

            //권한 처리
            PrincipalDetails principalDetails = new PrincipalDetails(findMember);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                    principalDetails.getAuthorities());

            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
