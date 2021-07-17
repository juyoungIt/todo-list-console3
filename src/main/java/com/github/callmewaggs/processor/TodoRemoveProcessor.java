package com.github.callmewaggs.processor;

import com.github.callmewaggs.domain.Hist_idGenerator;
import com.github.callmewaggs.domain.HistoryRepository;
import com.github.callmewaggs.domain.Log;
import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.domain.TodoRepository;
import com.github.callmewaggs.menu.TodoMenuParameter;
import java.util.List;

public class TodoRemoveProcessor implements TodoProcessor {

  private TodoRepository todoRepository;
  private HistoryRepository historyRepository;
  private Hist_idGenerator hist_idGenerator;

  public TodoRemoveProcessor(
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
    if (!children.isEmpty()) {
      throw new IllegalStateException("다른 할 일로 부터 의존되어 있습니다. 삭제를 원하는 경우 해당 의존성을 제거해주세요.");
    }
    Todo toBeRemoved = todoRepository.find(todoMenuParameter.getId());
    Long log_id = hist_idGenerator.generate();
    Log log = new Log(
    		log_id, 
    		todoMenuParameter.getMenu(),
    		todoMenuParameter.getId(), 
    		todoRepository.findSpec(todoMenuParameter.getId()).getContent()); // contents is empty!
    historyRepository.add(log); 
    todoRepository.remove(toBeRemoved);
  }
}
