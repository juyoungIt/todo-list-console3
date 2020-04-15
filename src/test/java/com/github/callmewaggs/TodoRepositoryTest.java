package com.github.callmewaggs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class TodoRepositoryTest {

  private TodoRepository todoRepository;

  @Before
  public void setup() {
    todoRepository = new TodoRepository();
  }

  @DisplayName("새로운 할 일을 리스트에 추가한다.")
  @Test
  public void add_todo() {
    // Arrange
    Todo todo = new Todo(1, "할일", new ArrayList<>());

    // Act
    todoRepository.add(todo);

    // Assert
    assertEquals(1, todoRepository.size());
  }

  @DisplayName("할 일을 id로 조회한다.")
  @Test
  public void find_todo_by_id() {
    // Arrange
    long id = 1;
    Todo todo = new Todo(id, "할일", new ArrayList<>());
    todoRepository.add(todo);

    long anotherId = 10;
    Todo anotherTodo = new Todo(anotherId, "또 다른 할일", new ArrayList<>());
    todoRepository.add(anotherTodo);

    // Act
    Todo foundTodo = todoRepository.find(id);
    Todo anotherFoundTodo = todoRepository.find(anotherId);

    // Assert
    assertEquals(todo, foundTodo);
    assertEquals(anotherTodo, anotherFoundTodo);
  }

  @DisplayName("존재하지 않는 할 일 id에 대한 조회는 예외를 던진다.")
  @Test
  public void exception_thrown_when_trying_to_find_todo_by_non_exist_id() {
    // Arrange
    long nonExistId = -1;

    // Act
    IllegalStateException actual =
        assertThrows(IllegalStateException.class, () -> todoRepository.find(nonExistId));

    // Assert
    assertEquals("잘못된 id가 입력되었습니다. id를 다시 확인해 주세요.", actual.getMessage());
  }

  @DisplayName("할 일을 모두 리스트로 반환한다.")
  @Test
  public void find_all_of_todo_objects() {
    // Arrange
    Todo todo1 = new Todo(1, "할 일1", new ArrayList<>());
    Todo todo2 = new Todo(2, "할 일2", new ArrayList<>());
    todoRepository.add(todo1);
    todoRepository.add(todo2);

    // Act
    List<Todo> todos = todoRepository.findAll();

    // Assert
    assertTrue(todos.stream().anyMatch(e -> e.equals(todo1)));
    assertTrue(todos.stream().anyMatch(e -> e.equals(todo2)));
    assertEquals(2, todos.size());
  }

  @DisplayName("여러 id에 대해 일치하는 할 일을 모두 찾아 리스트로 반환한다.")
  @Test
  public void find_all_of_input_id_list_and_return_its_todo_objects_list() {
    // Arrange
    Todo todo1 = new Todo(1, "할 일1", new ArrayList<>());
    Todo todo2 = new Todo(2, "할 일2", new ArrayList<>());
    todoRepository.add(todo1);
    todoRepository.add(todo2);

    List<Long> ids = Arrays.asList(todo1.getId(), todo2.getId());

    // Act
    List<Todo> todos = todoRepository.findAll(ids);

    // Assert
    assertTrue(todos.stream().anyMatch(e -> e.equals(todo1)));
    assertTrue(todos.stream().anyMatch(e -> e.equals(todo2)));
  }

  @DisplayName("입력 받은 id를 의존하고 있는 객체를 리스트로 만들어 반환한다.")
  @Test
  public void find_all_children_of_input_id() {
    // Arrange
    Todo todo1 = new Todo(1, "할 일1", new ArrayList<>());
    Todo todo2 = new Todo(2, "할 일2", Collections.singletonList(todo1));
    Todo todo3 = new Todo(3, "할 일3", Collections.singletonList(todo1));
    todoRepository.add(todo1);
    todoRepository.add(todo2);
    todoRepository.add(todo3);

    // Act
    List<Todo> children = todoRepository.findAllChildren(todo1.getId());

    // Assert
    assertTrue(children.stream().anyMatch(e -> e.equals(todo2)));
    assertTrue(children.stream().anyMatch(e -> e.equals(todo3)));
  }

  @DisplayName("할 일 객체를 받아와 일치하는 객체를 삭제한다.")
  @Test
  public void remove_todo_by_todo_object() {
    // Arrange
    Todo removed = new Todo(1, "삭제될 할 일", new ArrayList<>());
    todoRepository.add(removed);

    // Act
    todoRepository.remove(removed);

    // Assert
    assertEquals(0, todoRepository.size());
  }
}
