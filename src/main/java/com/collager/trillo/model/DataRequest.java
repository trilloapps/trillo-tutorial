/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.model;

import java.util.Map;

public class DataRequest {
  private String appName = null;
  private String dsName = null;
  private String className = null;
  private String where;
  private String orderBy;
  private String groupBy;
  private int start = 1;
  private int size = 10;
  private String sql;
  private String sqlTemplate;
  private Map<String, Object> params = null;
  private boolean includeDeleted = false;
  private boolean usingRowLimits;
  private boolean usingView;
  private String viewName; // DB view Name
  private boolean forAllUsers = false; // does not mask records by the user if the API user is an admin
  private String countQuery = null;
  
  private Exp filter;

  public String getClassName() {
    return className;
  }
  public void setClassName(String className) {
    this.className = className;
  }
  public String getWhere() {
    return where;
  }
  public void setWhere(String where) {
    this.where = where;
  }
  public String getOrderBy() {
    return orderBy;
  }
  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }
  @Deprecated
  public String getGroupBy() {
    return groupBy;
  }
  @Deprecated
  public void setGroupBy(String groupBy) {
    this.groupBy = groupBy;
  }
  public int getStart() {
    return start;
  }
  public void setStart(int start) {
    this.start = start;
  }
  public int getSize() {
    return size;
  }
  public void setSize(int size) {
    this.size = size;
  }
  public String getSql() {
    return sql;
  }
  public void setSql(String sql) {
    this.sql = sql;
  }

  public String getSqlTemplate() {
    return sqlTemplate;
  }

  public void setSqlTemplate(String sqlTemplate) {
    this.sqlTemplate = sqlTemplate;
  }
  public Map<String, Object> getParams() {
    return params;
  }
  public void setParams(Map<String, Object> params) {
    this.params = params;
  }
  public boolean isIncludeDeleted() {
    return includeDeleted;
  }
  public void setIncludeDeleted(boolean includeDeleted) {
    this.includeDeleted = includeDeleted;
  }
  public boolean isUsingRowLimits() {
    return usingRowLimits;
  }
  public void setUsingRowLimits(boolean usingRowLimits) {
    this.usingRowLimits = usingRowLimits;
  }
  public boolean isUsingView() {
    return usingView;
  }
  public void setUsingView(boolean usingView) {
    this.usingView = usingView;
  }
  public String getViewName() {
    return viewName;
  }
  public void setViewName(String viewName) {
    this.viewName = viewName;
  }
  public boolean isForAllUsers() {
    return forAllUsers;
  }
  public void setForAllUsers(boolean forAllUsers) {
    this.forAllUsers = forAllUsers;
  }
  public Exp getFilter() {
    return filter;
  }
  public void setFilter(Exp filter) {
    this.filter = filter;
  }
  public String getCountQuery() {
    return countQuery;
  }
  public void setCountQuery(String countQuery) {
    this.countQuery = countQuery;
  }
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }
  public String getDsName() {
    return dsName;
  }
  public void setDsName(String dsName) {
    this.dsName = dsName;
  }
}
