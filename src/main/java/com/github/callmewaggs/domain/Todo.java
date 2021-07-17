package com.github.callmewaggs.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Todo {

  private long id;
  private String content;
  private List<Todo> parents;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private LocalDateTime finishAt;

  public Todo(long id, String content, List<Todo> parents) {
    if (content.equals(" ")) {
      throw new IllegalArgumentException("할 일의 내용은 공백일 수 없습니다. 다시 입력해주세요.");
    }
    this.id = id;
    this.content = content;
    this.createAt = LocalDateTime.now();
    this.parents = parents;
  }
  
  // load and add Todo data to repository (through json file parsing)
  public Todo(long id, String content, List<Todo> parents, LocalDateTime createAt, LocalDateTime updateAt, LocalDateTime finishAt) {
	  this.id = id;
	  this.content = content;
	  this.parents = parents;
	  this.createAt = createAt;
	  this.updateAt = updateAt;
	  this.finishAt = finishAt;
	  // need deep copy technique here???
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public LocalDateTime getCreateAt() {
    return createAt;
  }

  public List<Todo> getParents() {
    return parents;
  }

  public LocalDateTime getUpdateAt() {
    return updateAt;
  }

  public LocalDateTime getFinishAt() {
    return finishAt;
  }

  public void update(String content, List<Todo> parents) {
    this.content = content;
    this.parents = parents;
    this.updateAt = LocalDateTime.now();
  }

  public void finish() {
    this.finishAt = LocalDateTime.now();
  }

  public void checkFinished() {
    if (this.finishAt == null) {
      throw new IllegalStateException("다른 할 일로 부터 의존되어 있습니다. 완료를 원하는 경우 다른 할 일을 먼저 완료해주세요.");
    }
  }
}
