package com.collager.trillo.util;

import java.util.Map;
import com.collager.trillo.pojo.Result;

public class OAuth1Api extends BaseApi {
  
  public static Result get(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url) {
    return remoteCallAsResult("OAuth1Api", "get", clientKey, clientSecret, token, 
        tokenSecret, realm, url);
  }
  
  public static Result get(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Map<String, String> headers) {
    return remoteCallAsResult("OAuth1Api", "get", clientKey, clientSecret, token, 
        tokenSecret, realm, url, headers);
  }
  
  public static Result post(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Object body) {
    return remoteCallAsResult("OAuth1Api", "post", clientKey, clientSecret, token, 
        tokenSecret, realm, url, body);
  }
  
  public static Result post(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Object body, Map<String, String> headers) {
    return remoteCallAsResult("OAuth1Api", "post", clientKey, clientSecret, token, 
        tokenSecret, realm, url, body, headers);
  }
  
  public static Result postSOAP(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, String body) {
    return remoteCallAsResult("OAuth1Api", "postSOAP", clientKey, clientSecret, token, 
        tokenSecret, realm, url, body);
  }
  
  public static Result postSOAP(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, String body, Map<String, String> headers) {
    return remoteCallAsResult("OAuth1Api", "postSOAP", clientKey, clientSecret, token, 
        tokenSecret, realm, url, body, headers);
  }
  
  public static Result postSOAPAsXML(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, String body) {
    return remoteCallAsResult("OAuth1Api", "postSOAPAsXML", clientKey, clientSecret, token, 
        tokenSecret, realm, url, body);
  }
  
  public static Result postSOAPAsXML(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, String body, Map<String, String> headers) {
    return remoteCallAsResult("OAuth1Api", "postSOAPAsXML", clientKey, clientSecret, token, 
        tokenSecret, realm, url, body, headers);
  }

  public static Result put(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Object body) {
    return remoteCallAsResult("OAuth1Api", "put", clientKey, clientSecret, token,
        tokenSecret, realm, url, body);
  }

  public static Result put(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Object body, Map<String, String> headers) {
    return remoteCallAsResult("OAuth1Api", "put", clientKey, clientSecret, token,
        tokenSecret, realm, url, body, headers);
  }

  public static Result delete(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url) {
    return remoteCallAsResult("OAuth1Api", "delete", clientKey, clientSecret, token,
        tokenSecret, realm, url);
  }

  public static Result delete(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Map<String, String> headers) {
    return remoteCallAsResult("OAuth1Api", "delete", clientKey, clientSecret, token,
        tokenSecret, realm, url, headers);
  }

  public static Result patch(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Object body) {
    return remoteCallAsResult("OAuth1Api", "patch", clientKey, clientSecret, token,
        tokenSecret, realm, url, body);
  }

  public static Result patch(String clientKey, String clientSecret, String token,
      String tokenSecret, String realm, String url, Object body, Map<String, String> headers) {
    return remoteCallAsResult("OAuth1Api", "patch", clientKey, clientSecret, token,
        tokenSecret, realm, url, body, headers);
  }
}
