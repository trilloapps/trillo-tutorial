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

public class ClaudeAIApi extends BaseApi {

  public static Result processPrompt(String text) {
    return remoteCallAsResult("ClaudeAIApi", "processPrompt", text);
  }

  public static Result processPrompt(String llmModel, String text) {
    return remoteCallAsResult("ClaudeAIApi", "processPrompt", llmModel, text);
  }
  
  public static Result processPrompt(String llmModel, String text, int nIteration) {
    return remoteCallAsResult("ClaudeAIApi", "processPrompt", llmModel, text, nIteration);
  }

  public static Result processPromptResultAsJson(String text) {
    return remoteCallAsResult("ClaudeAIApi", "processPromptResultAsJson", text);
  }

  public static Result processPromptResultAsJson(String llmModel, String text) {
    return remoteCallAsResult("ClaudeAIApi", "processPromptResultAsJson", llmModel, text);
  }

  @SuppressWarnings("unchecked")
  public static Result processPromptResultAsJson(String llmModel, String text, int nIterations) {
    return remoteCallAsResult("ClaudeAIApi", "processPromptResultAsJson", llmModel, text, nIterations);
  }
  
}