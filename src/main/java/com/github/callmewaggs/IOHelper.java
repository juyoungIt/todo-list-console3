package com.github.callmewaggs;

import com.github.callmewaggs.domain.Todo;
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
    System.out.println("  5: finish TODO");
    System.out.println("  0: exit\n");
  }

  public void printExample() {
    System.out.println("  EXAMPLE");
    System.out.println("  1<Enter>");
    System.out.println("  2 {content} [dependencies]<Enter>");
    System.out.println("  3 {id} {content} [dependencies]<Enter>");
    System.out.println("  4 {id}<Enter>");
    System.out.println("  5 {id}<Enter>");
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

  private String getContentWithDependencies(Todo todo) {
    String dependencies =
        todo.getParents().stream().map(e -> "@" + e.getId()).collect(Collectors.joining(" "));
    return String.join(" ", todo.getContent(), dependencies).trim();
  }

  private String getTimes(Todo todo) {
    return String.join(
        " | ",
        todo.getCreateAt() == null ? null : todo.getCreateAt().format(formatter),
        todo.getUpdateAt() == null ? null : todo.getCreateAt().format(formatter),
        todo.getFinishAt() == null ? null : todo.getCreateAt().format(formatter));
  }

  public void printMessage(String message) {
    System.out.println(message);
  }
}
