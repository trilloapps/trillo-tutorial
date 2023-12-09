package com.collager.trillo.util;

import java.util.Map;

public class Recipe {
  
  private String functionName;
  private String methodName;
  private Map<String, Object> parameters;
  
  public Recipe(String functionName, String methodName) {
    this.functionName = functionName;
    this.methodName = methodName;
  }
  
  public Object execute() {
    return RecipeApi.execute(functionName, methodName, parameters);
  }
  
  public Object execute(Map<String, Object> parameters) {
    return RecipeApi.execute(functionName, methodName, parameters);
  }

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }
}
