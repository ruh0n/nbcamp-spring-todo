package com.sparta.assignment.nbcampspringtodo.feature.comment;

import com.sparta.assignment.nbcampspringtodo.common.Timestamped;
import com.sparta.assignment.nbcampspringtodo.feature.todo.Todo;
import com.sparta.assignment.nbcampspringtodo.feature.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Setter
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  private User user;

  @Setter
  @ManyToOne
  @JoinColumn(name = "todo_id", nullable = false, updatable = false)
  private Todo todo;

  public Comment(CommentRequestDto requestDto, User user, Todo todo) {
    this.content = requestDto.getContent();

    this.user = user;
    this.todo = todo;
  }

  public void update(CommentRequestDto requestDto) {
    this.content = requestDto.getContent();
  }

}
