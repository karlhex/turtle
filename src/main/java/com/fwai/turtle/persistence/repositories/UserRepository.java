package com.fwai.turtle.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.fwai.turtle.persistence.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  @NonNull
  Optional<User> findById(@NonNull Long id);

}
