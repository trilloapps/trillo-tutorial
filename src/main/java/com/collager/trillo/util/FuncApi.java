package com.collager.trillo.util;

import com.collager.trillo.pojo.Result;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.collager.trillo.util.Util.convertToResult;

public class FuncApi extends BaseApi {

  private static String funcEndpoint = "/api/v1.1/func";

  public static Result executeFunction(String functionName, Map<String, Object> params) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("params", params);

    Object res = HttpRequestUtil.post(funcEndpoint + "/executeFunction/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result executeFunction(String appName, String functionName, Map<String, Object> params) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("appName", appName);
    body.put("params", params);

    Object res = HttpRequestUtil.post(funcEndpoint + "/executeFunction/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result executeFunction(String appName, String functionName, String methodName,
                                       Map<String, Object> params, boolean preAuthCall) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("appName", appName);
    body.put("params", params);
    body.put("methodName", methodName);
    body.put("preAuthCall", preAuthCall);

    Object res = HttpRequestUtil.post(funcEndpoint + "/executeFunction/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result executeFunctionWithMethod(String appName, String functionName, String methodName, Map<String, Object> params) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("appName", appName);
    body.put("params", params);
    body.put("methodName", methodName);
    Object res = HttpRequestUtil.post(funcEndpoint + "/executeFunctionWithMethod/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result executeFunctionWithMethod(String functionName, String methodName, Map<String, Object> params) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("params", params);
    body.put("methodName", methodName);
    Object res = HttpRequestUtil.post(funcEndpoint + "/executeFunctionWithMethod/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result executeSSH(String hostName, String command) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("hostName", hostName);
    body.put("command", command);
    Object res = HttpRequestUtil.post(funcEndpoint + "/executeSSH", new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result executeSSH(String hostName, String command, boolean async) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("hostName", hostName);
    body.put("command", command);
    body.put("async", async);
    Object res = HttpRequestUtil.post(funcEndpoint + "/executeSSH", new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result executeSSH(String command, boolean async) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("async", async);
    body.put("command", command);
    Object res = HttpRequestUtil.post(funcEndpoint + "/executeSSH", new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Object pingTask() {
    return HttpRequestUtil.get(funcEndpoint + "/pingTask");
  }


  public static Result createTask(String taskName, String taskType, String functionName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("params", params);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTask/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createTask(String taskName, String taskType, String appName, String functionName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("appName", appName);
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("params", params);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTask/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createTask2(String taskName, String taskType, String appName,
                                   String functionName, String methodName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("appName", appName);
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("params", params);
    body.put("methodName", methodName);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTask2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createTask2(String taskName, String taskType, String functionName,
                                   String methodName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("params", params);
    body.put("methodName", methodName);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTask2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createTask2(String taskName, String taskType, String functionName,
                                   String methodName, boolean inbuilt, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("params", params);
    body.put("methodName", methodName);
    body.put("inbuilt", inbuilt);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTask2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createTask2(String taskName, String taskType, String appName,
                                   String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("appName", appName);
    body.put("params", params);
    body.put("methodName", methodName);
    body.put("inbuilt", inbuilt);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTask2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createTaskBySourceUid(String taskName, String taskType, String sourceUid,
                                             String functionName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("sourceUid", sourceUid);
    body.put("params", params);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTaskBySourceUid/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createTaskBySourceUid(String taskName, String taskType, String sourceUid,
                                             String appName, String functionName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("appName", appName);
    body.put("sourceUid", sourceUid);
    body.put("params", params);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTaskBySourceUid/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
                                              String appName, String functionName, String methodName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("appName", appName);
    body.put("sourceUid", sourceUid);
    body.put("params", params);
    body.put("methodName", methodName);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTaskBySourceUid2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
                                              String functionName, String methodName, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("sourceUid", sourceUid);
    body.put("params", params);
    body.put("methodName", methodName);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTaskBySourceUid2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
                                              String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("sourceUid", sourceUid);
    body.put("params", params);
    body.put("methodName", methodName);
    body.put("inbuilt", inbuilt);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTaskBySourceUid2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result createTaskBySourceUid2(String taskName, String taskType, String sourceUid,
                                              String appName, String functionName, String methodName, boolean inbuilt, Map<String, Object> params) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("appName", appName);
    body.put("sourceUid", sourceUid);
    body.put("params", params);
    body.put("methodName", methodName);
    body.put("inbuilt", inbuilt);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createTaskBySourceUid2/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }


  public static Result createFunctionSysTask(String taskName, String taskType, String appName, String functionName,
                                             Map<String, Object> functionParams) {
    Map<String, Object> body = new HashMap<>();
    body.put("taskName", taskName);
    body.put("taskType", taskType);
    body.put("appName", appName);
    body.put("functionParams", functionParams);
    Object res = HttpRequestUtil.post(funcEndpoint + "/createFunctionSysTask/" + functionName, new JSONObject(body).toString());
    return convertToResult(res);
  }

}


