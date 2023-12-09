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

import com.collager.trillo.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DSSQLTemplate {

  private String name;
  private String sql;
  private boolean skipTenantCheck = false;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }
  
  public static DSSQLTemplate mapToMetaData(Map<String, Object> map) {
    DSSQLTemplate templateM = Util.fromMap(map, DSSQLTemplate.class);
    return templateM;
  }

  public boolean isSkipTenantCheck() {
    return skipTenantCheck;
  }

  public void setSkipTenantCheck(boolean skipTenantCheck) {
    this.skipTenantCheck = skipTenantCheck;
  }
  
}
