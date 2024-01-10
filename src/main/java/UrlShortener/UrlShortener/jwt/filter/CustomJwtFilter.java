package UrlShortener.UrlShortener.jwt.filter;

import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import UrlShortener.UrlShortener.jwt.token.CustomJwtToken;
import UrlShortener.UrlShortener.jwt.ResolveToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomJwtFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final ResolveToken resolveToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("filter start");
        //순수한 token으로 분리
        String jwt = resolveToken.resolveToken(request);
        //customToken 으로 넘겨주기
        CustomJwtToken customJwtToken = new CustomJwtToken(jwt);
        //customJwtToken은 authentication을 상속받아서 구현
        Authentication authentication = authenticationManager.authenticate(customJwtToken);

        //jwt 인증 방식이라서 따로 영속성 컨텍스트에 인증 객체를 저장하지 않음
        if(authentication.isAuthenticated()){
            log.info("사용자 인증 완료");
        }else{
            log.info("유효한 토큰이 아닙니다");
            throw new UnauthorizedException("유효한 토큰이 아닙디다");
        }
        filterChain.doFilter(request, response);
    }
}
