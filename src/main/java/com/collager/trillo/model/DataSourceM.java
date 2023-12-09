/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import com.collager.trillo.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*
 * Data source propertied are quite different from one data source to another.
 * Therefore, they are modeled as generic map. UI forms to edit data-source
 * will display proper names to user.
 * The best way to find out the properties specific to a type is to look at 
 * JSON file.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_EMPTY)
public class DataSourceM extends TreeNodeM {
  private String type = "unknown";
  private Map<String, String> properties;
  private List<ClassM> classList = new ArrayList<ClassM>();
  private String orgName; // orgName to which the data-source metadata belongs.
  private String appName; // orgName to which the data-source metadata belongs.
  private boolean permitsDBCreate = false;
  private boolean custom = false;

  private boolean useDefault = false;
  private boolean unreachable = false;
  
  @JsonIgnore
  private boolean createMode = false;
 
  @JsonIgnore
  private Map<String, ClassM> classMap = new ConcurrentHashMap<String, ClassM>();
  
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }
  
  @JsonIgnore
  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  @JsonIgnore
  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }
  
  public boolean isUseDefault() {
    return useDefault;
  }

  public void setUseDefault(boolean useDefault) {
    this.useDefault = useDefault;
  }

  public List<ClassM> getClassList() {
    return classList;
  }
  
  synchronized public void addClassM(ClassM classM) {
    if (classMap.get(classM.getName()) != null) {
      deleteClassMFromListByName(classM.getName());
    }
    classList.add(classM);
    classMap.put(classM.getName(), classM);
  }
  
  public ClassM getClassM(String name) {
    return classMap.get(name);
  }
  
  private void deleteClassMFromListByName(String name) {
    for (int i=0; i<classList.size(); i++) {
      if (name.equals(classList.get(i).getName())) {
        classList.remove(i);
        return;
      }
    }
  }

  public boolean isPermitsDBCreate() {
    return permitsDBCreate;
  }

  public void setPermitsDBCreate(boolean permitsDBCreate) {
    this.permitsDBCreate = permitsDBCreate;
  }
  
  @JsonIgnore
  public boolean isCreateMode() {
    return createMode;
  }

  public void setCreateMode(boolean createMode) {
    this.createMode = createMode;
  }
  
  public static DataSourceM mapToMetaData(Map<String, Object> map) {
    DataSourceM dsM = Util.fromMap(map, DataSourceM.class);
    return dsM;
  }

  public boolean isCustom() {
    return custom;
  }

  public void setCustom(boolean custom) {
    this.custom = custom;
  }

  public boolean isUnreachable() {
    return unreachable;
  }

  public void setUnreachable(boolean unreachable) {
    this.unreachable = unreachable;
  }
  
  @JsonIgnore
  public String getFullName() {
    return (StringUtils.isNotBlank(appName) ? appName + "." : "") 
        + getName();
  }
}
