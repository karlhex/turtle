package com.fwai.turtle.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fwai.turtle.config.security.SecurityConfiguration;
import com.fwai.turtle.config.security.UserDetailsServiceImpl;
import com.fwai.turtle.controller.HelloController;
import com.fwai.turtle.service.interfaces.JwtTokenService;

@WebMvcTest(controllers = HelloController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
class HelloControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtTokenService jwtTokenService;

  @MockBean
  private UserDetailsServiceImpl userDetailsService;


  @Test
  void shouldReturnDefaultMessage() throws Exception {
    this.mockMvc.perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello World"));
  }
}