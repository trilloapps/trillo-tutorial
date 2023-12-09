package com.collager.trillo.util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CacheApi extends BaseApi {
  
  private static String cacheEndpoint = "/api/v1.1/cache";
  public static void put(String cacheName, String key, Object value) {
    Map body = new HashMap();
    body.put("key", key);
    body.put("value", value);
    HttpRequestUtil.post(cacheEndpoint + "/" + cacheName + "/put", new JSONObject(body).toString());
  }
  
  public static Object get(String cacheName, String key) {
    return HttpRequestUtil.get(cacheEndpoint + "/" + cacheName + "/get?key=" + key);
  }
  
  public static Object remove(String cacheName, String key) {
    Map body = new HashMap();
    body.put("key", key);
    return HttpRequestUtil.post(cacheEndpoint + "/" + cacheName + "/remove", new JSONObject(body).toString());
  }
  
  public static void clear(String cacheName) {
    HttpRequestUtil.post(cacheEndpoint + cacheName + "/clear", null);
  }
  
  public static void putNoLock(String cacheName, String key, Object value) {
    Map body = new HashMap();
    body.put("key", key);
    body.put("value", value);
    HttpRequestUtil.post(cacheEndpoint + "/" + cacheName + "/putNoLock", new JSONObject(body).toString());
  }
  
  public static Object getNoLock(String cacheName, String key) {
    return HttpRequestUtil.get(cacheEndpoint + "/" + cacheName + "/getNoLock?key=" + key);
  }
  
  public static Object removeNoLock(String cacheName, String key) {
    Map body = new HashMap();
    body.put("key", key);
    return HttpRequestUtil.post(cacheEndpoint + "/" + cacheName + "/removeNoLock", new JSONObject(body).toString());
  }
  
  public static long publishMessage(String topic, String message){
    Map body = new HashMap();
    body.put("topic", topic);
    body.put("message", message);
    Object obj =  HttpRequestUtil.post(cacheEndpoint  + "/publishMessage", new JSONObject(body).toString());
    if (obj instanceof Long) {
      return ((Long)obj).longValue();
    }
    return -1;
  }
}


