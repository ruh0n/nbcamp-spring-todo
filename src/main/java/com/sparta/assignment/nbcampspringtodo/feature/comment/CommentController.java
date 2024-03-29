package com.sparta.assignment.nbcampspringtodo.feature.comment;

import com.sparta.assignment.nbcampspringtodo.common.ResponseDto;
import com.sparta.assignment.nbcampspringtodo.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "3. Comment API", description = "Operations about comments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/v1")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "Create a new comment")
  @PostMapping("/todos/{todoId}/comments")
  public ResponseEntity<ResponseDto<CommentResponseDto>> createComment(
      @PathVariable Long todoId,
      @Valid @RequestBody CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    CommentResponseDto data = commentService.createComment(requestDto, todoId, userDetails.getUsername());
    return ResponseEntity.ok(ResponseDto.of(data, "comment 생성 성공"));
  }

  @Operation(summary = "Update a comment")
  @PutMapping("/comments/{commentId}")
  public ResponseEntity<ResponseDto<CommentResponseDto>> updateComment(
      @PathVariable Long commentId,
      @Valid @RequestBody CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    CommentResponseDto data = commentService.updateComment(requestDto, commentId, userDetails.getUsername());
    return ResponseEntity.ok(ResponseDto.of(data, "comment 수정 성공"));
  }

  @Operation(summary = "Delete a comment")
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<ResponseDto<String>> deleteComment(
      @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    String data = commentService.deleteComment(commentId, userDetails.getUsername());
    return ResponseEntity.ok(ResponseDto.of(data, "comment 삭제 성공"));
  }

}
