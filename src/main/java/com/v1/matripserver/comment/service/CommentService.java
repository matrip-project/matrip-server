package com.v1.matripserver.comment.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.v1.matripserver.auth.JwtTokenUtil;
import com.v1.matripserver.comment.dto.CommentRequestDto;
import com.v1.matripserver.comment.dto.CommentResponseDto;
import com.v1.matripserver.comment.entity.Comment;
import com.v1.matripserver.comment.repository.CommentRepository;
import com.v1.matripserver.journey.entity.Journey;
import com.v1.matripserver.journey.service.JourneyService;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.member.service.MemberService;
import com.v1.matripserver.util.entity.Status;
import com.v1.matripserver.util.exceptions.BaseResponseStatus;
import com.v1.matripserver.util.exceptions.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentService {

    private final CommentRepository commentRepository;

    private final JourneyService journeyService;

    private final MemberService memberService;

    private static String secretKey = "test001";

    // 댓글 작성
    public void createComment(CommentRequestDto commentRequestDto){

        try{
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
        }catch (Exception e){

            log.error("생성에 실패했습니다: " + e.getMessage(), e);
            throw new CustomException(BaseResponseStatus.COMMON_CREATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 댓글 조회
    public List<CommentResponseDto> readComment(CommentRequestDto commentRequestDto, String token){

        try {
            Long journeyId = commentRequestDto.getJourneyId();
            List<Comment> commentList = commentRepository.readComment(journeyId);
            String memberEmail = JwtTokenUtil.getLoginId(token, secretKey);
            Member member = memberService.getMemberByEmail(memberEmail);

            // 변수 선언
            Long commentWriterId;
            Long journeyWriterId = null;
            CommentResponseDto commentResponseDto;
            Long parentId = null;

            // 게시글 작성자
            // 댓글이 존재할 때
            if (!commentList.isEmpty()) {
                journeyWriterId = commentList.get(0).getJourneyId().getMemberId().getId();
            }

            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

            for (Comment comment : commentList) {
                // 댓글 작성자
                if (comment.getParentId() != null) {
                    commentWriterId = comment.getParentId().getMemberId().getId();
                    parentId = comment.getParentId().getId();
                } else {
                    commentWriterId = comment.getMemberId().getId();
                }

                // 비밀 댓글일 때
                if (comment.isSecret()) {
                    // 댓글 작성자 혹은 게시글 작성자일 때
                    log.info(comment.getMemberId() + " " + commentWriterId);
                    if (commentWriterId.equals(member.getId()) || journeyWriterId.equals(
                        member.getId())) {
                        commentResponseDto = entityToDto(comment.getId(), comment.getContent(),
                            comment.isSecret(), comment.getCreated(), parentId, comment.getMemberId());
                        // 제 3자일 때
                    } else {
                        commentResponseDto = entityToDto(comment.getId(), "비밀 댓글 입니다.",
                            comment.isSecret(), comment.getCreated(), parentId, comment.getMemberId());
                    }

                }
                // 비밀 댓글이 아닐 때
                else {
                    commentResponseDto = entityToDto(comment.getId(), comment.getContent(),
                        comment.isSecret(), comment.getCreated(), parentId, comment.getMemberId());
                }

                commentResponseDtoList.add(commentResponseDto);
            }

            return commentResponseDtoList;
        }catch (Exception e){

            log.error("조회에 실패했습니다: " + e.getMessage(), e);
            throw new CustomException(BaseResponseStatus.COMMON_READ_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CommentResponseDto entityToDto(Long id, String content, boolean secret, LocalDateTime createAt, Long parentId, Member member){

        return CommentResponseDto.builder()
            .id(id)
            .content(content)
            .secret(secret)
            .createAt(createAt)
            .parentId(parentId)
            .memberId(member.getId())
            .memberName(member.getName())
            .memberEmail(member.getEmail())
            .build();
    }

    // 댓글 삭제
    public void deleteComment(Long id, String token){

        try {
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException(BaseResponseStatus.COMMON_NOT_FOUND, HttpStatus.NOT_FOUND));
            String memberEmail = JwtTokenUtil.getLoginId(token, secretKey);
            comment.setStatus(Status.DELETED);

            commentRepository.save(comment);
        }catch (Exception e){

            log.error("삭제에 실패했습니다: " + e.getMessage(), e);
            throw new CustomException(BaseResponseStatus.COMMON_DELETE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 댓글 수정
    public void updateComment(CommentRequestDto commentRequestDto){

        try{
            Comment comment = commentRepository.findById(commentRequestDto.getId()).orElseThrow(() -> new CustomException(BaseResponseStatus.COMMON_NOT_FOUND, HttpStatus.NOT_FOUND));
            comment.setContent(commentRequestDto.getContent());

            commentRepository.save(comment);
        }catch (Exception e){

            log.error("수정에 실패했습니다: " + e.getMessage(), e);
            throw new CustomException(BaseResponseStatus.COMMON_UPDATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
