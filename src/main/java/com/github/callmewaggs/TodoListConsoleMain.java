package com.github.callmewaggs;

import com.github.callmewaggs.processor.TodoCreateMenuProcessor;
import com.github.callmewaggs.processor.TodoFinishMenuProcessor;
import com.github.callmewaggs.processor.TodoRemoveMenuProcessor;
import com.github.callmewaggs.processor.TodoUpdateMenuProcessor;
import java.util.HashMap;
import java.util.Map;

public class TodoListConsoleMain {

  public static void main(String[] args) {
    TodoRepository todoRepository = new TodoRepository();
    IdGenerator idGenerator = new IdGenerator();

    Map<TodoMenu, TodoMenuProcessor> todoServicesMapping = new HashMap<>();
    todoServicesMapping.put(
        TodoMenu.CREATE, new TodoCreateMenuProcessor(todoRepository, idGenerator));
    todoServicesMapping.put(TodoMenu.UPDATE, new TodoUpdateMenuProcessor(todoRepository));
    todoServicesMapping.put(TodoMenu.REMOVE, new TodoRemoveMenuProcessor(todoRepository));
    todoServicesMapping.put(TodoMenu.FINISH, new TodoFinishMenuProcessor(todoRepository));

    IOHelper ioHelper = new IOHelper();
    TodoListConsole todoListConsole =
        new TodoListConsole(todoServicesMapping, todoRepository, ioHelper);
    todoListConsole.start();
  }
}
