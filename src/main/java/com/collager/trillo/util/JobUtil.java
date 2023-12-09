package com.collager.trillo.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.OCR.PageCollection;
import com.collager.trillo.util.OCR.Value;

public class JobUtil {
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> getImageRawData(String jobId, String imageId, String type) {
    Object r = DSApi.queryOne("JobRawData", "jobId = " + jobId + " and " + 
        "imageId = " + imageId + " and " + "type = '" + type + "'");
    Map<String, Object> map;
    if (r instanceof Map<?, ?>) {
      return (Map<String, Object>) r;
    } else {
      map = new HashMap<String, Object>();
      map.put("jobId", jobId);
      map.put("imageId", imageId);
      map.put("type", type);
    }
    return map;
  }
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> getFileRawData(String jobId, String fileId, String type) {
    Object r = DSApi.queryOne("JobRawData", "jobId = " + jobId + " and " + 
        "fileId = " + fileId + " and " + "type = '" + type + "'");
    Map<String, Object> map;
    if (r instanceof Map<?, ?>) {
      return (Map<String, Object>) r;
    } else {
      map = new HashMap<String, Object>();
      map.put("jobId", jobId);
      map.put("fileId", fileId);
      map.put("type", type);
    }
    return map;
  }
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> getImageResult(String jobId, String imageId, String type) {
    Object r = DSApi.queryOne("JobResult", "jobId = " + jobId + " and " + 
        "imageId = " + imageId + " and " + "type = '" + type + "'");
    Map<String, Object> map;
    if (r instanceof Map<?, ?>) {
      return (Map<String, Object>) r;
    } else {
      map = new HashMap<String, Object>();
      map.put("jobId", jobId);
      map.put("imageId", imageId);
      map.put("type", type);
    }
    return map;
  }
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> getFileResult(String jobId, String fileId, String type) {
    Object r = DSApi.queryOne("JobResult", "jobId = " + jobId + " and " + 
        "fileId = " + fileId + " and " + "type = '" + type + "'");
    Map<String, Object> map;
    if (r instanceof Map<?, ?>) {
      return (Map<String, Object>) r;
    } else {
      map = new HashMap<String, Object>();
      map.put("jobId", jobId);
      map.put("fileId", fileId);
      map.put("type", type);
    }
    return map;
  }
  
