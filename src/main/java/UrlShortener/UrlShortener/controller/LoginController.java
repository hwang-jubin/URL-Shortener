package UrlShortener.UrlShortener.controller;

import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.security.ClientUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final UserDetailsService userDetailsService;

    @PostMapping(value = "/member/login")
    public String login(@RequestBody  Member member ){
        UserDetails userDetails = userDetailsService.loadUserByUsername(member.getLoginId());
        return "환영합니다";
    }

}
