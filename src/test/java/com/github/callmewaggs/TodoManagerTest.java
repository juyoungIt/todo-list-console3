package com.github.callmewaggs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class TodoManagerTest {

  private TodoManager todoManager;
  private TodoRepository todoRepository;
  private IdGenerator idGenerator;

  @Before
  public void setup() {
    todoRepository = mock(TodoRepository.class);
    idGenerator = mock(IdGenerator.class);
    this.todoManager = new TodoManager(todoRepository, idGenerator);
  }

  @DisplayName("할 일을 생성한다.")
  @Test
  public void create_todo() {
    // Arrange
    String content = "할 일";
    when(idGenerator.generate()).thenReturn(1L);

    // Act
    todoManager.createTodo(content, new ArrayList<>());

    // Assert
    verify(idGenerator).generate();
    verify(todoRepository).add(any(Todo.class));
  }

  // TODO : Q. 이렇게 mocking 해서 사용하는게 맞는지(createTodo() 메서드는 위에서 검증했기 때문에
  //  Todo를 생성할 때 이를 굳이 다시 사용하지 않았음. 이래도 되는지
  @DisplayName("할 일을 생성할 때 의존성이 있는 경우 설정할 수 있다.")
  @Test
  public void create_todo_with_dependencies() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo anotherParent = new Todo(2, "또 다른 부모", new ArrayList<>());
    when(todoRepository.findAll(Arrays.asList(parent.getId(), anotherParent.getId())))
        .thenReturn(Arrays.asList(parent, anotherParent));

    // Act
    Todo actual =
        todoManager.createTodo("자식", Arrays.asList(parent.getId(), anotherParent.getId()));

    // Assert
    verify(idGenerator).generate();
    verify(todoRepository).findAll(Arrays.asList(parent.getId(), anotherParent.getId()));
    assertEquals(2, actual.getParents().size());
    assertEquals(
        actual.getParents().stream().filter(e -> e.equals(parent)).findAny().get(), parent);
    assertEquals(
        actual.getParents().stream().filter(e -> e.equals(anotherParent)).findAny().get(),
        anotherParent);
  }

  @DisplayName("할 일은 내용과 의존성을 수정할 수 있다.")
  @Test
  public void todo_can_be_updated() {
    // Arrange
    Todo todo = new Todo(1, "할 일", new ArrayList<>());
    when(todoRepository.find(todo.getId())).thenReturn(todo);

    // Act
    todoManager.updateTodo(todo.getId(), "갱신된 할 일", new ArrayList<>());

    // Assert
    verify(todoRepository).find(todo.getId());
    assertEquals("갱신된 할 일", todo.getContent());
  }

  @DisplayName("할 일을 수정할 때 자기 자신을 의존하도록 의존성을 설정하는 경우 예외를 던진다.")
  @Test
  public void exception_thrown_if_trying_to_depend_on_itself_when_update_todo() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo child = new Todo(2, "자식", new ArrayList<>());
    when(todoRepository.findAll(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(parent, child));
    when(todoRepository.find(child.getId())).thenReturn(child);

    // Act
    IllegalStateException actual =
        assertThrows(
            IllegalStateException.class,
            () -> todoManager.updateTodo(2, "예외 던지는 자식", Arrays.asList(1L, 2L)));

    // Assert
    assertEquals("자기 자신을 의존하도록 설정할 수 없습니다.", actual.getMessage());
  }

  @DisplayName("삭제할 할 일의 id를 입력 받아 할 일을 삭제한다.")
  @Test
  public void remove_todo_by_id() {
    // Arrange
    Todo todo = new Todo(1, "할 일", new ArrayList<>());
    when(todoRepository.findAllChildren(todo.getId())).thenReturn(new ArrayList<>());
    when(todoRepository.find(todo.getId())).thenReturn(todo);

    // Act
    todoManager.removeTodo(todo.getId());

    // Assert
    verify(todoRepository).remove(any(Todo.class));
  }

  @DisplayName("다른 할 일로 부터 의존되어 있는 할 일을 삭제하려는 경우 예외를 던진다.")
  @Test
  public void exception_thrown_when_trying_to_remove_depended_todo_object() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo child = new Todo(2, "자식", Collections.singletonList(parent));
    when(todoRepository.findAllChildren(child.getId()))
        .thenReturn(Collections.singletonList(parent));

    // Act
    IllegalStateException actual =
        assertThrows(IllegalStateException.class, () -> todoManager.removeTodo(child.getId()));

    // Assert
    assertEquals("다른 할 일로 부터 의존되어 있습니다. 삭제를 원하는 경우 해당 의존성을 제거해주세요.", actual.getMessage());
  }

  @DisplayName("완료할 할 일의 id를 입력 받아 할 일을 완료한다.")
  @Test
  public void finish_todo_by_id() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo child = new Todo(1, "자식", new ArrayList<>());
    when(todoRepository.find(parent.getId())).thenReturn(parent);
    when(todoRepository.findAllChildren(parent.getId()))
        .thenReturn(Collections.singletonList(child));
    child.finish();

    // Act
    todoManager.finishTodo(parent.getId());

    // Assert
    assertNotNull(parent.getFinishAt());
  }

  @DisplayName("부모를 완료할 때 자식이 하나라도 완료되지 않은 경우 예외를 던진다.")
  @Test
  public void exception_thrown_when_trying_to_finish_parent_if_at_least_one_of_child_is_not_finished() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo child = new Todo(1, "자식", new ArrayList<>());
    when(todoRepository.find(parent.getId())).thenReturn(parent);
    when(todoRepository.findAllChildren(parent.getId()))
        .thenReturn(Collections.singletonList(child));

    // Act
    IllegalStateException actual =
        assertThrows(IllegalStateException.class, () -> todoManager.finishTodo(parent.getId()));

    // Assert
    assertNotNull(actual.getMessage());
  }
}
