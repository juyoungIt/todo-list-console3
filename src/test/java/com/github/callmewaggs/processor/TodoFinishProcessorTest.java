package com.github.callmewaggs.processor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

public class TodoFinishProcessorTest {

  private TodoFinishProcessor todoFinishMenuProcessor;
  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private Hist_idGenerator hist_idGenerator;

  @Before
  public void setup() {
    this.todoRepository = mock(TodoRepository.class);
    this.todoFinishMenuProcessor = new TodoFinishProcessor(
    		todoRepository,
    		historyRepository,
    		hist_idGenerator);
    this.historyRepository = mock(HistoryRepository.class);
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

    TodoMenuParameter todoMenuParameter =
        new TodoMenuParameter(TodoMenu.FINISH, parent.getId(), null, null);

    // Act
    todoFinishMenuProcessor.run(todoMenuParameter);

    // Assert
    verify(todoRepository).findAllChildren(parent.getId());
    verify(todoRepository).find(parent.getId());
    assertNotNull(parent.getFinishAt());
  }

  @DisplayName("부모를 완료할 때 자식이 하나라도 완료되지 않은 경우 예외를 던진다.")
  @Test
  public void
  exception_thrown_when_trying_to_finish_parent_if_at_least_one_of_child_is_not_finished() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo child = new Todo(1, "자식", new ArrayList<>());
    when(todoRepository.find(parent.getId())).thenReturn(parent);
    when(todoRepository.findAllChildren(parent.getId()))
        .thenReturn(Collections.singletonList(child));

    TodoMenuParameter todoMenuParameter =
        new TodoMenuParameter(TodoMenu.FINISH, parent.getId(), null, null);

    // Act
    IllegalStateException actual =
        assertThrows(
            IllegalStateException.class, () -> todoFinishMenuProcessor.run(todoMenuParameter));

    // Assert
    verify(todoRepository).findAllChildren(parent.getId());
    verify(todoRepository, times(0)).find(parent.getId());
    assertNotNull(actual.getMessage());
  }
}
