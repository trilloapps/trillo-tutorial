package com.collager.trillo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.CSVApi;
import com.collager.trillo.util.CSVConst;

public class CSVIterator {
  
  private boolean iteratorEnded = false;
  private int startIndex;
  private int pageSize;
  private String query = "";
  private String fileName;
  private char separatorChar;
  private List<String> columnNames;
  private int columnNameLine;
  
  public CSVIterator(String fileName, String query, int startIndex, int pageSize) {
    this(fileName, CSVConst.DEFAULT_SEPARATOR_CHAR, new ArrayList<String>(), CSVConst.DEFAULT_COLUMN_NAME_LINE, query, startIndex, pageSize);
  }
  
  public CSVIterator(String fileName, List<String> columnNames, String query, int startIndex, int pageSize) {
    this(fileName, CSVConst.DEFAULT_SEPARATOR_CHAR, columnNames, CSVConst.DEFAULT_COLUMN_NAME_LINE, query, startIndex, pageSize);
  }

  public CSVIterator(String fileName, List<String> columnNames, int columnNameLine, String query,  int startIndex, int pageSize) {
    this(fileName, CSVConst.DEFAULT_SEPARATOR_CHAR, columnNames, columnNameLine, query, startIndex, pageSize);
  }

  public CSVIterator(String fileName, char separatorChar, String query, int startIndex, int pageSize) {
    this(fileName, separatorChar, null, CSVConst.DEFAULT_COLUMN_NAME_LINE, query, startIndex, pageSize);
  }
  
  public CSVIterator(String fileName, char separatorChar, List<String> columnNames, String query, int startIndex, int pageSize) {
    this(fileName, separatorChar, columnNames, CSVConst.DEFAULT_COLUMN_NAME_LINE, query, startIndex, pageSize);
  }

  public CSVIterator(String fileName, char separatorChar, List<String> columnNames,
      int columnNameLine, String query, int startIndex, int pageSize) {
    this.fileName = fileName;
    this.separatorChar = separatorChar;
    this.columnNames = columnNames;
    this.columnNameLine = columnNameLine;
    this.startIndex = startIndex;
    this.pageSize = pageSize;
    this.query = query;
  }

  public Object getPage() {
    if (iteratorEnded) {
      return Result.getFailedResult("Iterator reached the end of the result list");
    }
    List<Map<String, Object>> l = CSVApi.csvGetPage(fileName, separatorChar, 
        columnNames, columnNameLine, query, startIndex, pageSize);
    startIndex += l.size();
    if (l.size() < pageSize) {
      iteratorEnded = true;
    }
    return l;
  }
  
  public boolean hasNextPage() {
    return !iteratorEnded;
  }
  
  public void close() {
    iteratorEnded = true;
    // no need to close, this is a part of the remote client.
    // The actual processing is done on the server.
    // The remote server always closes the iterator before returning.
    // It creates a new iterator for each call. It is inefficient but OK for the development
    // purpose.
  }
}
