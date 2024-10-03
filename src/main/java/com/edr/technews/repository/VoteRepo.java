package com.edr.technews.repository;

import com.edr.technews.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepo extends JpaRepository<Vote, Long> {
  @Query("SELECT count(*) FROM Vote v where v.postId = :id")
  int numVotesByPostId(@Param("id") Long id);
}
