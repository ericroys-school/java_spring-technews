package com.edr.technews.repo;

import com.edr.technews.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
  public User findUserByEmail(String email) throws Exception;
}
