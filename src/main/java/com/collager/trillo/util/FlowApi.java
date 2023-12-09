package com.collager.trillo.util;

import java.util.Map;
import com.collager.trillo.pojo.Result;

public class FlowApi extends BaseApi {

  public static Result createFlow(String flowName, Map<String, Object> params) {
    return remoteCallAsResult("FlowApi", "createFlow", flowName, params);
  }

}
