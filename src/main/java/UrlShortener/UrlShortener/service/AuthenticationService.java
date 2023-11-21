package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.jwt.JwtAuthenticationFilter;
import UrlShortener.UrlShortener.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManagerBuilder authenticationManager;
    private final TokenProvider tokenProvider;

    public ResponseEntity login(Member member){

        //1. AuthenticationManager를 통해 인증을 시도하고 인증이 성공하면 Authentication 객체를 리턴받는다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getLoginId(), member.getPassword());

        // 해당 부분에 왔을 때 UserDetailsServiceImpl의 loadUserByUsername() 메소드가 실행된다.
        Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);

        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        String jwt = tokenProvider.generateToken(authentication.getName());

        HttpHeaders httpHeaders = new HttpHeaders();

        // response header에 jwt token에 넣어줌
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }



}
