package com.collager.trillo.util;

import com.collager.trillo.model.FilesPage;
import com.collager.trillo.pojo.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import io.trillo.util.Proxy;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.collager.trillo.util.HttpRequestUtil.HttpResponseToString;
import static com.collager.trillo.util.Util.convertToResult;

public class StorageApi extends BaseApi {

  private static String storageEndpoint = "/api/v1.1/storage";
  public static String getFilePath(String fileId) {
    Object res = HttpRequestUtil.get(storageEndpoint + "/" + fileId + "/getFilePath");
    return HttpResponseToString(res);
  }

  public static Long getFileIdByPath(String absoluteFilePath) {
    Object res = HttpRequestUtil.get(storageEndpoint + "/getFileIdByPath?absoluteFilePath=" + absoluteFilePath);
    String str = HttpResponseToString(res);
    try {
      return Long.parseLong(str);
    } catch(Exception exc) {
      return -1L;
    }
  }

  public static String getFolderPath(String folderId) {
    Object res = HttpRequestUtil.get(storageEndpoint + "/" + folderId + "/getFolderPath");
    return HttpResponseToString(res);
  }
  
  public static String getSignedUrl(String filePath) {
    Object res = HttpRequestUtil.get(storageEndpoint + "/getSignedUrl?filePath=" + filePath);
    return HttpResponseToString(res);
  }
  
  public static String getSignedUrl(String filePath, long duration, TimeUnit unit) {
    Object res = HttpRequestUtil.get(storageEndpoint + "/getSignedUrl?filePath=" + filePath
            + "&duration=" + duration + "&unit=" + unit);
    return HttpResponseToString(res);
  }
  
  public static Result copyFileToBucket(String sourceFilePath, String targetFilePath) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileToBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result writeToBucket(byte[] bytes, String targetFilePath, String contentType) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    String byteStr = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    body.put("bytes", byteStr);
    body.put("targetFilePath", targetFilePath);
    body.put("contentType", contentType);
    Object res = HttpRequestUtil.post(storageEndpoint + "/writeToBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }

  public static Result writeToBucket(String bucketName, byte[] bytes, String targetFilePath,
                                     String contentType) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    String byteStr = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    body.put("bytes", byteStr);
    body.put("targetFilePath", contentType);
    body.put("contentType", contentType);
    Object res = HttpRequestUtil.post(storageEndpoint + "/writeToBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileFromBucket(String sourceFilePath, String targetFilePath) {
    return downloadFile(Proxy.getServerUrl() + "/ds/copyFileFromBucket", "", null, sourceFilePath, targetFilePath);
  }
  
  public static Result copyFileToBucket2(String sourceFilePath, String targetFilePath) {
    // assume file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileToBucket2", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileFromBucket2(String sourceFilePath, String targetFilePath) {
    // assume file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileFromBucket2", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileWithinBucket(String sourceFilePath, String targetFilePath) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileWithinBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileWithinBucket(String sourceFilePath, String targetFilePath, boolean makePublic) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    body.put("makeCopy", makePublic);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileWithinBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result readFromBucket(String sourceFilePath) {
    return BaseApi.remoteCallAsResult("StorageApi", "readFromBucket", sourceFilePath);
  }
  
  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> listFiles(String pathName, Boolean versioned) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("pathName", pathName);
    body.put("versioned", versioned);
    Object res = HttpRequestUtil.post(storageEndpoint + "/listFiles", new JSONObject(body).toString());
    return (List<Map<String, Object>>) res;
  }
  
