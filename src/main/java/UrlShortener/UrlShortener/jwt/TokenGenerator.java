package UrlShortener.UrlShortener.jwt;


import UrlShortener.UrlShortener.config.CredentialConfig;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final CredentialConfig credentialConfig;
        /**
         * token 생성
         */
        public String generateToken(String loginId) {
            //민감하지 않은 정보를 섞기
            return Jwts.builder()
                    .setSubject(loginId)
                    .setIssuedAt(credentialConfig.getNow())
                    .setExpiration(credentialConfig.getExpiryDate())
                    .signWith(credentialConfig.getKey(), SignatureAlgorithm.HS512)
                    .compact();
        }
}