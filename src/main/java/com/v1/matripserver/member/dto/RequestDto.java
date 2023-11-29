package com.v1.matripserver.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class RequestDto {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record JoinDto(
            @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 입력값이 형식에 맞지 않습니다.")
            String email,
            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,16}$", message = "비밀번호 입력값이 형식에 맞지 않습니다.")
            String password,
            @NotBlank
            String name,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate birth,
            String nickname
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record LoginDto(
            @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 입력값이 형식에 맞지 않습니다.")
            String email,
            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,16}$", message = "비밀번호 입력값이 형식에 맞지 않습니다.")
            String password
    ) {}

}
