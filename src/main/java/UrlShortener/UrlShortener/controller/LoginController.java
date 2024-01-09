package UrlShortener.UrlShortener.controller;

import UrlShortener.UrlShortener.responseDto.LoginDto;
import UrlShortener.UrlShortener.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/member/login")
    public ResponseEntity<LoginDto> login(){
        ResponseEntity<LoginDto> login = authenticationService.login();

        return login;
    }
}
