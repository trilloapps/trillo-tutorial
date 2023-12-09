package com.collager.trillo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.collager.trillo.pojo.Result;
import org.json.JSONObject;

import static com.collager.trillo.util.Util.convertToResult;

public class CommandApi extends BaseApi {

  private static String commandEndpoint = "/api/v1.1/command";
  public static Result runOSCmd(List<String> argList) {
    Map body = new HashMap();
    body.put("argList1", argList);
    Object obj = HttpRequestUtil.post(commandEndpoint + "/runOSCmd", new JSONObject(body).toString());
    return convertToResult(obj);
  }
}
