/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */
package com.collager.trillo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileM extends BaseM {
  
  private String fileName = null;
  private String content = null;
  private long size;
  private long lastModified;
  private String url;
  
  protected String modelClassName;
  protected String uid = null;
  protected String folder = null; 
  
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    if (fileName != null) {
      this.fileName = fileName.replace("\\", "/");
    } else {
      this.fileName = null;
    }
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }
  public String getModelClassName() {
    return modelClassName;
  }
  public void setModelClassName(String modelClassName) {
    this.modelClassName = modelClassName;
  }
  public long getSize() {
    return size;
  }
  public void setSize(long size) {
    this.size = size;
  }
  public long getLastModified() {
    return lastModified;
  }
  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getFolder() {
    return folder;
  }
  public void setFolder(String folder) {
    this.folder = folder;
  }
}
