package UrlShortener.UrlShortener.security;

import UrlShortener.UrlShortener.config.CredentialConfig;
import UrlShortener.UrlShortener.exception.customHandler.CustomSecurityHandler;
import UrlShortener.UrlShortener.jwt.filter.CustomJwtFilter;
import UrlShortener.UrlShortener.jwt.filter.CustomLoginFilter;
import UrlShortener.UrlShortener.jwt.provider.CustomJwtProvider;
import UrlShortener.UrlShortener.jwt.provider.CustomLoginProvider;
import UrlShortener.UrlShortener.jwt.ResolveToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//기본적인 web 보완 활성화 어노테이션
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig{

    private final ResolveToken resolveToken;
    private final ClientUserDetailService clientUserDetailService;
    private final CredentialConfig credentialConfig;
    private final CustomSecurityHandler customSecurityHandler;

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
                .authorizeRequests()
                .antMatchers("/member/signup" ,"/public/**").permitAll()
                .and()
                .antMatcher("/member/login")
                .addFilterAt(new CustomLoginFilter(authenticationManager(null)), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception)->exception.accessDeniedHandler(customSecurityHandler));


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
                .antMatcher("/shorten/**")
                .addFilterAt(new CustomJwtFilter(authenticationManager(null),resolveToken), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception)->exception.accessDeniedHandler(customSecurityHandler));

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
