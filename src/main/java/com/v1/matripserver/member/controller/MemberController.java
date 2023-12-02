package com.v1.matripserver.member.controller;

import com.v1.matripserver.member.dto.RequestDto;
import com.v1.matripserver.member.dto.ResponseDto;
import com.v1.matripserver.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member 도메인", description = "사용자 CRUD API")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<?> join (
            @Valid
            @RequestBody
            RequestDto.JoinDto joinDto
    ) {
        memberService.join(joinDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto.loginDto> login (
            @Valid
            @RequestBody
            RequestDto.LoginDto loginDto
    ) {
        return ResponseEntity.ok(memberService.login(loginDto));
    }
}
