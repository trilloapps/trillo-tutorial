package com.collager.trillo.util;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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
import io.trillo.util.Proxy;

public class HttpRequestUtil {
  
  private static Logger log = LoggerFactory.getLogger(HttpRequestUtil.class);
  
  
  public static Object get(String path) {
    
    try {
      Request request = Request.Get(Proxy.getServerUrl() + path)
          .addHeader("Authorization", "Bearer " + Proxy.getAccessToken()).
          addHeader("x-org-name", Proxy.getOrgName()).addHeader("x-app-name", Proxy.getAppName());
      CloseableHttpClient cli = getHttpClient();
      Content content = Executor.newInstance(cli).execute(request).returnContent();
      try {
        Object response = Util.fromJSONString(content.toString(), Object.class);
        return response;
      } catch (Exception exc) {
        // not a value JSON return content as string
        return content.toString();
      }      
    } catch (Exception exc) {
      log.error("Failed remove call: " + exc.toString());
      return Result.getFailedResult("Failed remove call: " + exc.toString());
    }
  }
  
  public static Object post(String path, Object body) {
    HttpEntity httpEntity = new StringEntity(Util.asJSONPrettyString(body), ContentType.APPLICATION_JSON);
    try {
      Request request = Request.Post(Proxy.getServerUrl() + path)
          .addHeader("Authorization", "Bearer " + Proxy.getAccessToken()).
          addHeader("x-org-name", Proxy.getOrgName()).addHeader("x-app-name", Proxy.getAppName())
          .body(httpEntity);
      CloseableHttpClient cli = getHttpClient();
      Content content = Executor.newInstance(cli).execute(request).returnContent();
      try {
        Object response = Util.fromJSONString(content.toString(), Object.class);
        return response;
      } catch (Exception exc) {
        // not a value JSON return content as string
        return content.toString();
      }      
    } catch (Exception exc) {
      log.error("Failed remove call: " + exc.toString());
      return Result.getFailedResult("Failed remove call: " + exc.toString());
    }
  }

  protected static String HttpResponseToString(Object res) {
    if (res instanceof String) {
      return (String) res;
    }
    if (res != null) {
      return Util.asJSONPrettyString(res);
    }
    return null;
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


}
