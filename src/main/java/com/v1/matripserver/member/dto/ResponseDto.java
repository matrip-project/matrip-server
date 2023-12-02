package com.v1.matripserver.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.v1.matripserver.member.entity.Auth;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.member.entity.MemberLink;
import com.v1.matripserver.member.entity.MemberProfile;

import java.time.LocalDate;
import java.util.List;

public class ResponseDto {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record MemberDto (
            Long id,
            String email,
            String name,
            String nickname,
            LocalDate birth,
            char sex,
            String nation,
            String intro,
            Auth auth,
            int age,
            List<MemberProfile> profileList,
            List<MemberLink> linkList
    ) {
        public static MemberDto from(Member member) {
            return new MemberDto(
                    member.getId(),
                    member.getEmail(),
                    member.getName(),
                    member.getNickname(),
                    member.getBirth(),
                    member.getSex(),
                    member.getNation(),
                    member.getIntro(),
                    member.getAuth(),
                    member.getAge(),
                    member.getMemberProfileList(),
                    member.getMemberLinkList()
            );
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record LoginDto (
            String token,
            Long id,
            String name,
            Auth auth
    ) {

    }
}
