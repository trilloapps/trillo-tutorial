/*
 * Copyright (c)  2020 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.util;

public class GCSApi extends BaseApi {

  private static String gcsEndpoint = "/api/v1.1/gcs";
  public static String getTrilloGCSBucket() {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getTrilloGCSBucket");
  }
  
  public static String getTrilloGCSBucketURI() {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getTrilloGCSBucketURI");
  }

  public static String getGCSFileURI(String pathToFile) {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getGCSFileURI?pathToFile=" + pathToFile);
  }
  
  public static String getGCSFileURI(String path, String fileName) {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getGCSFileURI?path=" + path + "&fileName=" + fileName);
  }

  
  public static String getLocalPath (String bucketPath) {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getLocalPath?bucketPath=" + bucketPath);
  }

  public static String getGCSPath(String appDataPath) {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getGCSPath?appDataPath=" + appDataPath);
  }

  public static String getGCSTempPath() {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getGCSTempPath");
  }
  
  public static String getDefaultRootFolder() {
    return (String) HttpRequestUtil.get(gcsEndpoint + "/getDefaultRootFolder");
  }
}


