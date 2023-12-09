/*
 * Copyright (c) 2020 Trillo Inc. All Rights Reserved THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO
 * INC. The copyright notice above does not evidence any actual or intended publication of such
 * source code.
 *
 */

package com.collager.trillo.util;

import java.util.HashMap;
import java.util.Map;
import com.collager.trillo.pojo.Result;

public class OpenApi extends BaseApi {

  private static String completionsUrl = "https://api.openai.com/v1/completions";


  public static Result parseData(String resume, String prompt, String model, Double temperature, Integer maxTokens, String stop) {

    Map<String, String> headers = new HashMap<>();
    Map<String, Object> body = new HashMap<>();
    String token = (String) DSApi.valueByKey("openAIToken");;

    headers.put("Authorization", "Bearer " + token);
    body.put("model", model);
    body.put("prompt", prompt + "\n" + resume);
    body.put("temperature", temperature);
    body.put("max_tokens", maxTokens);
    body.put("stop", stop);

    return HttpApi.post(completionsUrl, body, headers);
  }


  public Result parseData(String resume, String prompt, String model, Double temperature, Integer maxTokens) {

    Map<String, String> headers = new HashMap<>();
    Map<String, Object> body = new HashMap<>();
    String token = (String) DSApi.valueByKey("openAIToken");;

    headers.put("Authorization", "Bearer " + token);
    body.put("model", model);
    body.put("prompt", prompt + "\n" + resume);
    body.put("temperature", temperature);
    body.put("max_tokens", maxTokens);
    body.put("stop", null);

    return HttpApi.post(completionsUrl, body, headers);
  }
}
