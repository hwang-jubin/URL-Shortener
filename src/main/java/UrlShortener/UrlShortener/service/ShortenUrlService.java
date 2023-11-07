package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.exception.customException.BadRequestException;
import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.repository.ShortenUrlRepository;
import UrlShortener.UrlShortener.util.UrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;
    private final UrlGenerator urlGenerator;

    public ShortenUrl createShortenUrl(ShortenUrl shortenUrl) {

//         URL 유효성 검사 - 형식이 맞지 않으면 예외를 던짐
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(shortenUrl.getOriginUrl())) {
            log.info("형식에 맞지 않는 url={}", shortenUrl.getOriginUrl());
            throw new BadRequestException("올바른 url 형식을 입력해주세요.");
        }

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
                id /= 62;
            }
            //배열을 String으로
            String encodedUrl = result.toString();

            //entity에 shortenurl 저장
            shortenUrl.saveEncodedUrl(encodedUrl);

            return shortenUrl;

    }

    public ShortenUrl deleteShortenUrl(Long id) {
        Optional<ShortenUrl> shortenUrlId = shortenUrlRepository.findById(id);

        if(shortenUrlId.isEmpty()){
            log.error("해당 id 에 대한 url을 찾을 수 없습니다");
           throw new BadRequestException("해당 ID에 대한 URL을 찾을 수 없습니다");
        }
        ShortenUrl shortenUrl = shortenUrlId.get();
        shortenUrl.checkingDeleteTime();

        return shortenUrl;
    }

    public void redirectUrl(String shortenUrl, HttpServletResponse httpServletResponse) throws IOException {

        String originUrl = shortenUrlRepository.findByShortenUrl(shortenUrl).getOriginUrl();
        httpServletResponse.sendRedirect(originUrl);

    }
}
