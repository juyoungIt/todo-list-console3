package com.github.callmewaggs.domain;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
	
	  private static HistoryRepository historyRepository = new HistoryRepository();
	  private List<Log> historyList;

	  // for singleton pattern
	  private HistoryRepository() {
	    this.historyList = new ArrayList<>();
	  }
	  
	  // for singleton pattern
	  public static HistoryRepository getInstance() {
		 return historyRepository;
	  }

	  public void add(Log log) {
	    historyList.add(log);
	  }

	  public Log find(long id) {
	    return historyList.stream()
	        .filter(log -> log.getLog_id() == id)
	        .findFirst()
	        .orElseThrow(() -> new IllegalStateException("잘못된 id가 입력되었습니다. id를 다시 확인해 주세요."));
	  }

	  public List<Log> findAll() {
	    return historyList;
	  }

	  public List<Log> findAll(List<Long> ids) {
	    List<Log> logs = new ArrayList<>();
	    for (long id : ids) {
	      Log log = find(id);
	      logs.add(log);
	    }
	    return logs;
	  }

	  public int size() {
	    return historyList.size();
	  }
}
