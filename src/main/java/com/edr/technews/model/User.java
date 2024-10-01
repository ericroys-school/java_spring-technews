package com.edr.technews.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "user")
public class User extends BaseEntity {

  private String username;
  private String email;

  @Column(unique = true)
  private String password;

  @Transient
  private boolean loggedIn;

  @OneToMany(
    mappedBy = "userId",
    cascade = CascadeType.ALL,
    fetch = FetchType.EAGER
  )
  private List<Post> posts;

  @OneToMany(
    mappedBy = "userId",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Vote> votes;

  @OneToMany(
    mappedBy = "userId",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Comment> comments;
}
