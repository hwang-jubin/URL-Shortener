package UrlShortener.UrlShortener.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static UrlShortener.UrlShortener.jwt.JwtAuthenticationFilter.AUTHORIZATION_HEADER;

@Component
public class ResolveToken {
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
