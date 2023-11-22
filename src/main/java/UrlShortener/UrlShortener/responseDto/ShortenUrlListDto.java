package UrlShortener.UrlShortener.responseDto;

import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.exception.customException.BadRequestException;
import UrlShortener.UrlShortener.util.UrlGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ShortenUrlListDto {

    private Long memberId;
    private List<ListDto> shortenUrlList;


    public ShortenUrlListDto(Member member, UrlGenerator urlGenerator) {
        this.memberId = member.getId();
        //shortenUrl의 list 를 stream으로 돌면서 Dto로 바꿔서 반환
        //shortenUrl에서 필요한 부분만 추출해서 보여줄 수 있음
        this.shortenUrlList = member.getShortenUrlList().stream()
                .map(shortenUrl -> {

                    if(shortenUrl.getDeleteShortenUrlDate()==null){
                        ListDto listDto = new ListDto();
                        listDto.setOriginUrl(shortenUrl.getOriginUrl());
                        listDto.setShortenUrl(urlGenerator.generator(shortenUrl.getShortenUrl()));
                        return listDto;
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }


    //shortenUrl entity를 Dto로 만들기
    @Data
    public class ListDto{
        private String originUrl;
        private String shortenUrl;
    }


}
