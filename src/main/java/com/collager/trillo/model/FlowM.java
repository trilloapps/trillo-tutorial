/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class FlowM extends BaseM { 
  private String type = null;
  private String orgName; // orgName to which the flow metadata belongs.
  private String appName; // orgName to which the flow metadata belongs.
  private List<FlowNodeM> flowNodes;
  
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @JsonIgnore
  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  @JsonIgnore
  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }
  
  public List<FlowNodeM> getFlowNodes() {
    return flowNodes;
  }

  public void setFlowNodes(List<FlowNodeM> flowNodes) {
    this.flowNodes = flowNodes;
  }
  
  public void addFlowNode(FlowNodeM flowNode) {
    if (flowNodes == null) {
      flowNodes = new ArrayList<FlowNodeM>();
    }
    flowNodes.add(flowNode);
  }
  
  public FlowNodeM addFunction(String name, String functionName) {
    FlowNodeM flowNode = new FlowNodeM();
    flowNode.setName(name);
    flowNode.setType(FlowNodeM.ACTIVITY_TYPE);
    flowNode.setActivityType(FlowNodeM.FUNCTION_ACTIVITY_TYPE);
    flowNode.setFunctionName(functionName);
    addFlowNode(flowNode);
    return flowNode;
  }

  public FlowNodeM addFunction(String name, String functionName, String failureFunctionName) {
    FlowNodeM flowNode = addFunction(name, functionName);
    flowNode.setFailureFunctionName(failureFunctionName);
    return flowNode;
  }
  
  public static FlowM newFlow(String name) {
    FlowM fl = new FlowM();
    fl.setName(name);
    return fl;
  }
}
