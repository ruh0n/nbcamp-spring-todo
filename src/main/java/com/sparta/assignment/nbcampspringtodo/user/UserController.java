package com.sparta.assignment.nbcampspringtodo.user;

import com.sparta.assignment.nbcampspringtodo.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignupResponseDto> signUp(
      @Valid @RequestBody SignupRequestDto requestDto
  ) {
    return userService.signup(requestDto);
  }

  @DeleteMapping()
  public ResponseEntity<String> deleteUser(
      @Valid @RequestBody DeleteUserRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    return userService.deleteUser(userDetails);
  }


}
