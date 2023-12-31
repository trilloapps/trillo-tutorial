package com.collager.trillo.pojo;

import java.io.Serializable;
import java.util.Map;

public class OAuth2Token implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String accessToken;
  private final String refreshToken;
  private long expiry = 0;  // in seconds
  private long refreshTokenExpiry; // in seconds
  
  private long createdAt; // in milli
  private String orgName;
  private String appName;
  private String serviceName;
  
  public static final String IMPLICIT_GRANT_TYPE = "implicit";
  public static final String AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code";
  public static final String PASSWORD_GRANT_TYPE = "password";
  public static final String CLIENT_CREDENTIALS_GRANT_TYPE = "client_credentials";
  
  public static final String GRANT_TYPE_ATTR_NAME = "grant_type";
  public static final String OAUTH_URL_ATTR_NAME = "oauthUrl";
  public static final String TOKEN_URL_ATTR_NAME = "tokenUrl";
  public static final String CLIENT_ID_ATTR_NAME = "client_id";
  public static final String CLIENT_SECRET_ATTR_NAME = "client_secret";
  public static final String USERNAME_ATTR_NAME = "username";
  public static final String PASSWORD_ATTR_NAME = "password";
  public static final String REDIRECT_URI_ATTR_NAME = "redirect_uri";
  public static final String SCOPE_ATTR_NAME = "scope";
  public static final String CODE_ATTR_NAME = "code";
  
  public static final String ACCESS_TOKEN_ATTR_NAME = "access_token";
  public static final String REFRESH_TOKEN_ATTR_NAME = "refresh_token";
  public static final String EXPIRES_IN_ATTR_NAME = "expires_in";
  public static final String REFRESH_EXPIRES_IN_ATTR_NAME = "refresh_expires_in"; // not oauth2 standard but used by some services
  public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
  
  
  public OAuth2Token(String accessToken, String refreshToken, long expiry, 
      String orgName, String appName, String serviceName) {
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
      this.expiry = expiry;
      createdAt = System.currentTimeMillis();
      if (accessToken == null) {
          this.expireAccessToken();
      }
      this.orgName = serviceName;
      this.appName = serviceName;
      this.serviceName = serviceName;
  }

  public String getAccessToken() {
      return this.accessToken;
  }

  public String getRefreshToken() {
      return this.refreshToken;
  }

  public long getExpiry() {
      return expiry;
  }

 
  public void expireAccessToken() {
      this.expiry = 0;
  }

  
  public boolean isAccessTokenExpired() {
    long expiresAt = createdAt  + expiry * 1000;
    return expiresAt < System.currentTimeMillis();
  }


  public long getRefreshTokenExpiry() {
    return refreshTokenExpiry;
  }

  public void setRefreshTokenExpiry(long refreshTokenExpiry) {
    this.refreshTokenExpiry = refreshTokenExpiry;
  }
  
  public String getOrgName() {
    return orgName;
  }

  public String getAppName() {
    return appName;
  }

  public String getServiceName() {
    return serviceName;
  }
  
  public static OAuth2Token makeOAuthToken(Map<String, Object> response, String serviceName) {
    return makeOAuthToken(response, "cloud", "shared", serviceName);
  }

  public static OAuth2Token makeOAuthToken(Map<String, Object> response, String orgName, String appName, String serviceName) {
    OAuth2Token token  = null;
    try {
      String accessToken = (String) response.get(OAuth2Token.ACCESS_TOKEN_ATTR_NAME);
      String refreshTokenToken = (String) response.get(OAuth2Token.REFRESH_TOKEN_ATTR_NAME);
      long tokenExpiresIn; 
      if (response.containsKey(OAuth2Token.EXPIRES_IN_ATTR_NAME)) {
        try {
          tokenExpiresIn = Long.parseLong("" + response.get(OAuth2Token.EXPIRES_IN_ATTR_NAME));
        } catch (Exception exc) {
          throw new RuntimeException("Failed to parse token expires in, error: " + exc.getMessage(), exc);
        }
      } else {
        tokenExpiresIn = 86400 * 30; // 30 days
      }
      token = new OAuth2Token(accessToken, refreshTokenToken, tokenExpiresIn, orgName, appName, serviceName);
    } catch (Exception exc2) {
      throw new RuntimeException("Failed to parse token, error: " + exc2.getMessage(), exc2);
    }
    long refreshTokenExpiresIn = -1;
    if (response.containsKey("refresh_expires_in")) {
      try {
        refreshTokenExpiresIn = Long.parseLong("" + response.get(OAuth2Token.REFRESH_EXPIRES_IN_ATTR_NAME));
      } catch (Exception exc) {
        throw new RuntimeException("Failed to parse refresh token expires in, error: " + exc.getMessage(), exc);
      }
    }
    token.setRefreshTokenExpiry(refreshTokenExpiresIn);
    return token;
  }

}
