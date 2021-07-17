package com.github.callmewaggs.menu;

import java.util.ArrayList;
import java.util.List;

public class TodoMenuParameter {

  private TodoMenu menu;
  private Long id;
  private String content;
  private List<Long> parentIds;

  public TodoMenuParameter(TodoMenu menu, Long id, String content, List<Long> parentIds) {
    this.menu = menu;
    this.id = id;
    this.content = content;
    this.parentIds = parentIds;
  }

  public static TodoMenuParameter parse(String input) {
    String[] parsed = input.split(" ");
    TodoMenu menu = TodoMenu.fromMenuNumber(parsed[0]);
    switch (menu) {
      case QUIT:
        return quit();
      case SHOW_LIST:
        return showList();
      case CREATE:
        return create(parsed);
      case UPDATE:
        return update(parsed);
      case REMOVE:
        return remove(parsed);
      case HISTORY:
    	return showHistory();
      case FINISH:
        return finish(parsed);
      case SAVE:
    	return save();
      case LOAD:
    	return load();
      default:
        throw new IllegalStateException("Wrong menu. try again.");
    }
  }

  // finish the todo
  private static TodoMenuParameter finish(String[] parsed) {
    long id = Long.parseLong(parsed[1]);
    return new TodoMenuParameter(TodoMenu.FINISH, id, null, null);
  }

  // remove the todo
  private static TodoMenuParameter remove(String[] parsed) {
    long id = Long.parseLong(parsed[1]);
    return new TodoMenuParameter(TodoMenu.REMOVE, id, null, null);
  }

  // update the todo
  private static TodoMenuParameter update(String[] parsed) {
    long id = Long.parseLong(parsed[1]);
    String content = parsed[2];
    List<Long> parentIds = getParentIds(parsed, 3);
    return new TodoMenuParameter(TodoMenu.UPDATE, id, content, parentIds);
  }

  // create the todo
  private static TodoMenuParameter create(String[] parsed) {
    String content = parsed[1];
    List<Long> parentIds = getParentIds(parsed, 2);
    return new TodoMenuParameter(TodoMenu.CREATE, null, content, parentIds);
  }
  
  // show the all history
  private static TodoMenuParameter showHistory() {
	  return new TodoMenuParameter(TodoMenu.HISTORY, null, null, null);
  }
  
  // save the todo list to json file
  private static TodoMenuParameter save() {
	  return new TodoMenuParameter(TodoMenu.SAVE, null, null, null);
  }
  
  // load the todo list from json file
  private static TodoMenuParameter load() {
	  return new TodoMenuParameter(TodoMenu.LOAD, null, null, null);
  }

  // show the all todo
  private static TodoMenuParameter showList() {
    return new TodoMenuParameter(TodoMenu.SHOW_LIST, null, null, null);
  }

  //  terminate the program
  private static TodoMenuParameter quit() {
    return new TodoMenuParameter(TodoMenu.QUIT, null, null, null);
  }

  // get the dependent works (parent works)
  private static List<Long> getParentIds(String[] parsed, int threshold) {
    List<Long> parents = new ArrayList<>();
    if (parsed.length > threshold) {
      for (int i = threshold; i < parsed.length; ++i) {
        parents.add(Long.parseLong(parsed[i].substring(1)));
      }
    }
    return parents;
  }

  public TodoMenu getMenu() {
    return menu;
  }

  public Long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public List<Long> getParentIds() {
    return parentIds;
  }
}
