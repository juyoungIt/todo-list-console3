package com.github.callmewaggs.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class TodoMenuParameterTest {

  @DisplayName("0을 포함한 문자열이 들어오면 quit 메서드를 실행하여 입력을 파싱한다.")
  @Test
  public void quit_test() {
    // Arrange
    String input = "0";

    // Act
    TodoMenuParameter actual = TodoMenuParameter.parse(input);

    // Assert
    assertEquals(TodoMenu.QUIT, actual.getMenu());
    assertNull(actual.getId());
    assertNull(actual.getContent());
    assertNull(actual.getParentIds());
  }

  @DisplayName("1을 포함한 문자열이 들어오면 showList 메서드를 실행하여 입력을 파싱한다.")
  @Test
  public void showList_test() {
    // Arrange
    String input = "1";

    // Act
    TodoMenuParameter actual = TodoMenuParameter.parse(input);

    // Assert
    assertEquals(TodoMenu.SHOW_LIST, actual.getMenu());
    assertNull(actual.getId());
    assertNull(actual.getContent());
    assertNull(actual.getParentIds());
  }

  @DisplayName("2를 포함한 문자열이 들어오면 create 메서드를 실행하여 입력을 파싱한다.")
  @Test
  public void create_test() {
    // Arrange
    String input = "2 할일";

    // Act
    TodoMenuParameter actual = TodoMenuParameter.parse(input);

    // Assert
    assertEquals(TodoMenu.CREATE, actual.getMenu());
    assertNull(actual.getId());
    assertNotNull(actual.getContent());
    assertNotNull(actual.getParentIds());
  }

  @DisplayName("2와 내용, @로 시작하는 의존성을 포함한 문자열이 들어오면 create 메서드를 실행하여 파싱한다.")
  @Test
  public void create_test_with_parents() {
    // Arrange
    String input = "2 할일 @1 @2";

    // Act
    TodoMenuParameter actual = TodoMenuParameter.parse(input);

    // Assert
    assertEquals(TodoMenu.CREATE, actual.getMenu());
    assertNull(actual.getId());
    assertNotNull(actual.getContent());
    assertNotNull(actual.getParentIds());
  }

  @DisplayName("3과 id, 내용, @로 시작하는 의존성을 포함한 문자열이 들어오면 update 메서드를 실행하여 파싱한다.")
  @Test
  public void update_test_with_parents() {
    // Arrange
    String input = "3 1 할일 @1 @2";

    // Act
    TodoMenuParameter actual = TodoMenuParameter.parse(input);

    // Assert
    assertEquals(TodoMenu.UPDATE, actual.getMenu());
    assertNotNull(actual.getId());
    assertNotNull(actual.getContent());
    assertNotNull(actual.getParentIds());
  }

  @DisplayName("4와 id를 포함한 문자열이 들어오면 remove 메서드를 실행하여 파싱한다.")
  @Test
  public void remove_test() {
    // Arrange
    String input = "4 1";

    // Act
    TodoMenuParameter actual = TodoMenuParameter.parse(input);

    // Assert
    assertEquals(TodoMenu.REMOVE, actual.getMenu());
    assertNotNull(actual.getId());
    assertNull(actual.getContent());
    assertNull(actual.getParentIds());
  }

  @DisplayName("5와 id를 포함한 문자열이 들어오면 finish 메서드를 실행하여 파싱한다.")
  @Test
  public void finish_test() {
    // Arrange
    String input = "5 1";

    // Act
    TodoMenuParameter actual = TodoMenuParameter.parse(input);

    // Assert
    assertEquals(TodoMenu.FINISH, actual.getMenu());
    assertNotNull(actual.getId());
    assertNull(actual.getContent());
    assertNull(actual.getParentIds());
  }

  @DisplayName("0, 1, 2, 3, 4, 5로 시작하는 문자열이 아닌 그 외의 입력이 들어오면 예외를 던진다.")
  @Test
  public void exception_thrown_when_wrong_input() {
    // Arrange
    String wrongInput = "-1";

    // Act
    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> TodoMenuParameter.parse(wrongInput));

    // Assert
    assertEquals(wrongInput + " is wrong menu. try again.", actual.getMessage());
  }
}
