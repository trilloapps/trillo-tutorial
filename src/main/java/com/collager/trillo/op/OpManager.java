package com.collager.trillo.op;

import java.util.ArrayList;
import java.util.List;
import com.collager.trillo.util.CSVConst;
import com.collager.trillo.util.LogApi;

public class OpManager {
  protected List<Op> ops = new ArrayList<Op>();
  
  public synchronized void register(Op op) {
    ops.add(op);
  }
  
  public synchronized void unregister(Op op) {
    ops.remove(op);
  }
  
  public synchronized void minWaitForAllOpsToEnd(int minutes) {
    waitForAllOpsToEnd(minutes * 60 * 1000);
  }
  
  public synchronized void waitForAllOpsToEnd(int millis) {
    Thread t;
    for (Op op : ops) {
      t = op.getThread();
      if (t != null) {
        try {
          t.join(millis);
        } catch (InterruptedException e) {
          LogApi.error("Failed to join a thread: " + e.getMessage(), e);
        }
      }
    }
  }
  
  public BucketOp newBucketOp(String bucketName, String bucketFolderName, String serviceAccount, 
      String format, String simpleFileName,
      boolean isTemp, boolean overwrite, int queueSize) {   
    String opName = simpleFileName;
    return new BucketOp(opName, this, bucketName, bucketFolderName, serviceAccount, format, simpleFileName,
        isTemp, overwrite, queueSize);
    
  }
  
  public BucketOp newCSVBucketOp(String bucketName, String bucketFolderName, String serviceAccount, 
      String simpleFileName,
      boolean isTemp, boolean overwrite, int queueSize, List<String> columnNames) {
    
    return newCSVBucketOp(bucketName, bucketFolderName, serviceAccount, simpleFileName,
        isTemp, overwrite, CSVConst.DEFAULT_SEPARATOR_CHAR, columnNames, CSVConst.DEFAULT_COLUMN_NAME_LINE, queueSize);
    
  }
  
  public BucketOp newCSVBucketOp(String bucketName, String bucketFolderName, String serviceAccount, 
      String simpleFileName, boolean isTemp, boolean overwrite, char separatorChar, 
      List<String> columnNames, int columnNameLine, int queueSize) {
    String opName = simpleFileName;
    return new BucketOp(opName, this, bucketName, bucketFolderName, serviceAccount, "csv", simpleFileName,
        isTemp, overwrite, separatorChar, columnNames, columnNameLine, queueSize);
    
  }
  
}
