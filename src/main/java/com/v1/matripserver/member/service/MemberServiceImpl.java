package com.v1.matripserver.member.service;

import com.v1.matripserver.auth.JwtTokenUtil;
import com.v1.matripserver.member.dto.ResponseDto;
import com.v1.matripserver.member.entity.Auth;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.member.entity.MemberLink;
import com.v1.matripserver.member.entity.MemberProfile;
import com.v1.matripserver.member.repository.MemberLinkRepository;
import com.v1.matripserver.member.repository.MemberProfileRepository;
import com.v1.matripserver.member.repository.MemberRepository;
import com.v1.matripserver.util.exceptions.BaseResponseStatus;
import com.v1.matripserver.util.exceptions.CustomException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.v1.matripserver.member.dto.RequestDto.*;

@Service
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberLinkRepository memberLinkRepository;

    public MemberServiceImpl(
            PasswordEncoder passwordEncoder,
            MemberRepository memberRepository,
            MemberProfileRepository memberProfileRepository,
            MemberLinkRepository memberLinkRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.memberProfileRepository = memberProfileRepository;
        this.memberLinkRepository = memberLinkRepository;
    }

    @Override
    @Transactional
    public void join(JoinDto joinDto) {

        if(checkEmailExist(joinDto.email()))
            throw new CustomException(BaseResponseStatus.DUPLICATED_EMAIL, HttpStatus.CONFLICT);

        if(checkNicknameExist(joinDto.nickname()) && StringUtils.hasText(joinDto.nickname()))
            throw new CustomException(BaseResponseStatus.DUPLICATED_NICKNAME, HttpStatus.CONFLICT);

        Member member = Member.builder()
                .email(joinDto.email())
                .password(passwordEncoder.encode(joinDto.password()))
                .name(joinDto.name())
                .birth(joinDto.birth())
                .nickname(joinDto.nickname())
                .build();

        member.setAuth(Auth.USER);

        memberRepository.save(member);
    }

    @Override
    public ResponseDto.LoginDto login(LoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new CustomException(BaseResponseStatus.COMMON_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(loginDto.password(), member.getPassword()))
            throw new CustomException(BaseResponseStatus.LOGIN_FAILED, HttpStatus.EXPECTATION_FAILED);

        // 로그인 성공 => Jwt Token 발급

        String secretKey = "test001";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(member.getEmail(), secretKey, expireTimeMs);

        return new ResponseDto.LoginDto(
                jwtToken,
                member.getId(),
                member.getName(),
                member.getAuth()
        );
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
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
    }

    @Override
    public ResponseDto.MemberDto getMyPageById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        return ResponseDto.MemberDto.from(member);
    }

    @Override
    public void updateMember(Long memberId, UpdateMemberDto updateMemberDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (updateMemberDto.nickname() != null) {
            member.setNickname(updateMemberDto.nickname().isEmpty() ? null : updateMemberDto.nickname());
        }
        if (updateMemberDto.intro() != null) {
            member.setIntro(updateMemberDto.intro());
        }

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void addProfile(Long memberId, AddProfileDto addProfileDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        memberProfileRepository.save(MemberProfile.builder()
                .member(member)
                .path(addProfileDto.path())
                .build());
    }

    @Override
    @Transactional
    public void addLink(Long memberId, AddLinkDto addLinkDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        memberLinkRepository.save(MemberLink.builder()
                .member(member)
                .path(addLinkDto.path())
                .build());
    }

    @Override
    public List<MemberLink> addAndGetLink(Long memberId, AddLinkDto addLinkDto) {
        addLink(memberId, addLinkDto);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        return member.getMemberLinkList();
    }

    @Override
    @Transactional
    public void deleteProfile(Long profileId) {
        MemberProfile memberProfile = memberProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로필 사진 입니다."));

        memberProfileRepository.delete(memberProfile);
    }

    @Override
    public void deleteLink(Long linkId) {
        MemberLink memberLink = memberLinkRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 소셜 링크 입니다."));

        memberLinkRepository.delete(memberLink);
    }

}
