package com.collager.trillo.util;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.collager.trillo.pojo.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import io.trillo.util.Proxy;

public class BaseApi {
  
  private static Logger log = LoggerFactory.getLogger(BaseApi.class);
  
  public static String asJSONPrettyString(Object obj) {
    return Util.asJSONPrettyString(obj);
  }

  public static String asJSONString(Object obj) {
    return Util.asJSONString(obj);
  }

  public static Map<String, Object> fromJSONString(String json) {
    TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {};

    try {
      return Util.fromJSONString(json, type);
    } catch (Exception exc) {
      log.error(exc.getMessage());

      return null;
    }
  }

  public static List<Map<String, Object>> fromJSONStringAsArray(String json) {
    TypeReference<List<Map<String, Object>>> type = new TypeReference<List<Map<String, Object>>>() {};

    try {
      return Util.fromJSONString(json, type);
    } catch (Exception exc) {
      log.error(exc.getMessage());

      return null;
    }
  }
  
  public static Map<String, Object> readJSON(String dir, String fileName) {
    File                f = new File(dir, fileName);
    Map<String, Object> m = Util.fromJSONSFile(f, new TypeReference<LinkedHashMap<String, Object>>() {});

    return m;
  }
  
  public static void writeJSON(String dir, String fileName, Object obj) {
    File d = new File(dir);

    if (!d.exists()) {
      d.mkdirs();
    }

    File   f = new File(dir, fileName);
    String s = Util.asJSONString(obj);

    FileUtil.writeFile(f, s);
  }

  public static void writePrettyJSON(String dir, String fileName, Object obj) {
    File d = new File(dir);

    if (!d.exists()) {
      d.mkdirs();
    }

    File   f = new File(dir, fileName);
    String s = Util.asJSONPrettyString(obj);

    FileUtil.writeFile(f, s);
  }

  public static Result successResult() {
    return Result.getSuccessResult();
  }

  public static Result successResult(String msg) {
    return Result.getSuccessResult(msg);
  }

  public static Result successResult(String msg, Object data) {
    return Result.getSuccessResult(msg, data);
  }
  
  public static Result errorResult(String msg) {
    return Result.getFailedResult(msg);
  }

 
  public static Result errorResult(String msg, int code) {
    return Result.getFailedResult(msg, code);
  }
  
  public static String extractMessage(Object obj) {
    return (obj instanceof Result) ? ((Result) obj).getDetailMessage() : "Unknown";
  }

  public static boolean isResult(Object obj) {
    return obj instanceof Result;
  }

  public static boolean isResultOrNull(Object obj) {
    return (obj == null) || (obj instanceof Result);
  }
  
  public static String UUID() {
    return UUID.randomUUID().toString();
  }

  public static String uidToClassName(String uid) {
    return Util.uidToClassName(uid);
  }
  
  public static long uidToId(String uid) {
    return Util.uidToId(uid);
  }

  public static String uidToIdStr(String uid) {
    return Util.uidToIdStr(uid);
  }

  public static Result waitForMillis(long tm) {
    try {
      Thread.sleep(tm);

      return Result.getSuccessResult();
    } catch (InterruptedException e) {
      return Result.getFailedResult(e.getLocalizedMessage());
    }
  }
  
  public static String getDataServiceUrl() {
    return remoteCallAsString("BaseApi", "getDataServiceUrl");
  }
  
  public static String getAccessToken() {
    return remoteCallAsString("BaseApi", "getAccessToken");
  }
  
  public static String getStatusInformUrl() {
    return remoteCallAsString("BaseApi", "getStatusInformUrl");
  }
  
  protected static Object remoteCall(String javaClassName, String javaMethodName, Object ...args) {
    return Proxy.remoteCall(javaClassName, javaMethodName, args);
  }
  
  protected static Result remoteCallAsResult(String javaClassName, String javaMethodName, Object ...args) {
    Object res = Proxy.remoteCall(javaClassName, javaMethodName, args);
    if (res instanceof Result) {
      return (Result) res;
    }
    throw new RuntimeException("Unexpected type");
  }
  
  @SuppressWarnings("unchecked")
  protected static Map<String, Object> remoteCallAsMap(String javaClassName, String javaMethodName, Object ...args) {
    Object res = Proxy.remoteCall(javaClassName, javaMethodName, args);
    if (res instanceof Map<?, ?>) {
      return (Map<String, Object>) res;
    }
    if (res instanceof Result) {
      Result result = (Result) res;
      throw new RuntimeException(result.getMessage()); 
    }
    throw new RuntimeException("Unexpected type");
  }
  
  protected static String remoteCallAsString(String javaClassName, String javaMethodName, Object ...args) {
    Object res = Proxy.remoteCall(javaClassName, javaMethodName, args);
    if (res instanceof String) {
      return (String) res;
    }
    if (res != null) {
      return Util.asJSONPrettyString(res);
    }
    return null;
  }

