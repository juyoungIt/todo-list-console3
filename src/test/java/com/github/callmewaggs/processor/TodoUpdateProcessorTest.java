package com.github.callmewaggs.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.callmewaggs.domain.Hist_idGenerator;
import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenu;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class TodoUpdateProcessorTest {

  private TodoUpdateProcessor todoUpdateMenuProcessor;
  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private Hist_idGenerator hist_idGenerator;

  @Before
  public void setup() {
    this.todoRepository = mock(TodoRepository.class);
    this.todoUpdateMenuProcessor = new TodoUpdateProcessor(
    		todoRepository,
    		historyRepository,
    		hist_idGenerator);
  }

  @DisplayName("할 일은 내용과 의존성을 수정할 수 있다.")
  @Test
  public void todo_can_be_updated() {
    // Arrange
    Todo todo = new Todo(1, "할 일", new ArrayList<>());
    when(todoRepository.findAll(any(List.class))).thenReturn(new ArrayList<>());
    when(todoRepository.find(todo.getId())).thenReturn(todo);

    TodoMenuParameter todoMenuParameter =
        new TodoMenuParameter(TodoMenu.UPDATE, todo.getId(), "갱신된 할 일", new ArrayList<>());

    // Act
    todoUpdateMenuProcessor.run(todoMenuParameter);

    // Assert
    verify(todoRepository).findAll(any(List.class));
    verify(todoRepository).find(todo.getId());
    assertEquals("갱신된 할 일", todo.getContent());
    assertEquals(0, todo.getParents().size());
  }

  @DisplayName("할 일을 수정할 때 자기 자신을 의존하도록 의존성을 설정하는 경우 예외를 던진다.")
  @Test
  public void exception_thrown_if_trying_to_depend_on_itself_when_update_todo() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo child = new Todo(2, "자식", new ArrayList<>());
    when(todoRepository.findAll(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(parent, child));
    when(todoRepository.find(child.getId())).thenReturn(child);

    TodoMenuParameter todoMenuParameter =
        new TodoMenuParameter(TodoMenu.UPDATE, child.getId(), "예외 던지는 자식", Arrays.asList(1L, 2L));

    // Act
    IllegalStateException actual =
        assertThrows(
            IllegalStateException.class, () -> todoUpdateMenuProcessor.run(todoMenuParameter));

    // Assert
    verify(todoRepository).findAll(Arrays.asList(1L, 2L));
    verify(todoRepository).find(child.getId());
    assertEquals("자기 자신을 의존하도록 설정할 수 없습니다.", actual.getMessage());
    assertNull(child.getUpdateAt());
  }
}
