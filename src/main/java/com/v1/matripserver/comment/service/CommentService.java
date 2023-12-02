package com.v1.matripserver.comment.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.v1.matripserver.comment.dto.CommentRequestDto;
import com.v1.matripserver.comment.dto.CommentResponseDto;
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

    // 댓글 조회
    public List<CommentResponseDto> readComment(CommentRequestDto commentRequestDto){

        Long journeyId = commentRequestDto.getJourneyId();
        List<Comment> commentList = commentRepository.readComment(journeyId);
        // 게시글 작성자
        Long journeyWriterId = commentList.get(0).getJourneyId().getMemberId().getId();
        log.info("journeyWriterId: " + journeyWriterId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        CommentResponseDto commentResponseDto;

        for (Comment comment: commentList){
            // 댓글 작성자
            Long commentWriterId = null;
            if (comment.getParentId() != null){
                commentWriterId = comment.getParentId().getMemberId().getId();
            }else{
                commentWriterId = comment.getMemberId().getId();
            }

            // 비밀 댓글일 때
            if (comment.isSecret()){
                // 댓글 작성자 혹은 게시글 작성자일 때
                log.info(comment.getId() + " " + commentWriterId);
                if (commentWriterId.equals(commentRequestDto.getMemberId()) || journeyWriterId.equals(commentRequestDto.getMemberId()) ){
                    commentResponseDto = entityToDto(comment.getId(), comment.getContent(),
                        comment.isSecret(), comment.getCreated(), comment.getMemberId());
                // 제 3자일 때
                }else{
                    commentResponseDto = entityToDto(comment.getId(), "비밀 댓글 입니다.",
                        comment.isSecret(), comment.getCreated(), comment.getMemberId());
                }

            }
            // 비밀 댓글이 아닐 때
            else{
                commentResponseDto = entityToDto(comment.getId(), comment.getContent(),
                    comment.isSecret(), comment.getCreated(), comment.getMemberId());
            }

            commentResponseDtoList.add(commentResponseDto);
        }

        return commentResponseDtoList;
    }

    private CommentResponseDto entityToDto(Long id, String content, boolean secret, LocalDateTime createAt, Member member){

        return CommentResponseDto.builder()
            .id(id)
            .content(content)
            .secret(secret)
            .createAt(createAt)
            .memberId(member.getId())
            .memberName(member.getName())
            .memberEmail(member.getEmail())
            .build();
    }

    // 댓글 삭제
    public void deleteComment(Long id){

        try {
            Comment comment = commentRepository.findById(id).get();
            comment.setStatus(Status.DELETED);

            commentRepository.save(comment);
        }catch (Exception e){

            throw new RuntimeException("" + e.getMessage(), e);
        }
    }

    // 댓글 수정
    public void updateComment(CommentRequestDto commentRequestDto){

        Comment comment = commentRepository.findById(commentRequestDto.getId()).get();
        comment.setContent(commentRequestDto.getContent());

        commentRepository.save(comment);
    }
}
