package com.v1.matripserver.member.dto;

import com.v1.matripserver.member.entity.Auth;

public class ResponseDto {

//    public record
    public record LoginDto (
            String token,
            Long id,
            String name,
            Auth auth
    ) {

    }
}
