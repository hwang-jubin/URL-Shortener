package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.config.CredentialConfig;
import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.jwt.TokenGenerator;
import UrlShortener.UrlShortener.repository.MemberRepository;
import UrlShortener.UrlShortener.responseDto.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final TokenGenerator tokenGenerator;
    private final MemberRepository memberRepository;

    public ResponseEntity<LoginDto> login(){

        String loginId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String jwt = tokenGenerator.generateToken(loginId);

        Member member = memberRepository.findByLoginId(loginId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        // response header에 jwt token에 넣어줌
        httpHeaders.add(CredentialConfig.AUTHORIZATION_HEADER, "Bearer " + jwt);

        LoginDto loginDto = new LoginDto(member);
        return new ResponseEntity<>(loginDto,httpHeaders, HttpStatus.OK);
    }
}
