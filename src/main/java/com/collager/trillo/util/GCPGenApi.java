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

import java.util.List;
import java.util.Map;

public class GCPGenApi extends BaseApi {

  public static Object chat(Map<String, Object> params) {
    return remoteCall("GCPGenApi", "chat", params);
  }

  public static Result summarizeText(String text) {
    return remoteCallAsResult("GCPGenApi", "summarizeText", text);
  }

  public static Result classifyText(List<String> inputClasses, String text) {
    return remoteCallAsResult("GCPGenApi", "classifyText", inputClasses, text);
  }

  public static Result ams(String text) {
    return remoteCallAsResult("GCPGenApi", "ams", text);
  }

  public static Result generateEmbedding(String text) {
    return remoteCallAsResult("GCPGenApi", "generateEmbedding", text);
  }

  public static Result generateImageInBucket(String prompt, Integer imageCount, String outputGcsUriFolder) {
    return remoteCallAsResult("GCPGenApi", "generateImageInBucket", prompt, imageCount, outputGcsUriFolder);
  }

  public static Result generateImageAsByte(String prompt, Integer imageCount) {
    return remoteCallAsResult("GCPGenApi", "generateImageAsByte", prompt, imageCount);
  }

  public static Result generateCode(String prompt) {
    return remoteCallAsResult("GCPGenApi", "generateCode", prompt);
  }

  public static Result text(String prompt) {
    return remoteCallAsResult("GCPGenApi", "text", prompt);
  }

  public static Result extractEntitiesFromTextList(String prompt, List<String> texts) {
    return remoteCallAsResult("GCPGenApi", "extractEntitiesFromTextList", prompt, texts);
  }

  public static Result extractEntitiesFromText(String prompt, String text) {
    return remoteCallAsResult("GCPGenApi", "extractEntitiesFromText", prompt, text);
  }

  public static Object vertexAiGetAnswer(String question, String datastoreId) {
    return remoteCall("GCPGenApi", "vertexAiGetAnswer", question, datastoreId);
  }
}