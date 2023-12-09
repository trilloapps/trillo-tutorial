package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.RuntimeContext;
import com.collager.trillo.pojo.ScriptParameter;

public class ServerlessFunction implements TrilloFunction, Loggable {
  
  protected RuntimeContext rtContext;
  
  protected List<Result> failedResults = null;
  protected int failedCount = 0;
  protected int successCount = 0;
  
  protected Result criticalFailureResult = null;
  
  public long getIdOfUser() {
    return rtContext.getIdOfUser();
  }
  public String getUserId() {
    return rtContext.getUserId();
  }
  public String getFirstName() {
    return rtContext.getFirstName();
  }
  public String getLastName() {
    return rtContext.getLastName();
  }
  public String getFullName() {
    return rtContext.getFullName();
  }
  public String getEmail() {
    return rtContext.getEmail();
  }
  public String getOrgName() {
    return rtContext.getOrgName();
  }
  public String getAppName() {
    return rtContext.getAppName();
  }
  public String getExternalId() {
    return rtContext.getExternalId();
  }
  public String getRole() {
    return rtContext.getRole();
  }
  public String getUserOrgName() {
    return rtContext.getUserOrgName();
  }
  public boolean isEmailVerified() {
    return rtContext.isEmailVerified();
  }
  public String getTenantId() {
    return rtContext.getTenantId();
  }
  public String getTenantName() {
    return rtContext.getTenantName();
  }
  public long getUserOrgId() {
    return rtContext.getUserOrgId();
  }
  public String getPictureUrl() {
    return rtContext.getPictureUrl();
  }
  public Object getV() {
    return rtContext.getV();
  }
  public String getTaskName() {
    return rtContext.getTaskName();
  }
  public long getExecutionId() {
    return rtContext.getExecutionId();
  }
  
  public RuntimeContext getRuntimeContextState() {
    return rtContext;
  }
  public void setRuntimeContextState(RuntimeContext rtContext) {
    this.rtContext = rtContext;
  }
  

  @SuppressWarnings("unchecked")
  public MapX getParameters() {
    Object v = getV();
    if (v instanceof Map<?, ?>) {
      return new MapX((Map<String, Object>) v, "parameters");
    } else {
      return new MapX("parameters");
    }
  }
  
  public void recordResult(String name, Result r) {
    r.setName(name);
    if (r.isFailed()) {
      LogApi.auditLogError("'" + name + "' failed", r.getMessage());
      failedCount++;
      if (failedResults == null) {
        failedResults = new ArrayList<Result>();
      }
      failedResults.add(r);
    } else {
      LogApi.auditLogInfo("'" + name + "' success", r.getMessage());
      successCount++;
    }
  }
  
  public Result getCriticalFailureResult() {
    return criticalFailureResult;
  }
  public void setCriticalFailureResult(Result criticalFailureResult) {
    this.criticalFailureResult = criticalFailureResult;
  }
  public void setCriticalFailure(String error) {
    criticalFailureResult = Result.getFailedResult(error);
  }
  public Result finalResult() {
    if (criticalFailureResult != null) {
      return criticalFailureResult;
    }
    return Result.getSuccessResult("APIs - succeeded: " + successCount + ", failed: " + failedCount);
  }
  
  public boolean isCriticalFailed() {
    return criticalFailureResult != null;
  }
  
  public boolean canContinue() {
    return criticalFailureResult == null;
  }
  
  @Override
  public Object handle(ScriptParameter params) {
   return  Result.getFailedResult("In order to implement it, override in the subclass of your function");
  }
  
  public static void setResponseContentType(String type) {
  }
}
