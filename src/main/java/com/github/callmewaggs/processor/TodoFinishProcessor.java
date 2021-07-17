package com.github.callmewaggs.processor;

import com.github.callmewaggs.domain.Hist_idGenerator;
import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.Log;
import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.List;

public class TodoFinishProcessor implements TodoProcessor {

  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private Hist_idGenerator hist_idGenerator;

  // constructor
  public TodoFinishProcessor(
		  TodoRepository todoRepository,
		  HistoryRepository historyRepository,
		  Hist_idGenerator hist_idGenerator) {
    this.todoRepository = todoRepository;
    this.historyRepository = historyRepository;
    this.hist_idGenerator = hist_idGenerator;
  }

  @Override
  public void run(TodoMenuParameter todoMenuParameter) {
    List<Todo> children = todoRepository.findAllChildren(todoMenuParameter.getId());
    for (Todo child : children) {
      child.checkFinished();
    }
    Todo toBeFinished = todoRepository.find(todoMenuParameter.getId());
    toBeFinished.finish();
    Long log_id = hist_idGenerator.generate();
    Log log = new Log(
    		log_id, 
    		todoMenuParameter.getMenu(),
    		todoMenuParameter.getId(),
    		todoRepository.findSpec(todoMenuParameter.getId()).getContent()); // contents is empty!
    historyRepository.add(log);
  }
}
