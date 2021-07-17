package com.github.callmewaggs.domain;

public class IdGenerator {

  private static IdGenerator idGenerator = new IdGenerator(); // for singleton pattern
  private long id;

  // for singleton pattern
  private IdGenerator() {
    this.id = 1;
  }

  public long generate() {
    return id++;
  }
  
  // for singleton pattern
  public static IdGenerator getInstance() {
	  return idGenerator;
  }
  
  public void changeStartPoint(int start) {
	  id = start;
  }
}
