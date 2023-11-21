package UrlShortener.UrlShortener.security;

import UrlShortener.UrlShortener.jwt.JwtAuthenticationFilter;
import UrlShortener.UrlShortener.jwt.TokenProvider;
import UrlShortener.UrlShortener.repository.MemberRepository;
import UrlShortener.UrlShortener.util.ResolveToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//기본적인 web 보완 활성화 어노테이션
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final ResolveToken resolveToken;

    private final MemberRepository memberRepository;

    //passwordEncoder 의 구현체로 BcryptpasswordEncoder를 bean 으로 등록해서 주입
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    //인증, 인가
    //일부 경로는 token이 없어도 접근 가능하게 함
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                // 세션 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 인증이 필요한 api 와 아닌 api 를 구별
                .authorizeRequests()
                .antMatchers("/", "/member/signup","/member/login","/originUrl","/url","/2").permitAll()
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증이 필요한 경우 401 상태 코드 반환

                .and()
                // CSRF 보안 비활성화
                .csrf().disable()
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider, resolveToken), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
