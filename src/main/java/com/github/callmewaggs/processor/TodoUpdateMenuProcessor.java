package com.github.callmewaggs.processor;

import com.github.callmewaggs.Todo;
import com.github.callmewaggs.TodoMenuParameter;
import com.github.callmewaggs.TodoMenuProcessor;
import com.github.callmewaggs.TodoRepository;
import java.util.List;

public class TodoUpdateMenuProcessor implements TodoMenuProcessor {

  private TodoRepository todoRepository;

  public TodoUpdateMenuProcessor(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  @Override
  public void run(TodoMenuParameter todoMenuParameter) {
    List<Todo> dependencies = todoRepository.findAll(todoMenuParameter.getParentIds());
    Todo toBeUpdated = todoRepository.find(todoMenuParameter.getId());
    if (dependencies.contains(toBeUpdated)) {
      throw new IllegalStateException("자기 자신을 의존하도록 설정할 수 없습니다.");
    }
    toBeUpdated.update(todoMenuParameter.getContent(), dependencies);
  }
}
