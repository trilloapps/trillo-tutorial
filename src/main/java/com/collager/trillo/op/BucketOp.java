package com.collager.trillo.op;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.collager.trillo.model.CSVWriter;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.CSVConst;
import com.collager.trillo.util.LogAction;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.MapX;
import com.collager.trillo.util.StorageApi;
import com.collager.trillo.util.Util;

public class BucketOp extends Op {
  
  
  private String format; // STRING, JSON, NDJSON, XML, CSV etc
  private String fileName;
  private String bucketFileName;
  @SuppressWarnings("unused")
  private String name = "";
  private String serviceAccount = null;
  private String bucketName = null;
  @SuppressWarnings("unused")
  private String bucketFolderName = null;
  
  //if isTemp true then a file with the given name is created inside /tmp
  // directory. In this case, it assumes that if the fileName includes path then it is relative.
  private boolean isTemp;
  
  // if false then it will throw an exception if the file already exists.
  private boolean overwrite;
  
  private File file;
  
  // Used for ndjson, string formats
  private FileOutputStream fw = null;
  private OutputStreamWriter os = null;
  private BufferedWriter bw = null;
  
  // used for CSV files
  private CSVWriter csvWriter = null;
  private char separatorChar;
  private List<String> columnNames;
  private int columnNameLine;
  
  private boolean cancelled = false;
  
  private static final int DEFAULT_QUEUE_SIZE = 1000;
  
  @SuppressWarnings("unchecked")
  public BucketOp(String opName, MapX params, Object ...keys) {
    List<String> missingKeys = params.getMissingKeys("bucketName", "bucketFolderName", 
        "serviceAccount", "simpleFileName",
        "fileFormat");
    if (missingKeys.size() > 0) {
      throw new RuntimeException("createBucketOp(), missing parameters: " + missingKeys.toString());
    }
    
    int queueSize = params.getInt("queueSize", DEFAULT_QUEUE_SIZE);
    
    if (keys.length > 0) {
        opManager = (OpManager) keys[0];
    }

    setup(opName, opManager, params.getString("bucketName"), 
        params.getString("bucketFolderName"), params.getString("serviceAccount"),
        params.getString("fileFormat"), params.getString("simpleFileName"), 
        params.getBoolean("isTemp"), params.getBoolean("overwrite"), queueSize);
    
    if ("csv".equals(params.getString("fileFormat"))) {
      setupCSVParams(params.getChar("separatorChar", CSVConst.DEFAULT_SEPARATOR_CHAR), 
          (List<String>)params.get("columnNames"), params.getInt("columnNameLine", CSVConst.DEFAULT_COLUMN_NAME_LINE));
    }
  }
  
  public BucketOp(String opName, OpManager opManager, String bucketName, String bucketFolderName, 
      String serviceAccount, String format, String simpleFileName,
      boolean isTemp, boolean overwrite, int queueSize) {
    setup(opName, opManager, bucketName, bucketFolderName, serviceAccount, 
        format, simpleFileName, isTemp, overwrite, queueSize);
  }
  
  public BucketOp(String opName, OpManager opManager, String bucketName, String bucketFolderName, 
      String serviceAccount, String format, String simpleFileName,
      boolean isTemp, boolean overwrite, char separatorChar, 
      List<String> columnNames, int columnNameLine, int queueSize) {
    setup(opName, opManager, bucketName, bucketFolderName, serviceAccount, 
        format, simpleFileName, isTemp, overwrite, queueSize);
    setupCSVParams(separatorChar, columnNames, columnNameLine);
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
  
  public void setup(String opName, OpManager opManager, String bucketName, String bucketFolderName, 
      String serviceAccount, String format, String simpleFileName,
      boolean isTemp, boolean overwrite, int queueSize) {
    super.setup(opName, opManager, queueSize);
    this.bucketName = bucketName;
    this.bucketFolderName = bucketFolderName;
    this.serviceAccount = serviceAccount;
    this.format = format;
    this.isTemp = isTemp;
    this.overwrite = overwrite;
    String folderString = StringUtils.isNotBlank(bucketFolderName) ? "/" + bucketFolderName : "";
    fileName = bucketName + folderString + "/" + simpleFileName;
    bucketFileName = folderString + "/" + simpleFileName;
  }
  
  public void setupCSVParams(char separatorChar, List<String> columnNames, int columnNameLine) {
    this.separatorChar = separatorChar;
    this.columnNames = columnNames;
    this.columnNameLine = columnNameLine;
  }
  
  public void start() {
    LogApi.auditInfo2(LogAction.BucketOp, "Starting: " + opName);
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
    
    if ("csv".equals(format)) {
      if (columnNames == null || columnNames.size() == 0) {
        throw new RuntimeException("Invalid value for 'columnName' parameter");
      }
      csvWriter = new CSVWriter(file.getPath(), separatorChar, columnNames, columnNameLine);
    } else {
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
    }
    
    this.startRunning();
  }
  
  public void doCommand(Command command) {
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
      default: LogApi.auditError2(LogAction.BucketOp, "Invalid command", "commandType", command.type);
    }
    
  }

