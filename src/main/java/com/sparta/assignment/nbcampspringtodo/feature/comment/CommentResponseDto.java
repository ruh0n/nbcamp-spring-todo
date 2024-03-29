package com.sparta.assignment.nbcampspringtodo.feature.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.assignment.nbcampspringtodo.feature.user.UserDetailResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {

  private final Long id;

  @Schema(example = "Sample Content")
  private final String content;
  private final UserDetailResponseDto user;


  public CommentResponseDto(Comment comment) {
    this.id = comment.getId();
    this.content = comment.getContent();
    this.user = new UserDetailResponseDto(comment.getUser());
  }

}
