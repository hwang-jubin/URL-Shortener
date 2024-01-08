package UrlShortener.UrlShortener.security;


import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.exception.customException.CustomUnauthorizedException;
import UrlShortener.UrlShortener.exception.customException.UnauthorizedException;
import UrlShortener.UrlShortener.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ClientUserDetailService implements UserDetailsService {
    
    private final MemberRepository memberRepository;
    
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        //loginId 를 db에서 조회 한 후 없으면 exception
        //있으면 userDetails를 구현한 구현체를 반환
        Optional<Member> findLonginId = memberRepository.findByLoginId(loginId);
        if(findLonginId.isEmpty()){
            log.info("해당 Id에 해당하는 사용자가 없습니다 :" + loginId);
            throw new CustomUnauthorizedException(loginId + "에 해당하는 사용자가 없습니다");
        }
        ClientUserDto clientUserDto = new ClientUserDto(findLonginId.get());

        return new ClientUserDetails(clientUserDto);
    }
}
