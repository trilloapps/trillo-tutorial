package com.collager.trillo.util;

import java.util.Map;
import com.collager.trillo.pojo.DocWorkflow.Activity;
import com.collager.trillo.pojo.Result;

abstract public class ActivityFunction extends ServerlessFunction {
  public Result process(Map<String, Object> params) {
    if (params.get("activity") instanceof Activity) {
      return executeFunctionForActivity((Activity) params.get("activity"));
    } else {
      return Result.getFailedResult("Invalid parameter");
    }
  }
  abstract public Result executeFunctionForActivity(Activity a);
}
