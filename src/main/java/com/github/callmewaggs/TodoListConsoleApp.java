package com.github.callmewaggs;

import java.util.ArrayList;
import java.util.List;

public class TodoListConsoleApp {

  private TodoManager todoManager;

  public TodoListConsoleApp(TodoManager todoManager) {
    this.todoManager = todoManager;
  }

  public void start() {
    IOHelper.printHelloMessage();
    IOHelper.printMenuWithExample();
    boolean flag = true;
    do {
      String input = IOHelper.inputCommand();
      String[] parsed = input.split(" ");
      String menu = parsed[0];
      try {
        switch (menu) {
          case "0":
            flag = false;
            break;
          case "1":
            showAllTodos();
            break;
          case "2":
            create(parsed);
            showAllTodos();
            break;
          case "3":
            update(parsed);
            showAllTodos();
            break;
          case "4":
            remove(parsed);
            showAllTodos();
            break;
          case "5":
            finish(parsed);
            showAllTodos();
            break;
          default:
            IOHelper.printMessage("Wrong menu. Try again.");
            break;
        }
      } catch (Exception e) {
        IOHelper.printMessage(e.getMessage());
      }
    } while (flag);
  }

  private void showAllTodos() {
    IOHelper.printTodoList(todoManager.getAllTodos());
  }

  private void create(String[] parsed) {
    String content = parsed[1];
    todoManager.createTodo(content, getParents(parsed, 2));
  }

  private void update(String[] parsed) {
    long id = Long.parseLong(parsed[1]);
    String content = parsed[2];
    todoManager.updateTodo(id, content, getParents(parsed, 3));
  }

  private List<Long> getParents(String[] parsed, int threshold) {
    List<Long> parents = new ArrayList<>();
    if (parsed.length > threshold) {
      for (int i = threshold; i < parsed.length; ++i) {
        parents.add(Long.parseLong(parsed[i].substring(1)));
      }
    }
    return parents;
  }

  private void remove(String[] parsed) {
    long id = Long.parseLong(parsed[1]);
    todoManager.removeTodo(id);
  }

  private void finish(String[] parsed) {
    long id = Long.parseLong(parsed[1]);
    todoManager.finishTodo(id);
  }
}
