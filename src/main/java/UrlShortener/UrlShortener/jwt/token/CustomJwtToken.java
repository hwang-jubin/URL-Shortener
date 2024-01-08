package UrlShortener.UrlShortener.jwt.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//TokenAuthorizationProvider가 사용하는 token객체
//customJwtToken을 사용하는 provider를 manager가 찾아가기 위해서 filter에서부터 token객체 만들어서 manager에게 전달
public class CustomJwtToken extends AbstractAuthenticationToken {

    //토큰
    private String token;
    //loginId
    @Getter @Setter
    private String loginId;

    //인증완료 토큰
    public CustomJwtToken(String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        setAuthenticated(true);
    }

    //인증안된 사용자 token
    //filter에서 authenticationManager에게 객체 넘겨줄 때 사용
    public CustomJwtToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    public void setAuthenticated(){
        setAuthenticated(true);
    }
//
//    public void setLoginId(String loginId){
//        this.loginId = loginId;
//    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }


}
