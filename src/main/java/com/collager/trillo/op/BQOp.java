package com.collager.trillo.op;

import com.collager.trillo.util.LogAction;
import com.collager.trillo.util.LogApi;

public class BQOp extends Op {

  public BQOp(String opName, OpManager opManager, int queueSize) {
    setup(opName, opManager, queueSize);
  }
  
  public void start() {
    LogApi.auditInfo2(LogAction.BqOp, "Starting: " + opName);
    this.startRunning();
  }

  @Override
  public void doCommand(Command command) {
  }
  
  @Override
  public void completeOp() {
  }
  
}
