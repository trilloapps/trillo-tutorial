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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({ "name", "children",
  "displayName", "description"})
public class TreeNodeM extends FileM {
  
  private String dirName = null;
  private List<BaseM> children = new ArrayList<BaseM>();

  public List<?> getChildren() {
    return children;
  }

  public void setChildren(List<BaseM> children) {
    this.children = children;
  }
  
  public void addChild(int index, BaseM child) {
    children.add(index, child);
  }
  
  public void addChild(BaseM child) {
    children.add(child);
  }
  
  public BaseM getChild(String name) {
    for (BaseM base : children) {
      if (base.getName().equals(name)) {
        return base;
      }
    }
    return null;
  }
  
  public BaseM getChildIgnoreCase(String name) {
    for (BaseM base : children) {
      if (base.getName().equalsIgnoreCase(name)) {
        return base;
      }
    }
    return null;
  }
  
  public FileM getChildFileM(String name) {
    BaseM m = this.getChild(name);
    if (m != null && m instanceof FileM) {
      return (FileM)m;
    }
    return null;
  }
  
  public TreeNodeM getChildTreeNodeM(String name) {
    BaseM m = this.getChild(name);
    if (m != null && m instanceof TreeNodeM) {
      return (TreeNodeM)m;
    }
    return null;
  }
  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getDirName() {
    return dirName;
  }

  public void setDirName(String dirName) {
    this.dirName = dirName;
  }
}
