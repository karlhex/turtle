package com.fwai.turtle.base.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.base.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  @NonNull
  Optional<User> findById(@NonNull Long id);

  @Query("SELECT u FROM User u LEFT JOIN u.employee e WHERE e IS NULL")
  Page<User> findUnmappedUsers(Pageable pageable);

  Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
    String username, String email, Pageable pageable);
}
