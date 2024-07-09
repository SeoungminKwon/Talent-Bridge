package sample.talentbridge.domain.member.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.talentbridge.domain.member.dto.MemberResponseDto;
import sample.talentbridge.domain.member.dto.MemberSignupRequestDto;
import sample.talentbridge.domain.member.entity.Member;
import sample.talentbridge.domain.member.service.MemberService;
import sample.talentbridge.global.response.ResponseMessage;
import sample.talentbridge.global.response.StatusEnum;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity<ResponseMessage> signUpMember(@Valid @RequestBody MemberSignupRequestDto signupRequestDto) {
        Member newMember = memberService.createMember(signupRequestDto);
        MemberResponseDto memberResponseDto = new MemberResponseDto(newMember);
        ResponseMessage message = new ResponseMessage(StatusEnum.OK, "성공", memberResponseDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
