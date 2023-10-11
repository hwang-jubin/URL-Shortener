package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.repository.ShortenUrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;


    @Transactional
    public ShortenUrl createShortenUrl(ShortenUrl shortenUrl) {

        //url 검증 제대로 된 형식의 url 인지 확인
        UrlValidator urlValidator = new UrlValidator();
        if(urlValidator.isValid(shortenUrl.getOriginUrl())){
            // db에 저장하면서 id 를 가지고 옴
            ShortenUrl savedshortenUrl = shortenUrlRepository.save(shortenUrl);
            // encoding 알고리듬
            Long id = savedshortenUrl.getId();

            //Base62 encoding
            String BASE62_CHARS="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            StringBuilder result = new StringBuilder();
            while(id > 0){
                int remainder = (int) (id % 62);
                result.insert(0, BASE62_CHARS.charAt(remainder));
            }

            String encodedUrl = result.toString();

        }


    }

    @Transactional
    public int deleteShortenUrl() {

        return 0;
    }
}