  public static Result saveImageRawData(String jobId, String imageId, String type, Object raw) {
    Map<String, Object> map = getImageRawData(jobId, imageId, type);
    String rawStr = raw instanceof String ? (String) raw : Util.asJSONPrettyString(raw);
    map.put("raw", rawStr);
    Object r =  DSApi.save("JobRawData", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveFileRawData(String jobId, String fileId, String type, Object raw) {
    Map<String, Object> map = getFileRawData(jobId, fileId, type);
    String rawStr = raw instanceof String ? (String) raw : Util.asJSONPrettyString(raw);
    map.put("raw", rawStr);
    Object r =  DSApi.save("JobRawData", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveImageProcessedPages(String jobId, String imageId, String type, List<Map<String, Object>> processedPages) {
    Map<String, Object> map = getImageResult(jobId, imageId, type);
    updateProcessedPagesPages(map, processedPages);
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveFileProcessedPages(String jobId, String fileId, String type, List<Map<String, Object>> processedPages) {
    Map<String, Object> map = getFileResult(jobId, fileId, type);
    updateProcessedPagesPages(map, processedPages);
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveImageData(String jobId, String imageId, String type, List<Map<String, Object>> data) {
    Map<String, Object> map = getImageResult(jobId, imageId, type);
    updateData(map, data);
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveFileData(String jobId, String fileId, String type, List<Map<String, Object>> data) {
    Map<String, Object> map = getFileResult(jobId, fileId, type);
    updateData(map, data);
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveImageData(String jobId, String imageId, String type, Map<String, Object> data) {
    Map<String, Object> map = getImageResult(jobId, imageId, type);
    map.put("value", Util.asJSONPrettyString(data));
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveFileData(String jobId, String fileId, String type, Map<String, Object> data) {
    Map<String, Object> map = getFileResult(jobId, fileId, type);
    map.put("value", Util.asJSONPrettyString(data));
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveImageTemplate(String jobId, String imageId, String type, String templateName) {
    Map<String, Object> map = getImageResult(jobId, imageId, type);
    updateTemplateName(map, templateName);
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  public static Result saveFileTemplate(String jobId, String fileId, String type, String templateName) {
    Map<String, Object> map = getFileResult(jobId, fileId, type);
    updateTemplateName(map, templateName);
    Object r =  DSApi.save("JobResult", map);
    if (r instanceof Result) {
      return (Result) r;
    } else {
      return Result.getSuccessResultWithData(r);
    }
  }
  
  private static void updateProcessedPagesPages(Map<String, Object> map,
      List<Map<String, Object>> processedPages) {
    Map<String, Object> valueMap = null;
    if (map.containsKey("value")) {
      try {
          valueMap = Util.fromJSONStringAsMap("" + map.get("value"));
      } catch(Exception exc) {
        
      }
    }
    if (valueMap == null) {
      valueMap = new LinkedHashMap<String, Object>();
    }
    valueMap.put("processedPages", processedPages);
    map.put("value", Util.asJSONPrettyString(valueMap));
    
  }
  
  private static void updateData(Map<String, Object> map,
      List<Map<String, Object>> data) {
    Map<String, Object> valueMap = null;
    if (map.containsKey("value")) {
      try {
          valueMap = Util.fromJSONStringAsMap("" + map.get("value"));
      } catch(Exception exc) {
        
      }
    }
    if (valueMap == null) {
      valueMap = new LinkedHashMap<String, Object>();
    }
    valueMap.put("data", data);
    map.put("value", Util.asJSONPrettyString(valueMap));
    
  }
  
  private static void updateTemplateName(Map<String, Object> map,
      String templateName) {
    Map<String, Object> valueMap = null;
    if (map.containsKey("value")) {
      try {
          valueMap = Util.fromJSONStringAsMap("" + map.get("value"));
      } catch(Exception exc) {
        
      }
    }
    if (valueMap == null) {
      valueMap = new LinkedHashMap<String, Object>();
    }
    valueMap.put("templateName", templateName);
    map.put("value", Util.asJSONPrettyString(valueMap));
    
  }

  public static Map<String, Object> getRawDataMap(Map<String, Object> jobResult) {
    Map<String, Object> rowData = null;
    if (jobResult.containsKey("raw")) {
      try {
          rowData = Util.fromJSONStringAsMap("" + jobResult.get("raw"));
      } catch(Exception exc) {
        rowData = new LinkedHashMap<String, Object>();
        rowData.put("error", "Failed to parse raw data, error: " + exc.getMessage());
      }
    }
    if (rowData == null) {
      rowData = new LinkedHashMap<String, Object>();
      rowData.put("error", "Missing raw data");
    }
    return rowData;
  }
  
  public static Value getValue(Map<String, Object> jobResult) {
    Value value = null;
    if (jobResult.containsKey("value")) {
      value = getValue("" + jobResult.get("value"));
    }
    if (value == null) {
      value = new Value();
      value.error = "Missing value map";
    }
    return value;
  }
  
  public static Value getValue(String valueStr) {
    Value value = null;
    try {
      value = Util.fromJSONString(valueStr, Value.class);
    } catch(Exception exc) {
      value = new Value();
      value.error = "Failed to parse value , error: " + exc.getMessage();
    }
    return value;
  }
  
  
  public static PageCollection getPages(String processedPages) {
    PageCollection pageCollection;
    try {
      pageCollection = Util.fromJSONString(processedPages, PageCollection.class);
    } catch(Exception exc) {
      pageCollection = new PageCollection();
      pageCollection.error = "Failed to parse page collection, error: " + exc.getMessage();
    }
    return pageCollection;
  }


}
