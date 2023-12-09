package com.collager.trillo.op;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.collager.trillo.util.LogApi;

abstract public class Op implements Runnable {
  
  //private int count = 0;
  
  public static final int DEFAULT_QUEUE_SIZE = 100;
  
  private BlockingQueue<Command> queue = new ArrayBlockingQueue<Command>(100);
  private Thread myThread = null;
  String opName;
  OpManager opManager;

  public Op() {
  }
  
  public void setup(String opName, OpManager opManager, int queueSize) {
    this.opName = opName;
    this.opManager = opManager;
  }
  
  @Override
  public void run() {
   
    while (true) {
      try {
        Command c = queue.take();
        doCommand(c);
        if ("stop".equals(c.type)) {
          break;
        }
      } catch (Exception exc) {
        LogApi.auditLogError(opName + " operation run loop exception: " + exc.getMessage());
        LogApi.error(opName + " operation run loop exception: " + exc.getMessage(), exc);
      }
    }
    
  }
  
  public void startRunning() {
    myThread = new Thread(this);
    myThread.start();
    opManager.register(this);
  }
  
  public void stopRunning() {
    opManager.unregister(this);
  }
  
  public Thread getThread() {
    return myThread;
  }
  
  public void queue(Command c ) {

    try {
      //count++;
      //LogApi.auditLogInfo(opName + " queue size before adding entry: " + queue.size());
      queue.put(c);
      //LogApi.auditLogInfo(opName + " queue size after adding entry: " + queue.size());
      //LogApi.logInfo(opName + ", " + count + " " + c.type + " (" + c.type + ")");
    } catch (InterruptedException e) {
      LogApi.error("SaveDSOp - failed to put an command in the queue, command: " + c.type);
    }
  }

  abstract public void start();
  abstract public void doCommand(Command command);
  abstract public void completeOp();
  
}
