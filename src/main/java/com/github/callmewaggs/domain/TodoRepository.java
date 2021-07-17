package com.github.callmewaggs.domain;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TodoRepository {

  private static TodoRepository todoRepository = new TodoRepository();
  private IdGenerator idGenerator = IdGenerator.getInstance();
  private List<Todo> todoList;

  // for singleton pattern
  private TodoRepository() {
    this.todoList = new ArrayList<>();
  }
  
  // for singleton pattern
  public static TodoRepository getInstance() {
	 return todoRepository;
  }

  public void add(Todo todo) {
    todoList.add(todo);
  }

  public Todo find(long id) {
    return todoList.stream()
        .filter(todo -> todo.getId() == id)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("잘못된 id가 입력되었습니다. id를 다시 확인해 주세요."));
  }

  public List<Todo> findAll() {
    return todoList;
  }
  
  public Todo findSpec(Long id) {
	  Todo todo = find(id);
	  return todo;
  }

  public List<Todo> findAll(List<Long> ids) {
    List<Todo> todos = new ArrayList<>();
    for (long id : ids) {
      Todo todo = find(id);
      todos.add(todo);
    }
    return todos;
  }

  public List<Todo> findAllChildren(long id) {
    List<Todo> children = new ArrayList<>();
    for (Todo todo : todoList) {
      if (todo.getParents().stream().anyMatch(e -> e.getId() == id)) {
        children.add(todo);
      }
    }
    return children;
  }

  public void remove(Todo todo) {
    todoList.remove(todo);
  }

  public int size() {
    return todoList.size();
  }
  
  // store data to json
  public String save() { 
	  try (Writer writer = new FileWriter("backup.json")) {
		  Gson gson = new GsonBuilder().setPrettyPrinting().create();
		  gson.toJson(TodoRepository.getInstance().findAll(), writer); // get the all store information
		  return "Success!";
	  }
	  catch(Exception e) { 
		  return "Failed...";
	  }
  }
  
  // load data from json
  public String load() {
	  try {
		  String path = "backup.json"; // file path for reading
		  String jsonData = readFileAsString(path); // load the json file content
		  
		  JsonParser parser = new JsonParser(); // 파싱을 위한 객체를 생성
		  JsonArray jsonArray = (JsonArray)parser.parse(jsonData);
		  // System.out.println(jsonArray);
		  
		  // access each todo
		  for(int i=0 ; i<jsonArray.size() ; i++) {
			  JsonObject object = (JsonObject)jsonArray.get(i);
			  // System.out.println("parents : " + object.get("parents"));
			  
			  long id = object.get("id").getAsLong();
			  String content = object.get("content").getAsString();
			  
			  // parent todo - for working dependency
			  List<Long> parentIds = new ArrayList<>();
			  for(int x=0 ; x<object.get("parents").getAsJsonArray().size() ; x++) {
				  parentIds.add(object.get("parents").getAsJsonArray().get(x).getAsJsonObject().get("id").getAsLong());
			  }
			  
			  List<Todo> parents = todoRepository.findAll(parentIds);
			  
			  // time data
			  LocalDateTime createAt = null;
			  LocalDateTime updateAt = null;
			  LocalDateTime finishAt = null;
			  
			  try {
				  createAt = LocalDateTime.of(
				  			object.get("createAt").getAsJsonObject().get("date").getAsJsonObject().get("year").getAsInt(),
				  			object.get("createAt").getAsJsonObject().get("date").getAsJsonObject().get("month").getAsInt(),
				  			object.get("createAt").getAsJsonObject().get("date").getAsJsonObject().get("day").getAsInt(),
				  			object.get("createAt").getAsJsonObject().get("time").getAsJsonObject().get("hour").getAsInt(),
				  			object.get("createAt").getAsJsonObject().get("time").getAsJsonObject().get("minute").getAsInt(),
				  			object.get("createAt").getAsJsonObject().get("time").getAsJsonObject().get("second").getAsInt(),
				  			object.get("createAt").getAsJsonObject().get("time").getAsJsonObject().get("nano").getAsInt()
				  			);
			  }
			  catch(Exception e) { }
			  
			  try {
				  updateAt = LocalDateTime.of(
				  			object.get("updateAt").getAsJsonObject().get("date").getAsJsonObject().get("year").getAsInt(),
				  			object.get("updateAt").getAsJsonObject().get("date").getAsJsonObject().get("month").getAsInt(),
				  			object.get("updateAt").getAsJsonObject().get("date").getAsJsonObject().get("day").getAsInt(),
				  			object.get("updateAt").getAsJsonObject().get("time").getAsJsonObject().get("hour").getAsInt(),
				  			object.get("updateAt").getAsJsonObject().get("time").getAsJsonObject().get("minute").getAsInt(),
				  			object.get("updateAt").getAsJsonObject().get("time").getAsJsonObject().get("second").getAsInt(),
				  			object.get("updateAt").getAsJsonObject().get("time").getAsJsonObject().get("nano").getAsInt()
				  			);
			  }
			  catch(Exception e) { }
			 
			  try {
				  finishAt = LocalDateTime.of(
				  			object.get("finishAt").getAsJsonObject().get("date").getAsJsonObject().get("year").getAsInt(),
				  			object.get("finishAt").getAsJsonObject().get("date").getAsJsonObject().get("month").getAsInt(),
				  			object.get("finishAt").getAsJsonObject().get("date").getAsJsonObject().get("day").getAsInt(),
				  			object.get("finishAt").getAsJsonObject().get("time").getAsJsonObject().get("hour").getAsInt(),
				  			object.get("finishAt").getAsJsonObject().get("time").getAsJsonObject().get("minute").getAsInt(),
				  			object.get("finishAt").getAsJsonObject().get("time").getAsJsonObject().get("second").getAsInt(),
				  			object.get("finishAt").getAsJsonObject().get("time").getAsJsonObject().get("nano").getAsInt()
				  			);
			  }
			  catch(Exception e) { }
			  
			  // create todo element
			  Todo todo = new Todo(id, content, parents, createAt, updateAt, finishAt);
			  TodoRepository.getInstance().add(todo);
		  }
		  idGenerator.changeStartPoint(jsonArray.size()+1);
		  return "Success!";
	  }
	  catch(Exception e) {
		  System.out.println(e);
		  return "Failed...";
	  }
  }
  
  public static String readFileAsString(String file)throws Exception
  {
      return new String(Files.readAllBytes(Paths.get(file)));
  }
}
