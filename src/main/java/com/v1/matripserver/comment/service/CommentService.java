package com.v1.matripserver.comment.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.v1.matripserver.comment.dto.CommentRequestDto;
import com.v1.matripserver.comment.entity.Comment;
import com.v1.matripserver.comment.repository.CommentRepository;
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

        Journey journey = journeyService.findJourney(commentRequestDto.getJourneyId());
        Member member = memberService.getMemberById(commentRequestDto.getMemberId());
        Long parentId = commentRequestDto.getParentId();

        Comment readComment = null;
        Optional<Comment> optionalComment = Optional.empty();
        if (parentId != null) {
            optionalComment = commentRepository.findById(parentId);
        }

        // 부모 댓글이 없으면 null로 첫 댓글 작성
        if (optionalComment.isPresent()) {
            // 부모 댓글이 있으면 depth가 1인지 검사.
            // 대댓댓글 방지.
            if (optionalComment.get().getParentId() == null){
                // 부모 댓글이 최상위 댓글이면, 최상위 댓글을 부모 댓글로 지정
                readComment = optionalComment.get();
            }else{
                // 부모 댓글이 최상위 댓글이 아니라면, 최상위 댓글을 부모 댓글로 지정
                readComment = optionalComment.get().getParentId();
            }
        }

        Comment comment = Comment.builder()
            .content(commentRequestDto.getContent())
            .secret(commentRequestDto.isSecret())
            .parent(readComment)
            .journey(journey)
            .member(member)
            .build();
        comment.setStatus(Status.ACTIVE);

        commentRepository.save(comment);
    }
}
