package UrlShortener.UrlShortener.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
public class ShortenUrl {

    @Id
    @GeneratedValue
    @Column(name = "shortenUrl_id")
    private Long id;

    @NotNull
    @NotBlank(message = "공백은 입력할 수 없습니다.")
    private String originUrl;

    private String shortenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @CreatedDate
    private LocalDateTime createShortenUrlDate;

    private LocalDateTime deleteShortenUrlDate;

    //객체 임의 생성 방지
    protected ShortenUrl() {

    }

    //
    public void saveEncodedUrl(String encodedUrl){
        this.shortenUrl = encodedUrl;
    }

    //삭제시간 컬럼에 삭제된 시간을 채우기
    public void checkingDeleteTime(){
        this.deleteShortenUrlDate = LocalDateTime.now();
    }


}
