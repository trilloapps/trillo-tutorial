package com.collager.trillo.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleSchema {
  private String type = "object";
  private boolean array;
  private boolean required;
  private Object exampleValue;
  private Map<String, SimpleSchema> properties = null;
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isArray() {
    return array;
  }
  public void setArray(boolean array) {
    this.array = array;
  }
  public Map<String, SimpleSchema> getProperties() {
    return properties;
  }
  public void setProperties(Map<String, SimpleSchema> properties) {
    this.properties = properties;
  }
  public void addProperty(String name, SimpleSchema property) {
    if (properties == null) {
      properties = new HashMap<String, SimpleSchema>();
    }
    properties.put(name, property);
  }
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
  public Object getExampleValue() {
    return exampleValue;
  }
  public void setExampleValue(Object exampleValue) {
    this.exampleValue = exampleValue;
  }
}
