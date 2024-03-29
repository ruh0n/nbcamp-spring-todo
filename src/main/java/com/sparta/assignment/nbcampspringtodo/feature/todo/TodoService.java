package com.sparta.assignment.nbcampspringtodo.feature.todo;

import com.sparta.assignment.nbcampspringtodo.common.UserVerifier;
import com.sparta.assignment.nbcampspringtodo.feature.comment.Comment;
import com.sparta.assignment.nbcampspringtodo.feature.comment.CommentRepository;
import com.sparta.assignment.nbcampspringtodo.feature.user.User;
import com.sparta.assignment.nbcampspringtodo.feature.user.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TodoService {

  private final UserVerifier userVerifier;
  private final UserRepository userRepository;
  private final TodoRepository todoRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public TodoDetailResponseDto createTodo(
      TodoRequestDto requestDto, String username
  ) {
    User user = userRepository.findByUsernameOrElseThrow(username);

    Todo todo = new Todo(requestDto, user);

    return new TodoDetailResponseDto(todoRepository.save(todo));
  }

  public TodoDetailResponseDto getTodoDetail(
      Long todoId, String username
  ) {
    Todo todo = userVerifier.verifyTodoWithUser(todoId, username);

    if (todo.isHidden()) {
      User user = userRepository.findByUsernameOrElseThrow(username);
      if (!Objects.equals(todo.getUser().getId(), user.getId())) {
        throw new IllegalArgumentException("권한 없음");
      }
    }

    List<Comment> comments = commentRepository.findAllByTodo_id(todo.getId());

    return new TodoDetailResponseDto(todo, comments);
  }

  public List<TodoResponseDto> getTodosByUserId(
      Long userId, String username
  ) {
    User targetUser = userRepository.findByIdOrElseThrow(userId);
    User currentUser = userRepository.findByUsernameOrElseThrow(username);

    List<Todo> todos;
    if (Objects.equals(targetUser.getId(), currentUser.getId())) {
      todos = todoRepository.findAllByUserIdOrderByLastModifiedDateDesc(currentUser.getId());
    } else {
      todos = todoRepository.findAllByUserIdAndHiddenIsFalseOrderByLastModifiedDateDesc(currentUser.getId());
    }

    return todos.stream().map(TodoResponseDto::new).toList();
  }

  public List<TodoResponseDto> getAllNotHiddenTodos(String username) {

    User user = userRepository.findByUsernameOrElseThrow(username);
    List<Todo> todos = todoRepository.findAllByHiddenIsFalseOrUserIdOrderByLastModifiedDateDesc(user.getId());

    return todos.stream().map(TodoResponseDto::new).toList();
  }

  public List<TodoResponseDto> searchTodoByTitle(
      String search, String username
  ) {
    User user = userRepository.findByUsernameOrElseThrow(username);
    List<Todo> todos = todoRepository.findTodoByTitle(search, user.getId());

    return todos.stream().map(TodoResponseDto::new).toList();
  }

  @Transactional
  public TodoResponseDto updateTodo(
      Long todoId, TodoRequestDto requestDto, String username
  ) {
    Todo todo = userVerifier.verifyTodoWithUser(todoId, username);

    todo.update(requestDto);

    return new TodoResponseDto(todo);
  }

  @Transactional()
  public String deleteTodo(Long todoId, String username) {
    Todo todo = userVerifier.verifyTodoWithUser(todoId, username);

    commentRepository.deleteAll(commentRepository.findAllByTodo_id(todo.getId()));

    todoRepository.deleteById(todo.getId());

    return "username: " + todo.getTitle();
  }

}
