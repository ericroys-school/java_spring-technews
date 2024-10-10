package com.edr.technews.controller;

import com.edr.technews.model.Comment;
import com.edr.technews.model.Post;
import com.edr.technews.model.User;
import com.edr.technews.repository.CommentRepo;
import com.edr.technews.repository.PostRepository;
import com.edr.technews.repository.UserRepository;
import com.edr.technews.repository.VoteRepo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomePageController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PostRepository postRepository;

  @Autowired
  CommentRepo commentRepo;

  @Autowired
  VoteRepo voteRepo;

  @GetMapping("/login")
  public String login(Model model, HttpServletRequest request) {
    if (request.getSession(false) != null) {
      return "redirect:/";
    }
    model.addAttribute("user", new User());
    return "login";
  }

  @GetMapping("/user/logout")
  public String logout(HttpServletRequest request) {
    if (request.getSession(false) != null) {
      request.getSession().invalidate();
    }
    return "redirect:/login";
  }

  @GetMapping("/")
  public String homepage(Model model, HttpServletRequest request) {
    User sUser = new User();
    if (request.getSession(false) != null) {
      sUser = (User) request.getSession().getAttribute("SESSION_USER");
      model.addAttribute("loggedIn", sUser.isLoggedIn());
    } else model.addAttribute("loggedIn", false);

    List<Post> ps = postRepository.findAll();
    for (Post p : ps) {
      p.setVoteCount(voteRepo.numVotesByPostId(p.getId()));
      Optional<User> user = userRepository.findById(p.getUserId());
      p.setUserName(user.isPresent() ? user.get().getUsername() : "not_found");
    }
    model.addAttribute("postList", ps);
    model.addAttribute("loggedIn", sUser.isLoggedIn());
    model.addAttribute("point", "point");
    model.addAttribute("points", "points");
    return "homepage";
  }

  @GetMapping("/dashboard")
  public String dashboardPageSetup(Model model, HttpServletRequest request)
    throws Exception {
    if (request.getSession(false) != null) {
      setupDashboardPage(model, request);
      return "dashboard";
    } else {
      model.addAttribute("user", new User());
      return "login";
    }
  }

  @GetMapping("/dashboardEmptyTitleAndLink")
  public String dashboardEmptyTitleAndLinkHandler(
    Model model,
    HttpServletRequest request
  ) throws Exception {
    setupDashboardPage(model, request);
    model.addAttribute(
      "notice",
      "To create a post the Title and Link must be populated!"
    );
    return "dashboard";
  }

  @GetMapping("/singlePostEmptyComment/{id}")
  public String singlePostEmptyCommentHandler(
    @PathVariable Long id,
    Model model,
    HttpServletRequest request
  ) throws Exception {
    setupSinglePostPage(id, model, request);
    model.addAttribute(
      "notice",
      "To add a comment you must enter the comment in the comment text area!"
    );
    return "single-post";
  }

  @GetMapping("/post/{id}")
  public String singlePostPageSetup(
    @PathVariable Long id,
    Model model,
    HttpServletRequest request
  ) throws Exception {
    setupSinglePostPage(id, model, request);
    return "single-post";
  }

  @GetMapping("/editPostEmptyComment/{id}")
  public String editPostEmptyCommentHandler(
    @PathVariable Long id,
    Model model,
    HttpServletRequest request
  ) throws Exception {
    if (request.getSession(false) != null) {
      setupEditPostPage(id, model, request);
      model.addAttribute(
        "notice",
        "To add a comment you must enter the comment in the comment text area!"
      );
      return "edit-post";
    } else {
      model.addAttribute("user", new User());
      return "login";
    }
  }

  @GetMapping("/dashboard/edit/{id}")
  public String editPostPageSetup(
    @PathVariable Long id,
    Model model,
    HttpServletRequest request
  ) throws Exception {
    if (request.getSession(false) != null) {
      setupEditPostPage(id, model, request);
      return "edit-post";
    } else {
      model.addAttribute("user", new User());
      return "login";
    }
  }

  public Model setupDashboardPage(Model model, HttpServletRequest request)
    throws Exception {
    User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");

    Long userId = sessionUser.getId();

    List<Post> postList = postRepository.findPostsByUserId(userId);
    for (Post p : postList) {
      p.setVoteCount(voteRepo.numVotesByPostId(p.getId()));
      Optional<User> user = userRepository.findById(p.getUserId());
      p.setUserName(user.isPresent() ? user.get().getUsername() : "not_found");
    }

    model.addAttribute("user", sessionUser);
    model.addAttribute("postList", postList);
    model.addAttribute("loggedIn", sessionUser.isLoggedIn());
    model.addAttribute("post", new Post());

    return model;
  }

  public Model setupSinglePostPage(
    Long id,
    Model model,
    HttpServletRequest request
  ) throws Exception {
    if (request.getSession(false) != null) {
      User sessionUser = (User) request
        .getSession()
        .getAttribute("SESSION_USER");
      model.addAttribute("sessionUser", sessionUser);
      model.addAttribute("loggedIn", sessionUser.isLoggedIn());
    }
    System.out.println("***********");
    Optional<Post> post = postRepository.findById(id);
    Post rp = new Post();
    List<Comment> comments = new ArrayList<Comment>();

    if (post.isPresent()) {
      rp = post.get();
      rp.setVoteCount(voteRepo.numVotesByPostId(rp.getId()));

      Optional<User> postUser = userRepository.findById(rp.getUserId());
      if (postUser.isPresent()) rp.setUserName(postUser.get().getUsername());

      comments = commentRepo.findCommentByPostId(rp.getId());
    } else System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDddd");

    model.addAttribute("post", post);

    model.addAttribute("commentList", comments);
    model.addAttribute("comment", new Comment());

    return model;
  }

  public Model setupEditPostPage(
    Long id,
    Model model,
    HttpServletRequest request
  ) throws Exception {
    if (request.getSession(false) != null) {
      User sessionUser = (User) request
        .getSession()
        .getAttribute("SESSION_USER");

      Optional<Post> returnPost = postRepository.findById(id);
      Post rp = new Post();
      List<Comment> commentList = new ArrayList<Comment>();

      if (returnPost.isPresent()) {
        rp = returnPost.get();
        Optional<User> tempUser = userRepository.findById(rp.getUserId());
        if (tempUser.isPresent()) rp.setUserName(tempUser.get().getUsername());
        rp.setVoteCount(voteRepo.numVotesByPostId(rp.getId()));
        commentList = commentRepo.findCommentByPostId(rp.getId());
      }

      model.addAttribute("post", rp);
      model.addAttribute("loggedIn", sessionUser.isLoggedIn());
      model.addAttribute("commentList", commentList);
      model.addAttribute("comment", new Comment());
    }

    return model;
  }
}
