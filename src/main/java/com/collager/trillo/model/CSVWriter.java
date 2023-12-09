package com.collager.trillo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.CSVApi;
import com.collager.trillo.util.CSVConst;

public class CSVWriter {
  
  private static Logger log = LoggerFactory.getLogger(CSVWriter.class);
  
  private String fileName; 
  private char separatorChar; 
  private List<String> columnNames;
  private int columnNameLine;
  
  List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
  
  
  public CSVWriter(String fileName, List<String> columnNames) {
    this(fileName, CSVConst.DEFAULT_SEPARATOR_CHAR, columnNames, CSVConst.DEFAULT_COLUMN_NAME_LINE);
  }

  public CSVWriter(String fileName, char separatorChar, List<String> columnNames) {
    this(fileName, separatorChar, columnNames, CSVConst.DEFAULT_COLUMN_NAME_LINE);
  }
  
  public CSVWriter(String fileName, char separatorChar, 
      List<String> columnNames, int columnNameLine) {
    this.fileName = fileName;
    this.separatorChar = separatorChar;
    this.columnNames = columnNames;
    this.columnNameLine = columnNameLine;
  }
  
  public void addRow(Map<String, Object> m) {
    rows.add(m);
  }
  
  public void addRows(List<Map<String, Object>> l) {
    rows.addAll(l);
  }

  public void close () {
    Result result = CSVApi.csvWriteFile(fileName, separatorChar, columnNames, columnNameLine, rows);
    if (result.isFailed()) {
      log.error("Failed to write the csv file, error: " + result.getMessage());
    } else {
      log.info("Successfully written CSV file");
    }
  }

}
