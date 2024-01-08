package UrlShortener.UrlShortener.security;

import UrlShortener.UrlShortener.config.CredentialConfig;
import UrlShortener.UrlShortener.jwt.JwtAuthenticationFilter;
import UrlShortener.UrlShortener.jwt.filter.CustomJwtFilter;
import UrlShortener.UrlShortener.jwt.filter.CustomLoginFilter;
import UrlShortener.UrlShortener.jwt.provider.CustomJwtProvider;
import UrlShortener.UrlShortener.jwt.TokenProvider;
import UrlShortener.UrlShortener.jwt.provider.CustomLoginProvider;
import UrlShortener.UrlShortener.jwt.token.CustomLoginToken;
import UrlShortener.UrlShortener.repository.MemberRepository;
import UrlShortener.UrlShortener.util.ResolveToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

//기본적인 web 보완 활성화 어노테이션
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final ResolveToken resolveToken;
    private final CustomJwtProvider customJwtProvider;
    private final ClientUserDetailService clientUserDetailService;
    private final CredentialConfig credentialConfig;

    //passwordEncoder 의 구현체로 BcryptpasswordEncoder를 bean 으로 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //인증, 인가
    @Bean
    public SecurityFilterChain LoginFilterChain(HttpSecurity http) throws Exception{

        http    // CSRF 보안 비활성화
                .csrf().disable()
                // 세션 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증이 필요한 경우 401 상태 코드 반환

                .and()
                .authorizeRequests()
                .antMatchers("/member/signup" ,"/public/**").permitAll()
                .and()
                .antMatcher("/member/login")
                .addFilterAt(new CustomLoginFilter(authenticationManager(null)), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityFilterChain tokenAuthenticationFilterChain(HttpSecurity http) throws Exception{

        http    // CSRF 보안 비활성화
                .csrf().disable()
                // 세션 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .antMatcher("/shorten/**")
                .addFilterAt(new CustomJwtFilter(authenticationManager(null),resolveToken), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }





    //providerManager의 목록에 customJwtProvider 등록
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new CustomJwtProvider(clientUserDetailService, credentialConfig));
        authenticationManagerBuilder.authenticationProvider(new CustomLoginProvider(clientUserDetailService, passwordEncoder()));
        return authenticationManagerBuilder.build();
    }

}
