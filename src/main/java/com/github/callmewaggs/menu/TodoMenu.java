package com.github.callmewaggs.menu;

import java.util.Arrays;
import java.util.Optional;

public enum TodoMenu {
  QUIT("0"),

  SHOW_LIST("1"),

  CREATE("2"),

  UPDATE("3"),

  REMOVE("4"),
  
  HISTORY("5"),
  
  FINISH("6"),
  
  SAVE("7"),
  
  LOAD("8");

  
  private String menuNumber; // the menu number from the users

  // constructor
  TodoMenu(String menuNumber) {
    this.menuNumber = menuNumber;
  }

  // search the menu and return the option name
  public static TodoMenu fromMenuNumber(String menuNumber) {
    Optional<TodoMenu> todoMenu =
        Arrays.stream(TodoMenu.values()).filter(e -> e.menuNumber.equals(menuNumber)).findAny();
    if (!todoMenu.isPresent()) {
      throw new IllegalArgumentException(menuNumber + " is wrong menu. try again.");
    }
    return todoMenu.get();
  }
}
