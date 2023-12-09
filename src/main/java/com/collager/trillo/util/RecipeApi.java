package com.collager.trillo.util;

import java.util.Map;
import com.collager.trillo.pojo.Result;

public class RecipeApi extends BaseApi {
  
  public static Result execute(String recipeName, String methodName, Map<String, Object> params) {
    return remoteCallAsResult("RecipeApi", "execute", recipeName, methodName, params);
  }

  public static Result execute(String appName, String recipeName, String methodName, 
      Map<String, Object> params) {
    return remoteCallAsResult("RecipeApi", "execute", appName, recipeName, methodName, params);
  }

}
