package com.github.callmewaggs.processor;

import com.github.callmewaggs.domain.Hist_idGenerator;
import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.IdGenerator;
import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.Log;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.List;

public class TodoCreateProcessor implements TodoProcessor {

  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private IdGenerator idGenerator;
  private Hist_idGenerator hist_idGenerator;

  // constructor
  public TodoCreateProcessor(
		  TodoRepository todoRepository, HistoryRepository historyRepository, 
		  IdGenerator idGenerator, Hist_idGenerator hist_idGenerator) {
    this.todoRepository = todoRepository;
    this.historyRepository = historyRepository;
    this.idGenerator = idGenerator;
    this.hist_idGenerator = hist_idGenerator;
  }

  @Override
  public void run(TodoMenuParameter todoMenuParameter) {
    List<Todo> dependencies = todoRepository.findAll(todoMenuParameter.getParentIds());
    long id = idGenerator.generate();
    Long log_id = hist_idGenerator.generate();
    Todo todo = new Todo(id, todoMenuParameter.getContent(), dependencies);
    Log log = new Log(
    		log_id, 
    		todoMenuParameter.getMenu(),
    		id, 
    		todoMenuParameter.getContent());
    
    todoRepository.add(todo);
    historyRepository.add(log);
  }
}
