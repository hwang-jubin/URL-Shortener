package UrlShortener.UrlShortener.security;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class ClientUserDetails implements UserDetails {
    //loginId : username은 중복이 없음
    //말 그래도 username 이 아니고 uid 가 들어가ㅑ 하기 때문에 loginId를 받기로 함 -> loginId 는 중복검사를 하기 때문에 uid
    private String username;
    private String password;
    //계정 잠금 여부
    private boolean accountNonLocked = true;
    //사용자 계정 만료
    private boolean accountNonExpired = true;
    //비밀번호 만료
    private boolean credentialNonExpired = true;
    //사용자 활성화
    private boolean enable = true;
    //사용자 권한 목록
    //아직 권한에 대한 것에 대해서는 예정 없음
    private Collection<? extends GrantedAuthority> authorities = null;

    public ClientUserDetails(ClientUserDto clientUserDto) {
        this.username = clientUserDto.getLoginId();
        this.password = clientUserDto.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}
