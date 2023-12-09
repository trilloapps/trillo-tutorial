package com.collager.trillo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.StringUtils;
import com.collager.trillo.pojo.Result;

public class FileWriterThread implements Loggable, Runnable {
  
  
  private String format; // STRING, JSON, NDJSON, XML, CSV etc
  private String fileName;
  private String bucketFileName;
  @SuppressWarnings("unused")
  private String name = "";
  
  //if isTemp true then a file with the given name is created inside /tmp
  // directory. In this case, it assumes that if the fileName includes path then it is relative.
  private boolean isTemp;
  
  // if false then it will throw an exception if the file already exists.
  private boolean overwrite;
  
  private File file;
  
  private FileOutputStream fw = null;
  private OutputStreamWriter os = null;
  private BufferedWriter bw = null;
  
  private BlockingQueue<Command> queue = new ArrayBlockingQueue<Command>(100);
  
  private Thread myThread = null;
  
  @SuppressWarnings("unused")
  private int count = 0;
  
  private boolean cancelled = false;
  
  public FileWriterThread(String format, String fileName, String bucketFileName, boolean isTemp, boolean overwrite) {
    this.format = format;
    this.fileName = fileName;
    this.bucketFileName = bucketFileName;
    this.isTemp = isTemp;
    this.overwrite = overwrite;
  }
  
  public FileWriterThread(String fileName, String bucketFileName, boolean isTemp, boolean overwrite) {
    this("ndjson", fileName, bucketFileName, isTemp, overwrite);
  }
  
  public String getFileName() {
    return fileName;
  }

  public String getBucketFileName() {
    return bucketFileName;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  public void run() {
    try {
      while (true) {
        Command c = queue.take();
        doCommand(c);
        if ("stop".equals(c.type)) {
          break;
        }
      }
    } catch (InterruptedException ex) {
    }
    
  }
  
  public void start() {
    LogApi.auditLogInfo("Starting: " + fileName);
    if (isTemp) {
      file = new File("/tmp", fileName);
    } else {
      file = new File(fileName);
    }
    
    name = file.getName();
    
    if (!overwrite && file.exists()) {
      throw new RuntimeException("File exists, overwrite is not permitted");
    }
    
    File dir = file.getParentFile();
    
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        throw new RuntimeException("Filed to make directory: " + dir.getPath());
      }
    }
    
    try {
      fw = new FileOutputStream(file);
      os = new OutputStreamWriter(fw, "UTF-8");
      bw = new BufferedWriter(os);
    } catch (Exception exc) {
      if (bw != null) try {bw.close();} catch(Exception exc2) {};
      if (os != null) try {os.close();} catch(Exception exc2) {};
      if (fw != null) try {fw.close();} catch(Exception exc2) {};
      throw new RuntimeException(
          "Failed to write file: " + file.getName() + ", Error: " + exc.getMessage());
    }
    
    myThread = new Thread(this);
    myThread.start();
  }
  
  private void doCommand(Command command) {
    switch (command.type) {
      
      case "write" : 
        doWrite((WriteCommand)command);
        break;
      case "copyToBucket" : 
        doCopyToBucket((CopyToBucketCommand)command);
        break;
      case "stop" : 
        doStop((StopCommand)command);
        break;
      case "close" : 
        doClose();
        break;
      default: LogApi.auditLogError("Invalid command: " + command.type);
    }
    
  }

  private void doWrite(WriteCommand command) {
    if ("ndjson".equals(format)) {
      doWriteNdjson(command);
    } else if ("string".equals(format)) {
      doWriteString(command);
    }
  }
  
  private void doClose() {
    if (bw != null) try {
      try {bw.flush();} catch(Exception exc) {};
      bw.close(); 
      bw = null;
    } catch(Exception exc) {};
    if (os != null) try {os.close(); os = null;} catch(Exception exc) {};
    if (fw != null) try {fw.close(); fw = null;} catch(Exception exc) {};
  }
  
  private void doCopyToBucket(CopyToBucketCommand command) {
    LogApi.auditLogInfo("Copying to bucket: " + fileName);
    Result result;
    if (StringUtils.isNotBlank(command.bucketName)) {
      result = StorageApi.copyLargeFileToBucket(command.bucketName, command.serviceAccount, file.getPath(), bucketFileName);
    } else {
      result = StorageApi.copyLargeFileToBucket(file.getPath(), bucketFileName);
    }
    
    if (result.isFailed()) {
      LogApi.auditLogError("Failed copy file to bucket, source file: " + 
          fileName + ", target file: " + bucketFileName + 
          ", bucket: " + (command.bucketName != null ? command.bucketName : "Trillo bucket") + ", error: " + result.getMessage());
    } else {
      LogApi.auditLogInfo("Successfully copied file to bucket, source file: " + 
          fileName + ", target file: " + bucketFileName + 
          ", bucket: " + (command.bucketName != null ? command.bucketName : "Trillo bucket"));
    }
  }

  @SuppressWarnings("unchecked")
  private void doWriteNdjson(WriteCommand command) {
    List<Object> l;
    if (command.object instanceof List<?>) {
      l = (List<Object>) command.object;
    } else {
      l = new ArrayList<Object>();
      l.add(command.object);
    }
    //log().info("Writing to file: " + file.getName() + ", number of entries: " + l.size());
    for (Object obj : l) {
      try {
        bw.write(Util.asJSONString(obj) + "\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  private void doWriteString(WriteCommand command) {
    String str = command.object instanceof String ? (String) command.object : "" + command.object;
    //log().info("Writing to file: " + file.getName() + ", string of length: " + str.length());
    try {
      bw.write(str + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void doStop(StopCommand command) {
    doClose();
    if (command.deleteOnStop && file != null) {
      file.delete();
    }
  }
  
  public void close() {
    Command c = new CloseCommand();
    queue(c);
  }
  
  public void write(Object object) {
    Command c = new WriteCommand(object);
    queue(c);
  }
  
  public void copyToBucket() {
    copyToBucket(null, null);
  }
  
  public void copyToBucket(String bucketName, String serviceAccount) {
    Command c = new CopyToBucketCommand(bucketName, serviceAccount);
    queue(c);
  }
  
  public void logFileSize() {
    if (file != null) {
      LogApi.auditInfo2(LogAction.BucketOp,
          "BucketOp file size", "fileName", fileName, "fileSize", file.length());
    }
  }
  
  public void stop(boolean deleteOnStop) {
    Command c = new StopCommand(deleteOnStop);
    queue(c);
  }
  
  abstract public static class Command {
    String type;
    public Command(String type) {
      this.type = type;
    }
  }
  
  public static class WriteCommand extends Command {
    Object object;
    public WriteCommand(Object object) {
      super("write");
      this.object = object;
    }
  }
  
  public static class CopyToBucketCommand extends Command {
    String bucketName;
    String serviceAccount;
    public CopyToBucketCommand(String bucketName, String serviceAccount) {
      super("copyToBucket");
      this.bucketName = bucketName;
      this.serviceAccount = serviceAccount;
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
  
  private void queue(Command c ) {
    try {
      count++;
      //log().info(count + " " + c.type + " (" + name + ")");
      queue.put(c);
      //log().info("added: " + count);
    } catch (InterruptedException e) {
      LogApi.auditLogError("Failed to put an command in the queue, fileName: " + fileName, e.getMessage());
    }
  }
}
