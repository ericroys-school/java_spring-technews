package com.edr.technews.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "post")
public class Post extends BaseEntity {

  private String title;
  private String uri;

  @Transient
  private String userName;

  @Transient
  private int voteCount;

  private Long userId;

  @OneToMany(
    mappedBy = "postId",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Comment> comments;
}
