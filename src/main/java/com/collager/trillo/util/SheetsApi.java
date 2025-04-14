/*
 * Copyright (c) 2020 Trillo Inc. All Rights Reserved THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO
 * INC. The copyright notice above does not evidence any actual or intended publication of such
 * source code.
 *
 */

package com.collager.trillo.util;

import com.collager.trillo.pojo.Result;

import java.util.ArrayList;

public class SheetsApi extends BaseApi {

  public static Result createSheet(String sheetName) {
    return remoteCallAsResult("SheetsApi", "createSheet", sheetName);
  }

  public static Result getSheetById(String sheetId) {
    return remoteCallAsResult("SheetsApi", "getSheetById", sheetId);
  }

  public static Result getWorksheets(String sheetId) {
    return remoteCallAsResult("SheetsApi", "getWorksheets", sheetId);
  }

  public static Result getWorksheet(String sheetId, String worksheetName) {
    return remoteCallAsResult("SheetsApi", "getWorksheet", sheetId, worksheetName);
  }

  public static Result createWorksheet(String sheetId, String worksheetName) {
    return remoteCallAsResult("SheetsApi", "createWorksheet", sheetId, worksheetName);
  }

  public static Result copyWorksheetUsingTemplate(String sheetId, String sourceWorksheetId, String targetWorksheetName) {
    return remoteCallAsResult("SheetsApi", "copyWorksheetUsingTemplate", sheetId, sourceWorksheetId, targetWorksheetName);
  }

  public static Result writeDataToWorksheet(String sheetId, String worksheetName, String startColumn, String endColumn, ArrayList<ArrayList<Object>> values) {
    return remoteCallAsResult("SheetsApi", "writeDataToWorksheet", sheetId, worksheetName, startColumn, endColumn, values);

  }

  public static Result insertBlankRowsToWorksheet(String sheetId, String worksheetId, Integer startRowIndex, Integer endRowIndex) {
    return remoteCallAsResult("SheetsApi", "insertBlankRowsToWorksheet", sheetId, worksheetId, startRowIndex, endRowIndex);
  }


}
