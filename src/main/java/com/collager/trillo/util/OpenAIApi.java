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

public class OpenAIApi extends BaseApi {

  public static Object chat(Map<String, Object> params) {
    return remoteCall("OpenAIApi", "chat", params);
  }

  public static Object generateImage(Map<String, Object> params) {
    return remoteCall("OpenAIApi", "generateImage", params);
  }

  public static Object multimodalChat(Map<String, Object> params) {
    return remoteCall("OpenAIApi", "multimodalChat", params);
  }
  
  public static Object convertQuestionToSql(Map<String, Object> params) {
    // todo - server side implementation
    return remoteCall("OpenAIApi", "convertQuestionToSql", params);
  }
}