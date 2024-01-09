package UrlShortener.UrlShortener.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Date;

@Configuration
@Getter
public class CredentialConfig {

    private String secret = "anViaW5mbGdramZkc2xramtkZmpka2ZqZGtrZGpma2RqZmRrZmpzbGtqYSfri5A764yA44WU6rC464yc44WQ44WP44S544S0O+OFo+OFh+2UoOumji/jhYHtgrvjhYcuLOOFiu2UnS7jhaHjhYrtk4zjhaHrgq3tlJQ744Wj652864yAW+qwuOuMnOOFk+2eiOOFj+2UmyzjhY0v44WBO+uLnSfjhYHsl5DjhY/rnrTslaDtl4jrgpjrp4bjhY3jhY3jhY3jhLk764yA44WT44S544WO44Sx";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    //secret 을 이용하여 signature 생성
    private Key key;
    Date now = new Date();
    private Date expiryDate = new Date(now.getTime() + 3600000); // 1시간

    public CredentialConfig() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
