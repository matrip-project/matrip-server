package com.v1.matripserver.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.v1.matripserver.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
