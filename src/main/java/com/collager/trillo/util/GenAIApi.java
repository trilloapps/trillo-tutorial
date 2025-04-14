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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenAIApi extends BaseApi {

  public static Result summarizeText(String text) {
    return remoteCallAsResult("GenAIApi", "summarizeText", text);
  }
  
  public static Result summarizeText(String llmModel, String text) {
    return remoteCallAsResult("GenAIApi", "summarizeText", llmModel, text);
  }
  
  public static Result summarizeText(String llmModel, String text, double temperature, int maxOutputTokens, int topK,
      double topP) {
    return remoteCallAsResult("GenAIApi", "summarizeText", llmModel, text, maxOutputTokens, topK, topP);
  }

  public static Result generateEmbedding(String text) {
    return remoteCallAsResult("GenAIApi", "generateEmbedding", text);
  }
  
  public static Result generateEmbedding(String llmModel, String text) {
    return remoteCallAsResult("GenAIApi", "generateEmbedding", llmModel, text);
  }
  
  public static Result text(String prompt) {
    return remoteCallAsResult("GenAIApi", "text", prompt);
  }
  
  public static Result text(String llmModel, String prompt) {
    return remoteCallAsResult("GenAIApi", "text", llmModel, prompt);
  }
  
  public static Result text2(String prompt) {
    return remoteCallAsResult("GenAIApi", "text2", prompt);
  }
  
  public static Result classifyText(List<String> inputClasses, String text) {
    return remoteCallAsResult("GenAIApi", "classifyText", inputClasses, text);
  }
  
  public static Result classifyText2(List<String> inputClasses, String text) {
    return remoteCallAsResult("GenAIApi", "classifyText2", inputClasses, text);
  }
  
  public static Result ams(String text) {
    return remoteCallAsResult("GenAIApi", "ams", text);
  }

  public static Result generateImageInBucket(String prompt, int imageCount, String outputGcsUriFolder) {
    return remoteCallAsResult("GenAIApi", "generateImageInBucket", prompt, imageCount, outputGcsUriFolder);
  }

  public static Result generateImageInBucket(String llmModel, String prompt, int imageCount, String outputGcsUriFolder) {
    return remoteCallAsResult("GenAIApi", "generateImageInBucket", llmModel, prompt, imageCount, outputGcsUriFolder);
  }
  
  public static Result generateImageAsByte(String prompt, int imageCount) {
    return remoteCallAsResult("GenAIApi", "generateImageAsByte", prompt, imageCount);
  }
  
  public static Result generateImageAsByte(String llmModel, String prompt, Integer imageCount) {
    return remoteCallAsResult("GenAIApi", "generateImageAsByte", llmModel, prompt, imageCount);
  }
  
  public static Object vertexAiGetAnswer(String question, String datastoreId) {
    return remoteCallAsResult("GenAIApi", "vertexAiGetAnswer", question, datastoreId);
  }

  public static Object vertexAiGetAnswer(String question, String datastoreId, int pageSize, String modelVersion) {
    return remoteCallAsResult("GenAIApi", "vertexAiGetAnswer", question, datastoreId, pageSize, modelVersion);
  }

  public static Result chat(String content, String author, String context) {
    return remoteCallAsResult("GenAIApi", "chat", content, author, context);
  }
  
  public static Result chat(Map<String, Object> message) {
    return remoteCallAsResult("GenAIApi", "chat", message);
  }

  public static Result chat(List<Map<String, Object>> messages) {
    return remoteCallAsResult("GenAIApi", "chat", messages);
  }
  
  public static Result chat(String llmModel, List<Map<String, Object>> messages) {
    return remoteCallAsResult("GenAIApi", "chat", llmModel, messages);
  }
  
  public static Result generateCode(String prompt) {
    return remoteCallAsResult("GenAIApi", "generateCode", prompt);
  }
  
  public static Result generateCode(String llmModel, String prompt) {
    return remoteCallAsResult("GenAIApi", "generateCode", llmModel, prompt);
  }

  public static Result extractEntity(String text) {
    return remoteCallAsResult("GenAIApi", "extractEntity", text);
  }
  
  public static Result extractEntity(String llmModel, String text) {
    return remoteCallAsResult("GenAIApi", "extractEntity", llmModel, text);
  }
  
  public static Result extractEntity(String llmModel, String text, String llmOperation) {
    return remoteCallAsResult("GenAIApi", "extractEntity", llmModel, text, llmOperation);
  }
  
  public static Result processPrompt(String prompt) {
    return remoteCallAsResult("GenAIApi", "processPrompt", prompt);
  }
  
  public static Result processPrompt(String llmModel, String prompt) {
    return remoteCallAsResult("GenAIApi", "processPrompt", llmModel, prompt);
  }
  
  public static Result processPrompt(String llmModel, String prompt, String llmOperation) {
    return remoteCallAsResult("GenAIApi", "processPrompt", llmModel, prompt, llmOperation);
  }
  
  
  public static Object rerankDocuments(String query, List<Map<String, Object>> records) {
    return remoteCallAsResult("GenAIApi", "rerankDocuments", query, records);
  }
  
  public static Result processMultimodalPrompt(ArrayList<Map<String, Object>> multimodalPrompt) {
    return remoteCallAsResult("GenAIApi", "processMultimodalPrompt", multimodalPrompt);
  }
  
  public static Result processMultimodalPrompt(String llmModel, ArrayList<Map<String, Object>> multimodalPrompt) {
    return remoteCallAsResult("GenAIApi", "processMultimodalPrompt", llmModel, multimodalPrompt);
  }

  public static Result processMultimodalPromptAsJSON(String llmModel, ArrayList<Map<String, Object>> multimodalPrompt) {
    return remoteCallAsResult("GenAIApi", "processMultimodalPromptAsJSON", llmModel, multimodalPrompt);
  }

  public static Result extractEntitiesFromTextList(String prompt, List<String> texts) {
    return remoteCallAsResult("GenAIApi", "extractEntitiesFromTextList", prompt, texts);
  }

  public static Result extractEntitiesFromText(String prompt, String text) {
    return remoteCallAsResult("GenAIApi", "extractEntitiesFromText", prompt, text);
  }
  
  public static Result processPromptResultAsJson(String llmModel, String prompt, int nIterations) {
    return remoteCallAsResult("GenAIApi", "processPromptResultAsJson", llmModel, prompt, nIterations);
  }

  public static int getMaxInputTokens(String model) {
    Object v = remoteCall("GenAIApi", "getMaxInputTokens", model);
    if (v instanceof Integer) {
      return (Integer) v;
    } else {
      return 1000000;
    }
  }
}