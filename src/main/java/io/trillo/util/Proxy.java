/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package io.trillo.util;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Util;
import com.fasterxml.jackson.core.type.TypeReference;

public class Proxy {

  private static Logger log = LoggerFactory.getLogger(Proxy.class);

  private static Map<String, Object> loginResponse = null;
  private static Map<String, Object> user = null;
  private static String serverUrl;
  private static String userId;
  private static String password;
  private static String orgName = "cloud";
  private static String appName = "shared";
  private static String tenantName = null;

  private static String accessToken = null;

  private static boolean privilegedMode = false;
  private static boolean privilegedUserMode = false;

  public static void setArgs(Map<String, Object> m) {
    setServerUrl("" + m.get("serverUrl"));
    setUserId("" + m.get("userId"));
    setPassword("" + m.get("password"));
    if (m.containsKey("orgName")) {
      setOrgName("" + m.get("orgName"));
    }
    if (m.containsKey("appName")) {
      setAppName("" + m.get("appName"));
    }
    if (m.containsKey("tenantName")) {
      tenantName = "" + m.get("tenantName");
    }
  }

  public static Map<String, Object> getLoginResponse() {
    return loginResponse;
  }

  public static String getUserId() {
    return userId;
  }

  public static String getOrgName() {
    return orgName;
  }

  public static String getAppName() {
    return appName;
  }

  public static String getServerUrl() {
    return serverUrl;
  }

  public static void setServerUrl(String serverUrl) {
    Proxy.serverUrl = serverUrl;
  }

  public static void setUserId(String userId) {
    Proxy.userId = userId;
  }

  public static void setPassword(String password) {
    Proxy.password = password;
  }

  public static void setOrgName(String orgName) {
    Proxy.orgName = orgName;
  }

  public static void setAppName(String appName) {
    Proxy.appName = appName;
  }

  public static String getAccessToken() {
    if (accessToken != null) {
      return accessToken;
    }
    login();
    return accessToken;
  }

  public static boolean isLoggedIn() {
    return accessToken != null;
  }

  @SuppressWarnings("unchecked")
  public static boolean login() {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    map.put("j_username", userId);
    map.put("j_password", password);
    if (tenantName != null) {
      map.put("tenant_display_name", tenantName);
    }
    HttpEntity httpEntity = new StringEntity(Util.asJSONPrettyString(map), ContentType.APPLICATION_JSON);
    try {
      Request request = Request.Post(serverUrl + "/ajaxLogin").addHeader("x-org-name", orgName).addHeader("x-app-name", "auth").body(httpEntity);
      CloseableHttpClient cli = getHttpClient();
      Content content = Executor.newInstance(cli).execute(request).returnContent();
      loginResponse = Util.fromJSONString(content.toString(), new TypeReference<Map<String, Object>>() {});
      if (!loginResponse.containsKey("accessToken")) {
        log.error("Failed to login, see the response below");
        log.error(content.toString());
        throw new RuntimeException("Failed to login, check the arguments your supplied");
      }
      user = ((Map<String, Object>)loginResponse.get("user"));
      accessToken = (String) loginResponse.get("accessToken");
      log.info("Successfully logged in: " + user.get("userId"));
      return true;
    } catch (Exception exc) {
      log.error("Failed to login: " + exc.toString());
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  public static Object remoteCall(String javaClassName, String javaMethodName, Object ...args) {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    map.put("javaClassName", javaClassName);
    map.put("javaMethodName", javaMethodName);
    map.put("args", args);
    if (privilegedMode) {
      map.put("privilegedMode", privilegedMode);
    }
    if (privilegedUserMode) {
      map.put("privilegedUserMode", privilegedUserMode);
    }
    HttpEntity httpEntity = new StringEntity(Util.asJSONPrettyString(map), ContentType.APPLICATION_JSON);
    try {
      Request request = Request.Post(serverUrl + "/ds/remoteCall")
        .addHeader("Authorization", "Bearer " + getAccessToken()).addHeader("x-org-name", orgName).addHeader("x-app-name", appName)
        .body(httpEntity);
      CloseableHttpClient cli = getHttpClient();
      Content content = Executor.newInstance(cli).execute(request).returnContent();
      try {
        Object response = Util.fromJSONString(content.toString(), Object.class);
        if (response instanceof Map<?, ?>) {
          Map<String, Object> m = (Map<String, Object>) response;
          if (m.containsKey("_rtag") && "_r_".equals("" + m.get("_rtag"))) {
            return Util.fromMap(m, Result.class);
          }
        }
        return response;
      } catch (Exception exc) {
        // not a value JSON return content as string
        return content.toString();
      }

    } catch (Exception exc) {
      log.error("Failed remoteCall: " + exc.toString());
      return Result.getFailedResult("Failed remoteCall: " + exc.toString());
    }
  }

  public static CloseableHttpClient getHttpClient() {
    try {
      TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          @Override
          public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // TODO Auto-generated method stub

          }
          @Override
          public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // TODO Auto-generated method stub

          }
        }
      };

      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(sc).build();
      return httpClient;

    } catch (Exception e) {
      return null;
    }
  }

  public static String getIdOfCurrentUser() {
    return "" + user.get("id");
  }

  public static long getIdOfCurrentUserAsLong() {
    return Long.parseLong("" + user.get("id"));
  }

  public static void switchToPrivilegedMode() {
    privilegedMode = true;
  }

  public static void resetPrivilegedMode() {
    privilegedMode = false;
  }

  public static boolean isPrivilegedMode() {
    return privilegedMode;
  }

  public static void switchToPrivilegedUserMode() {
    privilegedUserMode = true;
  }

  public static void resetPrivilegedUserMode() {
    privilegedUserMode = false;
  }

  public static boolean isPrivilegedUserMode() {
    return privilegedUserMode;
  }

}
