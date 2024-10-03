package com.edr.technews.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "z_user")
public class User extends BaseEntity {

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String email;

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
