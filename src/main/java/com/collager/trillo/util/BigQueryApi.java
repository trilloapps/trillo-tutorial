package com.collager.trillo.util;

import com.collager.trillo.model.BigQueryIterator;
import com.collager.trillo.pojo.Result;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.collager.trillo.util.Util.convertToResult;

public class BigQueryApi extends BaseApi {

  private static String bigQueryEndpoint = "/api/v1.1/bq";
  public static Result createTable(String datasetName, String tableName, List<Map<String, Object>> classAttrs) {
    return remoteCallAsResult("BigQueryApi", "createTable", datasetName, tableName, classAttrs);
  }

  public static Result createTable(String datasetName, String tableName, String bucketName, String bucketFileName) {
    return remoteCallAsResult("BigQueryApi", "createTable", datasetName, tableName, bucketName, bucketFileName);
  }
  
  public static Result createTable(String datasetName, String tableName,
      List<Map<String, Object>> csvSchema, List<Map<String, Object>> classAttrs, List<Map<String, Object>> mappings) {
    return remoteCallAsResult("BigQueryApi", "createTable", datasetName, tableName, csvSchema, classAttrs, mappings);
  }
 
  public static Object getBQDataSets() {
    return HttpRequestUtil.get(bigQueryEndpoint + "/getBQDataSets");
  }

  public static Object getBQTables(String datasetName) {
    return HttpRequestUtil.get(bigQueryEndpoint + "/getBQTables?datasetName=" + datasetName);
  }

  public static Object getTable(String datasetAndTableName) {
    return HttpRequestUtil.get(bigQueryEndpoint + "/getTable?datasetAndTableName=" + datasetAndTableName);
  }
  
  public static Object getTable(String datasetName, String tableName) {
    return HttpRequestUtil.get(bigQueryEndpoint + "/getTable?datasetName=" + datasetName + "&tableName=" + tableName);
  }
  
  public static List<Object> getTableFields(String datasetAndTableName) {
    return (List<Object>) HttpRequestUtil.get(bigQueryEndpoint + "/getTableFields?datasetAndTableName=" + datasetAndTableName);
  }
  
  public static List<Object> getTableFields(String datasetName, String tableName) {
    return (List<Object>) HttpRequestUtil.get(bigQueryEndpoint + "/getTableFields?datasetName=" + datasetName + "&tableName=" + tableName);
  }
  
  public static Result executeQuery(String queryString) {
    return remoteCallAsResult("BigQueryApi", "executeQuery", queryString);
  }
  
  /**
   *
   * data iterator for application and data source
   *
   * @param query
   * @param startIndex
   * @param pageSize
   * Example in Trillo Function:
   *    Object iter = getBigQueryIterator(query, 0, 100);
   *    Object page;
   *    while (iter.hasNext()) {
   *      page = iter.getPage();
   *    }
   * @return object
   */
  public static BigQueryIterator getBigQueryIterator(String query, int startIndex, int pageSize) {
    return new BigQueryIterator(query, startIndex, pageSize);
  }
  
  public static Result insertRows(
      String datasetName, String tableName, List<Map<String, Object>> list) {
    return remoteCallAsResult("BigQueryApi", "insertRows", datasetName, tableName, list);
  }
  
  public static Result importCSVIntoTable(String datasetName, String tableName, String pathToFile, 
      List<Map<String, Object>> csvSchema, List<Map<String, Object>> classAttrs, List<Map<String, Object>> mappings, int numberOfRowsToSkip) {
    Map body = new HashMap();
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("pathToFile", pathToFile);
    body.put("csvSchema", csvSchema);
    body.put("classAttrs", classAttrs);
    body.put("mappings", mappings);
    body.put("numberOfRowsToSkip", numberOfRowsToSkip);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/importCSVIntoTable", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result exportTableToCSV(
      String datasetName,
      String tableName,
      String pathToFile) {
    Map body = new HashMap();
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("pathToFile", pathToFile);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/exportTableToCSV", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result exportTableToCSV(
      String datasetName,
      String tableName,
      String path, String fileName) {
    Map body = new HashMap();
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("path", path);
    body.put("fileName", fileName);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/exportTableToCSV", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result exportTableToCSVByURI(
      String datasetName,
      String tableName,
      String destinationUri) {
    Map body = new HashMap();
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("destinationUri", destinationUri);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/exportTableToCSVByURI", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static Result exportTable(
      String datasetName,
      String tableName,
      String destinationUri,
      String dataFormat) {
    Map body = new HashMap();
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("destinationUri", destinationUri);
    body.put("dataFormat", dataFormat);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/exportTable", new JSONObject(body).toString());
    return convertToResult(res);
  }
  
  public static int bigQueryToCSV(String filePath, String[] columnNames, String datasetName, String tableName,
      String query) {
    Map body = new HashMap();
    body.put("filePath", filePath);
    body.put("columnNames", columnNames);
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("query", query);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/bigQueryToCSV", new JSONObject(body).toString());
    if (res instanceof Result) {
      return -1;
    }
    try {
      int n = Integer.parseInt("" + res);
      return n;
    } catch(Exception exc) {
      return -1;
    }
  }
  
  public static int bigQueryToCSV(String filePath, String[] columnNames, String datasetName, String tableName,
        String query, String functionName) {
    Map body = new HashMap();
    body.put("filePath", filePath);
    body.put("columnNames", columnNames);
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("query", query);
    body.put("functionName", functionName);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/bigQueryToCSV", new JSONObject(body).toString());
    if (res instanceof Result) {
      return -1;
    }
    try {
      int n = Integer.parseInt("" + res);
      return n;
    } catch(Exception exc) {
      return -1;
    }
  }
  
  public static List<Object> getPage(String query, long start, long size) {
    return remoteCallAsList("BigQueryApi", "getPage", query, start, size);
  }

  public static Result importJSONbyURIIntoTable(String datasetName, String tableName,
                                                String sourceUri) {
    Map body = new HashMap();
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("sourceUri", sourceUri);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/importJSONbyURIIntoTable", new JSONObject(body).toString());
    if (res instanceof Map) {
      return Util.convertToResult(res);
    }
    return Result.getFailedResult("Failed to load data.");

  }

  public static Result importJSONbyURIIntoTable(String datasetName, String tableName,
                                                String sourceUri, List<Map<String, Object>> schema) {
    Map body = new HashMap();
    body.put("datasetName", datasetName);
    body.put("tableName", tableName);
    body.put("sourceUri", sourceUri);
    body.put("schema", schema);
    Object res = HttpRequestUtil.post(bigQueryEndpoint + "/importJSONbyURIIntoTable", new JSONObject(body).toString());
    if (res instanceof Map) {
      return Util.convertToResult(res);
    }
    return Result.getFailedResult("Failed to load data.");

  }

  public static Object bigQueryToCSVWithScript(String filePath, String[] columnNames,
                                            String dataSetName, String bqTableName, String query, String script, String scriptFlavor) {
    return remoteCall("BigQueryApi", "bigQueryToCSVWithScript",
      filePath, columnNames, dataSetName, bqTableName, query, script, scriptFlavor);
  }

  public static Result createTableFromCSV(String datasetName, String tableName, String bucketName, String bucketFileName) {
    return remoteCallAsResult("BigQueryApi", "createTableFromCSV",
      datasetName, tableName, bucketName, bucketFileName);
  }

  public static Result createTableFromCSV(String datasetName, String tableName, String bucketFileName) {
    return remoteCallAsResult("BigQueryApi", "createTableFromCSV",
      datasetName, tableName, bucketFileName);
  }



}
