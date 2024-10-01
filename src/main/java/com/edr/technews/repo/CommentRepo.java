package com.edr.technews.repo;

import com.edr.technews.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
  List<Comment> findCommentByPostId(Long postId) throws Exception;
}
