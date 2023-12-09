package com.collager.trillo.op;

import java.util.List;
import java.util.Map;
import com.collager.trillo.util.LogAction;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.MapX;

public class DbOp extends Op {
  
  private static final int DEFAULT_QUEUE_SIZE = 1000;
  
  public DbOp(String opName, OpManager opManager, MapX params) {
    int queueSize = params.getInt("queueSize", DEFAULT_QUEUE_SIZE);
    setup(opName, opManager, queueSize);
  }
  
  public void start() {
    LogApi.auditInfo2(LogAction.DbOp, "Starting: " + opName);
    this.startRunning();
  }
  
  public void doCommand(Command command) {
    switch (command.type) {
      case "save" : 
        doSave((SaveCommand)command);
        break;
      case "saveMany" : 
        doSaveMany((SaveManyCommand)command);
        break;
      case "saveMapList" : 
        doSaveMapList((SaveMapListCommand)command);
        break;
      case "saveManyIgnoreError" : 
        doSaveManyIgnoreError((SaveManyIgnoreErrorCommand)command);
        break;
      case "stop" : 
        doStop((StopCommand)command);
        break;
      case "close" : 
        doClose();
        break;
      default: LogApi.error("Invalid command: " + command.type);
    }
    
  }

  private void doSave(SaveCommand command) {
    
  }
  
  private void doSaveMany(SaveManyCommand command) {
    
  }

  private void doSaveMapList(SaveMapListCommand command) {
    
  }
  
  private void doSaveManyIgnoreError(SaveManyIgnoreErrorCommand command) {
    
  }

  private void doClose() {
    
  }
  
  public void doStop(StopCommand command) {
    doClose();
  }
  
  
  public void save(String className, Object entity) {
    Command c = new SaveCommand(className, entity);
    queue(c);
  }
  
  public void saveMany(String className, Iterable<Object> entities) {
    Command c = new SaveManyCommand(className, entities);
    queue(c);
  }
  
  public void saveMapList(String className, List<Map<String, Object>> list) {
    Command c = new SaveMapListCommand(className, list);
    queue(c);
  }
  
  public void saveManyIgnoreError(String className, Iterable<Object> entities) {
    Command c = new SaveManyIgnoreErrorCommand(className, entities);
    queue(c);
  }
  
  public void close() {
    Command c = new CloseCommand();
    queue(c);
  }
  
  public void stop(boolean deleteOnStop) {
    Command c = new StopCommand(deleteOnStop);
    queue(c);
  }
  
  public static class SaveCommand extends Command {
    String className;
    Object entity;
    public SaveCommand(String className, Object entity) {
      super("save");
      this.className = className;
      this.entity = entity;
    }
  }
  
  public static class SaveManyCommand extends Command {
    String className;
    Iterable<Object> entities;
    public SaveManyCommand(String className, Iterable<Object> entities) {
      super("saveMany");
      this.className = className;
      this.entities = entities;
    }
  }
  
  public static class SaveMapListCommand extends Command {
    String className;
    List<Map<String, Object>> list;
    public SaveMapListCommand(String className, List<Map<String, Object>> list) {
      super("saveMapList");
      this.className = className;
      this.list = list;
    }
  }
  
  public static class SaveManyIgnoreErrorCommand extends Command {
    String className;
    Iterable<Object> entities;
    public SaveManyIgnoreErrorCommand(String className, Iterable<Object> entities) {
      super("saveManyIgnoreError");
      this.className = className;
      this.entities = entities;
    }
  }
  
  public static class StopCommand extends Command {
    public StopCommand(boolean deleteOnStop) {
      super("stop");
    }
  }
  
  public static class CloseCommand extends Command {
    public CloseCommand() {
      super("close");
    }
  }
  
  
  public void completeOp() {
    close();
    stop(false); 
  }
}
