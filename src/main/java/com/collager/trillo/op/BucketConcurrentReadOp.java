package com.collager.trillo.op;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang3.StringUtils;
import com.collager.trillo.model.FilesPage;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.FileUtil;
import com.collager.trillo.util.LogAction;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.MapX;
import com.collager.trillo.util.StorageApi;
import com.collager.trillo.util.Util;

public class BucketConcurrentReadOp extends Op {
  
  public static final int DEFAULT_CONCURRENT_TASKS = 50;
  
  @SuppressWarnings("unused")
  private String name = "";
  private String bucketName = null;
  private String bucketFolderName = null;
  
  private int numberOfConcurrentReadTasks = DEFAULT_CONCURRENT_TASKS;
  ThreadPoolExecutor executor = null;
  
  private boolean cancelled = false;
  private boolean allFilesAdded = false;
  
  private String format = "";
  
  private BlockingQueue<FileContent> filesQueue = new ArrayBlockingQueue<FileContent>(100);
  
  
  public BucketConcurrentReadOp(String opName, MapX params) {
    List<String> missingKeys = params.getMissingKeys("bucketFolderName", 
        "serviceAccount");
    if (missingKeys.size() > 0) {
      throw new RuntimeException("BucketConcurrentReadOp(), missing parameters: " + missingKeys.toString());
    }
    
    int queueSize = params.getInt("queueSize", DEFAULT_QUEUE_SIZE);
    int numberOfConcurrentReadTasks = params.getInt("numberOfConcurrentReadTasks", DEFAULT_CONCURRENT_TASKS);
    
    setup(opName, opManager, params.getString("bucketName"), 
        params.getString("bucketFolderName"), params.getString("format"),
        queueSize, numberOfConcurrentReadTasks);
    
  }
  
  /* public BucketConcurrentReadOp(String opName, OpManager opManager, String bucketFolderName, 
      String format, int queueSize, int numberOfConcurrentReadTasks) {
    setup(opName, opManager, null, bucketFolderName, 
        queueSize, numberOfConcurrentReadTasks);
  } */
  
  public BucketConcurrentReadOp(String opName, OpManager opManager, String bucketName, String bucketFolderName, 
      String format, int queueSize, int numberOfConcurrentReadTasks) {
    setup(opName, opManager, bucketName, bucketFolderName, format,
        queueSize, numberOfConcurrentReadTasks);
  }
  
  
  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
  
  public void setup(String opName, OpManager opManager, String bucketName, String bucketFolderName, 
      String format,
      int queueSize, int numberOfConcurrentReadTasks) {
    super.setup(opName, opManager, queueSize);
    this.bucketName = bucketName;
    this.bucketFolderName = bucketFolderName;
    this.numberOfConcurrentReadTasks = numberOfConcurrentReadTasks;
    this.format = format;
  }
  
  public void start() {
    LogApi.auditInfo2(LogAction.BucketOp, "Starting: " + opName);
    Command c = new StartCommand();
    queue(c);
    this.startRunning();
  }
  
  public void doCommand(Command command) {
    switch (command.type) {
      case "start" : 
        doStart((StartCommand)command);
        break;
      case "stop" : 
        doStop((StopCommand)command);
        break;
      default: LogApi.auditError2(LogAction.BucketOp, "Invalid command", "commandType", command.type);
    }
  }

  private void doStart(StartCommand command) {
    
    int pageSize = 100;
    String pageToken = null;
    FilesPage page;
    
    
    executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfConcurrentReadTasks);
   
    while (true) {
      page = StorageApi.getFilesPage(bucketName, bucketFolderName, false, pageToken, pageSize);   
      addReadTasks(page);
      pageToken = page.getNextPageToken();
      if (pageToken == null) {
        if (executor.getActiveCount() == 0) {
          FileContent fileContent = new FileContent();
          filesQueue.add(fileContent);
        }
        allFilesAdded = true;
        break;
      }
    }
  }

  private void addReadTasks(FilesPage page) {
    List<Map<String, Object>> files = page.getFiles();
    
    FileReadTask fileReadTask;
    String ext;
    boolean useFormat = StringUtils.isNotBlank(format);
    for (Map<String, Object> file : files) {
      fileReadTask = new FileReadTask(file);
      if (useFormat) {
        ext = FileUtil.getFileExtension("" + file.get("fileName"));
        if (!format.equals(ext)) {
          continue;
        }
      }
      executor.execute(fileReadTask);
    }
  }
  
  public synchronized FileContent nextFile() {
    if (filesQueue.size() == 0 && allFilesAdded && executor.getActiveCount() == 0) {
      return null;
    }
    
    try {
      return filesQueue.take();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  public void doStop(StopCommand command) {
    stopRunning();
  }
  
  public void close() {
    Command c = new CloseCommand();
    queue(c);
  }
  
  public void stop(boolean deleteOnStop) {
    Command c = new StopCommand(deleteOnStop);
    queue(c);
  }
  
  public static class StartCommand extends Command {
    public StartCommand() {
      super("start");
    }
  }
  
  public static class StopCommand extends Command {
    boolean deleteOnStop;
    public StopCommand(boolean deleteOnStop) {
      super("stop");
      this.deleteOnStop = deleteOnStop;
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

  public String getBucket() {
    return this.bucketName;
  }

  public class FileContent {
    private String fileName = null;
    private Object content = null;
    private boolean failed = false;
    private String errorMessage = null;
    public String getFileName() {
      return fileName;
    }
    public void setFileName(String fileName) {
      this.fileName = fileName;
    }
    public Object getContent() {
      return content;
    }
    public void setContent(Object content) {
      this.content = content;
    }
    public boolean isFailed() {
      return failed;
    }
    public void setFailed(boolean failed) {
      this.failed = failed;
    }
    public String getErrorMessage() {
      return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
    }
  }
  
  public Object convert(Object data) {
    if (data == null) {
      return data;
    }
    
    if ("json".equals(format) && data instanceof byte[]) {
      String jsonString = new String((byte[])data, StandardCharsets.UTF_8);
      jsonString = jsonString.trim();
      if (jsonString.startsWith("[")) {
        return Util.fromJSONStringAsListOfMap(jsonString);
      } else {
        return Util.fromJSONStringAsMap(jsonString);
      }
    }
    
    return data;
  }
  
  class FileReadTask implements Runnable {
    private Map<String, Object> file;

    public FileReadTask(Map<String, Object> file) {
      this.file = file;
    }

    @Override
    public void run() {
      FileContent fileContent = new FileContent();
      String sourceFilePath = (String) file.get("fullPath");
      fileContent.setFileName(sourceFilePath);
      try {
        Result result = StorageApi.readFromBucket(bucketName, sourceFilePath);
        if (result.isFailed()) {
          LogApi.auditError2(LogAction.BucketOp, "Failed to read file", "sourceFilePath", sourceFilePath);
        }
        Object content;
        content = convert(result.getData());
        fileContent.setContent(content);
      } catch (Exception exc) {
        fileContent.setFailed(true);
        fileContent.setErrorMessage(exc.getMessage());
      }
      filesQueue.add(fileContent);
    }
  }
}
