package com.github.callmewaggs.domain;

import java.time.LocalDateTime;

import com.github.callmewaggs.menu.TodoMenu;

public class Log {
	private Long log_id;           // log id
	private TodoMenu instType;     // instruction type
	private Long todo_id;          // todo id
	private String contents;       // the contents of todo
	private LocalDateTime logTime; // the log time
	
	// constructor
	public Log(Long log_id, TodoMenu instType, Long todo_id, String contents) {
		this.log_id = log_id;
		this.instType = instType;
		this.todo_id = todo_id;
		this.contents = contents;
		this.logTime = LocalDateTime.now(); // update the log time
	}
	
	// gettter
	public Long getLog_id() {
		return log_id;
	}
	public TodoMenu getInstType() {
		return instType;
	}
	public Long getTodo_id() {
		return todo_id;
	}
	public String getContents() {
		return contents;
	}
	public LocalDateTime getLogTime() {
		return logTime;
	}
}
