package com.collager.trillo.op;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.Util;

public class ParallelOpTest {
  
  ParallelOp parallelOp;
  
  private void executeTest() {
    parallelOp = new ParallelOp();
    
    Map<String, Object> params1 = new LinkedHashMap<String, Object>();
    params1.put("value", "paramter_of_w1");
    parallelOp.addWorker(new Worker1(), params1);
    
    Map<String, Object> params2 = new LinkedHashMap<String, Object>();
    params2.put("value", "paramter_of_w2");
    parallelOp.addWorker(new Worker2(), params2);
    
    
    parallelOp.execute();
    
    if (parallelOp.getFailedCount() > 0) {
      LogApi.info("Failed count: " + parallelOp.getFailedCount());
      LogApi.info("Failed results message: \n" + parallelOp.getFailedMessages());
    }
    
    List<Result> results = parallelOp.getResults();
    
    LogApi.info("Result detail:\n" + Util.asJSONPrettyString(results));
  }

  public static void main(String[] args) {
    (new ParallelOpTest()).executeTest();
  }
  
  static class Worker1 implements Worker {
    private String name = "Worker1";
    private int waitTime = 4000;
    @Override
    synchronized public Result perfrom(Map<String, Object> params) {
      LogApi.info(name + " - started");
      LogApi.info("Parameters: " + Util.asJSONPrettyString(params));
      try {
        wait(waitTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      LogApi.info(name + " - completed");
      return Result.getSuccessResult();
    }
  }
  
  static class Worker2 implements Worker {
    private String name = "Worker1";
    private int waitTime = 5000;
    @Override
    synchronized public Result perfrom(Map<String, Object> params) {
      LogApi.info(name + " - started");
      LogApi.info("Parameters: " + Util.asJSONPrettyString(params));
      try {
        wait(waitTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      LogApi.info(name + " - failed");
      return Result.getFailedResult("Failed after " + waitTime + " ms");
    }
  }
}
