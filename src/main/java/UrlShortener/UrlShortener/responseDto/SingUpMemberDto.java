package UrlShortener.UrlShortener.responseDto;

import UrlShortener.UrlShortener.domain.Member;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class SingUpMemberDto {

    private Long id;
    private String loginId;
    private String username;
    private LocalDateTime createdMemberDate;

    public SingUpMemberDto(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.username = member.getUsername();
        this.createdMemberDate = member.getCreatedMemberDate();
    }
}
