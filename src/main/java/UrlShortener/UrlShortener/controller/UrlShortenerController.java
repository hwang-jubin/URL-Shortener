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

    private final ShortenUrlService shortenUrlService;
    private final UrlGenerator urlGenerator;

    /**
     * shortenUrl 생성 api
     * @param shortenUrl
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/shorten/url")
    public Result shortenUrlCreation(@RequestBody ShortenUrl shortenUrl , HttpServletRequest httpServletRequest){
        ShortenUrl newShortenUrl = shortenUrlService.createShortenUrl(shortenUrl , httpServletRequest);
        ShortenUrlDto responseShortenUrlDto = new ShortenUrlDto(urlGenerator, shortenUrl);
        Result result = new Result(responseShortenUrlDto);

        return result;
    }

    /**
     * shortenUrl 삭제
     * @param id
     * @return
     */
    @DeleteMapping(value = "/shorten/{id}")
    public Result<ShortenUrl> deleteShortenUrl(@PathVariable Long id){

        ShortenUrl shortenUrl = shortenUrlService.deleteShortenUrl(id);
        Result<ShortenUrl> result = new Result<>(shortenUrl);

        return result;
    }

    /**
     * shortenUrl 을 원본 url 로 redirect
     * @param shortenUrl
     * @param httpServletResponse
     */
    @GetMapping(value="/public/shorten/{shortenUrl}")
    public void redirectUrl(@PathVariable String shortenUrl, HttpServletResponse httpServletResponse){
        try {
            shortenUrlService.redirectUrl(shortenUrl, httpServletResponse);
        } catch (IOException e) {
            throw new RuntimeException("redirect에 실패했습니다");
        }
    }

    /**
     * list 보여주는 api
     * @param request
     * @return
     */
    @GetMapping(value ="/shorten/urls")
    public Result UrlList(HttpServletRequest request){

        Member member = shortenUrlService.getUrlList(request);
        ShortenUrlListDto urlListDto = new ShortenUrlListDto(member,urlGenerator);
        Result result = new Result(urlListDto);
        log.info(String.valueOf(result));

        return result;
    }
}
