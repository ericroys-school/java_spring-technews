package com.edr.technews.controller;

import com.edr.technews.model.Post;
import com.edr.technews.model.User;
import com.edr.technews.repository.UserRepository;
import com.edr.technews.repository.VoteRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private VoteRepo voteRepo;

  @GetMapping(path = "/{id}")
  public User getUserById(@PathVariable Long id) {
    Optional<User> res = userRepository.findById(id);
    if (!res.isPresent()) return new User();
    List<Post> posts = res.get().getPosts();
    for (Post p : posts) p.setVoteCount(voteRepo.numVotesByPostId(p.getId()));
    return res.get();
  }

  @PostMapping
  public User addUser(@RequestBody User user) {
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    return userRepository.save(user);
  }

  @PutMapping(path = "/{id}")
  public User updateUser(@PathVariable Long id, @RequestBody User user) {
    Optional<User> _u = userRepository.findById(id);
    if (_u.isPresent()) {
      user.setId(_u.get().getId());
      userRepository.save(user);
    }
    return user;
  }

  @DeleteMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable Long id) {
    userRepository.deleteById(id);
  }
}
