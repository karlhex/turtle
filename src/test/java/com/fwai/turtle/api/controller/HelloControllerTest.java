package com.fwai.turtle.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fwai.turtle.security.SecurityConfiguration;

@WebMvcTest(controllers = HelloController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
class HelloControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturnDefaultMessage() throws Exception {
    this.mockMvc.perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello World"));
  }
}