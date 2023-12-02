package com.v1.matripserver.member.service;

import com.v1.matripserver.member.dto.ResponseDto;
import com.v1.matripserver.member.entity.Member;

import static com.v1.matripserver.member.dto.RequestDto.*;

public interface MemberService {

    boolean checkEmailExist(String email);

    boolean checkNicknameExist(String nickname);

    Member getMemberById(Long memberId);

    Member getMemberByEmail(String email);

    void join(JoinDto joinDto);

    ResponseDto.loginDto login(LoginDto loginDto);

}
