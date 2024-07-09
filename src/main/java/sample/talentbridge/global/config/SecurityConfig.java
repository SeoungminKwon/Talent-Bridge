package sample.talentbridge.global.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import sample.talentbridge.global.jwt.JwtFilter;
import sample.talentbridge.global.jwt.JwtUtil;
import sample.talentbridge.global.oauth2.CustomOAuth2UserService;
import sample.talentbridge.global.oauth2.CustomSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtUtil jwtUtil;

    @Value("${custom.site.frontUrl}")
    private String frontUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //cors 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList(frontUrl));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //JwtFilter 추가
        http
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                                                 .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                                                                                       .userService(customOAuth2UserService))
                                                 .successHandler(customSuccessHandler)
                );

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                                                         .requestMatchers("/").permitAll()
                                                         .requestMatchers("/api/v1/members/**").permitAll()
                                                         .requestMatchers("/h2-console/**").permitAll()
                                                         .anyRequest().authenticated());

        // X-Frame-Options 비활성화
        http.headers(headers -> headers
                                        .frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
