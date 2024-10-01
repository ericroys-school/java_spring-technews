package com.edr.technews.model;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "vote")
public class Vote extends BaseEntity {

  private Long userId;
  private Long postId;
}
