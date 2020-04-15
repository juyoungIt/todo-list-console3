package com.github.callmewaggs.domain;

public class IdGenerator {

  private long id;

  public IdGenerator() {
    this.id = 1;
  }

  public long generate() {
    return id++;
  }
}
