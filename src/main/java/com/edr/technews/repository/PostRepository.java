package com.edr.technews.repository;

import com.edr.technews.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  public List<Post> findPostsByUserId(Long userId) throws Exception;
}
