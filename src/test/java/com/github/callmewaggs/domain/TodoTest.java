package com.github.callmewaggs.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class TodoTest {

  @DisplayName("할 일은 id, 내용, 의존성이 있어야 생성된다.")
  @Test
  public void todo_is_created_only_with_id_and_content() {
    // Arrange
    long id = 1;
    String content = "밥먹기";
    List<Todo> dependencies = new ArrayList<>();

    // Act
    Todo actual = new Todo(id, content, dependencies);

    // Assert
    assertEquals(id, actual.getId());
    assertEquals(content, actual.getContent());
    assertEquals(dependencies, actual.getParents());
  }

  @DisplayName("할 일을 생성할 때 내용이 공백이면 예외를 던진다.")
  @Test
  public void exception_thrown_when_creating_todo_if_the_content_is_blank() {
    // Arrange
    long id = 1;
    String blank = " ";
    List<Todo> dependencies = new ArrayList<>();

    // Act
    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new Todo(id, blank, dependencies));

    // Assert
    assertEquals("할 일의 내용은 공백일 수 없습니다. 다시 입력해주세요.", actual.getMessage());
  }

  @DisplayName("할 일은 생성과 동시에 생성 시간이 자동으로 할당된다.")
  @Test
  public void createAt_assigned_automatically_when_creating_todo() {
    // Arrange
    long id = 1;
    String content = "할일";
    List<Todo> dependencies = new ArrayList<>();

    // Act
    Todo todo = new Todo(id, content, dependencies);

    // Assert
    String expected =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    assertEquals(
        expected, todo.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }

  @DisplayName("할 일은 다른 할 일과 의존성을 가질 수 있다.")
  @Test
  public void todo_can_have_dependency_with_other_todo_object() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo anotherParent = new Todo(2, "또 다른 부모", new ArrayList<>());

    // Act
    Todo child = new Todo(3, "자식", Arrays.asList(parent, anotherParent));

    // Assert
    assertEquals(2, child.getParents().size());
  }

  @DisplayName("할 일의 내용을 수정하면 수정 시간이 자동으로 갱신된다.")
  @Test
  public void updateAt_assigned_automatically_when_update_todo_content() {
    // Arrange
    Todo todo = new Todo(1, "할일", new ArrayList<>());
    String newContent = "새로운 할 일";
    List<Todo> newDependencies = new ArrayList<>();

    // Act
    todo.update(newContent, newDependencies);

    // Assert
    assertEquals(newContent, todo.getContent());
    assertEquals(newDependencies, todo.getParents());
    String expected =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    assertEquals(
        expected, todo.getUpdateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }

  @DisplayName("할 일을 완료하면 완료 시간이 자동으로 할당된다.")
  @Test
  public void finishAt_assigned_automatically_when_finish_todo() {
    // Arrange
    Todo todo = new Todo(1, "할일", new ArrayList<>());

    // Act
    todo.finish();
    LocalDateTime actual = todo.getFinishAt();

    // Assert
    String expected =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    assertEquals(expected, actual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }

  @DisplayName("할 일이 완료 되었는지 확인할 때 완료 되지 않은 경우 예외를 던진다.")
  @Test
  public void exception_thrown_when_check_finished_if_it_is_not_finished() {
    // Arrange
    Todo todo = new Todo(1, "할일", new ArrayList<>());

    // Act
    IllegalStateException actual = assertThrows(IllegalStateException.class, todo::checkFinished);

    // Assert
    assertEquals("다른 할 일로 부터 의존되어 있습니다. 완료를 원하는 경우 다른 할 일을 먼저 완료해주세요.", actual.getMessage());
  }

  @DisplayName("할 일이 완료 되었는지 확인할 때 완료 된 경우 아무 일도 일어나지 않는다.")
  @Test
  public void nothing_happened_when_check_finished_if_it_finished() {
    // Arrange
    Todo todo = new Todo(1, "할일", new ArrayList<>());
    todo.finish();

    // Act
    todo.checkFinished();

    // Assert
    assertNotNull(todo.getFinishAt());
  }

  @DisplayName("할 일은 id, 내용, 의존성, 작성 시간, 최종 수정 시간, 완료 시간에 대한 정보를 제공한다.")
  @Test
  public void todo_provides_information_about_id_content_createAt_updateAt_and_finishAt() {
    // Arrange
    long id = 1;
    String content = "할일";
    List<Todo> dependencies = new ArrayList<>();

    // Act
    Todo todo = new Todo(id, content, dependencies);
    String createAt =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    String newContent = "새로운 할 일";
    todo.update(newContent, dependencies);
    String updateAt =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    todo.finish();
    String finishAt =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // Assert
    assertEquals(id, todo.getId());
    assertEquals(newContent, todo.getContent());
    assertEquals(dependencies, todo.getParents());
    assertEquals(
        createAt, todo.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    assertEquals(
        updateAt, todo.getUpdateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    assertEquals(
        finishAt, todo.getFinishAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }
}
