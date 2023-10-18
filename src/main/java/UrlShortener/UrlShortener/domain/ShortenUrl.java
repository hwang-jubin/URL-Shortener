package UrlShortener.UrlShortener.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ShortenUrl {

    @Id
    @GeneratedValue
    @Column(name = "shortenUrl_id")
    private Long id;

    @NotNull
    private String originUrl;
    private String shortenUrl;

    @CreatedDate
    private LocalDateTime createShortenUrlDate;

    private LocalDateTime deleteShortenUrlDate;


}
