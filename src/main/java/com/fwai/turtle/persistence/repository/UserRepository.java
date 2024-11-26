package com.fwai.turtle.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import com.fwai.turtle.persistence.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  @NonNull
  Optional<User> findById(@NonNull Long id);

  @Query("SELECT u FROM User u WHERE u.employee IS NULL")
  List<User> findUnmappedUsers();

}
