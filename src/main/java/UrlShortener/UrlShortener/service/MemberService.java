package UrlShortener.UrlShortener.service;

import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.domain.ShortenUrl;
import UrlShortener.UrlShortener.exception.customException.BadRequestException;
import UrlShortener.UrlShortener.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member CreateMember(Member member){

        String loginId = member.getLoginId();
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);

        if(findMember.isPresent()){

            throw new BadRequestException("중복된 id가 있습니다. 다른 id 를 입력해주세요.");
        }

        //encoding 된 password를 저장
        member.encodedPassword(passwordEncoder.encode(member.getPassword()));

        //db에 저장한 후
        Member savedMember = memberRepository.save(member);

        //저장된 entity를 반환
        return savedMember;
    }
}
