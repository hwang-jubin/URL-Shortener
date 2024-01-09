package UrlShortener.UrlShortener.jwt.provider;

import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import UrlShortener.UrlShortener.jwt.token.CustomJwtToken;
import UrlShortener.UrlShortener.jwt.token.CustomLoginToken;
import UrlShortener.UrlShortener.repository.MemberRepository;
import UrlShortener.UrlShortener.security.ClientUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginProvider implements AuthenticationProvider {

    private final ClientUserDetailService clientUserDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserDetails userDetails = clientUserDetailService.loadUserByUsername((String) authentication.getPrincipal());

        String encodedPassword = bCryptPasswordEncoder.encode(authentication.getCredentials().toString());

        if(bCryptPasswordEncoder.matches(authentication.getCredentials().toString(),userDetails.getPassword())){
            log.info("비밀번호가 인증되었습니다");
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        }else{
            log.info("비밀번호가 일치하지 않습니다");
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomLoginToken.class.isAssignableFrom(authentication);
    }
}
