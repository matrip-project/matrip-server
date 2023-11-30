package com.v1.matripserver.comment.service;

import org.springframework.stereotype.Service;

import com.v1.matripserver.comment.dto.CommentRequestDto;
import com.v1.matripserver.comment.entity.Comment;
import com.v1.matripserver.comment.repository.CommentRepository;
import com.v1.matripserver.journey.dto.JourneyResponseDto;
import com.v1.matripserver.journey.entity.Journey;
import com.v1.matripserver.journey.service.JourneyService;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.member.service.MemberService;
import com.v1.matripserver.util.entity.Status;

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

        Journey journey = journeyService.find(commentRequestDto.getJourneyId());
        Member member = memberService.getMemberById(commentRequestDto.getMemberId());

        Comment comment = Comment.builder()
            .content(commentRequestDto.getContent())
            .secret(commentRequestDto.isSecret())
            .journey(journey)
            .member(member)
            .build();
        comment.setStatus(Status.ACTIVE);

        commentRepository.save(comment);
    }
}
