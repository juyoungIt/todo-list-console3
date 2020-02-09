package com.github.callmewaggs.processor;

import com.github.callmewaggs.IdGenerator;
import com.github.callmewaggs.Todo;
import com.github.callmewaggs.TodoMenuParameter;
import com.github.callmewaggs.TodoMenuProcessor;
import com.github.callmewaggs.TodoRepository;
import java.util.List;

public class TodoCreateMenuProcessor implements TodoMenuProcessor {

  private TodoRepository todoRepository;
  private IdGenerator idGenerator;

  public TodoCreateMenuProcessor(TodoRepository todoRepository, IdGenerator idGenerator) {
    this.todoRepository = todoRepository;
    this.idGenerator = idGenerator;
  }

  @Override
  public void run(TodoMenuParameter todoMenuParameter) {
    List<Todo> dependencies = todoRepository.findAll(todoMenuParameter.getParentIds());
    long id = idGenerator.generate();
    Todo todo = new Todo(id, todoMenuParameter.getContent(), dependencies);
    todoRepository.add(todo);
  }
}
