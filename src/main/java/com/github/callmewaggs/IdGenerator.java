package com.github.callmewaggs;

public class IdGenerator {

  private long id;

  public IdGenerator() {
    this.id = 1;
  }

  public long generate() {
    return id++;
  }
}
