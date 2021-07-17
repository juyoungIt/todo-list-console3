package com.github.callmewaggs;

import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenu;
import com.github.callmewaggs.menu.TodoMenuParameter;
import com.github.callmewaggs.processor.TodoProcessor;
import java.util.Map;

public class TodoListConsole {

  private Map<TodoMenu, TodoProcessor> todoProcessorMapping;
  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private IOHelper ioHelper;

  public TodoListConsole(
      Map<TodoMenu, TodoProcessor> todoProcessorMapping,
      TodoRepository todoRepository,
      HistoryRepository historyRepository,
      IOHelper ioHelper) {
    this.todoProcessorMapping = todoProcessorMapping;
    this.todoRepository = todoRepository;
    this.historyRepository = historyRepository;
    this.ioHelper = ioHelper;
  }

  void start() {
    ioHelper.printHelloMessage();
    ioHelper.printMenuWithExample();
    while (true) {
      try {
        String input = ioHelper.inputCommand();
        TodoMenuParameter todoMenuParameter = TodoMenuParameter.parse(input);
        TodoMenu menu = todoMenuParameter.getMenu();
        if (menu == TodoMenu.QUIT) {
          break;
        }
        if (menu == TodoMenu.SHOW_LIST) {
          ioHelper.printTodoList(todoRepository.findAll());
          continue;
        }
        // print the all history log
        if (menu == TodoMenu.HISTORY) {
        	ioHelper.printHistory(historyRepository.findAll());
        	continue;
        }
        // save the current todo list to json file
        if(menu == TodoMenu.SAVE) {
        	ioHelper.printMessage(TodoRepository.getInstance().save());
        }
        // load the saved todo list from the json file
        if(menu == TodoMenu.LOAD) {
        	ioHelper.printMessage(TodoRepository.getInstance().load());
        }
        TodoProcessor todoProcessor = todoProcessorMapping.get(menu);
        todoProcessor.run(todoMenuParameter);
        ioHelper.printTodoList(todoRepository.findAll());
      } catch (Exception e) {
        ioHelper.printMessage(e.getMessage());
      }
    }
  }
}
