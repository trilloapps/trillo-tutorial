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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({ "impl", "cls", "filter", "orderBy", "serviceUrl", "query"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSpec {
  
 //specific to viewSpec attributes and for old version compatibility
  private String impl; 
  private String cls;
  private String filter;
  private String orderBy;
  private String query;
  private Map<String, Object>options;
  private Object body;
  
  private String serviceUrl;
  private String method;
  private SimpleSchema querySchema;
  private Map<String, Object> headers;
  private SimpleSchema bodySchema;
  private SimpleSchema outputSchema;
  private Object testBody;
  private Map<String, Object> testQuery;
  private Map<String, Object> bodyMapping;
  private Map<String, Object> queryMapping;
  private String bodyParamName;
  private String bodyMappingScript;
  private String outputMappingScript;
  private String uid = null;
  public String getImpl() {
    return impl;
  }
  public void setImpl(String impl) {
    this.impl = impl;
  }
  public String getCls() {
    return cls;
  }
  public void setCls(String cls) {
    this.cls = cls;
  }
  public String getFilter() {
    return filter;
  }
  public void setFilter(String filter) {
    this.filter = filter;
  }
  public String getOrderBy() {
    return orderBy;
  }
  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }
  public String getServiceUrl() {
    return serviceUrl;
  }
  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
    uid = serviceUrl;
  }
  public Map<String, Object> getOptions() {
    return options;
  }
  public void setOptions(Map<String, Object> options) {
    this.options = options;
  }
  public String getQuery() {
    return query;
  }
  public void setQuery(String query) {
    this.query = query;
  }
  public Object getBody() {
    return body;
  }
  public void setBody(Object body) {
    this.body = body;
  }
  public Map<String, Object> getHeaders() {
    return headers;
  }
  public void setHeaders(Map<String, Object> headers) {
    this.headers = headers;
  }
  public String getMethod() {
    return method;
  }
  public void setMethod(String method) {
    this.method = method;
  }
  public SimpleSchema getBodySchema() {
    return bodySchema;
  }
  public void setBodySchema(SimpleSchema bodySchema) {
    this.bodySchema = bodySchema;
  }
  public SimpleSchema getOutputSchema() {
    return outputSchema;
  }
  public void setOutputSchema(SimpleSchema outputSchema) {
    this.outputSchema = outputSchema;
  }
  public SimpleSchema getQuerySchema() {
    return querySchema;
  }
  public void setQuerySchema(SimpleSchema querySchema) {
    this.querySchema = querySchema;
  }
  public Object getTestBody() {
    return testBody;
  }
  public void setTestBody(Object testBody) {
    this.testBody = testBody;
  }
  public Map<String, Object> getTestQuery() {
    return testQuery;
  }
  public void setTestQuery(Map<String, Object> testQuery) {
    this.testQuery = testQuery;
  }
  public Map<String, Object> getBodyMapping() {
    return bodyMapping;
  }
  public void setBodyMapping(Map<String, Object> bodyMapping) {
    this.bodyMapping = bodyMapping;
  }
  public Map<String, Object> getQueryMapping() {
    return queryMapping;
  }
  public void setQueryMapping(Map<String, Object> queryMapping) {
    this.queryMapping = queryMapping;
  }
  public String getBodyParamName() {
    return bodyParamName;
  }
  public void setBodyParamName(String bodyParamName) {
    this.bodyParamName = bodyParamName;
  }
  public String getBodyMappingScript() {
    return bodyMappingScript;
  }
  public void setBodyMappingScript(String bodyMappingScript) {
    this.bodyMappingScript = bodyMappingScript;
  }
  public String getOutputMappingScript() {
    return outputMappingScript;
  }
  public void setOutputMappingScript(String outputMappingScript) {
    this.outputMappingScript = outputMappingScript;
  }
  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }
}
