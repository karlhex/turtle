package com.fwai.turtle.service.impl;

import com.fwai.turtle.api.dto.SigninAns;
import com.fwai.turtle.api.dto.SigninReq;
import com.fwai.turtle.persistence.model.Role;
import com.fwai.turtle.persistence.model.User;
import com.fwai.turtle.service.interfaces.JwtTokenService;
import com.fwai.turtle.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationProvider;
import java.util.Collections;
import java.util.Optional;
import com.fwai.turtle.persistence.types.RoleType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

  @Mock
  private UserService userService;

  @Mock
  private JwtTokenService jwtTokenService;

  @Mock
  private AuthenticationProvider authenticationProvider;

  @InjectMocks
  private AuthServiceImpl authService;

  private static final String TEST_EMAIL = "test@email.com";
  private static final String TEST_PASSWORD = "password";
  private static final String TEST_USERNAME = "testUser";
  private static final String TEST_FIRST_NAME = "Test";
  private static final String TEST_LAST_NAME = "User";
  private static final String TOKEN = "TOKEN";

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    User testUser = new User();
    testUser.setEmail(TEST_EMAIL);
    testUser.setUsername(TEST_USERNAME);
    testUser.setFirstName(TEST_FIRST_NAME);
    testUser.setLastName(TEST_LAST_NAME);
    testUser.setRoles(Collections.singleton(new Role(RoleType.USER)));

    when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
    when(jwtTokenService.createToken(TEST_USERNAME, Collections.singleton(new Role(RoleType.USER)))).thenReturn(TOKEN);
    Authentication authentication = mock(Authentication.class);
    when(authenticationProvider.authenticate(any())).thenReturn(authentication);
  }

  @Test
  void signin_ValidUser_ReturnsSigninAns() {
    SigninReq signinReq = new SigninReq();
    signinReq.setEmail(TEST_EMAIL);
    signinReq.setPassword(TEST_PASSWORD);

    SigninAns signinAns = authService.signin(signinReq);

    assertNotNull(signinAns);
    assertEquals(TEST_FIRST_NAME, signinAns.getFirstName());
    assertEquals(TEST_LAST_NAME, signinAns.getLastName());
    assertNotNull(signinAns.getToken());
  }

  @Test
  void signin_InvalidUser_ThrowsRuntimeException() {
    SigninReq signinReq = new SigninReq();
    signinReq.setEmail("invalid@email.com");
    signinReq.setPassword(TEST_PASSWORD);

    assertThrows(RuntimeException.class, () -> authService.signin(signinReq));
  }

  @Test
  void signin_EmptyCredentials_ReturnsNull() {
    SigninReq signinReq = new SigninReq();
    signinReq.setEmail("");
    signinReq.setPassword("");

    SigninAns signinAns = authService.signin(signinReq);

    assertNull(signinAns);
  }

  @Test
  void signin_NullCredentials_ReturnsNull() {
    SigninReq signinReq = new SigninReq();
    signinReq.setEmail(null);
    signinReq.setPassword(null);

    SigninAns signinAns = authService.signin(signinReq);

    assertNull(signinAns);
  }
}