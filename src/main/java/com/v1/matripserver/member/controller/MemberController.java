package com.v1.matripserver.member.controller;

import com.v1.matripserver.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.v1.matripserver.member.dto.RequestDto.*;

@Tag(name = "Member 도메인", description = "사용자 CRUD API")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<?> createMember(
            @Valid
            @RequestBody
            CreateMemberDto createMemberDto
    ){
        memberService.createMember(createMemberDto);
        return ResponseEntity.ok().build();
    }
}
