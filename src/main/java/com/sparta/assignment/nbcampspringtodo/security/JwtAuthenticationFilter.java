package com.sparta.assignment.nbcampspringtodo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.assignment.nbcampspringtodo.common.JwtUtil;
import com.sparta.assignment.nbcampspringtodo.feature.user.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtUtil jwtUtil;
  private String username;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
    setFilterProcessesUrl("/api/user/v1/users/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response
  ) throws AuthenticationException {
    try {
      LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
      this.username = requestDto.getUsername();
      log.info(username + " 로그인 시도");

      return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword(), null));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult
  ) {
    this.username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

    String token = jwtUtil.createToken(username);

    log.info(username + " 로그인 성공");
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed
  ) {

    log.info(username + " 로그인 실패");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }

}
