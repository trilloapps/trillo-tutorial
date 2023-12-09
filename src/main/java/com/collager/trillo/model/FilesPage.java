/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.model;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilesPage {
  private List<Map<String, Object>> files;
  private String nextPageToken;
  public List<Map<String, Object>> getFiles() {
    return files;
  }
  public void setFiles(List<Map<String, Object>> files) {
    this.files = files;
  }
  public String getNextPageToken() {
    return nextPageToken;
  }
  public void setNextPageToken(String nextPageToken) {
    this.nextPageToken = nextPageToken;
  }
}
