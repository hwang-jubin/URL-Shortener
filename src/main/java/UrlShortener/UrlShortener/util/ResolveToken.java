package UrlShortener.UrlShortener.util;

import UrlShortener.UrlShortener.config.CredentialConfig;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class ResolveToken {
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(CredentialConfig.AUTHORIZATION_HEADER);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
