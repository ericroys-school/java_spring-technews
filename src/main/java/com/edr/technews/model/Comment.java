package com.edr.technews.model;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "comment")
public class Comment extends BaseEntity {

  private String commentTxt;
  private Long userId;
  private Long postId;
}
