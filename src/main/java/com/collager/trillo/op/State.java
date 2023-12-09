package com.collager.trillo.op;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.collager.trillo.util.ListX;
import com.collager.trillo.util.MapX;

public class State extends MapX {
  
  private static final long serialVersionUID = 1L;
  
  private Map<String, BucketOp> bucketOps = new LinkedHashMap<String, BucketOp>();
  private Map<String, DbOp> dbOps = new LinkedHashMap<String, DbOp>();
  private Map<String, BQOp> bqOps = new LinkedHashMap<String, BQOp>();
  private Map<String, RestOp> restOps = new LinkedHashMap<String, RestOp>();
  
  ListX errors = new ListX();

  private OpManager opManager = null;
  
  private int successCount = 0;
  private int failureCount = 0;
  private State enclosingState;
 
  public State(String name, State enclosingState) {
    this.name = name;
    this.enclosingState = enclosingState;
  }
  
  public State(State enclosingState) {
    this.enclosingState = enclosingState;
  }
  
  public State(String name) {
    this.name = name;
  }
  
  public State() {
    
  }
  
  public BucketOp getBucketOp(String opName) {
    return bucketOps.get(opName);
  }
  public void setBucketOp(String opName, BucketOp bucketOp) {
    bucketOps.put(opName, bucketOp);
  }
  public DbOp getDbOp(String opName) {
    return dbOps.get(opName);
  }
  public void setDbOp(String opName, DbOp dbOp) {
    dbOps.put(opName, dbOp);
  }
  public BQOp getBqOp(String opName) {
    return bqOps.get(opName);
  }
  public void setBqOp(String opName, BQOp bqOp) {
    bqOps.put(opName, bqOp);
  }
  public RestOp getRestOp(String opName) {
    return restOps.get(opName);
  }
  public void setRestOp(String opName, RestOp restOp) {
    restOps.put(opName, restOp);
  }

  public OpManager getOpManager() {
    return opManager;
  }

  public void setOpManager(OpManager opManager) {
    this.opManager = opManager;
  }

  public void createBucketOpIfNotExists(String opName, MapX params) {
    if (bucketOps.get(opName) != null) {
      return;
    }
   
    BucketOp bucketOp = null;
    if (this.opManager != null) {
        bucketOp = new BucketOp(opName,params, this.opManager);
    } else {
        bucketOp = new BucketOp(opName,params);
    }
    //bucketOp.
    bucketOps.put(opName, bucketOp);
    bucketOp.start();
    
  }
  
  
  public void createDbOpIfNotExists(String opName, MapX params) {
    if (dbOps.get(opName) != null) {
      return;
    }
    DbOp dbOp = new DbOp(opName, _getOpManager(), params);
    dbOps.put(opName, dbOp);
    dbOp.start();
  }
  
  public void completeOps() {
    
    Iterator<BucketOp> bucketOpIterator = bucketOps.values().iterator();
    
    while (bucketOpIterator.hasNext()) {
      bucketOpIterator.next().completeOp();
    }
    
    Iterator<DbOp> dbOpIterator = dbOps.values().iterator();
    
    while (dbOpIterator.hasNext()) {
      dbOpIterator.next().completeOp();
    }
  }
  
  public MapX select(String ...keys) {
    MapX m = new MapX();
    for (String key : keys) {
      if (!containsKey(key)) {
        m.put(key, get(key));
      }
    }
    return m;
  }
  
  public void addError(String sourceName, String message) {
    MapX m = new MapX();
    if (StringUtils.isNotBlank(name)) {
      m.put("name", name);
    }
    m.put("sourceName", sourceName);
    m.put("level", "error");
    m.put("message", message);
    errors.add(m);
  }
  
  public void addCritical(String sourceName, String message) {
    MapX m = new MapX();
    m.put("sourceName", sourceName);
    m.put("level", "critical");
    m.put("message", message);
    errors.add(m);
  }
  
  public Map<String, BucketOp> getBucketOps() {
    return bucketOps;
  }

  public Map<String, DbOp> getDbOps() {
    return dbOps;
  }

  public Map<String, BQOp> getBqOps() {
    return bqOps;
  }

  public Map<String, RestOp> getRestOps() {
    return restOps;
  }

  public ListX getErrors() {
    return errors;
  }

  public int getSuccessCount() {
    return successCount;
  }

  public int getFailureCount() {
    return failureCount;
  }

  private OpManager _getOpManager() {
    if (opManager == null) {
      opManager = new OpManager();
    }
    return opManager;
  }

  public State getEnclosingState() {
    return enclosingState;
  }
  
  public boolean hasErrors() {
    return errors.size() > 0;
  }
}
