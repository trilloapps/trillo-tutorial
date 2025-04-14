/*
 * Copyright (c)  2020 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.util;

import com.collager.trillo.pojo.Result;

import java.util.Map;

public class OpenAIApi2 extends BaseApi {

  public static Result processPrompt(String text) {
    return remoteCallAsResult("OpenAIApi2", "processPrompt", text);
  }

  public static Result processPrompt(String llmModel, String text) {
    return remoteCallAsResult("OpenAIApi2", "processPrompt", llmModel, text);
  }
  
  public static Result processPrompt(String llmModel, String text, int nIteration) {
    return remoteCallAsResult("OpenAIApi2", "processPrompt", llmModel, text, nIteration);
  }

  public static Result processPromptResultAsJson(String text) {
    return remoteCallAsResult("OpenAIApi2", "processPromptResultAsJson", text);
  }

  public static Result processPromptResultAsJson(String llmModel, String text) {
    return remoteCallAsResult("OpenAIApi2", "processPromptResultAsJson", llmModel, text);
  }

  @SuppressWarnings("unchecked")
  public static Result processPromptResultAsJson(String llmModel, String text, int nIterations) {
    return remoteCallAsResult("OpenAIApi2", "processPromptResultAsJson", llmModel, text, nIterations);
  }

  public static Object chat(Map<String, Object> params) {
    return remoteCall("OpenAIApi2", "chat", params);
  }

  public static Object generateImage(Map<String, Object> params) {
    return remoteCall("OpenAIApi2", "generateImage", params);
  }

  public static Object multimodalChat(Map<String, Object> params) {
    return remoteCall("OpenAIApi2", "multimodalChat", params);
  }
  
  public static Object convertQuestionToSql(Map<String, Object> params) {
    // todo - server side implementation
    return remoteCall("OpenAIApi2", "convertQuestionToSql", params);
  }
}