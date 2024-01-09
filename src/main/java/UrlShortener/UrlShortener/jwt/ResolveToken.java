package UrlShortener.UrlShortener.jwt;

import UrlShortener.UrlShortener.config.CredentialConfig;
import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class ResolveToken {
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(CredentialConfig.AUTHORIZATION_HEADER);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }else{
            throw new UnauthorizedException("Authorization header 형식이 잘못 되었습니다");
        }
    }
}
