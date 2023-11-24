package com.v1.matripserver.member.service;

import com.v1.matripserver.member.dto.RequestDto;
import com.v1.matripserver.member.dto.ResponseDto;
import com.v1.matripserver.member.entity.Auth;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.v1.matripserver.member.dto.RequestDto.*;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean checkEmailExist(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean checkNicknameExist(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
    }

    @Override
    public void createMember(CreateMemberDto createMemberDto) {

        if(checkEmailExist(createMemberDto.email()))
            throw new IllegalStateException("중복된 이메일이 존재합니다.");

        if(checkNicknameExist(createMemberDto.nickname()))
            throw new IllegalStateException("중복된 닉네임 입니다.");

        Member member = Member.builder()
                .email(createMemberDto.email())
                .password(createMemberDto.password())
                .name(createMemberDto.name())
                .birth(createMemberDto.birth())
                .nickname(createMemberDto.nickname())
                .build();

        member.setAuth(Auth.USER);

        memberRepository.save(member);
    }
}
