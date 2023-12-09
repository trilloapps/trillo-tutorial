package com.collager.trillo.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.collager.trillo.model.ClassM;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.collager.trillo.util.Util.convertToResult;

public class MDApi extends BaseApi {

  private static String mdEndpoint = "/api/v1.1/md";
  public static Object createMetadata(Map<String, Object> md) {
    Object obj= HttpRequestUtil.post(mdEndpoint + "/createMetadata", new JSONObject(md).toString());
    return convertToResult(obj);
  }
  
  public static Object saveMetadata(String appName, String dsName, ClassM clsM) {
    return saveMetadata(appName, dsName, null, clsM);
  }
  
  public static Object saveMetadata(String appName, String dsName, String schemaName, ClassM clsM) {
    
    Map<String, Object> md = new LinkedHashMap<String, Object>();
    md.put("name", clsM.getName());
    md.put("modelClassName", "ClassM");
    md.put("folder", appName + "/dataSource/" + dsName + (StringUtils.isNotBlank(schemaName) ?
        ("/" + schemaName) : ""));
    md.put("appName", appName);
    String str = Util.asJSONString(clsM);
    md.put("content", Util.fromJSONStringAsMap(str));
    
    return saveMetadata(md);
  }
  
  public static Object saveMetadata(Map<String, Object> md) {
    Object obj= HttpRequestUtil.post(mdEndpoint + "/saveMetadata", new JSONObject(md).toString());
    return convertToResult(obj);
  }
  
  public static Object getMetadata(String id) {
    return HttpRequestUtil.get(mdEndpoint + "/getMetadata?id=" + id);
  }
  
  public static Object getMetadata(String modelClassName, String folder, String name) {
    return HttpRequestUtil.get(mdEndpoint + "/getMetadata?modelClassName=" + modelClassName + "&folder=" + folder + "&name=" + name);
  }
}
