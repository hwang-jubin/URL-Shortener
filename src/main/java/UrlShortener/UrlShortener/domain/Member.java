package UrlShortener.UrlShortener.domain;

import lombok.Getter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private LocalDateTime createdMemberDate;
    private LocalDateTime deletedMemberDate;

    @OneToMany(mappedBy = "member")
    private List<ShortenUrl> shortenUrlList = new ArrayList<>();

    // 객체 생성 막음
    protected Member() {
    }
    public Member encodedPassword(String encode) {
        this.password = encode;
        return this;
    }

//    public boolean matchesPassword(Member member){
//        member.getPassword();
//
//    }

}