  public static FilesPage getFilesPage(String bucketName, String pathName,
      Boolean versioned) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("pathName", pathName);
    body.put("versioned", versioned);
    Object res = HttpRequestUtil.post(storageEndpoint + "/getFilesPage", new JSONObject(body).toString());
    return convertToFilesPage(res);
  }

  public static FilesPage getFilesPage(String bucketName, String pathName,
                                                    Boolean versioned, String pageToken, int pageSize) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("pathName", pathName);
    body.put("versioned", versioned);
    body.put("pageToken", pageToken);
    body.put("pageSize", pageSize);
    Object res = HttpRequestUtil.post(storageEndpoint + "/getFilesPage", new JSONObject(body).toString());
    return convertToFilesPage(res);
  }

  /* ------------- */
  
  public static String getSignedUrl(String bucketName, String filePath) {
    return remoteCallAsString("StorageApi", "getSignedUrl", bucketName, filePath);
  }
  
  public static String getSignedUrl(String bucketName, String filePath, long duration, TimeUnit unit) {
    Object res = HttpRequestUtil.get(storageEndpoint + "/getSignedUrl?bucketName=" + bucketName + "&filePath=" + filePath
            + "&duration=" + duration + "&unit=" + unit);
    return HttpResponseToString(res);
  }
  
  public static Result copyFileToBucket(String bucketName, String sourceFilePath, String targetFilePath) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileToBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileToBucket(String bucketName, String serviceAccountPropName, 
      String sourceFilePath, String targetFilePath) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("serviceAccountPropName", serviceAccountPropName);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileToBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileFromBucket(String bucketName, String sourceFilePath, String targetFilePath) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileFromBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  
  public static Result copyFileFromBucket(String bucketName, String serviceAccountPropName, 
      String sourceFilePath, String targetFilePath) {
    return downloadFile(Proxy.getServerUrl() + "/ds/copyFileFromBucket", bucketName, serviceAccountPropName, sourceFilePath, targetFilePath);
  }
  
  public static Result copyFileToBucket2(String bucketName, String sourceFilePath, String targetFilePath) {
    // assumes file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileToBucket2", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileFromBucket2(String bucketName, String serviceAccountPropName, 
      String sourceFilePath, String targetFilePath) {
    // assumes file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("serviceAccountPropName", serviceAccountPropName);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileFromBucket2", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileFromBucket2(String bucketName, String sourceFilePath, String targetFilePath) {
    // assumes file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileFromBucket2", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileToBucket2(String bucketName, String serviceAccountPropName, 
      String sourceFilePath, String targetFilePath) {
    // assumes file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("serviceAccountPropName", serviceAccountPropName);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileToBucket2", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileWithinBucket(String bucketName, String sourceFilePath, String targetFilePath) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileWithinBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result copyFileWithinBucket(String bucketName, String sourceFilePath, String targetFilePath, boolean makePublic) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("makePublic", makePublic);
    body.put("targetFilePath", targetFilePath);
    body.put("sourceFilePath", sourceFilePath);
    Object res = HttpRequestUtil.post(storageEndpoint + "/copyFileWithinBucket", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result readFromBucket(String bucketName, String sourceFilePath) {
    return BaseApi.remoteCallAsResult("StorageApi", "readFromBucket", bucketName, sourceFilePath);
  }
  
  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> listFiles(String bucketName, String pathName, Boolean versioned) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("pathName", pathName);
    body.put("versioned", versioned);
    Object res = HttpRequestUtil.post(storageEndpoint + "/listFiles", new JSONObject(body).toString());
    return (List<Map<String, Object>>) res;
  }

  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> listFiles(String bucketName, String pathName, Boolean versioned,
                                                    String pageToken, int pageSize) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("pathName", pathName);
    body.put("versioned", versioned);
    body.put("pageToken", pageToken);
    body.put("pageSize", pageSize);
    Object res = HttpRequestUtil.post(storageEndpoint + "/listFiles", new JSONObject(body).toString());
    return (List<Map<String, Object>>) res;
  }
  
  public static String getBucketName() {
    Object res = HttpRequestUtil.get(storageEndpoint + "/getBucketName");
    return HttpResponseToString(res);
  }

  /* ------------- */
  
  public static Object saveFileObject(Map<String, Object> fileObject) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("fileObject", fileObject);
    return HttpRequestUtil.post(storageEndpoint + "/saveFileObject", new JSONObject(body).toString());
  }
  
  private static Result uploadFile(String url, String bucketName, String serviceAccountPropName, String sourceFilePath, String targetFilePath) {
    CloseableHttpClient httpclient = Proxy.getHttpClient();
    try {
      HttpPost httpPost = new HttpPost(url);
      httpPost.addHeader("Authorization", "Bearer " + Proxy.getAccessToken());
      httpPost.addHeader("x-org-name", Proxy.getOrgName());
      httpPost.addHeader("x-app-name", Proxy.getAppName());
      File file = new File(sourceFilePath);
      FileBody bin = new FileBody(file);
      StringBody bucketNameBody = new StringBody(bucketName, ContentType.TEXT_PLAIN);
      StringBody targetFileBody = new StringBody(targetFilePath, ContentType.TEXT_PLAIN);

      MultipartEntityBuilder builder = MultipartEntityBuilder.create().addPart("file", bin)
          .addPart("bucketName", bucketNameBody)
          .addPart("targetFilePath", targetFileBody);
      
      if (serviceAccountPropName != null) {
        StringBody serviceAccountPropNameBody = new StringBody(serviceAccountPropName, ContentType.TEXT_PLAIN);
        builder = builder.addPart("serviceAccountPropName", serviceAccountPropNameBody);
      }
      HttpEntity reqEntity = builder.build();

      httpPost.setEntity(reqEntity);

      CloseableHttpResponse response = httpclient.execute(httpPost);
      try {
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null && 
            (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
          String responseString = EntityUtils.toString(resEntity, "UTF-8");
          return Result.getSuccessResultWithData(responseString);
          //System.out.println(responseString);
        }  else {
          if (resEntity == null) {
            return Result.getFailedResult("No response received, HTTP code: " 
                + response.getStatusLine().getStatusCode());
          }
          try {
            String content = IOUtils.toString(resEntity.getContent(), StandardCharsets.UTF_8);
            Map<String, Object> m = Util.fromJSONString(content, new TypeReference<Map<String, Object>>() {});
            if (m.containsKey("_rtag")) {
              Result result = Util.fromMap(m, Result.class);
              return result;
            }
            return Result.getFailedResult("Invalid response received, HTTP code: " 
                + response.getStatusLine().getStatusCode() + " \n " + content);
          } catch (Exception exc) {
            
          }
          return Result.getFailedResult("Invalid response received, HTTP code: " 
              + response.getStatusLine().getStatusCode());
        }
      } catch (Exception exc1) {
        return Result.getFailedResult(exc1.toString());
      } finally {
        try {
          response.close();
        } catch (Exception exc2) {
        }
      }
    } catch (Exception exc3) {
      return Result.getFailedResult(exc3.toString());
    } finally {
      try {
        httpclient.close();
      } catch (Exception exc4) {
      }
    }
  }
  
  private static Result downloadFile(String url, String bucketName, String serviceAccountPropName, String sourceFilePath, String targetFilePath) {
    CloseableHttpClient httpclient = Proxy.getHttpClient();
    try {
      HttpGet httpGet = new HttpGet(url);
      httpGet.addHeader("Authorization", "Bearer " + Proxy.getAccessToken());
      httpGet.addHeader("x-org-name", Proxy.getOrgName());
      httpGet.addHeader("x-app-name", Proxy.getAppName());
      
      URIBuilder builder = new URIBuilder(httpGet.getURI())
          .addParameter("bucketName", bucketName)
          .addParameter("sourceFilePath", sourceFilePath);
      
      if (serviceAccountPropName != null) {
        builder.addParameter("serviceAccountPropName", serviceAccountPropName);
      }
      URI uri = builder.build();
       
      ((HttpRequestBase) httpGet).setURI(uri);
     
      CloseableHttpResponse response = httpclient.execute(httpGet);
      try {
        HttpEntity resEntity = response.getEntity();
        if ((resEntity != null) && 
            (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
          FileUtil.copyFile(resEntity.getContent(), new File(targetFilePath));
        } else {
          if (resEntity == null) {
            return Result.getFailedResult("No response received, HTTP code: " 
                + response.getStatusLine().getStatusCode());
          }
          try {
            String content = IOUtils.toString(resEntity.getContent(), StandardCharsets.UTF_8);
            Map<String, Object> m = Util.fromJSONString(content, new TypeReference<Map<String, Object>>() {});
            if (m.containsKey("_rtag")) {
              Result result = Util.fromMap(m, Result.class);
              return result;
            }
            return Result.getFailedResult("Invalid response received, HTTP code: " 
                + response.getStatusLine().getStatusCode() + " \n " + content);
          } catch (Exception exc) {
            
          }
          return Result.getFailedResult("Invalid response received, HTTP code: " 
              + response.getStatusLine().getStatusCode());
        }
      } catch (Exception exc1) {
        return Result.getFailedResult(exc1.toString());
      } finally {
        try {
          response.close();
        } catch (Exception exc2) {
        }
      }
    } catch (Exception exc3) {
      return Result.getFailedResult(exc3.toString());
    } finally {
      try {
        httpclient.close();
      } catch (Exception exc4) {
      }
    }
    return Result.getSuccessResult();
  }
  
  public static Object shareWithTenants(Map<String, Object> params) {
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("params", params);
    return HttpRequestUtil.post(storageEndpoint + "/shareWithTenants", new JSONObject(body).toString());
  }
  
  public static Result copyLargeFileToBucket(String sourceFilePath, String targetFilePath) {
    return uploadFile(Proxy.getServerUrl() + "/ds/copyFileToBucket", "", null, sourceFilePath, targetFilePath);
  }
  
  public static Result copyLargeFileToBucket(String bucketName, String sourceFilePath, String targetFilePath) {
    return uploadFile(Proxy.getServerUrl() + "/ds/copyFileToBucket", bucketName, null, sourceFilePath, targetFilePath);
  }
  
  public static Result copyLargeFileToBucket(String bucketName, String serviceAccountPropName, 
      String sourceFilePath, String targetFilePath) {
    return uploadFile(Proxy.getServerUrl() + "/ds/copyFileToBucket", bucketName, serviceAccountPropName, sourceFilePath, targetFilePath);
  }
  
  public static Result copyLargeFileToBucket2(String sourceFilePath, String targetFilePath) {
    // assume file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object obj = HttpRequestUtil.post(storageEndpoint + "/copyLargeFileToBucket2", new JSONObject(body).toString());
    return convertToResult(obj);
  }
  
  public static Result copyLargeFileToBucket2(String bucketName, String sourceFilePath, String targetFilePath) {
    // assumes file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object obj = HttpRequestUtil.post(storageEndpoint + "/copyLargeFileToBucket2", new JSONObject(body).toString());
    return convertToResult(obj);
  }
  
  
  public static Result copyLargeFileToBucket2(String bucketName, String serviceAccountPropName, 
      String sourceFilePath, String targetFilePath) {
    // assumes file on the container file system
    Map<String, Object> body = new LinkedHashMap<String, Object> ();
    body.put("bucketName", bucketName);
    body.put("serviceAccountPropName", serviceAccountPropName);
    body.put("sourceFilePath", sourceFilePath);
    body.put("targetFilePath", targetFilePath);
    Object obj = HttpRequestUtil.post(storageEndpoint + "/copyLargeFileToBucket2", new JSONObject(body).toString());
    return convertToResult(obj);
  }
  
  public static Result makePublic(String bucketName, String filePath) {
    return BaseApi.remoteCallAsResult("StorageApi", "makePublic", bucketName, filePath);
  }

  public static Result deleteFileFromBucket(String bucketName, String sourceFilePath) {
    return BaseApi.remoteCallAsResult("StorageApi", "deleteFileFromBucket", bucketName, sourceFilePath);
  }

  public static Result makePublic(String bucketName, String serviceAccountPropName, 
      String filePath) {
    return BaseApi.remoteCallAsResult("StorageApi", "makePublic", bucketName, serviceAccountPropName, filePath);
  }
  
  @SuppressWarnings("unchecked")
  private static FilesPage convertToFilesPage(Object obj) {
    if (obj instanceof Map<?, ?>) {
      Map<String, Object> m = (Map<String, Object>) obj;
      if (m.containsKey("_rtag") && "_r_".equals("" + m.get("_rtag"))) {
        return null;
      }  else {
        FilesPage filesPage = Util.fromMap(m, FilesPage.class);
        return filesPage;
      }
    }
    return null;
  }
  
  /* 
   private static Result uploadFile(String url, String filePath) {
    CloseableHttpClient httpclient = Proxy.getHttpClient();
    try {
      File file = new File(filePath);
      FileBody bin = new FileBody(file);
      
      Request request = Request.Post(url)
          .addHeader("Authorization", "Bearer " + Proxy.getAccessToken())
          .addHeader("x-org-name", Proxy.getOrgName()).addHeader("x-app-name", Proxy.getAppName())
          .bodyFile(file, bin.getContentType());

      Content content = Executor.newInstance(httpclient).execute(request).returnContent();
      System.out.println(content.toString());
    } catch (Exception exc3) {
      return Result.getFailedResult(exc3.toString());
    } finally {
      try {
        httpclient.close();
      } catch (Exception exc4) {
      }
    }
    return Result.getSuccessResult();
  }
   */
}

