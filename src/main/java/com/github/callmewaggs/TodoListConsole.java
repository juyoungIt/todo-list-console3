package com.github.callmewaggs;

import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenu;
import com.github.callmewaggs.menu.TodoMenuParameter;
import com.github.callmewaggs.processor.TodoMenuProcessor;
import java.util.Map;

public class TodoListConsole {

  private Map<TodoMenu, TodoMenuProcessor> todoProcessorMapping;
  private TodoRepository todoRepository;
  private IOHelper ioHelper;

  public TodoListConsole(
      Map<TodoMenu, TodoMenuProcessor> todoProcessorMapping,
      TodoRepository todoRepository,
      IOHelper ioHelper) {
    this.todoProcessorMapping = todoProcessorMapping;
    this.todoRepository = todoRepository;
    this.ioHelper = ioHelper;
  }

  public void start() {
    ioHelper.printHelloMessage();
    ioHelper.printMenuWithExample();
    while (true) {
      try {
        String input = ioHelper.inputCommand();
        TodoMenuParameter todoMenuParameter = TodoMenuParameter.parse(input);
        if (todoMenuParameter.getMenu() == TodoMenu.QUIT) {
          break;
        }
        if (todoMenuParameter.getMenu() == TodoMenu.SHOW_LIST) {
          ioHelper.printTodoList(todoRepository.findAll());
          continue;
        }
        TodoMenuProcessor todoMenuProcessor = todoProcessorMapping.get(todoMenuParameter.getMenu());
        todoMenuProcessor.run(todoMenuParameter);
        ioHelper.printTodoList(todoRepository.findAll());
      } catch (Exception e) {
        ioHelper.printMessage(e.getMessage());
      }
    }
  }
}
