package UrlShortener.UrlShortener.jwt.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Objects;

//CustomLoginProvider가 사용하는 token 객체
public class CustomLoginToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credential;

    @JsonCreator
    public CustomLoginToken(@JsonProperty("loginId")String loginId , @JsonProperty("password")String password) {
        super(null);
        this.principal = loginId;
        this.credential = password;
        setAuthenticated(false);
    }
    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credential;
    }

    //detailService에서 id 검증 후 pw를 token에 넣어서 provider에게 전달
    public Object setCredential(String password){
        this.principal = password;
        return principal;
    }
}
