package com.collager.trillo.util;

import static com.collager.trillo.util.Util.convertToResult;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;
import com.collager.trillo.pojo.Result;

public class FuncApi extends BaseApi {

  private static String funcEndpoint = "/api/v1.1/func";

  public static Result executeFunction(String functionName, Map<String, Object> params) {
    return remoteCallAsResult("CoreFuncApi", "executeFunction",
      functionName, params);
  }

  public static Result executeFunction(String appName, String functionName, Map<String, Object> params) {
    return remoteCallAsResult("CoreFuncApi", "executeFunction",
      appName, functionName, params);
  }
  
  public static Result executeFunctionWithMethod(String functionName, String methodName, Map<String, Object> params) {
    return remoteCallAsResult("CoreFuncApi", "executeFunctionWithMethod",
      functionName, methodName, params);
  }

  public static Result executeFunctionWithMethod(String appName, String functionName, String methodName, Map<String, Object> params) {
    return remoteCallAsResult("CoreFuncApi", "executeFunctionWithMethod",
      appName, functionName, methodName, params);
  }

  public static Result executeSSH(String hostName, String command) {
    return remoteCallAsResult("CoreFuncApi", "executeSSH",
      hostName, command);
  }

  public static Result executeSSH(String hostName, String command, boolean async) {
    return remoteCallAsResult("CoreFuncApi", "executeSSH",
      hostName, command, async);
  }

  public static Result executeSSH(String command, boolean async) {
    return remoteCallAsResult("CoreFuncApi", "executeSSH",
      command, async);
  }

  
  public static Object pingTask() {
    return remoteCall("FuncApi", "pingTask");
  }
  
  
  public static Result createTask(String taskName, String taskType, String functionName,
      Map<String, Object> params) {
    return createTask2(taskName, taskType,functionName, null, params);
  }
  
  
  public static Result createTask(String taskName, String taskType, String appName,
      String functionName, Map<String, Object> params) {
    return createTask2(taskName, taskType, appName, functionName, null, params);
  }
  
  
  public static Result createTask2(String taskName, String taskType, String appName,
      String functionName, String methodName, Map<String, Object> params) {
    return createTask2(taskName, taskType, appName, functionName, methodName, false, params);
  }
  
  
  public static Result createTask2(String taskName, String taskType, String functionName,
      String methodName, Map<String, Object> params) {
    return createTask2(taskName, taskType, functionName, methodName, false, params);
  }
  
  
  public static Result createTask2(String taskName, String taskType, String functionName,
      String methodName, boolean inbuilt, Map<String, Object> params) {
    String appName = BaseApi.appForFunction(functionName);
    functionName = BaseApi.function(functionName);
    return createTask2(taskName, taskType, appName, functionName, methodName, inbuilt, params);
  }

  
  
  public static Result createTask2(String taskName, String taskType, String appName,
      String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    Map<String, Object> reqBody = new LinkedHashMap<String, Object>();
    reqBody.put("name", taskName);
    reqBody.put("type", taskType);
    reqBody.put("functionName", functionName);
    reqBody.put("params", params);
    if (methodName != null) {
      reqBody.put("methodName", methodName);
    }
    reqBody.put("inbuilt", inbuilt);
    Object obj= HttpRequestUtil.post(funcEndpoint + "/" + functionName + "/createTask2", new JSONObject(reqBody).toString());
    return convertToResult(obj);
  }

 
  public static Result createTaskBySourceUid(String taskName, String taskType, String sourceUid,
      String functionName, Map<String, Object> params) {
    return createTaskBySourceUid2(taskName, taskType, sourceUid, functionName, null, params);
  }

 
  public static Result createTaskBySourceUid(String taskName, String taskType, String sourceUid,
      String appName, String functionName, Map<String, Object> params) {
    return createTaskBySourceUid2(taskName, taskType, sourceUid, appName, functionName, null, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String appName, String functionName, String methodName, Map<String, Object> params) {
    return createTaskBySourceUid2(taskName, taskType, sourceUid, appName, functionName, methodName, false, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String functionName, String methodName, Map<String, Object> params) {
    return createTaskBySourceUid2(taskName, taskType, sourceUid, 
        functionName, methodName, false, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    String appName = BaseApi.appForFunction(functionName);
    functionName = BaseApi.function(functionName);
    return createTaskBySourceUid(taskName, taskType, sourceUid, appName, functionName, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String appName, String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    Map<String, Object> reqBody = new LinkedHashMap<String, Object>();
    reqBody.put("name", taskName);
    reqBody.put("type", taskType);
    reqBody.put("sourceUid", sourceUid);
    reqBody.put("functionName", functionName);
    reqBody.put("params", params);
    if (methodName != null) {
      reqBody.put("methodName", methodName);
    }
    reqBody.put("inbuilt", inbuilt);
    Object obj= HttpRequestUtil.post(funcEndpoint + "/" + functionName + "/createTaskBySourceUid2", new JSONObject(reqBody).toString());
    return convertToResult(obj);
  }
  
  public static Result executeFunction(String appName, String functionName, String methodName,
                                       Map<String, Object> params, boolean preAuthCall) {
    return remoteCallAsResult("FuncApi", "executeFunction",
      appName, functionName, methodName, params, preAuthCall);
  }

  public static Result createFunctionSysTask(String taskName, String taskType, String appName, String functionName,
                                             Map<String, Object> functionParams) {
    return remoteCallAsResult("FuncApi", "createFunctionSysTask",
      taskName, taskType, appName, functionName, functionParams);
  }

}


