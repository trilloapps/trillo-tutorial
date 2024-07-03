/*
 * Copyright (c)  2020 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */



package com.collager.trillo.util;

import io.trillo.util.Proxy;

import java.util.Map;


public class UMApi extends BaseApi {

  private static String umEndpoint = "/_service/um";

  public static Map<String, Object> getCurrentUser() {
    return remoteCallAsMap("UMApi", "getCurrentUser");
  }
 
  public static String getCurrentUserId() {
    return Proxy.getUserId();
  }

  public static String getIdOfCurrentUser() {
    return Proxy.getIdOfCurrentUser();
  }

  public static long getIdOfCurrentUserAsLong() {
    return Proxy.getIdOfCurrentUserAsLong();
  }
  
  public static void switchToPrivilegedMode() {
    Proxy.switchToPrivilegedMode();
  }
  
  public static void resetPrivilegedMode() {
    Proxy.resetPrivilegedMode();
  }

  public static boolean isPrivilegedMode() {
    return Proxy.isPrivilegedMode();
  }

  public static void switchToPrivilegedUserMode() {
    Proxy.switchToPrivilegedUserMode();
  }
  
  public static void resetPrivilegedUserMode() {
    Proxy.resetPrivilegedUserMode();
  }

  public static boolean isPrivilegedUserMode() {
    return Proxy.isPrivilegedUserMode();
  }

  public static Object deleteObject(long id) {
    return HttpRequestUtil.delete(umEndpoint + "/" + "deleteUser" + "?id=" + id);
  }
}

