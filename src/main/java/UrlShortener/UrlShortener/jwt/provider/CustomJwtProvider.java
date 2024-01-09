package UrlShortener.UrlShortener.jwt.provider;

import UrlShortener.UrlShortener.config.CredentialConfig;
import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import UrlShortener.UrlShortener.jwt.token.CustomJwtToken;
import UrlShortener.UrlShortener.jwt.token.CustomLoginToken;
import UrlShortener.UrlShortener.security.ClientUserDetailService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtProvider implements AuthenticationProvider {

    private final ClientUserDetailService clientUserDetailService;
    private final CredentialConfig credentialConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        CustomJwtToken customJwtToken = null;

        //interface인 authentication 의 자식으로 customJwtToken가 넘어온다면
        if (authentication instanceof Authentication) {
            //interface 객체인 authentication을 구현체인 customjwttoken으로 class 캐스팅(부모->자식)
            customJwtToken = (CustomJwtToken) authentication;
        } else {
            // exception handling
            //server 가 뜰 때 error 를 일으키게 하기
        }

        //token 분해 해서 loginId와 pw 를 추출하기
        //CustomJwtToken에서 token 객체 가져오기
        String jwt = (String) authentication.getPrincipal();

        //key 를 이용해서 코드를 열고, loginId 및 만료시간을 검증하는 절차
            try {
                //claim 정보
                Claims claims = Jwts.parser()
                        .setSigningKey(credentialConfig.getSecret())
                        .parseClaimsJws(jwt)
                        .getBody();

                //claim 에서 loginId 추출해서 loginId 검증
                String loginId = claims.getSubject();
                UserDetails userDetails = clientUserDetailService.loadUserByUsername(loginId);
                //토큰에 loginId 저장
                customJwtToken.setLoginId(userDetails.getUsername());

                //claim 에서 만료 시간 가져와서 만료일자 검증
                Date expiration = claims.getExpiration();
                Date now = new Date();

                if (expiration != null && now.after(expiration)) {
                    // 토큰이 만료된 경우
                    throw new UnauthorizedException("만료된 토큰입니다");
                }
                SecurityContextHolder.getContext().setAuthentication(customJwtToken);
            } catch (SignatureException e) {
                // 서명(key)이 올바르지 않은 경우 또는 토큰이 만료된 경우
                throw new UnauthorizedException("유효한 토큰이 아닙니다");
            }catch (SecurityException | MalformedJwtException e) {
                log.info("잘못된 JWT 서명입니다");
                throw new UnauthorizedException("잘못된 JWT 서명입니다");
            } catch (ExpiredJwtException e) {
                log.info("만료된 JWT 토큰입니다");
                throw new UnauthorizedException("만료된 JWT 토큰입니다");
            } catch (UnsupportedJwtException e) {
                log.info("지원되지 않는 JWT 토큰입니다");
                throw new UnauthorizedException("지원되지 않는 JWT 토큰입니다");
            } catch (IllegalArgumentException e) {
                log.info("JWT 토큰이 잘못되었습니다");
                throw new UnauthorizedException("JWT 토큰이 잘못되었습니다");
            }

            //인증완료 되었다는 표시하기
            authentication.setAuthenticated(true);

            return authentication;
        }

    //ProviderManager에서 어떤 프로바이더가 어떤 종류의 Authentication 객체를 처리할지 결정
    @Override
    public boolean supports(Class<?> authentication) {
        //여기에 customJwtToken을 인증객체로 사용한다고 표시 하기
        return CustomJwtToken.class.isAssignableFrom(authentication);
    }
}
