package com.collager.trillo.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowNodeM {
  
  public static String START_TYPE = "start";
  public static String END_TYPE = "end";
  public static String MULTI_ACTIVITY_TYPE = "multiActivity";
  public static String PARALLEL_TYPE = "parallelActivity";
  public static String REPEAT_ACTIVITY_TYPE = "repeatActivity";
  public static String BRANCH_TYPE = "branch";
  public static String DEFAULT_BRANCH_TYPE = "defaultBranch";
  public static String PARALLEL_BRANCH_TYPE = "parallelBranch";
  public static String BRANCH_ENTRY_TYPE = "branchEntry";
  public static String DEFAULT_ENTRY_TYPE = "defaultBranchEntry";
  public static String REPEAT_BRANCH_ENTRY_TYPE = "repeatBranchEntry";
  public static String REPEAT_START_EXP_TYPE = "repeatStartExp";
  public static String REPEAT_END_EXP_TYPE = "repeatEndExp";
  public static String ACTIVITY_TYPE = "activity";
  
  public static String FUNCTION_ACTIVITY_TYPE = "function";
  public static String SCRIPT_ACTIVITY_TYPE = "script";
  public static String FLOW_ACTIVITY_TYPE = "flow";
  
  private String name;
  private String type;
  private String activityType;
 
  private List<FlowNodeM> flowNodes;
  private FlowNodeM parent = null;
  
  private String functionName;
  private String failureFunctionName;
  private int timeout = -1;
  private String inputParameterId;
  private String inputParameterName;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getActivityType() {
    return activityType;
  }
  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }
  public int getTimeout() {
    return timeout;
  }
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }
  public List<FlowNodeM> getFlowNodes() {
    return flowNodes;
  }
  public void setFlowNodes(List<FlowNodeM> flowNodes) {
    this.flowNodes = flowNodes;
  }
  public void updateParent(FlowNodeM parent) {
    this.parent = parent;
    if (flowNodes != null) {
      for (FlowNodeM child : flowNodes) {
        child.updateParent(this);
      }
    }
  }
  public FlowNodeM retreiveParent() {
    return parent;
  }
  public FlowNodeM getStartNode() {
    if (flowNodes != null && flowNodes.size() > 0 && "start".equals(flowNodes.get(0).getType())) {
      return flowNodes.get(0);
    }
    return null;
  }
  public String getFunctionName() {
    return functionName;
  }
  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }
  public String getFailureFunctionName() {
    return failureFunctionName;
  }
  public void setFailureFunctionName(String failureFunctionName) {
    this.failureFunctionName = failureFunctionName;
  }
  public String getInputParameterId() {
    return inputParameterId;
  }

  public void setInputParameterId(String inputParameterId) {
    this.inputParameterId = inputParameterId;
  }

  public String getInputParameterName() {
    return inputParameterName;
  }

  public void setInputParameterName(String inputParameterName) {
    this.inputParameterName = inputParameterName;
  }
}
