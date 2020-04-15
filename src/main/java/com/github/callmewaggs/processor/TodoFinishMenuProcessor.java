package com.github.callmewaggs.processor;

import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.List;

public class TodoFinishMenuProcessor implements TodoMenuProcessor {

  private TodoRepository todoRepository;

  public TodoFinishMenuProcessor(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  @Override
  public void run(TodoMenuParameter todoMenuParameter) {
    List<Todo> children = todoRepository.findAllChildren(todoMenuParameter.getId());
    for (Todo child : children) {
      child.checkFinished();
    }
    Todo toBeFinished = todoRepository.find(todoMenuParameter.getId());
    toBeFinished.finish();
  }
}
