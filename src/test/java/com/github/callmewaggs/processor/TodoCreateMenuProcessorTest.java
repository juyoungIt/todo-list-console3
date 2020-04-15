package com.github.callmewaggs.processor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.callmewaggs.domain.IdGenerator;
import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenu;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class TodoCreateMenuProcessorTest {

  private TodoCreateMenuProcessor todoCreateMenuProcessor;
  private TodoRepository todoRepository;
  private IdGenerator idGenerator;

  @Before
  public void setup() {
    this.todoRepository = mock(TodoRepository.class);
    this.idGenerator = mock(IdGenerator.class);
    this.todoCreateMenuProcessor = new TodoCreateMenuProcessor(todoRepository, idGenerator);
  }

  @DisplayName("할 일을 생성한다.")
  @Test
  public void create_todo() {
    // Arrange
    TodoMenuParameter todoMenuParameter = new TodoMenuParameter(TodoMenu.CREATE, null, "할 일", null);
    when(idGenerator.generate()).thenReturn(1L);

    // Act
    todoCreateMenuProcessor.run(todoMenuParameter);

    // Assert
    verify(idGenerator).generate();
    verify(todoRepository).add(any(Todo.class));
  }

  @DisplayName("할 일을 생성할 때 의존성이 있는 경우 설정할 수 있다.")
  @Test
  public void create_todo_with_dependencies() {
    // Arrange
    Todo parent = new Todo(1, "부모", new ArrayList<>());
    Todo anotherParent = new Todo(2, "또 다른 부모", new ArrayList<>());
    when(todoRepository.findAll(Arrays.asList(parent.getId(), anotherParent.getId())))
        .thenReturn(Arrays.asList(parent, anotherParent));

    TodoMenuParameter todoMenuParameter =
        new TodoMenuParameter(
            TodoMenu.CREATE, null, "자식", Arrays.asList(parent.getId(), anotherParent.getId()));

    // Act
    todoCreateMenuProcessor.run(todoMenuParameter);

    // Assert
    verify(idGenerator).generate();
    verify(todoRepository).findAll(Arrays.asList(parent.getId(), anotherParent.getId()));
    verify(todoRepository).add(any(Todo.class));
  }
}
