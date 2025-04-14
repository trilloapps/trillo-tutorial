/*
 * Copyright (c)  2020 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */



package com.collager.trillo.util;

import java.util.Map;
import io.trillo.util.Proxy;


public class UMApi extends BaseApi {
  
  public static final String ROLE_ADMIN = "admin";
  public static final String ROLE_DEV = "dev";
  public static final String ROLE_USER = "user";
  public static final String ROLE_NONE = "none";
  public static final String ROLE_SERVICE = "service";
  public static final String ROLE_EDITOR = "editor";

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
  
  public static boolean isCurrentUserService() {
    Map<String, Object> user = getCurrentUser();
    return user != null && ROLE_SERVICE.equals("" + user.get("role"));
  }
  
  public static boolean isCurrentUserAdmin() {
    Map<String, Object> user = getCurrentUser();
    return user != null && ROLE_ADMIN.equals("" + user.get("role"));
  }
  
  public static boolean isCurrentUserEditor() {
    Map<String, Object> user = getCurrentUser();
    String role = "" + user.get("role");
    return user != null && (ROLE_ADMIN.equals(role) || ROLE_EDITOR.equals(role));
  }
  
  public static String getCurrentContextAccessToken() {
    return Proxy.getAccessToken();
  }

  public static Object getUser(String id) {
    // todo - server side implementation
    return remoteCall("UMApi", "getUser", id);
  }

  public static Object getUserByUserId(String userId) {
    // todo - server side implementation
    return remoteCall("UMApi", "getUserByUserId", userId);
  }

  public static Object newUser() {
    // todo - server side implementation
    return remoteCall("UMApi", "newUser");
  }

  public static Object newUser(Map<String, Object> userMap) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "newUser", userMap);
  }

  public static Object inviteUser(Map<String, Object> params) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "inviteUser", params);
  }

  public static Object editUser(Map<String, Object> userMap) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "editUser", userMap);
  }

  public static Object changePassword(Map<String, Object> user) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "changePassword", user);
  }

  public static Object resetPassword(Map<String, Object> user) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "resetPassword", user);
  }

  public static Object tenantOrg(Map<String, Object> org) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "tenantOrg", org);
  }

  public static Object authState(Map<String, Object> params) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "authState", params);
  }

  public static Object contacts(Map<String, Object> params) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "contacts", params);
  }

  public static Object toggleSuspendActive(Map<String, Object> params) {
    // todo - server side implementation
    return remoteCall("UMApi", "toggleSuspendActive", params);
  }

  public static Object getRoles() {
    // todo - server side implementation
    return remoteCall("UMApi", "getRoles");
  }

  public static Object saveObject(Map<String, Object> role) {
    // todo - server side implementation
    return remoteCall("UMApi", "saveObject", role);
  }

  public static Object deleteObject(String roleName) {
    // todo - server side implementation
    return remoteCall("UMApi", "deleteObject", roleName);
  }

  public static Object switchUser(Map<String, Object> params) {
    // todo - server side implementation
    return remoteCall("UMApi", "switchUser", params);
  }

  public static Object editMyProfile(Map<String, Object> user) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "editMyProfile", user);
  }

  public static Object changeMyPassword(Map<String, Object> user) {
    // todo - server side implementation
    return remoteCallAsResult("UMApi", "changeMyPassword", user);
  }

  public static Object getCurrentUserTenantOrg() {
    // todo - server side implementation
    return remoteCall("UMApi", "getCurrentUserTenantOrg");
  }
}