  private void doWrite(WriteCommand command) {
    if (format.contains("ndjson")) {
      doWriteNdjson(command);
    } else if ("csv".equals(format)) {
      doWriteCSV(command);
    } else if ("string".equals(format)) {
      doWriteString(command);
    } else {
      LogApi.logError("In write no matching format data will go missing ");
    }
  }
  
  private void doClose() {
    if (csvWriter != null) {
      csvWriter.close();
    } else {
      if (bw != null) try {
        try {bw.flush();} catch(Exception exc) {};
        bw.close(); 
        bw = null;
      } catch(Exception exc) {};
      if (os != null) try {os.close(); os = null;} catch(Exception exc) {};
      if (fw != null) try {fw.close(); fw = null;} catch(Exception exc) {};
    }
  }
  
  private void doCopyToBucket(CopyToBucketCommand command) {
    LogApi.auditInfo2(LogAction.BucketOp, "Copying to bucket", "fileName", fileName);
    Result result;
    if (csvWriter != null) {
      // csv files are created on the remote machine using CSVWriter therefore use copyLargeFileToBucket2
      if (StringUtils.isNotBlank(bucketName)) {
        result = StorageApi.copyLargeFileToBucket2(bucketName, serviceAccount, file.getPath(), bucketFileName);
      } else {
        result = StorageApi.copyLargeFileToBucket2(file.getPath(), bucketFileName);
      }
    } else {
      if (StringUtils.isNotBlank(bucketName)) {
        result = StorageApi.copyLargeFileToBucket(bucketName, serviceAccount, file.getPath(), bucketFileName);
      } else {
        result = StorageApi.copyLargeFileToBucket(file.getPath(), bucketFileName);
      }
    }
    
    if (result.isFailed()) {
      LogApi.auditError(LogAction.BucketOp, "Failed copy file to bucket", result.getMessage(), null, "sourceFile",  
          fileName, "targetFile", bucketFileName, 
          "bucket", (StringUtils.isNotBlank(bucketName) ? bucketName : "Trillo bucket"));
    } else {
      LogApi.auditInfo2(LogAction.BucketOp, "Successfully copied file to bucket", "sourceFile",  
          fileName, "targetFile", bucketFileName, 
          "bucket", (StringUtils.isNotBlank(bucketName) ? bucketName : "Trillo bucket"));
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
    //LogApi.logInfo("Writing to file: " + file.getName() + ", number of entries: " + l.size());
    for (Object obj : l) {
      try {
        bw.write(Util.asJSONString(obj) + "\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private void doWriteCSV(WriteCommand command) {
    List<Map<String, Object>> l;
    if (command.object instanceof List<?>) {
      l = (List<Map<String, Object>>) command.object;
    } else {
      l = new ArrayList<Map<String, Object>>();
      l.add((Map<String, Object>)command.object);
    }
    csvWriter.addRows(l);
  }
  
  private void doWriteString(WriteCommand command) {
    String str = command.object instanceof String ? (String) command.object : "" + command.object;
    //LogApi.logInfo("Writing to file: " + file.getName() + ", string of length: " + str.length());
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
    stopRunning();
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
    Command c = new CopyToBucketCommand();
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
  
  public static class WriteCommand extends Command {
    Object object;
    public WriteCommand(Object object) {
      super("write");
      this.object = object;
    }
  }
  
  public static class CopyToBucketCommand extends Command {
    public CopyToBucketCommand() {
      super("copyToBucket");
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
    copyToBucket();
    stop(false);
  }

  public String getFilePath() {
    return file.getPath();
  }

  public String getFolder() {
    return file.getParent();
  }

  public String getFilename() {
    return file.getName();
  }

  public String getBucketFilePath() {
    return this.bucketFileName;
  }

  public String getBucket() {
    return this.bucketName;
  }

  public String getServiceAccount() {
    return this.serviceAccount;
  }
}
