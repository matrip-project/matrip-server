package com.v1.matripserver.member.service;

import com.v1.matripserver.member.dto.ResponseDto;
import com.v1.matripserver.member.entity.Member;

import static com.v1.matripserver.member.dto.RequestDto.*;

public interface MemberService {

    boolean checkEmailExist(String email);

    boolean checkNicknameExist(String nickname);

    Member getMemberById(Long memberId);

    Member getMemberByEmail(String email);

    ResponseDto.MemberDto getMyPageById(Long memberId);

    void join(JoinDto joinDto);

    ResponseDto.LoginDto login(LoginDto loginDto);

}
