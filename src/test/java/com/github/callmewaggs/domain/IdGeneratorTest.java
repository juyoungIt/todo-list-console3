package com.github.callmewaggs.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IdGeneratorTest {

  @Test
  public void generate_test() {
    IdGenerator idGenerator = IdGenerator.getInstance();
    for (int i = 1; i < 10000; ++i) {
      assertEquals(i, idGenerator.generate());
    }
    assertEquals(10000, idGenerator.generate());
  }
}
