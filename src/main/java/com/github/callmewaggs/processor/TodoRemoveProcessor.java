package com.github.callmewaggs.processor;

import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.List;

public class TodoRemoveProcessor implements TodoProcessor {

  private TodoRepository todoRepository;

  public TodoRemoveProcessor(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  @Override
  public void run(TodoMenuParameter todoMenuParameter) {
    List<Todo> children = todoRepository.findAllChildren(todoMenuParameter.getId());
    if (!children.isEmpty()) {
      throw new IllegalStateException("다른 할 일로 부터 의존되어 있습니다. 삭제를 원하는 경우 해당 의존성을 제거해주세요.");
    }
    Todo toBeRemoved = todoRepository.find(todoMenuParameter.getId());
    todoRepository.remove(toBeRemoved);
  }
}
