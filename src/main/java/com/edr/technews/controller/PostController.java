package com.edr.technews.controller;

import com.edr.technews.model.Post;
import com.edr.technews.model.User;
import com.edr.technews.model.Vote;
import com.edr.technews.repository.PostRepository;
import com.edr.technews.repository.UserRepository;
import com.edr.technews.repository.VoteRepo;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/post")
public class PostController {

  @Autowired
  PostRepository postRepo;

  @Autowired
  VoteRepo voteRepo;

  @Autowired
  UserRepository userRepo;

  @GetMapping
  public List<Post> getAllPosts() {
    List<Post> postList = postRepo.findAll();
    for (Post p : postList) {
      p.setVoteCount(voteRepo.numVotesByPostId(p.getId()));
    }
    return postList;
  }

  @GetMapping(params = "/{id}")
  public Post getPost(@PathVariable Long id) {
    Optional<Post> p = postRepo.findById(id);
    return p.isPresent() ? p.get() : new Post();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Post createPost(@RequestBody Post post) {
    return postRepo.save(post);
  }

  @PutMapping(path = "/{id}")
  public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
    Optional<Post> _p = postRepo.findById(id);
    if (_p.isPresent()) {
      post.setId(_p.get().getId());
      postRepo.save(post);
    }
    return post;
  }

  @PutMapping(path = "/upvote")
  public String upVote(@RequestBody Vote vote, HttpServletRequest request) {
    String res = "";
    if (request.getSession(false) != null) {
      Optional<Post> p = null;
      User u = (User) request.getSession().getAttribute("SESSION_USER");
      vote.setUserId(u.getId());
      voteRepo.save(vote);

      p = postRepo.findById(vote.getPostId());
      if (p.isPresent()) p
        .get()
        .setVoteCount(voteRepo.numVotesByPostId(vote.getPostId()));
    } else {
      res = "login";
    }
    return res;
  }

  @DeleteMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePost(@PathVariable Long id) {
    postRepo.deleteById(id);
  }
}
