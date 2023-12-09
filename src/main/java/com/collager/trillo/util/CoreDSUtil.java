package com.collager.trillo.util;

public class CoreDSUtil {

  public static final String POSTGRESQL = "postgresql";
  public static final String MYSQL = "mysql";
  public static final String JDBC = "jdbc";
  public static final String MICROSOFTSQL = "microsoftsql";
  
  public enum AttrType {
    CHAR,
    STRING,
    TEXT,
    MEDIUMTEXT,
    LONGTEXT,
    LONGSTRING,
    VARCHAR,
    DECIMAL,
    NUMERIC,
    BOOLEAN,
    BYTE,
    SHORT,
    INTEGER,
    INT,
    LONG,
    BIGINTEGER,
    FLOAT,
    DOUBLE,
    BINARY,
    VARBINARY,
    LONGVARBINARY,
    DATE,
    DATETIME,
    TIME,
    TIMESTAMP,
    BLOB,
    MEDIUMBLOB,
    LONGBLOB,
    JSON,
    JSONSTR
  };
   
  public static String updateLimitClause(String query, int offset, int size, String dbType) {
    String updatedQueryString;
    if (CoreDSUtil.POSTGRESQL.equals(dbType)) {
      updatedQueryString = query + " offset " + offset + " limit " + ( offset + size);
    } if (CoreDSUtil.MICROSOFTSQL.equals(dbType)) {
      updatedQueryString = query + " offset " + offset + " rows fetch next " + size + " rows only";
    } else {
      updatedQueryString = query + " limit " + offset + "," + ( offset + size);
    }
    return updatedQueryString;
  }
  
  public static String asStr(AttrType t) {
    return t.toString().toLowerCase();
  }

}
