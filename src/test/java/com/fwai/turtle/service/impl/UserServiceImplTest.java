package com.fwai.turtle.service.impl;

import com.fwai.turtle.persistence.model.User;
import com.fwai.turtle.persistence.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Autowired
  @InjectMocks
  private UserServiceImpl userService;

  private static final String TEST_EMAIL = "test@example.com";

  @BeforeEach
  void setUp() {
    log.info("UserServiceImplTest setUp");
  }

  @AfterEach
  void tearDown() {
    reset(userRepository);
  }

  @Test
  @DisplayName("findByEmail Should return user when found")
  void findByEmail_ShouldReturnUser_WhenFound() {
    User expectedUser = User.builder().email(TEST_EMAIL).build();

    when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(expectedUser));

    User result = userService.findByEmail(TEST_EMAIL)
        .orElseThrow(() -> new RuntimeException("User " + TEST_EMAIL + " not found"));

    log.info(result.toString());

    assertNotNull(result);
    assertEquals(TEST_EMAIL, result.getEmail());
    verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
  }

  @Test
  void findByEmail_ShouldReturnNull_WhenNotFound() {
    when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

    Optional<User> result = userService.findByEmail(TEST_EMAIL);

    assert (result.isEmpty());
    verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
  }
}