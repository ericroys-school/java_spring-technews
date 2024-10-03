package com.edr.technews.controller;

import com.edr.technews.model.Comment;
import com.edr.technews.repository.CommentRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

  @Autowired
  CommentRepo commentRepo;

  @GetMapping
  public List<Comment> getComments() {
    return commentRepo.findAll();
  }

  @GetMapping(path = "/{id}")
  public Comment getComment(@PathVariable Long id) {
    Optional<Comment> c = commentRepo.findById(id);
    return c.isPresent() ? c.get() : new Comment();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Comment createComment(@RequestBody Comment comment) {
    return commentRepo.save(comment);
  }

  @PutMapping(path = "/{id}")
  public Comment updateComment(@RequestBody Comment comment) {
    return commentRepo.save(comment);
  }

  @DeleteMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteComment(@PathVariable Long id) {
    commentRepo.deleteById(id);
  }
}
