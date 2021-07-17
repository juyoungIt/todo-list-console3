package com.github.callmewaggs;

import com.github.callmewaggs.domain.Todo;
import com.github.callmewaggs.menu.TodoMenu;
import com.github.callmewaggs.domain.Log;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class IOHelper {

  private Scanner scanner = new Scanner(System.in);
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public void printHelloMessage() {
    System.out.println("TODO LIST CONSOLE APPLICATION - Todo editor");
  }

  public void printMenuWithExample() {
    printMenu();
    printExample();
  }

  public void printMenu() {
    System.out.println("  MENU");
    System.out.println("  1: show TODO list");
    System.out.println("  2: create TODO");
    System.out.println("  3: update TODO");
    System.out.println("  4: remove TODO");
    System.out.println("  5: show history log");
    System.out.println("  6: finish TODO");
    System.out.println("  7: save TODO to json file");
    System.out.println("  8: load TODO from json file");
    System.out.println("  0: exit\n");
  }

  public void printExample() {
    System.out.println("  EXAMPLE");
    System.out.println("  1 <Enter>");
    System.out.println("  2 {content} [dependencies]<Enter>");
    System.out.println("  3 {id} {content} [dependencies]<Enter>");
    System.out.println("  4 {id}<Enter>");
    System.out.println("  5 <Enter>");
    System.out.println("  6 {id}<Enter>");
    System.out.println("  7 <Enter>");
    System.out.println("  8 <Enter>");
    System.out.println("  dependencies must be written with @ (ex. @1)\n");
  }

  public String inputCommand() {
    System.out.print("$ ");
    return scanner.nextLine();
  }

  public void printTodoList(List<Todo> todoList) {
    System.out.println("| id | content | 작성일시 | 최종수정일시 | 완료처리 |");
    for (Todo todo : todoList) {
      String information =
          String.join(
              " | ",
              String.valueOf(todo.getId()),
              getContentWithDependencies(todo),
              getTimes(todo));
      System.out.println("| " + information + " |");
    }
  }
  
  // for history function
  public void printHistory(List<Log> logList) {
	  System.out.println("| log id | instruction type | todo id | todo contents | log time |");
	  for (Log log : logList) { 
		  String information =
	          String.join(
	              " | ",
	              log.getLog_id().toString(),
	              getInstructionType(log),
	              log.getTodo_id().toString(),
	              log.getContents(),
	              log.getLogTime().format(formatter));
	      System.out.println("| " + information + " |");
	    }
  }

  private String getContentWithDependencies(Todo todo) {
    String dependencies = todo.getParents().stream().map(e -> "@" + e.getId()).collect(Collectors.joining(" "));
    return String.join(" ", todo.getContent(), dependencies).trim();
  }
  
  public String getInstructionType(Log log) {
	  TodoMenu todoMenu = log.getInstType();
	  
	  if(todoMenu == TodoMenu.SHOW_LIST)
		  return "SHOW_LIST";
	  else if(todoMenu == TodoMenu.CREATE)
		  return "CREATE";
	  else if(todoMenu == TodoMenu.UPDATE)
		  return "UPDATE";
	  else if(todoMenu == TodoMenu.REMOVE)
		  return "REMOVE";
	  else if(todoMenu == TodoMenu.HISTORY)
		  return "SHOW_HISTORY";
	  else if(todoMenu == TodoMenu.SAVE)
		  return "SAVE";
	  else if(todoMenu == TodoMenu.LOAD)
		  return "LOAD";
	  else if(todoMenu == TodoMenu.FINISH)
		  return "FINISH";
	  else
		  return null;
  }

  private String getTimes(Todo todo) {
    return String.join(
        " | ",
        todo.getCreateAt() == null ? null : todo.getCreateAt().format(formatter),
        todo.getUpdateAt() == null ? null : todo.getUpdateAt().format(formatter),
        todo.getFinishAt() == null ? null : todo.getFinishAt().format(formatter));
  }

  public void printMessage(String message) {
    System.out.println(message);
  }
}
