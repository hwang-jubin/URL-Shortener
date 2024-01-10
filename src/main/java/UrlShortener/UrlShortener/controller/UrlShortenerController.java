package UrlShortener.UrlShortener.controller;


import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.responseDto.ShortenUrlCreationDto;
import UrlShortener.UrlShortener.responseDto.ShortenUrlDeleteDto;
import UrlShortener.UrlShortener.responseDto.ShortenUrlListDto;
import UrlShortener.UrlShortener.service.ShortenUrlService;
import UrlShortener.UrlShortener.util.UrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        ShortenUrlCreationDto responseShortenUrlCreationDto = new ShortenUrlCreationDto(urlGenerator, shortenUrl);
        Result result = new Result(responseShortenUrlCreationDto);

        return result;
    }

    /**
     * shortenUrl 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/shorten/{id}")
    public Result<ShortenUrlDeleteDto> deleteShortenUrl(@PathVariable Long id){

        ShortenUrl shortenUrl = shortenUrlService.deleteShortenUrl(id);
        ShortenUrlDeleteDto shortenUrlDeleteDto = new ShortenUrlDeleteDto(urlGenerator,shortenUrl);
        Result<ShortenUrlDeleteDto> result = new Result<>(shortenUrlDeleteDto);

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
