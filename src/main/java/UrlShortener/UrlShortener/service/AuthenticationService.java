package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.config.CredentialConfig;
import UrlShortener.UrlShortener.jwt.TokenGenerator;
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

    public ResponseEntity login(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //이름? id?
        String jwt = tokenGenerator.generateToken(principal.toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        // response header에 jwt token에 넣어줌
        httpHeaders.add(CredentialConfig.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }
}
