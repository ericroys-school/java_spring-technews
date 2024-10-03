package com.edr.technews.repository;

import com.edr.technews.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository
  extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
  public User findUserByEmail(String email) throws Exception;
}