  protected static Boolean remoteCallAsBoolean(String javaClassName, String javaMethodName, Object ...args) {
    Object res = Proxy.remoteCall(javaClassName, javaMethodName, args);
    if (res instanceof Boolean) {
      return (Boolean) res;
    }
    if (res instanceof Result) {
      Result result = (Result) res;
      throw new RuntimeException(result.getMessage());
    }
    throw new RuntimeException("Unexpected type");
  }
  
  @SuppressWarnings("unchecked")
  protected static List<Object> remoteCallAsList(String javaClassName, String javaMethodName, Object ...args) {
    Object res = Proxy.remoteCall(javaClassName, javaMethodName, args);
    if (res instanceof List<?>) {
      return (List<Object>) res;
    }
    if (res instanceof Result) {
      Result result = (Result) res;
      throw new RuntimeException(result.getMessage()); 
    }
    throw new RuntimeException("Unexpected type");
  }
  
  @SuppressWarnings("unchecked")
  protected static List<Map<String, Object>> remoteCallAsListOfMaps(String javaClassName, String javaMethodName, Object ...args) {
    Object res = Proxy.remoteCall(javaClassName, javaMethodName, args);
    if (res instanceof List<?>) {
      return (List<Map<String, Object>>) res;
    }
    if (res instanceof Result) {
      Result result = (Result) res;
      throw new RuntimeException(result.getMessage()); 
    }
    throw new RuntimeException("Unexpected type");
  }
 
  public static String app(String className) {
    if (StringUtils.isBlank(className)) {
      return Util.SHARED_APP_NAME;
    }
    String[] sl = className.split("\\.");
    if (sl.length > 2) {
      // <appName>.<dsName>.<className> pattern
      // <appName>.<className> pattern
      return sl[0];
    }
    return Util.SHARED_APP_NAME;
  }
  
  public static String appForFunction(String functionName) {
    if (StringUtils.isBlank(functionName)) {
      return Util.SHARED_APP_NAME;
    }
    String[] sl = functionName.split("\\.");
    if (sl.length > 2) {
      // <appName>.<functionName> pattern
      return sl[0];
    }
    return Util.SHARED_APP_NAME;
  }
  
  public static String ds(String className) {
    if (StringUtils.isBlank(className)) {
      return Util.COMMON_DS_NAME;
    }
    String[] sl = className.split("\\.");
    if (sl.length > 2) {
      // <appName>.<dsName>.<className> pattern
      return sl[1];
    } else if (sl.length > 1) {
      // <dsName>.<className> pattern
      return sl[0];
    }
    return Util.COMMON_DS_NAME;
  }
  
  public static String cls(String className) {
    if (className == null) {
      return "";
    }
    String[] sl = className.split("\\.");
    if (sl.length > 2) {
      if (sl.length == 3) {
        // <appName>.<dsName>.<className> pattern
        return sl[2];
      } else {
        // <appName>.<dsName>.<schema>.<className> pattern
        return sl[2] + "." + sl[3];
      }
      
    } else if (sl.length > 1) {
      // <dsName>.<className> pattern
      return sl[1];
    }
    return className;
  }
  
  
  public static String appFromDs(String ds) {
    if (StringUtils.isBlank(ds)) {
      return Util.SHARED_APP_NAME;
    }
    String[] sl = ds.split("\\.");
    if (sl.length == 2) {
      // <appName>.<dsName> pattern
      return sl[0];
    }
    return Util.SHARED_APP_NAME;
  }
  
  public static String dsFromDs(String dsName) {
    if (StringUtils.isBlank(dsName)) {
      return Util.COMMON_DS_NAME;
    }
    String[] sl = dsName.split("\\.");
    if (sl.length == 2) {
      // <appName>.<dsName> pattern
      return sl[1];
    }
    return dsName;
  }
  
  public static String appForFlow(String flowName) {
    if (StringUtils.isBlank(flowName)) {
      return Util.SHARED_APP_NAME;
    }
    String[] sl = flowName.split("\\.");
    if (sl.length > 2) {
      // <appName>.<flowName> pattern
      return sl[0];
    }
    return Util.SHARED_APP_NAME;
  }
  
  public static String function(String functionName) {
    if (StringUtils.isBlank(functionName)) {
      return "";
    }
    String[] sl = functionName.split("\\.");
    if (sl.length == 2) {
      return sl[1];
    }
    return functionName;
  }
  
  public static String schema(String className) {
    if (className == null) {
      return "";
    }
    String[] sl = className.split("\\.");
    if (sl.length > 1) {
      return sl[sl.length - 2];
    }
    return "";
  }
  
  public static String flow(String flowName) {
    if (StringUtils.isBlank(flowName)) {
      return "";
    }
    String[] sl = flowName.split("\\.");
    if (sl.length == 2) {
      return sl[1];
    }
    return flowName;
  }
  
  public static String clsWithoutSchema(String className) {
    if (className == null) {
      return "";
    }
    String[] sl = className.split("\\.");
    return sl[sl.length - 1];
  }
}


