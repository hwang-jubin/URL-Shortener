package UrlShortener.UrlShortener.jwt.filter;

import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import UrlShortener.UrlShortener.jwt.token.CustomLoginToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ObjectMapper objectMapper = new ObjectMapper();
        CustomLoginToken customLoginToken = objectMapper.readValue(request.getInputStream(), CustomLoginToken.class);
        Authentication authentication = authenticationManager.authenticate(customLoginToken);

        if(authentication.isAuthenticated()){
            log.info("사용자 인증 완료");
        }else{
            log.info("회원이 아닙니다");
            throw new UnauthorizedException("회원이 아닙니다");
        }
        filterChain.doFilter(request, response);
    }
}
