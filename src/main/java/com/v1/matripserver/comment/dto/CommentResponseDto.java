package com.v1.matripserver.comment.dto;

import java.time.LocalDateTime;

import com.v1.matripserver.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private Member member;
}
