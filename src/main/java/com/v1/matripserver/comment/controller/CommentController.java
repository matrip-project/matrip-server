package com.v1.matripserver.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v1.matripserver.comment.dto.CommentRequestDto;
import com.v1.matripserver.comment.dto.CommentResponseDto;
import com.v1.matripserver.comment.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;
    
    // 댓글 작성
    @PostMapping("")
    public ResponseEntity createComment(CommentRequestDto commentRequestDto){

        try {
            commentService.createComment(commentRequestDto);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            log.error("" + e.getMessage(), e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
