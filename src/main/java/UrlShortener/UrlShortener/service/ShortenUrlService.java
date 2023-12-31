package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.exception.customException.BadRequestException;
import UrlShortener.UrlShortener.jwt.JwtAuthenticationFilter;
import UrlShortener.UrlShortener.jwt.TokenProvider;
import UrlShortener.UrlShortener.repository.MemberRepository;
import UrlShortener.UrlShortener.repository.ShortenUrlRepository;
import UrlShortener.UrlShortener.util.ResolveToken;
import UrlShortener.UrlShortener.util.UrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final ResolveToken resolveToken;

    public ShortenUrl createShortenUrl(ShortenUrl shortenUrl, HttpServletRequest request) {
        //jwt에서 loginId 추출 후, shorthenUrl 생성시 추가
        String jwt = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);

        String loginId = tokenProvider.getLoginIdFromToken(resolveToken.resolveToken(request));

//         URL 유효성 검사 - 형식이 맞지 않으면 예외를 던짐
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(shortenUrl.getOriginUrl())) {
            log.info("형식에 맞지 않는 url={}", shortenUrl.getOriginUrl());
            System.out.println("잘못된 url 요청");
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

            //entity에 member, shortenUrl 저장
            Optional<Member> member = memberRepository.findByLoginId(loginId);
            //entity에 shortenurl 저장
            shortenUrl.saveEncodedUrlAndMember(encodedUrl, member.get());

        return shortenUrl;

    }

    public ResponseEntity deleteShortenUrl(Long id) {
        Optional<ShortenUrl> shortenUrlId = shortenUrlRepository.findById(id);

        if(shortenUrlId.isEmpty()){
           return ResponseEntity.badRequest().body("요청하신 Id에 해당하는 URL이 존재하지 않습니다");

        }
        ShortenUrl shortenUrl = shortenUrlId.get();

        if(shortenUrl.getDeleteShortenUrlDate()!=null){
            return ResponseEntity.badRequest().body("이미 삭제된 URL입니다");
        }

        shortenUrl.checkingDeleteTime();

        return ResponseEntity.ok(shortenUrl);
    }


    public void redirectUrl(String shortenUrl, HttpServletResponse httpServletResponse) throws IOException {

        String originUrl = shortenUrlRepository.findByShortenUrl(shortenUrl).getOriginUrl();
        httpServletResponse.sendRedirect(originUrl);

    }
}
