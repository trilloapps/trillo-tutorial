package com.collager.trillo.util;

import java.util.Map;

import com.collager.trillo.pojo.Result;

public class SvcApi extends BaseApi {
  
  public static Result get(String serviceName, String partialPath, String queryStr,
      Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "get", serviceName, partialPath, queryStr, pathVariables);
  }
  
  public static Result get(String appName, String serviceName, String partialPath, String queryStr,
      Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "get", appName, serviceName, partialPath, queryStr, pathVariables);
  }

  public static Result post(String serviceName, String partialPath, String queryStr, Map<String, Object> body,
         Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "post", serviceName, partialPath, queryStr, body, pathVariables);
  }
  
  public static Result post(String appName, String serviceName, String partialPath, String queryStr,
         Map<String, Object> body, Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "post", appName, serviceName, partialPath, queryStr, body, pathVariables);
  }
  
  public static Result put(String serviceName, String partialPath, String queryStr, Map<String, Object> body,
      Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "put", serviceName, partialPath, queryStr, body, pathVariables);
  }
  
  public static Result put(String appName, String serviceName, String partialPath, String queryStr,
        Map<String, Object> body, Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "put", appName, serviceName, partialPath, queryStr, body, pathVariables);
  }

  public static Result delete(String serviceName, String partialPath, String queryStr, Map<String, Object> body,
                                     Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "delete", serviceName, partialPath, queryStr, body, pathVariables);
  }

  public static Result delete(String appName, String serviceName, String partialPath, String queryStr,
                                     Map<String, Object> body, Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "delete", appName, serviceName, partialPath, queryStr, body, pathVariables);
  }

  public static Result getFile(String serviceName, String partialPath, String queryStr,
                                      Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "getFile", serviceName, partialPath, queryStr, pathVariables);
  }

  public static Result getFile(String appName, String serviceName, String partialPath, String queryStr,
                                      Map<String, String> pathVariables) {
    return remoteCallAsResult("SvcApi", "getFile", appName, serviceName, partialPath, queryStr, pathVariables);
  }
}


