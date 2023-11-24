package com.v1.matripserver.comment.service;

import org.springframework.stereotype.Service;

import com.v1.matripserver.comment.dto.CommentRequestDto;
import com.v1.matripserver.comment.entity.Comment;
import com.v1.matripserver.comment.repository.CommentRepository;
import com.v1.matripserver.member.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentService {

    private final CommentRepository commentRepository;

    private final JourneyService journeyService;

    private final MemberService memberService;

    // 댓글 작성
    public void createComment(CommentRequestDto commentRequestDto){

        Journey journey = journeyService.;
        Member member = memberService.;

        Comment comment = Comment.builder()
            .content(commentRequestDto.getContent())
            .journey(journey)
            .member(member)
            .build();

        commentRepository.save(comment);
    }
}
