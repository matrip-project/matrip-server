package com.v1.matripserver.comment.dto;

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
public class CommentRequestDto {

    private Long id;
    private String content;

    // 멤버 및 게시글 추가 필요
    private Long journeyId;
    private Long memberId;
}
