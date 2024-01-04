package com.v1.matripserver.member.controller;

import com.v1.matripserver.member.dto.RequestDto;
import com.v1.matripserver.member.dto.ResponseDto;
import com.v1.matripserver.member.entity.MemberLink;
import com.v1.matripserver.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ResponseDto.LoginDto> login (
            @Valid
            @RequestBody
            RequestDto.LoginDto loginDto
    ) {
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    @Operation(summary = "사용자 정보 조회")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ResponseDto.MemberDto> getMember (
            @PathVariable
            Long memberId
    ) {
        return ResponseEntity.ok(memberService.getMyPageById(memberId));
    }

    @Operation(summary = "사용자 정보 수정")
    @PutMapping("/member/{memberId}")
    public ResponseEntity<?> updateMember (
            @PathVariable
            Long memberId,
            @RequestBody
            RequestDto.UpdateMemberDto updateMemberDto
    ) {
        memberService.updateMember(memberId, updateMemberDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 프로필 사진 추가")
    @PostMapping("/member/{memberId}/profile")
    public ResponseEntity<?> addProfile (
            @PathVariable
            Long memberId,
            @RequestBody
            RequestDto.AddProfileDto addProfileDto
    ) {
        memberService.addProfile(memberId, addProfileDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 프로필 사진 삭제")
    @DeleteMapping("/profile/{profileId}")
    public ResponseEntity<?> deleteProfile (
            @PathVariable
            Long profileId
    ) {
        memberService.deleteProfile(profileId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 소셜 링크 추가")
    @PostMapping("/member/{memberId}/link")
    public ResponseEntity<List<MemberLink>> addLink (
            @PathVariable
            Long memberId,
            @RequestBody
            RequestDto.AddLinkDto addLinkDto
    ) {
        return ResponseEntity.ok(memberService.addAndGetLink(memberId, addLinkDto));
    }

    @Operation(summary = "사용자 소셜 링크 삭제")
    @DeleteMapping("/link/{linkId}")
    public ResponseEntity<?> deleteLink (
            @PathVariable
            Long linkId
    ) {
        memberService.deleteLink(linkId);
        return ResponseEntity.ok().build();
    }

}
