package UrlShortener.UrlShortener.responseDto;

import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.util.UrlGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShortenUrlDeleteDto {

    @JsonIgnore
    private final UrlGenerator urlGenerator;

    private Long shortenUrlId;
    private String originUrl;
    private String shortenUrl;
    private Long memberId;
    private LocalDateTime createdShortenUrlDate;
    private LocalDateTime deleteShortenUrlDate;

    public ShortenUrlDeleteDto(UrlGenerator urlGenerator, ShortenUrl shortenUrl) {
        this.urlGenerator = urlGenerator;
        this.shortenUrlId = shortenUrl.getId();
        this.originUrl = shortenUrl.getOriginUrl();
        this.shortenUrl = this.urlGenerator.generator(shortenUrl.getShortenUrl());
        this.memberId = shortenUrl.getMember().getId();
        this.createdShortenUrlDate = shortenUrl.getCreateShortenUrlDate();
        this.deleteShortenUrlDate = shortenUrl.getDeleteShortenUrlDate();
    }
}
