package com.v1.matripserver.member.service;

import com.v1.matripserver.member.entity.Auth;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.member.repository.MemberRepository;
import com.v1.matripserver.util.exceptions.BaseResponseStatus;
import com.v1.matripserver.util.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.v1.matripserver.member.dto.RequestDto.*;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
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
            throw new CustomException(BaseResponseStatus.DUPLICATED_EMAIL, HttpStatus.CONFLICT);

        if(checkNicknameExist(createMemberDto.nickname()) && StringUtils.hasText(createMemberDto.nickname()))
            throw new CustomException(BaseResponseStatus.DUPLICATED_NICKNAME, HttpStatus.CONFLICT);

        Member member = Member.builder()
                .email(createMemberDto.email())
                .password(passwordEncoder.encode(createMemberDto.password()))
                .name(createMemberDto.name())
                .birth(createMemberDto.birth())
                .nickname(createMemberDto.nickname())
                .build();

        member.setAuth(Auth.USER);

        memberRepository.save(member);
    }
}
