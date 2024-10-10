package com.edr.technews.controller;

import com.edr.technews.model.Comment;
import com.edr.technews.model.Post;
import com.edr.technews.model.User;
import com.edr.technews.model.Vote;
import com.edr.technews.repository.CommentRepo;
import com.edr.technews.repository.PostRepository;
import com.edr.technews.repository.UserRepository;
import com.edr.technews.repository.VoteRepo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TechNewsController {

  @Autowired
  PostRepository postRepository;

  @Autowired
  VoteRepo voteRepo;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CommentRepo commentRepo;

  @PostMapping("/post")
  public String addPostDashboardPage(
    @ModelAttribute Post post,
    Model model,
    HttpServletRequest request
  ) {
    if (
      post.getTitle() == null ||
      post.getTitle() == "" ||
      post.getUri() == null ||
      post.getUri() == ""
    ) {
      return "redirect:/login";
    }
    if (request.getSession(false) == null) return "redirect:/login";

    User u = (User) request.getSession().getAttribute("SESSION_USER");
    post.setUserId(u.getId());
    postRepository.save(post);
    return "redirect:/dashboard";
  }

  @PostMapping("/post/{id}")
  public String updatePostDashboardPage(
    @PathVariable Long id,
    @ModelAttribute Post post,
    Model model,
    HttpServletRequest request
  ) {
    if (request.getSession(false) == null) {
      model.addAttribute("user", new User());
      return "redirect/dashboard";
    }
    Optional<Post> p = postRepository.findById(id);
    if (p.isPresent()) {
      p.get().setTitle(post.getTitle());
      postRepository.save(p.get());
    }
    return "redirect:/dashboard";
  }

  @PostMapping("/comment")
  public String createCommentCommentsPage(
    @ModelAttribute Comment comment,
    Model model,
    HttpServletRequest request
  ) {
    if (comment.getCommentTxt() == null || comment.getCommentTxt() == "") {
      return "redirect:/singlePostEmptyComment/" + comment.getPostId();
    }
    if (request.getSession(false) != null) {
      User u = (User) request.getSession().getAttribute("SESSION_USER");
      comment.setUserId(u.getId());
      commentRepo.save(comment);
      return "redirect:/post/" + comment.getPostId();
    }
    return "login";
  }

  @PostMapping("/comment/edit")
  public String createCommentEditPage(
    @ModelAttribute Comment comment,
    HttpServletRequest request
  ) {
    if (comment.getCommentTxt() == null || comment.getCommentTxt() == "") {
      return "redirect:/editPostEmptyComment/" + comment.getPostId();
    }
    if (request.getSession(false) != null) {
      User u = (User) request.getSession().getAttribute("SESSION_USER");
      comment.setUserId(u.getId());
      commentRepo.save(comment);
      return "redirect:/dashboard/edit/" + comment.getPostId();
    }
    return "redirect:/login";
  }

  @PutMapping("/post/upvote")
  public void addVoteCommentsPage(
    @RequestBody Vote vote,
    HttpServletRequest request
  ) {
    if (request.getSession(false) != null) {
      Optional<Post> p = null;
      User u = (User) request.getSession().getAttribute("SESSION_USER");
      vote.setUserId(u.getId());
      voteRepo.save(vote);
      p = postRepository.findById(vote.getPostId());
      if (p.isPresent()) p
        .get()
        .setVoteCount(voteRepo.numVotesByPostId(vote.getPostId()));
    }
  }

  @PostMapping("/user")
  public String signup(
    @ModelAttribute User user,
    Model model,
    HttpServletRequest request
  ) {
    if (
      user.getUsername() == null ||
      user.getUsername() == "" ||
      user.getPassword() == null ||
      user.getPassword() == "" ||
      user.getEmail() == null ||
      user.getEmail() == ""
    ) {
      model.addAttribute("notice", "provide username, email and password");
      return "login";
    }
    try {
      user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
      userRepository.save(user);
    } catch (DataIntegrityViolationException die) {
      model.addAttribute("notice", "email address is already in use");
      return "login";
    } catch (Exception e) {
      model.addAttribute("notice", e.getMessage());
    }

    User u = null;
    try {
      u = userRepository.findUserByEmail(user.getEmail());
    } catch (Exception e) {}

    if (u == null) {
      model.addAttribute("notice", "user not recognized");
      return "login";
    }
    u.setLoggedIn(true);
    request.getSession().setAttribute("SESSION_USER", u);
    return "redirect:/dashboard";
  }

  @PostMapping("/user/login")
  public String login(
    @ModelAttribute User user,
    Model model,
    HttpServletRequest request
  ) {
    if (
      (
        user.getPassword().equals(null) ||
        user.getPassword().isEmpty() ||
        user.getEmail().equals(null) ||
        user.getEmail().isEmpty()
      )
    ) {
      model.addAttribute("notice", "email and password is required");
      return "login";
    }

    User u = null;
    try {
      u = userRepository.findUserByEmail(user.getEmail());
    } catch (Exception e) {}

    if (u == null) {
      model.addAttribute("notice", "email address not recognized");
      return "login";
    }

    if (!BCrypt.checkpw(user.getPassword(), u.getPassword())) {
      model.addAttribute("notice", "authentication failed");
      return "login";
    }

    u.setLoggedIn(true);
    request.getSession().setAttribute("SESSION_USER", u);

    return "redirect:/dashboard";
  }
}
