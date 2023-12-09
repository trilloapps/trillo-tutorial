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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({ "name", "class2Name", "mappingAttribute", "type", "dir",
                      "displayName", "description"})
public class AssociationM extends BaseM {
  
  /**
   * 
   * "mappingAttribute" is the attribute in the class2 that matches with the "uid" of the class1.
   * It is used for ManyToOne association.
   * 
   * ManyToMany association are saved in a table (index) which has the same name as the association.
   * This table has "id1" and "id2" attributes. "id1" is the id of the class1  and "id2" is the id of 
   * associated class2.
   *
   */
  
  private String type;
  private  String dir = "CLS1_TO_CLS2";
  private String class2Name;
  private AttributeMapping attrMapping;
  private AttributeMapping importAttrMapping;
  
  private String where = null;
  private String orderBy = null;
  private String groupBy = null;
  private String having = null;
  private int start = 1;
  private int size = 10000;
  // SQL is used for retrieving many-to-many relationships.
  // For example:
  // "sql" : "SELECT c.id, c.myid as myId, c.contactid as contactId, c.createdat as createdAt, c.updatedat as updatedAt, 
  // u.userid as contactUserId, u.firstname as contactFirstName, u.lastname as contactLastName, u.pictureurl as contactPictureUrl 
  // FROM Contact_tbl c, user_tbl u WHERE (u.id=c.contactid) AND (c.myid={{_user_id_}})"
  private String sql = null;
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getDir() {
    return dir;
  }
  public void setDir(String dir) {
    this.dir = dir;
  }
  public String getClass2Name() {
    return class2Name;
  }
  public void setClass2Name(String class2Name) {
    this.class2Name = class2Name;
  }
  public AttributeMapping getAttrMapping() {
    return attrMapping;
  }
  public void setAttrMapping(AttributeMapping attrMapping) {
    this.attrMapping = attrMapping;
  }
  public AttributeMapping getImportAttrMapping() {
    return importAttrMapping;
  }
  public void setImportAttrMapping(AttributeMapping importAttrMapping) {
    this.importAttrMapping = importAttrMapping;
  }
 
  public static class AttributeMapping {
    private String myAttr;
    private String class2Attr;
    public String getMyAttr() {
      return myAttr;
    }
    public void setMyAttr(String myAttr) {
      this.myAttr = myAttr;
    }
    public String getClass2Attr() {
      return class2Attr;
    }
    public void setClass2Attr(String class2Attr) {
      this.class2Attr = class2Attr;
    }
    public AttributeMapping copy() {
      AttributeMapping m = new AttributeMapping();
      m.class2Attr = class2Attr;
      m.myAttr = myAttr;
      return m;
    }
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
  public String getGroupBy() {
    return groupBy;
  }
  public void setGroupBy(String groupBy) {
    this.groupBy = groupBy;
  }
  public String getHaving() {
    return having;
  }
  public void setHaving(String having) {
    this.having = having;
  }
  @JsonInclude(Include.NON_DEFAULT)
  public int getStart() {
    return start;
  }
  public void setStart(int start) {
    this.start = start;
  }
  @JsonInclude(Include.NON_DEFAULT)
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
  public boolean checkIfManyToMany() {
    return "MANY_TO_MANY".equalsIgnoreCase(type);
  }
  public boolean checkIfOneToMany() {
    return "ONE_TO_MANY".equalsIgnoreCase(type);
  }
  public boolean checkIfManyToOne() {
    return "MANY_TO_ONE".equalsIgnoreCase(type);
  }
  public boolean checkIfOneToOne() {
    return "ONE_TO_ONE".equalsIgnoreCase(type);
  }
  public static AssociationM mapToMetaData(Map<String, Object> map) {
    AssociationM assocM = Util.fromMap(map, AssociationM.class);
    return assocM;
  }
}
