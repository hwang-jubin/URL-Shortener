package UrlShortener.UrlShortener.controller;


import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.repository.ShortenUrlRepository;
import UrlShortener.UrlShortener.responseDto.ShortenUrlDto;
import UrlShortener.UrlShortener.responseDto.ShortenUrlListDto;
import UrlShortener.UrlShortener.service.ShortenUrlService;
import UrlShortener.UrlShortener.util.UrlGenerator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UrlShortenerController {
    private final ShortenUrlRepository shortenUrlRepository;

    private final ShortenUrlService shortenUrlService;

    private final UrlGenerator urlGenerator;

    @PostMapping(value = "/url")
    public Result shortenUrlCreation(@RequestBody ShortenUrl shortenUrl , HttpServletRequest httpServletRequest){
        ShortenUrl newShortenUrl = shortenUrlService.createShortenUrl(shortenUrl , httpServletRequest);
        ShortenUrlDto responseShortenUrlDto = new ShortenUrlDto(urlGenerator, shortenUrl);
        Result result = new Result(responseShortenUrlDto);

        return result;

    }

    @DeleteMapping(value = "{id}")
    public Result deleteShortenUrl(@PathVariable Long id, HttpServletRequest request){
        ShortenUrl shortenUrl = shortenUrlService.deleteShortenUrl(id, request);
        ShortenUrlDto shortenUrlDto = new ShortenUrlDto(urlGenerator, shortenUrl);
        Result<ShortenUrlDto> result = new Result<>(shortenUrlDto);
        return result;
    }

    @GetMapping(value="/shorten/{shortenUrl}")
    public void redirectUrl(@PathVariable String shortenUrl, HttpServletResponse httpServletResponse){
        try {
            shortenUrlService.redirectUrl(shortenUrl, httpServletResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value ="/urls")
    public Result UrlList(HttpServletRequest request){

        Member member = shortenUrlService.getUrlList(request);
        ShortenUrlListDto urlListDto = new ShortenUrlListDto(member,urlGenerator);
        Result result = new Result(urlListDto);
        log.info(String.valueOf(result));

        return result;
    }


    @Data
    static class Result <T>{
        private T data;

        public Result(T data) {
            this.data = data;
        }
    }


}
