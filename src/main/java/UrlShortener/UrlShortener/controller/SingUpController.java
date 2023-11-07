package UrlShortener.UrlShortener.controller;

import UrlShortener.UrlShortener.domain.Member;
import UrlShortener.UrlShortener.responseDto.SingUpMemberDto;
import UrlShortener.UrlShortener.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SingUpController {

    private final MemberService memberService;

    @PostMapping(value = "/member/signup")
    public Result<SingUpMemberDto> singUp(@RequestBody @Valid Member member){

        Member createdMember = memberService.CreateMember(member);
        SingUpMemberDto singUpMemberDto = new SingUpMemberDto(member);
        Result<SingUpMemberDto> result = new Result<>(singUpMemberDto);

        return result;
    }


    @Data
    static class Result <T>{
        private T data;

        public Result(T data) {
            this.data = data;
        }
    }


}
