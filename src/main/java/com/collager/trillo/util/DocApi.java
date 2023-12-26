/*
 * Copyright (c)  2020 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.util;

import java.util.List;
import java.util.Map;

public class DocApi extends BaseApi {

  public static List<Map<String, Object>> getDocList(long folderId, String orderBy) {
    return remoteCallAsListOfMaps("DocApi", "getDocList", folderId, orderBy);
  }

  public static String getDocFolderPath(String folderId) {
    return remoteCallAsString("DocApi", "getDocFolderPath", folderId);
  }

  public static String getBucketPath(Map<String, Object> docObject) {
    return remoteCallAsString("DocApi", "getBucketPath", docObject);
  }

  public static Object getDocAIResult(long docId) {
    return remoteCall("DocApi", "getDocAIResult", docId);
  }

  public static Object getDocAIResults(long folderId, int start, int pageSize) {
    return remoteCall("DocApi", "getDocAIResults", folderId, start, pageSize);
  }

  public static Object getSummaryResult(long docId, String type) {
    return remoteCall("DocApi", "getSummaryResult", docId, type);
  }

  public static Object getSummaryResults(long folderId, int start, int pageSize) {
    return remoteCall("DocApi", "getSummaryResults", folderId, start, pageSize);
  }
  
  public static String getUrl(String documentType, boolean batchMode) {
    return remoteCallAsString("DocApi", "getUrl", documentType, batchMode);
  }
}


