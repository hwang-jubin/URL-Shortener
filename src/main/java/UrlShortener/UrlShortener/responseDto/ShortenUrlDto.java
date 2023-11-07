package UrlShortener.UrlShortener.responseDto;


import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.util.UrlGenerator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ShortenUrlDto {


    private final UrlGenerator urlGenerator;
    
    private Long shortenUrlId;
    private String originUrl;
    private String shortenUrl;
    private LocalDateTime createdShortenUrlDate;
    private LocalDateTime deletedShortenUrlDate;

    public ShortenUrlDto(UrlGenerator urlGenerator, ShortenUrl shortenUrl) {
        this.urlGenerator = urlGenerator;
        this.shortenUrlId = shortenUrl.getId();
       this.originUrl = shortenUrl.getOriginUrl();
       this.shortenUrl = this.urlGenerator.generator(shortenUrl.getShortenUrl());
       this.createdShortenUrlDate = shortenUrl.getCreateShortenUrlDate();
    }

}
