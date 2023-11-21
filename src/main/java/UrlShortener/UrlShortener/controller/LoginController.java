package UrlShortener.UrlShortener.controller;

import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/member/login")
    public ResponseEntity login(@RequestBody  Member member ){

        ResponseEntity response = authenticationService.login(member);

        return response;
    }

}
