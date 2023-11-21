package UrlShortener.UrlShortener.responseDto;


import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.util.UrlGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ShortenUrlDto {

    @JsonIgnore
    private final UrlGenerator urlGenerator;
    
    private Long shortenUrlId;
    private String originUrl;
    private String shortenUrl;
    private Long memberId;
    private LocalDateTime createdShortenUrlDate;


    public ShortenUrlDto(UrlGenerator urlGenerator, ShortenUrl shortenUrl) {
        this.urlGenerator = urlGenerator;
        this.shortenUrlId = shortenUrl.getId();
       this.originUrl = shortenUrl.getOriginUrl();
       this.shortenUrl = this.urlGenerator.generator(shortenUrl.getShortenUrl());
       this.memberId = shortenUrl.getMember().getId();
       this.createdShortenUrlDate = shortenUrl.getCreateShortenUrlDate();
    }

}
