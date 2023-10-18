package UrlShortener.UrlShortener.controller;


import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.repository.ShortenUrlRepository;
import UrlShortener.UrlShortener.responseDto.ShortenUrlDto;
import UrlShortener.UrlShortener.service.ShortenUrlService;
import UrlShortener.UrlShortener.util.UrlGenerator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UrlShortenerController {
    private final ShortenUrlRepository shortenUrlRepository;

    private final ShortenUrlService shortenUrlService;

    private final UrlGenerator urlGenerator;

    @PostMapping(value = "/url")
    public Result shortenUrlCreation(@RequestBody ShortenUrl shortenUrl){
        System.out.println("controller start");
        ShortenUrl newShortenUrl = shortenUrlService.createShortenUrl(shortenUrl);
        System.out.println("service end");
        ShortenUrlDto responseShortenUrlDto = new ShortenUrlDto(urlGenerator, shortenUrl);

        System.out.println("controller end");
        Result result = new Result(responseShortenUrlDto);
        System.out.println(result);
        return result;

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteShortenUrl(@PathVariable Long id){
        ResponseEntity responseEntity = shortenUrlService.deleteShortenUrl(id);
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

    @GetMapping(value="/{shortenUrl}")
    public void redirectUrl(@PathVariable String shortenUrl, HttpServletResponse httpServletResponse){


        try {
            shortenUrlService.redirectUrl(shortenUrl, httpServletResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Data
    static class Result <T>{
        private T data;

        public Result(T data) {
            this.data = data;
        }
    }


}
