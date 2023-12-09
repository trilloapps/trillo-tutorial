package com.collager.trillo.model;

import java.util.List;
import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.BaseApi;
import com.collager.trillo.util.CoreDSUtil;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.Util;

public class DataIterator {
 
  private DataRequest dataRequest = null;
  private DataResult dataResult = null;
  private Object result = null;
  private int index = 0;
  private boolean iteratorEnded = false;
  private boolean noMorePage = false;
  private boolean orderById = false;
  private String originalQuery = null;
  private Object lastId;
  private String idAttrName = "id";
  private String dsType = CoreDSUtil.MYSQL;
  private boolean firstPage = true;
  private int nextStart = 1;
  private String countQuery = null;
  private int totalItems = -1;
  private boolean errored = false;
  private String message = null;
  
  public DataIterator(String className) {
    this(className, new Exp(), null, 1, 10);
  }
  
  public DataIterator(String className, int start, int pageSize) {
    this(className, new Exp(), null, start, pageSize);
  }
  
  public DataIterator(String className, String where, String orderBy) {
    this(className, where, orderBy, 1, 10);
  }
  
  public DataIterator(String className, String where, String orderBy, int start, int pageSize) {
    dataRequest = new DataRequest();
    dataRequest.setClassName(className);
    dataRequest.setWhere(where);
    dataRequest.setOrderBy(orderBy);
    dataRequest.setStart(start);
    dataRequest.setSize(pageSize);
    dataRequest.setUsingRowLimits(true);
    dataRequest.setAppName(BaseApi.app(className));
    dataRequest.setDsName(BaseApi.ds(className));
    dataRequest.setClassName(BaseApi.cls(className));
  }
  
  public DataIterator(String className, Exp filter, String orderBy) {
    this(className, filter, orderBy, 1, 10);
  }
  
  public DataIterator(String className, Exp filter, String orderBy, int start, int pageSize) {
    dataRequest = new DataRequest();
    dataRequest.setClassName(className);
    dataRequest.setFilter(filter);
    dataRequest.setOrderBy(orderBy);
    dataRequest.setStart(start);
    dataRequest.setSize(pageSize);
    dataRequest.setUsingRowLimits(true);
    dataRequest.setAppName(BaseApi.app(className));
    dataRequest.setDsName(BaseApi.ds(className));
    dataRequest.setClassName(BaseApi.cls(className));
  }
  
  public DataIterator(String className, String sqlTemplate) {
    this(className, sqlTemplate, 1, 10);
  }
  
  public DataIterator(String className, String sqlTemplate, int start, int pageSize) {
    dataRequest = new DataRequest();
    dataRequest.setSqlTemplate(sqlTemplate);
    dataRequest.setStart(start);
    dataRequest.setSize(pageSize);
    dataRequest.setUsingRowLimits(true);
  }
  
  public DataIterator(int start, int pageSize) {
    this(start, pageSize, "");
  }
  
  public DataIterator(int start, int pageSize, String sqlQuery) {
    // using sql query
    dataRequest = new DataRequest();
    dataRequest.setSql(sqlQuery);
    dataRequest.setStart(start);
    dataRequest.setSize(pageSize);
    dataRequest.setUsingRowLimits(true);
  }
  
  public DataIterator(String appName, String dsName, int start, int pageSize, String sqlQuery) {
    // using sql query
    dataRequest = new DataRequest();
    dataRequest.setAppName(appName);
    dataRequest.setDsName(dsName);
    dataRequest.setSql(sqlQuery);
    dataRequest.setStart(start);
    dataRequest.setSize(pageSize);
    dataRequest.setUsingRowLimits(true);
  }
  
  @Deprecated
  public DataIterator(int start, int pageSize, String sqlQuery, boolean orderById) {
    // using sql query
    this(start, pageSize, sqlQuery, "id", orderById);
  }
  
  @Deprecated
  public DataIterator(int start, int pageSize, String sqlQuery, String idAttrName, boolean orderById) {
    // providing idAttrName (when there is an ambiguity in sql, or it is not named as "id")
    dataRequest = new DataRequest();
    dataRequest.setSql(sqlQuery);
    dataRequest.setStart(start);
    dataRequest.setSize(pageSize);
    dataRequest.setUsingRowLimits(true);
    this.orderById = orderById;
    this.originalQuery = sqlQuery;
    this.idAttrName = idAttrName;
  }
  
  public String getAppName() {
    return dataRequest.getAppName();
  }

  public void setAppName(String appName) {
    dataRequest.setAppName(appName);
  }

  public String getDsName() {
    return dataRequest.getDsName();
  }

  public void setDsName(String dsName) {
    dataRequest.setDsName(dsName);
  }
  
  public String getDsType() {
    return dsType;
  }

  public void setDsType(String dsType) {
    this.dsType = dsType;
  }

  public DataRequest getDataRequest() {
    return dataRequest;
  }
  
  public DataResult getDataResult() {
    return dataResult;
  }

  public Object getResult() {
    return result;
  }
  
  public Object hasError() {
    return errored;
  }

  public Object getMessage() {
    return message;
  }
  
  public int getIndex() {
    return index;
  }

