package com.edr.technews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import lombok.Data;

// @Entity
// @JsonIgnoreProperties({ "hibernateLazyInitialier", "handler" })
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "sid", nullable = false)
  private String sid = UUID.randomUUID().toString();

  @Column(name = "created", nullable = false)
  private long created = new Date().getTime();

  @Column(name = "createdBy")
  private String createdBy;

  @Column(name = "modified", nullable = false)
  private long modified = new Date().getTime();

  @Column(name = "modifiedBy")
  private String modifiedBy;
}
