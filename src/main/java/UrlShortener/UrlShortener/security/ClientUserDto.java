package UrlShortener.UrlShortener.security;

import UrlShortener.UrlShortener.domain.Member;
import lombok.Getter;

@Getter
public class ClientUserDto {
    // userDetails 의 username
    private String loginId;
    private String password;

    public ClientUserDto(Member member) {
        this.loginId = member.getLoginId();
        this.password = member.getPassword();
    }
}
