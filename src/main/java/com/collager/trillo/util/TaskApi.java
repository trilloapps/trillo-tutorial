package com.collager.trillo.util;

import java.util.Map;
import com.collager.trillo.pojo.Result;

public class TaskApi extends BaseApi {

  public static boolean isMyTaskCancelled() {

    return false; // return trivially false for the local testing
  }

  public static long getTaskId() {
    return -1;
  }

  public static void cancelRemoteRequest(String requestId) {
    return;
  }

  public static Result enqueueSvcRequest(String queueId, String svcUrl, String svcToken,
      String serviceName, String callbackFunctionName, String methodName, Map<String, Object> serviceRequestBody,
      Map<String, Object> context) {
    return remoteCallAsResult("TaskApi", "enqueueSvcRequest", queueId, svcUrl, svcToken,
        serviceName, callbackFunctionName, methodName, serviceRequestBody, context);
  }

  public static Object getTaskStatus(long id) {
    // todo - server side implementation
    return remoteCall("TaskApi", "getTaskStatus", id);
  }

  public static boolean pingTask() {
    // todo - server side implementation
    return (boolean) remoteCall("TaskApi", "pingTask");
  }

  public static void cancelTask(String taskId) {
    // todo - server side implementation
    remoteCall("TaskApi", "cancelTask", taskId);
  }
}