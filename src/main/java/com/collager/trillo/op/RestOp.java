package com.collager.trillo.op;

import com.collager.trillo.util.LogAction;
import com.collager.trillo.util.LogApi;

public class RestOp extends Op {

  public RestOp(String opName, OpManager opManager, int queueSize) {
    setup(opName, opManager, queueSize);
  }
  
  public void start() {
    LogApi.auditInfo2(LogAction.RestOp, "Starting: " + opName);
    this.startRunning();
  }

  @Override
  public void doCommand(Command command) {    
  }

  @Override
  public void completeOp() {
  }

}
