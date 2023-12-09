package com.collager.trillo.util;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.collager.trillo.pojo.Result;


public class HttpApi extends BaseApi {

  public static Result get(String requestUrl) {
    return remoteCallAsResult("HttpApi", "get", requestUrl);
  }

  public static Result get2(String requestUrl) {
    return remoteCallAsResult("HttpApi", "get2", requestUrl);
  }

  public static Result get(String requestUrl, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "get", requestUrl, headers);
  }

  public static Result get(String requestUrl, Map<String, String> headers, Integer retryCount, Integer waitTime) {
    return remoteCallAsResult("HttpApi", "get", requestUrl, headers, retryCount, waitTime);
  }

  public static Result post(String requestUrl, Object body) {
    return remoteCallAsResult("HttpApi", "post", requestUrl, body);
  }

  public static Result post(String requestUrl, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "post", requestUrl, body, headers);
  }

  public static Result postFormData(String requestUrl, Map<String, String> body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "postFormData", requestUrl, body, headers);
  }

  public static Result postFormDataAsString(String requestUrl, Map<String, String> body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "postFormDataAsString", requestUrl, body, headers);
  }

  public static Result put(String requestUrl, Object body) {
    return remoteCallAsResult("HttpApi", "put", requestUrl, body, null);
  }

  public static Result put(String requestUrl, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "put", requestUrl, body, headers);
  }

  public static Result patch(String requestUrl, Object body) {
    return remoteCallAsResult("HttpApi", "patch", requestUrl, body, null);
  }

  public static Result patch(String requestUrl, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "patch", requestUrl, body, headers);
  }

  public static Result delete(String requestUrl, Object body) {
    return remoteCallAsResult("HttpApi", "delete", requestUrl, body);
  }

  public static Result delete(String requestUrl, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "delete", requestUrl, body, headers);
  }

  public static Result getAsString(String requestUrl, String contentType) {
    return remoteCallAsResult("HttpApi", "getAsString", requestUrl);
  }

  public static Result getAsString(String requestUrl, String contentType, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "getAsString", requestUrl, contentType, headers);
  }

  public static Result getAsString(String requestUrl, String contentType, Object body) {
    return remoteCallAsResult("HttpApi", "getAsString", requestUrl, contentType, body);
  }

  public static Result getAsString(String requestUrl, String contentType, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "getAsString", requestUrl, contentType, body, headers);
  }

  public static Result postAsString(String requestUrl, String contentType, Object body) {
    return remoteCallAsResult("HttpApi", "postAsString", requestUrl, contentType, body);
  }

  public static Result postAsString(String requestUrl, String contentType, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "postAsString", requestUrl, contentType, body, headers);
  }

  public static Result putAsString(String requestUrl, String contentType, Object body) {
    return remoteCallAsResult("HttpApi", "putAsString", requestUrl, contentType, body, null);
  }

  public static Result putAsString(String requestUrl, String contentType, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "putAsString", requestUrl, contentType, body, headers);
  }

  public static Result patchAsString(String requestUrl, String contentType, Object body) {
    return remoteCallAsResult("HttpApi", "patchAsString", requestUrl, contentType, body, null);
  }

  public static Result patchAsString(String requestUrl, String contentType, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "patchAsString", requestUrl, contentType, body, headers);
  }

  public static Result deleteAsString(String requestUrl, String contentType, Object body) {
    return remoteCallAsResult("HttpApi", "deleteAsString", requestUrl, contentType, body);
  }

  public static Result deleteAsString(String requestUrl, String contentType, Object body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "deleteAsString", requestUrl, contentType, body, headers);
  }

  public static Result readFileAsJSON(String url, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "readFileAsJSON", url, headerMap);
  }

  public static Result readFileAsJSON(String url, Charset encoding, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "readFileAsJSON", url, encoding, headerMap);
  }

  public static Result readFileAsString(String url, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "readFileAsString", url, headerMap);
  }

  public static Result readFileAsString(String url, Charset encoding, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "readFileAsString", url, encoding, headerMap);
  }

  public static Result readFileAsBytes(String url, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "readFileAsBytes", url, headerMap);
  }

  public static Result writeFile(String url, String filePath, String fileFieldName, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "writeFile", url, filePath, fileFieldName, headerMap);
  }

  public static Result writeFileBytes(String url,
                                      String fileName, byte[] fileContent, String fileFieldName, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "writeFileBytes", url, fileName, fileContent, fileFieldName, headerMap);
  }

  public static Result writeFile(String url, String filePath, String fileFieldName,
                                 Map<String, String> headerMap, Map<String, Object> additionalFields) {
    return remoteCallAsResult("HttpApi", "writeFile", url, filePath, fileFieldName,
      headerMap, additionalFields);
  }

  public static Result writeFileBytes(String url,
                                      String fileName, byte[] fileContent, String fileFieldName, Map<String,
    String> headerMap, Map<String, Object> additionalFields) {
    return remoteCallAsResult("HttpApi", "writeFileBytes", url, fileName, fileContent,
      fileFieldName, headerMap, additionalFields);
  }

  public static Result writeFileBytes(String url,
                                      String fileName, String fileContentAsString, String fileFieldName, Map<String, String> headerMap) {
    return remoteCallAsResult("HttpApi", "writeFileBytes", url, fileName, fileContentAsString, fileFieldName, headerMap);
  }

  public static Result writeFileBytes(String url,
                                      String fileName, String fileContentAsString, String fileFieldName, Map<String,
    String> headerMap, Map<String, Object> additionalFields) {
    return remoteCallAsResult("HttpApi", "writeFileBytes", url, fileName, fileContentAsString,
      fileFieldName, headerMap, additionalFields);
  }

  public static Result getOAuth2Token(String url, Map<String, String> body) {
    return remoteCallAsResult("HttpApi", "getOAuth2Token", url, body);
  }

  public static Result getOAuth2TokenRefreshTokenGrant(String url, String clientId, String clientSecret,
                                                       String refreshToken) {
    return remoteCallAsResult("HttpApi", "getOAuth2TokenRefreshTokenGrant", url, clientId, clientSecret, refreshToken);
  }

  public static Result postSOAP(String soapUrl, String body, Map<String, String> headers) {
    return remoteCallAsResult("HttpApi", "postSOAP", soapUrl, body, headers);
  }

  @SuppressWarnings("unchecked")
  public static Result getWithRetry(String requestUrl, Map<String, String> headers) {
    Result result;
    Map<String, Object> props;
    Map<String, Object> headerMap = null;
    long rateLimitReset = -1;
    long delta;
    int retryCount = 0;
    List<Object> sl;
    while (true) {
      result = get(requestUrl, headers);
      if (result.getCode() == 429 && retryCount < 3) {
        LogApi.auditLogInfo("Looking for header, code: " + result.getCode());
        rateLimitReset = -1;
        props = result.getProps();
        if (props != null) {
          if (props.containsKey("responseHeader") && !props.containsKey("responseHeaders")) {
            // for backward compatibility
            props.put("responseHeaders", props.get("responseHeader"));
          }
          if (props.get("responseHeaders") instanceof Map<?, ?>) {
            headerMap = (Map<String, Object>) props.get("responseHeaders");
          } else if (props.get("responseHeaders") instanceof Object) {
            headerMap = Util.covertObjectToMap(props.get("responseHeaders"));
          }
        }

        //LogApi.auditLogInfo("Header map", headerMap != null ? BaseApi.asJSONPrettyString(headerMap) : "");

        if (headerMap != null && headerMap.get("X-RateLimit-Reset") instanceof List<?>) {
          sl = (List<Object>) headerMap.get("X-RateLimit-Reset");
          if (sl.size() > 0) {
            rateLimitReset = Long.parseLong("" + sl.get(0)) * 1000; // convert seconds to millis
            delta = rateLimitReset - System.currentTimeMillis() + 5000;
            if (delta > 0) {
              LogApi.auditLogError("Waiting for: " + delta + " milliseconds");
              BaseApi.waitForMillis(delta);
            }
            retryCount++;
          } else {
            break;
          }
        } else {
          break;
        }
      } else {
        break;
      }
    }
    return result;
  }
}


