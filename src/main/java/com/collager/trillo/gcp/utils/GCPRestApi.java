/*
 * Copyright (c)  2020 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.gcp.utils;

import com.collager.trillo.util.BaseApi;

import java.util.Map;

public class GCPRestApi extends BaseApi {
  
  public static Object get(String requestUrl) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "get", requestUrl);
  }
  
  public static Object post(String requestUrl, Object body) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "post", requestUrl, body);
  }
  
  public static Object post(String requestUrl, Object body, Map<String, String> headers) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "post", requestUrl, body, headers);
  }

  public static Object put(String requestUrl, Object body) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "put", requestUrl, body);
  }

  public static Object patch(String requestUrl, Object body) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "patch", requestUrl, body);
  }

  public static Object delete(String requestUrl, Object body) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "delete", requestUrl, body);
  }
  
  public static Object get(String requestUrl, String serviceAccountKeyPropName, String authenticationPath) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "get", requestUrl, serviceAccountKeyPropName, authenticationPath);
  }
  
 
  public static Object post(String requestUrl, Object body, String serviceAccountKeyPropName, String authenticationPath) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "post", requestUrl, body, serviceAccountKeyPropName, authenticationPath);
  }
  
  public static Object post(String requestUrl, Object body, Map<String, String> headers, String serviceAccountKeyPropName, String authenticationPath) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "post", requestUrl, body, headers, serviceAccountKeyPropName, authenticationPath);
  }

  public static Object put(String requestUrl, Object body, String serviceAccountKeyPropName, String authenticationPath) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "put", requestUrl, body, serviceAccountKeyPropName, authenticationPath);
  }

  public static Object patch(String requestUrl, Object body, String serviceAccountKeyPropName, String authenticationPath) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "patch", requestUrl, body, serviceAccountKeyPropName, authenticationPath);
  }
  
  public static Object delete(String requestUrl, Object body, String serviceAccountKeyPropName, String authenticationPath) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "delete", requestUrl, body, serviceAccountKeyPropName, authenticationPath);
  }
  
  public static Object get(String requestUrl, String refreshToken, String clientId, String clientSecret) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "get", requestUrl, refreshToken, clientId, clientSecret);
  }
  
  public static Object post(String requestUrl, Object body, String refreshToken, String clientId, String clientSecret) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "post", requestUrl, body, refreshToken, clientId, clientSecret);
  }
  
  public static Object post(String requestUrl, Object body, Map<String, String> headers, String refreshToken, String clientId, String clientSecret) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "post", requestUrl, body, headers, refreshToken, clientId, clientSecret);
  }
  
  public static Object put(String requestUrl, Object body, String refreshToken, String clientId, String clientSecret) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "put", requestUrl, body, refreshToken, clientId, clientSecret);
  }

  public static Object patch(String requestUrl, Object body, String refreshToken, String clientId, String clientSecret) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "patch", requestUrl, body, refreshToken, clientId, clientSecret);
  }
  
  public static Object delete(String requestUrl, Object body, String refreshToken, String clientId, String clientSecret) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "delete", body, refreshToken, clientId, clientSecret);
  }

  public static Object publish(String topicId, Object message) {
    return remoteCall("com.collager.trillo.gcp.utils.GCPRestApi", "publish", topicId, message);
  }
  
  public static String getProjectId() {
    return remoteCallAsString("com.collager.trillo.gcp.utils.GCPRestApi", "getProjectId");
  }

}


