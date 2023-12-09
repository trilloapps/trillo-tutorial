/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */
package com.collager.trillo.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/*
 * Meta model class BaseM (M suffix indicates meta model)
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseM {
  
  
  private String id = null;
  private String name;
  private String displayName;
  private String description;
  private String securityPolicies;
  
  private boolean errored = false;
  private String message = null;

  private Map<String, Object>props = null;
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDisplayName() {
    return displayName;
  }
  
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getSecurityPolicies() {
    return securityPolicies;
  }
  
  public void setSecurityPolicies(String securityPolicies) {
    this.securityPolicies = securityPolicies;
  }

  public Map<String, Object> getProps() {
    return props;
  }

  public void setProps(Map<String, Object> props) {
    this.props = props;
  }

  public void setProp(String name, Object value) {
    if (props == null) {
      props = new HashMap<String, Object>();
    }
    props.put(name, value);
  }
  
  public Object getProp(String name) {
    if (props != null) {
      return props.get(name);
    }
    return null;
  }
  
  public Object removeProp(String name) {
    if (props == null) {
      return null;
    }
    return props.remove(name);
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isErrored() {
    return errored;
  }
  public void setErrored(boolean errored) {
    this.errored = errored;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public void addMessage(String message) {
    if (this.message == null) {
      this.message = message;
    } else {
      this.message += "\n" + message;
    }
  }
}
