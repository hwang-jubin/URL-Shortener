package UrlShortener.UrlShortener.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



@Slf4j
@Component
public class TokenProvider {

        @Value("${jwt.token.secret}")
        private String secret;
        //secret 을 이용하여 signature 생성
        private Key key;

        /**
         * 생성자
         */
        public TokenProvider() {
            byte[] keyBytes = Decoders.BASE64.decode("anViaW5mbGdramZkc2xramtkZmpka2ZqZGtrZGpma2RqZmRrZmpzbGtqYSfri5A764yA44WU6rC464yc44WQ44WP44S544S0O+OFo+OFh+2UoOumji/jhYHtgrvjhYcuLOOFiu2UnS7jhaHjhYrtk4zjhaHrgq3tlJQ744Wj652864yAW+qwuOuMnOOFk+2eiOOFj+2UmyzjhY0v44WBO+uLnSfjhYHsl5DjhY/rnrTslaDtl4jrgpjrp4bjhY3jhY3jhY3jhLk764yA44WT44S544WO44Sx");
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }

        /**
         * token 생성
         */
        public String generateToken(String loginId) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + 3600000); // 1시간
            //민감하지 않은 정보를 섞기
            return Jwts.builder()
                    .setSubject(loginId)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }

        /**
         *토큰을 받아 클레임을 만들고 권한 정보를 빼서 시큐리티 유저 객체를 만들어 Authentication 객체 반환
         */
        public Authentication getAuthentication(String token) {

            //비밀 키를 넣어서 parser를 생성 후 claim 을 추출
            //claim 은 일반적으로 사용자의 정보, 권한 등을 담고 있음
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            //I AM과 같은 기능은 사용 계획 없음
//            //body 에서 auth 부분을 ',' 으로 끊은 다음, SimpleGrantedAuthority의 기본 생성자를 호출하여 새로운 SimpleGrantedAuthority 객체로 만들고
//            //GrantedAuthority interface 형으 List 로 반환
//            Collection<? extends GrantedAuthority> authorities =
//                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                            .map(SimpleGrantedAuthority::new)
//                            .collect(Collectors.toList());

            //principal(주체), credentials(자격 증명), 그리고 부여된 권한(authorities) 등을 포함
            //부여된 권한은 생략
            return new UsernamePasswordAuthenticationToken(claims.getSubject(),token);
        }

        /**
         * spring이 제공하는 Jwts 가 string 으로 이루어져있는 jwt를 분석해서 loginId를 추출하는 과정
         * 없으면 null
         */
        public String getLoginIdFromToken(String jwt){
            try {
                String loginId = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();

                return loginId;
            } catch (SignatureException e) {
                // 서명이 올바르지 않은 경우 또는 토큰이 만료된 경우
                return null;
            }

        }

        /**
         * 토큰 유효성 검사
         * @param token
         * @return
         */
        public boolean validateToken(String jwt) {
            try {
                //token을 기반으로 signature를 섞어서 암호화 한 jwt 를
                //다시 signature 를 이용해서 claim(body) 을 얻음
                log.info(jwt);
                Claims claims = Jwts.parser().setSigningKey(key.getEncoded()).parseClaimsJws(jwt).getBody();
                return true;
            } catch (SecurityException | MalformedJwtException e) {
                log.info("잘못된 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                log.info("만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                log.info("지원되지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {
                log.info("JWT 토큰이 잘못되었습니다.");
            }
            return false;
        }
}