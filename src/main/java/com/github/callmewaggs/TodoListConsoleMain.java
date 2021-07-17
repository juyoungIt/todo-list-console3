package com.github.callmewaggs;

import com.github.callmewaggs.domain.Hist_idGenerator;
import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.IdGenerator;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenu;
import com.github.callmewaggs.processor.TodoCreateProcessor;
import com.github.callmewaggs.processor.TodoFinishProcessor;
import com.github.callmewaggs.processor.TodoProcessor;
import com.github.callmewaggs.processor.TodoRemoveProcessor;
import com.github.callmewaggs.processor.TodoUpdateProcessor;
import java.util.HashMap;
import java.util.Map;

public class TodoListConsoleMain {

  public static void main(String[] args) {
    TodoRepository todoRepository = TodoRepository.getInstance();
    HistoryRepository historyRepository = HistoryRepository.getInstance();
    IdGenerator idGenerator = IdGenerator.getInstance();
    Hist_idGenerator hist_idGenerator = new Hist_idGenerator();

    Map<TodoMenu, TodoProcessor> todoServicesMapping = new HashMap<>();
    todoServicesMapping.put(
        TodoMenu.CREATE, new TodoCreateProcessor(
        		todoRepository, 
        		historyRepository,
        		idGenerator,
        		hist_idGenerator));
    todoServicesMapping.put(
    		TodoMenu.UPDATE, new TodoUpdateProcessor(
    				todoRepository,
    				historyRepository,
    				hist_idGenerator));
    todoServicesMapping.put(
    		TodoMenu.REMOVE, new TodoRemoveProcessor(
    				todoRepository,
    				historyRepository,
    				hist_idGenerator));
    todoServicesMapping.put(
    		TodoMenu.FINISH, new TodoFinishProcessor(
    				todoRepository,
    				historyRepository,
    				hist_idGenerator));

    IOHelper ioHelper = new IOHelper();
    TodoListConsole todoListConsole =
        new TodoListConsole(todoServicesMapping, todoRepository, historyRepository, ioHelper);
    todoListConsole.start();
  }
}
