package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.config.CredentialConfig;
import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.exception.customException.BadRequestException;
import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import UrlShortener.UrlShortener.jwt.token.CustomJwtToken;
import UrlShortener.UrlShortener.repository.MemberRepository;
import UrlShortener.UrlShortener.repository.ShortenUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final MemberRepository memberRepository;

    CustomJwtToken customJwtToken = null;

    /**
     * shortenUrl 생성
     * @param shortenUrl
     * @param request
     * @return
     */
    public ShortenUrl createShortenUrl(ShortenUrl shortenUrl, HttpServletRequest request) {
        //jwt에서 loginId 추출 후, shorthenUrl 생성시 추가
        String jwt = request.getHeader(CredentialConfig.AUTHORIZATION_HEADER);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof CustomJwtToken){
            customJwtToken = (CustomJwtToken) authentication;
        }else{
            //예외처리
        }
        //token loginId와 삭제할 shortenUrl의 member LogId를 비교해서 같아야 삭제할 수 있음
        String loginId = customJwtToken.getLoginId();

//         URL 유효성 검사 - 형식이 맞지 않으면 예외를 던짐
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(shortenUrl.getOriginUrl())) {
            log.info("형식에 맞지 않는 url={}", shortenUrl.getOriginUrl());
            throw new BadRequestException("올바른 url 형식을 입력해주세요.");
        }
            // db에 저장하면서 id 를 가지고 옴
            ShortenUrl savedshortenUrl = shortenUrlRepository.save(shortenUrl);

            // encoding 알고리즘
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

    /**
     * shortenUrl 삭제
     * @param id
     * @return
     */
    public ShortenUrl deleteShortenUrl(Long id) {
        Optional<ShortenUrl> shortenUrlId = shortenUrlRepository.findById(id);

        if(shortenUrlId.isEmpty()){
            throw new BadRequestException("삭제를 요청하신 URL이 존재하지 않습니다");
        }
        ShortenUrl shortenUrl = shortenUrlId.get();

        if(shortenUrl.getDeleteShortenUrlDate()!=null){
            throw new BadRequestException("이미 삭제된 URL 입니다");
        }

//        memberId 가 shortUrl 이 가지고 있는  memberId 와 같아야지 지울 수 있음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof CustomJwtToken){
            customJwtToken = (CustomJwtToken) authentication;
        }else{
            //예외처리
            throw new ClassCastException("Authentication is not an instance of CustomJwtToken");
        }
        //token loginId와 삭제할 shortenUrl의 member LogId를 비교해서 같아야 삭제할 수 있음
        String tokenLoginId = customJwtToken.getLoginId();
        String shortenUrlLoginId = shortenUrl.getMember().getLoginId();
        if(tokenLoginId == shortenUrlLoginId){
            shortenUrl.checkingDeleteTime();
            return shortenUrl;
        }else{
            throw new UnauthorizedException("삭제 권한이 없는 사용자 입니다");
        }
    }

    /**
     * 원본 url 로 redirect하기
     * @param shortenUrl
     * @param httpServletResponse
     * @throws IOException
     */
    public void redirectUrl(String shortenUrl, HttpServletResponse httpServletResponse) throws IOException {

        String originUrl = shortenUrlRepository.findByShortenUrl(shortenUrl).getOriginUrl();
        httpServletResponse.sendRedirect(originUrl);

    }

    /**
     * UrlList 가져오기
     * @param request
     * @return
     */
    public Member getUrlList(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof CustomJwtToken){
            customJwtToken = (CustomJwtToken) authentication;
        }else{
            //예외처리
            throw new ClassCastException("Authentication is not an instance of CustomJwtToken");
        }
        String loginId = customJwtToken.getLoginId();
        Optional<Member> member = memberRepository.findByLoginId(loginId);

        return member.get();
    }
}
