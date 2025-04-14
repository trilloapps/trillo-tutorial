/*
 * Copyright (c) 2020 Trillo Inc. All Rights Reserved THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO
 * INC. The copyright notice above does not evidence any actual or intended publication of such
 * source code.
 *
 */

package com.collager.trillo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.collager.trillo.model.CSVIterator;
import com.collager.trillo.pojo.Result;
import org.json.JSONObject;

import static com.collager.trillo.util.Util.convertToResult;

public class CSVApi extends BaseApi {

  private static String csvEndpoint = "/api/v1.1/csv";
  public static List<Map<String, Object>> csvGetAllRows(String filePath) {
    return (List<Map<String, Object>>) HttpRequestUtil.get(csvEndpoint + "/csvGetAllRows?filePath=" + filePath);
  }

  public static List<Map<String, Object>> csvGetAllRows(String filePath, String separator) {
    return (List<Map<String, Object>>) HttpRequestUtil.get(csvEndpoint + "/csvGetAllRows?filePath=" + filePath + "&separator=" + separator);
  }

  public static List<Map<String, Object>> csvGetAllRows(String filePath, String separator,
      String columnNames) {
    return (List<Map<String, Object>>) HttpRequestUtil.get(csvEndpoint + "/csvGetAllRows?filePath=" + filePath + "&separator="
        + separator + "&columnNames=" + columnNames);
  }

  public static List<Map<String, Object>> csvGetAllRows(String filePath, String separator,
      String columnNames, int columnNameLine) {
    return (List<Map<String, Object>>) HttpRequestUtil.get(csvEndpoint + "/csvGetAllRows?filePath=" + filePath + "&separator="
        + separator + "&columnNames=" + columnNames + "&columnNameLine=" +columnNameLine);

  }
  
  public static List<Map<String, Object>> csvGetPage(String fileName, char separatorChar, List<String> columnNames,
      int columnNameLine, String query, int startIndex, int pageSize) {
    return (List<Map<String, Object>>) HttpRequestUtil.get(csvEndpoint + "/csvGetPage?fileName=" + fileName + "&separatorChar="
            + separatorChar + "&columnNames=" + columnNames + "&columnNameLine=" + columnNameLine + "&query="
            + query + "&startIndex=" + startIndex + "&pageSize=" + pageSize);
  }

  public static Result csvWriteFile(String fileName, char separatorChar,
      List<String> columnNames, int columnNameLine, List<Map<String, Object>> rows) {
    Map<String, Object> body = new HashMap<>();
    body.put("fileName", fileName);
    body.put("separatorChar", separatorChar);
    body.put("columnNames", columnNames);
    body.put("columnNameLine", columnNameLine);
    body.put("rows", rows);
    Object obj= HttpRequestUtil.post(csvEndpoint + "/csvWriteFile", new JSONObject(body).toString());
    return convertToResult(obj);
  }
  
  public static CSVIterator getCSVIterator(String fileName, int startIndex,
      int pageSize) {
    return new CSVIterator(fileName, "", startIndex, pageSize);
  }

  public static CSVIterator getCSVIterator(String fileName, String query, int startIndex,
      int pageSize) {
    return new CSVIterator(fileName, query, startIndex, pageSize);
  }

  public static CSVIterator getCSVIterator(String fileName, char separatorChar, String query,
      int startIndex, int pageSize) {
    return new CSVIterator(fileName, separatorChar, query, startIndex, pageSize);
  };

  public static CSVIterator getCSVIterator(String fileName, List<String> columnNames, String query,
      int startIndex, int pageSize) {
    return new CSVIterator(fileName, columnNames, query, startIndex, pageSize);
  }


  public static CSVIterator getCSVIterator(String fileName, char separatorChar,
      List<String> columnNames, String query, int startIndex, int pageSize) {
    return new CSVIterator(fileName, separatorChar, columnNames, query, startIndex, pageSize);
  }

  public static CSVIterator getCSVIterator(String fileName, List<String> columnNames,
      int columnNameLine, String query, int startIndex, int pageSize) {
    return new CSVIterator(fileName, columnNames, columnNameLine, query, startIndex, pageSize);
  }

  public static CSVIterator getCSVIterator(String fileName, char separatorChar,
      List<String> columnNames, int columnNameLine, String query, int startIndex, int pageSize) {
    return new CSVIterator(fileName, separatorChar, columnNames, columnNameLine, query, startIndex,
        pageSize);
  }
}
