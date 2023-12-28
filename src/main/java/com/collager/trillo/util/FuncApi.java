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
    return remoteCallAsResult("FuncApi", "createTask",
      taskName, taskType, functionName, params);
  }
  
  
  public static Result createTask(String taskName, String taskType, String appName,
      String functionName, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTask",
      taskName, taskType, appName, functionName, params);
  }
  
  
  public static Result createTask2(String taskName, String taskType, String appName,
      String functionName, String methodName, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTask2",
      taskName, taskType, appName, functionName, methodName, params);
  }
  
  
  public static Result createTask2(String taskName, String taskType, String functionName,
      String methodName, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTask2",
      taskName, taskType, functionName, methodName, params);
  }
  
  
  public static Result createTask2(String taskName, String taskType, String functionName,
      String methodName, boolean inbuilt, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTask2",
      taskName, taskType, functionName, methodName, inbuilt, params);
  }

  
  
  public static Result createTask2(String taskName, String taskType, String appName,
      String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTask2",
      taskName, taskType, appName, functionName, methodName, inbuilt, params);
  }

 
  public static Result createTaskBySourceUid(String taskName, String taskType, String sourceUid,
      String functionName, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTaskBySourceUid",
      taskName, taskType, sourceUid, functionName, params);
  }

 
  public static Result createTaskBySourceUid(String taskName, String taskType, String sourceUid,
      String appName, String functionName, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTaskBySourceUid",
      taskName, taskType, sourceUid, appName, functionName, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String appName, String functionName, String methodName, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTaskBySourceUid2",
      taskName, taskType, sourceUid, appName, functionName, methodName, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String functionName, String methodName, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTaskBySourceUid2",
      taskName, taskType, sourceUid, functionName, methodName, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTaskBySourceUid2",
      taskName, taskType, sourceUid, functionName, methodName, inbuilt, params);
  }
  
  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
      String appName, String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    return remoteCallAsResult("FuncApi", "createTaskBySourceUid2",
      taskName, taskType, sourceUid, appName, functionName, methodName, inbuilt, params);
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


