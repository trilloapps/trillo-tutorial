package com.collager.trillo.op;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.LogAction;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.Util;


public class ParallelOp {

  private static final ExecutorService executorService = 
      Executors.newFixedThreadPool(5);

  
  private List<Callable<Result>> callables = new ArrayList<Callable<Result>>();
  private List<Future<Result>> futures;
  private List<Result> results = new ArrayList<Result>();
  private int failedCount = 0;
  private String failedMessages = "";
  
  public void addWorker(Worker worker, Map<String, Object> params) {
    callables.add(new WorkerThread(worker, Util.deepCopy(params)));
    
  }
  
  public void execute() {
    try {
      futures = executorService.invokeAll(callables);
      
      executorService.shutdown();
      
      // all tasks are done
      collectResult(futures);
    } catch (InterruptedException e) {
      LogApi.auditCritical(LogAction.ParallelOp, 
          "Failed to 'execute' a parallel op, with error: " + e.getMessage(), 
          ExceptionUtils.getStackTrace(e), null);
    }
  }
  
  private void collectResult(List<Future<Result>> futures) {
    Result r;
    for (Future<Result> future : futures) {
      try {
        r = future.get();
        results.add(r);
        if (r.isFailed()) {
          failedCount++;
          failedMessages += (failedMessages.length() > 0 ? "\n" : "") + r.getMessage();
        }
      } catch (Exception e) {
        LogApi.auditCritical(LogAction.ParallelOp, 
            "Failed to 'collectResult' of a parallel op, with error: " + e.getMessage(), 
            ExceptionUtils.getStackTrace(e), null);
      } 
    }
  }
  
  public int getFailedCount() {
    return failedCount;
  }

  public String getFailedMessages() {
    return failedMessages;
  }
  
  public List<Result> getResults() {
    return results;
  }

  class WorkerThread implements Callable<Result> {
    
    Worker worker;
    Map<String, Object> params;
    
    WorkerThread(Worker worker, Map<String, Object> params) {
      this.worker = worker;
      this.params = params;
    }

    @Override
    public Result call() throws Exception {
      return worker.perfrom(params);
    }
    
  }
}
