package com.collager.trillo.util;

public class JobApi extends BaseApi {
  
  public static boolean isJobPresent(String jobId) {
    Object res = remoteCall("JobApi", "isJobPresent", jobId);
    if (res instanceof Boolean) {
      return (Boolean) res;
    }
    return false;
  }
  
  public static Object updateStatus(String jobId, String status) {
    return remoteCall("JobApi", "updateStatus", jobId, status);
  }
  
  public static boolean isJobRunning(String jobId) {
    Object res = remoteCall("JobApi", "isJobRunning", jobId);
    if (res instanceof Boolean) {
      return (Boolean) res;
    }
    return false;
  }
  
  public static Object putState(String jobId, String attrName, Object attrValue) {
    return remoteCall("JobApi", "putState", jobId, attrName, attrValue);
  }
  
  public static Object getState(String jobId, String attrName) {
    return remoteCall("JobApi", "getState", jobId, attrName);
  }
  
  public static Object removeState(String jobId, String attrName) {
    return remoteCall("JobApi", "removeState", jobId, attrName);
  }
}
