package com.v1.matripserver.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.v1.matripserver.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    // 댓글 조회
    // 1. 삭제된 댓글은 조회가 안 되어야 함.
    // 2. 해당 게시판의 댓글만 조회.
    // 3. 최상위 댓글이 삭제되었을 때, 대댓글도 조회되지 않아야 함.
    // fetch 조인을 통해 한 번의 쿼리 실행 (즉시 로딩).
    @Query("select c from Comment c left join fetch c.parentId p"
        + " where c.status = 'ACTIVE'"
        + " and c.journeyId.id = :journeyId"
        + " and (p is null or (p is not null and p.status = 'ACTIVE'))")
    List<Comment> readComment(Long journeyId);
}