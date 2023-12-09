package com.v1.matripserver.comment.dto;

import java.time.LocalDateTime;

import com.v1.matripserver.journey.entity.Journey;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.util.entity.Status;

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
    private boolean secret;
    private LocalDateTime createAt;

    private Long memberId;
    private Long parentId;
    private String memberName;
    private String memberEmail;
}
