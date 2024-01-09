package UrlShortener.UrlShortener.responseDto;

import UrlShortener.UrlShortener.domain.Member;
import lombok.Data;

@Data
public class LoginDto {

    private Long memberId;
    private String loginId;
    private String username;

    public LoginDto(Member member){
        this.memberId= member.getId();
        this.loginId = member.getLoginId();
        this.username = member.getUsername();

    }
}
