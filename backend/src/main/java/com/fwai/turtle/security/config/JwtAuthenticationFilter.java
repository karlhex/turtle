package com.fwai.turtle.security.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fwai.turtle.security.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.ServletException;
import org.springframework.context.annotation.Lazy;
import java.io.IOException;
import org.springframework.lang.NonNull;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.fwai.turtle.security.service.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwai.turtle.base.dto.ApiResponse;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;
  private final UserDetailsService userDetailsService;
  private final TokenBlacklistService blackListService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public JwtAuthenticationFilter(
      JwtTokenService jwtTokenService, 
      @Lazy UserDetailsService userDetailsService,
      TokenBlacklistService blackListService) {
    this.jwtTokenService = jwtTokenService;
    this.userDetailsService = userDetailsService;
    this.blackListService = blackListService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    
    try {
      final String userName = jwtTokenService.getUsernameFromToken(jwt);
      log.info("jwt " + jwt + " userName " + userName);

      if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
        if (this.jwtTokenService.validateToken(jwt) 
            && !this.blackListService.isTokenBlacklisted(jwt)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write(objectMapper.writeValueAsString(
          ApiResponse.error(401, "Token expired")
      ));
    } catch (io.jsonwebtoken.JwtException e) {
      log.error("JWT token validation failed: {}", e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write(objectMapper.writeValueAsString(
          ApiResponse.error(401, "Invalid token")
      ));
    } catch (Exception e) {
      log.error("Unexpected error during authentication: {}", e.getMessage(), e);
      // Don't catch non-authentication exceptions, let them propagate
      throw e;
    }
  }
}
