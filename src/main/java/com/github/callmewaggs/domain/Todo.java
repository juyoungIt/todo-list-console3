package com.github.callmewaggs.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Todo todo = (Todo) o;
    return id == todo.id
        && Objects.equals(content, todo.content)
        && Objects.equals(parents, todo.parents)
        && Objects.equals(createAt, todo.createAt)
        && Objects.equals(updateAt, todo.updateAt)
        && Objects.equals(finishAt, todo.finishAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, content, parents, createAt, updateAt, finishAt);
  }
}
