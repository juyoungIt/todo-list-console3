package com.github.callmewaggs.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.callmewaggs.domain.Hist_idGenerator;
import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenu;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class TodoRemoveProcessorTest {

  private TodoRemoveProcessor todoRemoveMenuProcessor;
  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private Hist_idGenerator hist_idGenerator;

  @Before
  public void setup() {
    this.todoRepository = mock(TodoRepository.class);
    this.todoRemoveMenuProcessor = new TodoRemoveProcessor(
    		todoRepository,
    		historyRepository,
    		hist_idGenerator);
  }

  @DisplayName("삭제할 할 일의 id를 입력 받아 할 일을 삭제한다.")
  @Test
  public void remove_todo_by_id() {
    // Arrange
    Todo todo = new Todo(1, "할 일", new ArrayList<>());
    when(todoRepository.findAllChildren(todo.getId())).thenReturn(new ArrayList<>());
    when(todoRepository.find(todo.getId())).thenReturn(todo);

    TodoMenuParameter todoMenuParameter =
        new TodoMenuParameter(TodoMenu.REMOVE, todo.getId(), null, null);

    // Act
    todoRemoveMenuProcessor.run(todoMenuParameter);

    // Assert
    verify(todoRepository).findAllChildren(todoMenuParameter.getId());
    verify(todoRepository).find(todoMenuParameter.getId());
    verify(todoRepository).remove(any(Todo.class));
  }

  @DisplayName("다른 할 일로 부터 의존되어 있는 할 일을 삭제하려는 경우 예외를 던진다.")
  @Test
  public void exception_thrown_when_trying_to_remove_depended_todo_object() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo child = new Todo(2, "자식", Collections.singletonList(parent));
    when(todoRepository.findAllChildren(parent.getId()))
        .thenReturn(Collections.singletonList(child));

    TodoMenuParameter todoMenuParameter =
        new TodoMenuParameter(TodoMenu.REMOVE, parent.getId(), null, null);

    // Act
    IllegalStateException actual =
        assertThrows(
            IllegalStateException.class, () -> todoRemoveMenuProcessor.run(todoMenuParameter));

    // Assert
    verify(todoRepository).findAllChildren(todoMenuParameter.getId());
    verify(todoRepository, times(0)).find(todoMenuParameter.getId());
    verify(todoRepository, times(0)).remove(any(Todo.class));
    assertEquals("다른 할 일로 부터 의존되어 있습니다. 삭제를 원하는 경우 해당 의존성을 제거해주세요.", actual.getMessage());
  }
}
