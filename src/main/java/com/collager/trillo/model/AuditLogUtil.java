package com.collager.trillo.model;

import java.util.HashMap;
import java.util.Map;

import io.trillo.util.Proxy;


public class AuditLogUtil {
  
  final static int MAX_SUMMARY_SIZE = 255;
  final static int MAX_ACTION_SIZE = 48;
  
  public static Map<String, Object> makeAuditLog(String type, String sourceUid, String action, String summary, String detail, String json) {
    Map<String, Object> m = makeAuditLog(sourceUid, type, action, summary, detail);
    m.put("json", json);
    return m;
  }
  
  public static Map<String, Object> makeAuditLog(String type, String sourceUid, String action, String summary, String detail) {
    Map<String, Object> m = makeAuditLog(sourceUid, type, action, summary);
    m.put("detail", detail);
    return m;
  }
  
  public static Map<String, Object> makeAuditLog(String type, String sourceUid, String action, String summary) {
    Map<String, Object> m = createLogObject(type, action, summary);
    m.put("sourceUid", sourceUid);
    return m;
  }
  
  public static Map<String, Object> makeTaskLog(String type, String action, String summary, long taskExecId) {
    Map<String, Object> m = createLogObject(type, action, summary);
    m.put("taskExecId", taskExecId);
    return m;
  }
  
  public static Map<String, Object> createLogObject(String type, String action, String summary) {
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("summary", summary);
    m.put("type", type);
    
    if (action.length() > MAX_ACTION_SIZE) {
      action = action.substring(0, MAX_ACTION_SIZE);
    }
    m.put("action", action);
    if (summary.length() > MAX_SUMMARY_SIZE) {
      summary = summary.substring(0, MAX_SUMMARY_SIZE);
    }
    
    m.put("idOfUser", Proxy.getIdOfCurrentUser());
    m.put("userId", Proxy.getUserId());
    m.put("taskExecId", "-1");
   
    return m;
  }
}
