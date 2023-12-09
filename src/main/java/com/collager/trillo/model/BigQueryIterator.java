package com.collager.trillo.model;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.BigQueryApi;
import com.collager.trillo.util.Loggable;
import com.collager.trillo.util.Util;

public class BigQueryIterator implements Loggable {
  @SuppressWarnings("unused")
  private String serviceName;
  private String query;
  private long start;
  private long size;
  private boolean iteratorEnded = false;
  private boolean noMorePage = false;
  private Object result = null;
  private Iterator<Object> listIterator = null;
  private List<Object> page;
 
  public BigQueryIterator(String serviceName) {
    this(serviceName, "");
  }

  public BigQueryIterator(String serviceName, String query) {
    this(serviceName, query, 1, 10);
  }

  public BigQueryIterator(String query, long start, long size) {
    this(null, query, start, size);
  }

  public BigQueryIterator(String serviceName, String query, long start, long size) {
    this.serviceName =
        StringUtils.isBlank(serviceName) ? Util.GCP_SSERVICE_NAME : serviceName;
    this.query = query;
    this.start = start;
    this.size = size;
  }
  
  private void retrievePage() {
    
    try {
      page = getBQPage();
      listIterator = null; // set iterator to null so it can be initialized from the page
    } catch (Exception exc) {
      page = null;
      result = Result.getFailedResult(
          "Failed to query page, error: " + exc.getMessage());
    }
  }

  public Object getPage() {
    if (noMorePage) {
      return null;
    }
    
    retrievePage();
    
    if (page == null) {
      noMorePage = true;
      iteratorEnded = true;
      return result;
    }
    
    if (page.size() < size) {
      noMorePage = true;
    }

    return page;
  }
  
  private void fetch() {
    if (noMorePage) {
      // if no more pages, end the iterator
      iteratorEnded = true;
      return;
    }
    retrievePage();
    if (page == null) {
      noMorePage = true;
      iteratorEnded = true;
      return;
    }
    listIterator = page.iterator();
    if (!listIterator.hasNext()) {
      noMorePage = true;
      iteratorEnded = true;
    }
  }
  
  public Object getNext() {
    if (iteratorEnded) {
      return null;
    } else {
      if (listIterator == null || !listIterator.hasNext()) {
        fetch();
        if (iteratorEnded) {
          return null;
        }
      } 
      return listIterator.next();
    }
  }

  public boolean hasNext() {
    if (listIterator == null) {
      fetch();
    }
    return !iteratorEnded;
  }

  public boolean hasNextPage() {
    if (listIterator == null) {
      fetch();
    }
    return !iteratorEnded;
  }
  
  public Object getResult() {
    return result;
  }
  
  private List<Object> getBQPage() {
    return BigQueryApi.getPage( query, start, size);
  }
}
