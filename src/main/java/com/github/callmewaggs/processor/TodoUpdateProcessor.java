package com.github.callmewaggs.processor;

import com.github.callmewaggs.domain.Hist_idGenerator;
import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.Log;
import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.List;

public class TodoUpdateProcessor implements TodoProcessor {

  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private Hist_idGenerator hist_idGenerator;

  public TodoUpdateProcessor(
		  TodoRepository todoRepository,
		  HistoryRepository historyRepository,
		  Hist_idGenerator hist_idGenerator) {
    this.todoRepository = todoRepository;
    this.historyRepository = historyRepository;
    this.hist_idGenerator = hist_idGenerator;
  }

  @Override
  public void run(TodoMenuParameter todoMenuParameter) {
    List<Todo> dependencies = todoRepository.findAll(todoMenuParameter.getParentIds());
    Todo toBeUpdated = todoRepository.find(todoMenuParameter.getId());
    if (dependencies.contains(toBeUpdated)) {
      throw new IllegalStateException("자기 자신을 의존하도록 설정할 수 없습니다.");
    }
    toBeUpdated.update(todoMenuParameter.getContent(), dependencies);
    Long log_id = hist_idGenerator.generate();
    Log log = new Log(
    		log_id, 
    		todoMenuParameter.getMenu(),
    		todoMenuParameter.getId(), 
    		todoMenuParameter.getContent());
    historyRepository.add(log);
  }
}
