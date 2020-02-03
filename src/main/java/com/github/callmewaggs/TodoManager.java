package com.github.callmewaggs;

import java.util.List;

public class TodoManager {

  private TodoRepository todoRepository;
  private IdGenerator idGenerator;

  public TodoManager(TodoRepository todoRepository, IdGenerator idGenerator) {
    this.todoRepository = todoRepository;
    this.idGenerator = idGenerator;
  }

  public Todo createTodo(String content, List<Long> parentIds) {
    List<Todo> dependencies = todoRepository.findAll(parentIds);
    long id = idGenerator.generate();
    Todo todo = new Todo(id, content, dependencies);
    todoRepository.add(todo);
    return todo;
  }

  public void updateTodo(long id, String content, List<Long> parentIds) {
    List<Todo> dependencies = todoRepository.findAll(parentIds);
    Todo toBeUpdated = todoRepository.find(id);
    if (dependencies.contains(toBeUpdated)) {
      throw new IllegalStateException("자기 자신을 의존하도록 설정할 수 없습니다.");
    }
    toBeUpdated.update(content, dependencies);
  }

  public void removeTodo(long id) {
    List<Todo> children = todoRepository.findAllChildren(id);
    if (!children.isEmpty()) {
      throw new IllegalStateException("다른 할 일로 부터 의존되어 있습니다. 삭제를 원하는 경우 해당 의존성을 제거해주세요.");
    }
    Todo toBeRemoved = todoRepository.find(id);
    todoRepository.remove(toBeRemoved);
  }

  public void finishTodo(long id) {
    List<Todo> children = todoRepository.findAllChildren(id);
    for (Todo child : children) {
      child.checkFinished();
    }
    Todo toBeFinished = todoRepository.find(id);
    toBeFinished.finish();
  }
}
