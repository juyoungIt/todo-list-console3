package com.github.callmewaggs;

public class TodoListConsoleMain {

  public static void main(String[] args) {
    TodoRepository todoRepository = new TodoRepository();
    IdGenerator idGenerator = new IdGenerator();
    TodoManager todoManager = new TodoManager(todoRepository, idGenerator);
    TodoListConsoleApp todoListConsoleApp = new TodoListConsoleApp(todoManager);
    todoListConsoleApp.start();
  }
}