  public Result initialize() {
    if (iteratorEnded) {
      return Result.getFailedResult("DataIterator has ended");
    }
    if (dataResult == null) {
      fetch();
    }
    
    if (errored) {
      return Result.getFailedResult(message);
    }
    
    return Result.getSuccessResult();
  }
  
  @SuppressWarnings("unchecked")
  public Object getNext() {
    if (iteratorEnded) {
      return null;
    } else {
      if (dataResult == null) {
        fetch();
        if (iteratorEnded) {
          return null;
        } 
      }
      Object obj = dataResult.getItems().get(index);
      lastId = ((Map<String, Object>)obj).get(idAttrName);
      index++;
      if (index >= dataResult.getItems().size()) {
        nextStart = dataRequest.getStart() + dataResult.getItems().size();
        dataResult = null;
      }
      return obj;
    }
  }

  private void fetch() {
    if (noMorePage) {
      // if no more pages, end the iterator
      iteratorEnded = true;
      return;
    }
    if (firstPage) {
      firstPage = false;
      if (originalQuery != null && orderById) {
        dataRequest.setUsingRowLimits(false);
        dataRequest.setSql(CoreDSUtil.updateLimitClause(originalQuery + " order by " + idAttrName, 
            0,  dataRequest.getSize(), dsType));
      }
      dataResult = retrievePage();
      index = 0;
    } else {
      // all items of the current page have been iterated over
      // Get the next page
      if (originalQuery != null && orderById) {
        if (lastId != null) {
          dataRequest.setUsingRowLimits(false);
          String query = originalQuery.toLowerCase().indexOf(" where ") > 0 ? 
              originalQuery + " and (" + idAttrName + " > " + lastId + ")" :  
                originalQuery + " where " + idAttrName + " > " + lastId;
          dataRequest.setSql(CoreDSUtil.updateLimitClause(query + " order by " + idAttrName, 
              0,  dataRequest.getSize(), dsType));
        } else {
          dataRequest.setStart(dataRequest.getStart() + dataResult.getItems().size());
          dataRequest.setSql(originalQuery + " order by " + idAttrName);
        }
      } else {
        dataRequest.setStart(nextStart);
      }
      dataResult = retrievePage();
      index = 0;
    }
    if (dataResult == null || dataResult.getItems().size() == 0) {
      // if dataResult is null then it is an error and end the iteration.
      noMorePage = true;
      iteratorEnded = true;
    }
  }
  
  public List<Map<String, Object>> getPage() {
    if (dataResult != null) {
      List<Map<String, Object>> l = dataResult.getItemsAsListOfMaps();
      index = l.size();
      nextStart = dataRequest.getStart() + dataResult.getItems().size();
      dataResult = null; // so next time a new page is fetched
      return l;
    }
    if (noMorePage) {
      return null;
    }
    fetch();
    if (noMorePage) {
      return null;
    } else {
      List<Map<String, Object>> l = dataResult.getItemsAsListOfMaps();
      index = l.size();
      nextStart = dataRequest.getStart() + dataResult.getItems().size();
      dataResult = null; // so next time a new page is fetched
      return l;
    }
  }
  
  public boolean hasPage() {
    if (noMorePage) {
      return false;
    }
    if (dataResult == null) {
      fetch();
    }
    return !iteratorEnded;
  }
  
  public boolean hasNext() {
    if (iteratorEnded) {
      return false;
    }
    if (dataResult == null) {
      fetch();
    }
    return !iteratorEnded;
  }
  
  @SuppressWarnings("unchecked")
  private DataResult retrievePage() {
    //log().info("Sql: " + dataRequest.getSql());
    if (totalItems == -1) {
      dataRequest.setCountQuery(countQuery);
    }
    Object res = DSApi.getPage(dataRequest);
    // reset count query
    dataRequest.setCountQuery(null);
    Map<String, Object> m = null;
    if (res instanceof Result) {
      result = res;
      Result r = (Result) res;
      errored = r.isFailed();
      message = r.getMessage();
      LogApi.error("Failed to retrieve page, error: " + r.getMessage());
      return null;
    }
    if (res instanceof Map<?, ?>) {
      m = (Map<String, Object>) res;
    }
    if (m == null || (m.containsKey("_rtag") && "_r_".equals("" + m.get("_rtag")))) {
      if (m != null) {
        result = m;
        errored = "true".equals("" + m.get("failed"));
        message =  (String) m.get("message");
        LogApi.error("Failed to retrieve data, error message: " + ((Result)res).getMessage());
      }
      noMorePage = true;
      iteratorEnded = true;
      return null;
    }
    
    DataResult dataResult = Util.fromMap(m, DataResult.class);
    if (totalItems == -1) {
      totalItems = dataResult.getTotalItems();
    }
    //log().info("Number of entries: " + dataResult.getItems().size());
    if (dataResult.getItems().size() < dataRequest.getSize()) {
      noMorePage = true;
    }
    return dataResult;
  }

  public int getTotalItems() {
    if (totalItems != -1) {
      return totalItems;
    }
    if (iteratorEnded) {
      return totalItems;
    }
    if (dataResult == null) {
      fetch();
    }
    return totalItems;
  }

  public String getCountQuery() {
    return countQuery;
  }

  public void setCountQuery(String countQuery) {
    this.countQuery = countQuery;
  }
}