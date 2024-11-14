package com.fwai.turtle.service.impl;

import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.types.RoleType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = JwtTokenServiceImpl.class)
public class JwtTokenServiceImplTest {

    @Autowired
    @InjectMocks
    private JwtTokenServiceImpl jwtTokenService;

    private static final String USER_NAME = "testUser";

    @Mock
    private Key mockKey;

    @BeforeEach
    public void setUp() {
        // Initialization or common setup code can go here if needed.
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when username is null")
    public void createToken_WithNullUsername_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> jwtTokenService.createToken(null, Collections.emptySet()));
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when username is empty")
    public void createToken_WithEmptyUsername_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> jwtTokenService.createToken("", Collections.emptySet()));
    }

    @Test
    @DisplayName("Create token returns a valid token")
    public void createToken_ReturnsValidToken() {
        Role role = new Role(RoleType.USER);
        Set<Role> roles = Collections.singleton(role);

        String token = jwtTokenService.createToken(USER_NAME, roles);

        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
        // Further token validation logic...
    }

    @Test
    @DisplayName("Generate token with empty roles successfully")
    public void createToken_WithEmptyRoles_ReturnsValidToken() {
        String token = jwtTokenService.createToken(USER_NAME, Collections.emptySet());

        assertNotNull(token, "Token should not be null when roles are empty");
        assertFalse(token.isEmpty(), "Token should not be empty when roles are empty");
    }

    @Test
    @DisplayName("getUsernameFrom token successfully")
    public void getUsernameFromToken_WithValidToken_ReturnsUsername() {
        Role role = new Role(RoleType.USER);
        Set<Role> roles = Collections.singleton(role);
        String token = jwtTokenService.createToken(USER_NAME, roles);
        String username = jwtTokenService.getUsernameFromToken(token);

        assertEquals(USER_NAME, username, "Username should match the expected value");
    }
}