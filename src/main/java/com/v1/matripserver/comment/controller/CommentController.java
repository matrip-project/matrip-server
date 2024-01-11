package com.v1.matripserver.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.v1.matripserver.comment.dto.CommentRequestDto;
import com.v1.matripserver.comment.dto.CommentResponseDto;
import com.v1.matripserver.comment.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    
    // 댓글 작성
    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createComment(@RequestBody CommentRequestDto commentRequestDto){

        commentService.createComment(commentRequestDto);
    }

    // 댓글 조회
    @GetMapping("")
    public ResponseEntity<List<CommentResponseDto>> readComment(CommentRequestDto commentRequestDto, @RequestHeader(value = "Authorization") String accessToken) {

        List<CommentResponseDto> commentResponseDtoList = commentService.readComment(commentRequestDto, accessToken);
        return ResponseEntity.ok(commentResponseDtoList);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id, @RequestHeader(value = "Authorization") String accessToken){

        commentService.deleteComment(id, accessToken);
    }

    // 댓글 수정
    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(@RequestBody CommentRequestDto commentRequestDto){
        commentService.updateComment(commentRequestDto);
    }
}
