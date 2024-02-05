package com.collager.trillo.util;

import java.util.Map;
import com.collager.trillo.pojo.OAuth2Token;
import com.collager.trillo.pojo.Result;

public class TokenApi extends BaseApi {
  
  public static String retrieveSecret(String secretName) {
    return remoteCallAsString("TokenApi", "retrieveSecret", secretName);
  }
  
  public static Result addOAuthToken(String key, OAuth2Token token) {
    Result result = remoteCallAsResult("TokenApi", "addOAuthToken", key, token);
    return result;
  }
  
  public static OAuth2Token getOAuthToken(String key) {
    Result result = remoteCallAsResult("TokenApi", "getOAuthToken", key);
    if (result.isFailed()) {
      LogApi.error("Failed to getOAuthToken, error: " + result.getMessage());
    } else if (result.getData() instanceof Map<?, ?>) {
      @SuppressWarnings("unchecked")
      Map<String, Object> m = (Map<String, Object>) result.getData();
      return Util.fromMap(m, OAuth2Token.class);
    }
    return null;
  }
  
  public static Result removeOAuthToken(String key) {
    Result result = remoteCallAsResult("TokenApi", "removeOAuthToken", key);
    return result;
  }

}
