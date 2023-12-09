/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.model;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.collager.trillo.model.AttributeM.AttrToAttrMapping;
import com.collager.trillo.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({ "name", "superClass", "attributes", "associations", "displayName", "description", "tableDbViewName"})
public class ClassM extends BaseM {
  
  static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  
  private String superClass = null;
  private String nameInDS = null; // name in data source such as table (RDBMS),
                                  // collection (NoSQL) etc.
  private boolean tableCreateable = true;
  // This flag is used when data is migrated.
  // There are cases when the table data has to erased and re-imported. This occurs when there is no
  // unique key of the record (single column or composite key both are not possible).
  private boolean deleteAllBeforeSync = false;
  private String primaryKeyGenerator = "serial";
  private List<AttributeM> attributes = new ArrayList<AttributeM>();
  private List<AssociationM> associations = new ArrayList<AssociationM>();
  private List<DSSQLTemplate> dsSqlTemplates = null;
  private List<ApiSpec> apiSpecs;
  private String viewCreateStmt;
  
  private String appName;
  private String dsName;
  private String tableDbViewName = null; // if set, creates aggregate view of the table
  private boolean custom = false;
  private String schemaName=null;
  private String dsType = null;
  private boolean multiTenancyOptional = false;
  private Map<String, Object> uiProps;

  public List<DSSQLTemplate> getDsSqlTemplates() {
    return dsSqlTemplates;
  }

  public void setDsSqlTemplates(List<DSSQLTemplate> dsSqlTemplates) {
    this.dsSqlTemplates = dsSqlTemplates;
  }
  
  public DSSQLTemplate getDsSqlTemplateByName(String name) {
    if (dsSqlTemplates == null) {
      return null;
    }
    for (DSSQLTemplate template : dsSqlTemplates) {
      if (name.equalsIgnoreCase(template.getName())) {
        return template;
      }
    }
    return null;
  }

  public String getSuperClass() {
    return superClass;
  }

  public void setSuperClass(String superClass) {
    this.superClass = superClass;
  }

  public String getNameInDS() {
    if (nameInDS == null) {
      return getName() + "_tbl";
    }
    return nameInDS;
  }

  public void setNameInDS(String nameInDS) {
    this.nameInDS = nameInDS;
  }

  public boolean isTableCreateable() {
    return tableCreateable;
  }

  public void setTableCreateable(boolean tableCreateable) {
    this.tableCreateable = tableCreateable;
  }

  public String getPrimaryKeyGenerator() {
    return primaryKeyGenerator;
  }

  public void setPrimaryKeyGenerator(String primaryKeyGenerator) {
    this.primaryKeyGenerator = primaryKeyGenerator;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public List<AttributeM> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<AttributeM> attrs) {
    this.attributes = attrs;
  }

  public List<AssociationM> getAssociations() {
    return associations;
  }

  public void setAssociations(List<AssociationM> associations) {
    this.associations = associations;
  }

  public void addAttribute(AttributeM attrM) {
    AttributeM current = this.getAttribute(attrM.getName());
    if (current == null) {
      attributes.add(attrM);
    }
  }

  public void addAssociation(AssociationM associationM) {
    AssociationM current = this.getAssociation(associationM.getName());
    if (current == null) {
      associations.add(associationM);
    }
  }

  public AttributeM getAttribute(String attrName) {
    if (attrName == null) {
      return null;
    }
    for (AttributeM attrM : attributes) {
      if (attrName.equalsIgnoreCase(attrM.getName())) {
        return attrM;
      }
    }
    return null;
  }

  public AttributeM getAttributeByNameInDs(String nameInDS) {
    for (AttributeM attrM : attributes) {
      if (nameInDS.equalsIgnoreCase(attrM.getNameInDS())) {
        return attrM;
      }
    }
    return null;
  }

  public AttributeM getAttributeByBestMatch(String name) {
    for (AttributeM attrM : attributes) {
      if (name.equalsIgnoreCase(attrM.getName2())) {
        return attrM;
      }
    }
    // try by nameInDS
    AttributeM a = getAttributeByNameInDs(name);
    if (a != null) {
      return a;
    }
    // try by name
    return getAttribute(name);
  }

  public AssociationM getAssociation(String assocName) {
    for (AssociationM assocM : associations) {
      if (assocName.equalsIgnoreCase(assocM.getName())) {
        return assocM;
      }
    }
    return null;
  }

  public AssociationM getAssociationByClass2(String class2Name) {
    for (AssociationM assocM : associations) {
      if (class2Name.equalsIgnoreCase(assocM.getClass2Name())) {
        return assocM;
      }
    }
    return null;
  }

  public AttributeM retrievePrimaryKeyAttribute() {
    for (AttributeM attrM : attributes) {
      if (attrM.isPersistent() && attrM.isPrimaryKey()) {
        return attrM;
      }
    }
    return null;
  }

  @JsonIgnore
  public String getAssociationsJson() {
    return Util.asJSONString(associations);
  }

  public void setAssociationsJson(String json) {
    associations = Util.fromJSONString(json, new TypeReference<List<AssociationM>>() {
    });
  }
  
  public String getViewCreateStmt() {
    return viewCreateStmt;
  }

  public void setViewCreateStmt(String viewCreateStmt) {
    this.viewCreateStmt = viewCreateStmt;
  }

  public void addAttributes(List<AttributeM> l) {
    for (AttributeM attr : l) {
      addAttribute(attr);
    }
  }

  public void addAssociations(List<AssociationM> l) {
    for (AssociationM a : l) {
      addAssociation(a);
    }
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isMultiTenancyOptional() {
    return multiTenancyOptional;
  }

  public void setMultiTenancyOptional(boolean multiTenancyOptional) {
    this.multiTenancyOptional = multiTenancyOptional;
  }

  public AttributeM retrieveTenantAttribute() {
    for (AttributeM attr : this.attributes) {
      if (attr.isTenant()) {
        return attr;
      }
    }
    return null;
  }

  public AttributeM retrieveRepresentUserAttribute() {
    for (AttributeM attr : this.attributes) {
      if (attr.isRepresentsUser()) {
        return attr;
      }
    }
    return null;
  }
  
  public AttributeM retrieveRepresentOrderingAttribute() {
    for (AttributeM attr : this.attributes) {
      if (attr.isRepresentsOrdering()) {
        Object v = attr.convertValue("");
        if (v instanceof Number) {
          return attr;
        }
      }
    }
    return null;
  }
  
  public String retrieveTableNameWithoutSchema() {
    if (nameInDS != null) {
      return nameInDS;
    }
    String name = getName();
    return name.toLowerCase() + "_tbl";
  }

  public String retrieveTableName() {
    String name;
    if (nameInDS != null) {
      name = nameInDS;
    } else {
      name = getName();
    }
    if (StringUtils.isNotBlank(schemaName)) {
      return "\"" + schemaName + "\"" + "." + "\"" + name + "\"";
    }
    return name;
  }
  
  public String retrieveApiClassPath() {
    return (StringUtils.isNotBlank(schemaName) ? 
        schemaName + "." : "") + getName();
  }
  
  @JsonIgnore
  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  @JsonIgnore
  public String getDsName() {
    return dsName;
  }

  public void setDsName(String dsName) {
    this.dsName = dsName;
  }

  @JsonIgnore
  public String getDsType() {
    return dsType;
  }

  public void setDsType(String dsType) {
    this.dsType = dsType;
  }

  public void fixIt() {
    for (AttributeM attr : this.attributes) {
      attr.fixIt();
    }
  }
  
  public List<ApiSpec> getApiSpecs() {
    return apiSpecs;
  }

  public void setApiSpecs(List<ApiSpec> apiSpecs) {
    this.apiSpecs = apiSpecs;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isDeleteAllBeforeSync() {
    return deleteAllBeforeSync;
  }

  public void setDeleteAllBeforeSync(boolean deleteAllBeforeSync) {
    this.deleteAllBeforeSync = deleteAllBeforeSync;
  }
  
  public boolean mapValues(String buffer, Map<String, Object> targetMap) {
    String v;
    int st;
    int end;
    for (AttributeM attrM : attributes) {
      st = attrM.getSourceStartPos();
      end = attrM.getSourceEndPos();
      if (st > -1 && end > -1 && ((end - st) >= 0)) {
        try {
          v = buffer.substring(st-1, end);
          v = v.trim();
        } catch (Exception exc) {
          // ignore error and continue
          continue;
        }
      } else {
        continue;
      }
      try {
        targetMap.put(attrM.getName(), AttributeM.convertValue(v, attrM.getType(), attrM.getSourceFormat()));
      } catch (Exception exc) {
        LOG.error("Failed to convert attribute : " + attrM.getName() + "\n " + exc.getMessage() + "\n ignoring error. Function may be handling it");
        return false;
      }
    }
    return true;
  }
  
  public void mapValues(Map<String, Object> srcMap, Map<String, Object> targetMap) {
    String temp;
    Map<String, Object> srcCopy = new HashMap<String, Object>(srcMap);
    for (AttributeM attrM : attributes) {
      if (attrM.isPrimaryKey()) {
        String pkGen = this.getPrimaryKeyGenerator();
        if (pkGen == null) {
          // serial is assumed, the attribute value will be auto generated, don't set it
          continue;
        }
        pkGen = pkGen.toLowerCase();
        if ("uuid".equals(pkGen) || "serial".equals(pkGen)) {
          continue;
        }
      }
      temp = attrM.getNameInSource();
      if ("none".equals(temp)) {
        // this is used as the special value to indicate that no mapping 
        // is required (the column is auto generated or defaultValue is used or left null).
        // This is useful when there is matching name attribute present in the source but it is not really 
        // supposed to be the same attribute.
        continue;
      }
      if (temp == null || !srcMap.containsKey(temp)) {
        temp = attrM.getName();
      }
      if (temp != null && srcMap.containsKey(temp)) {
        srcCopy.remove(temp);
        try {
          targetMap.put(attrM.getName(), AttributeM.convertValue(srcMap.get(temp), attrM.getType(), attrM.getSourceFormat()));
        } catch (Exception exc) {
          targetMap.put(attrM.getName(), srcMap.get(temp));
          LOG.warn("Failed to convert attribute : " + attrM.getName() + "\n " + exc.getMessage() + "\n ignoring error. Function may be handling it");
        }
      }
    }
    targetMap.putAll(srcCopy); // finally merge remaining entries of the srcCopy
  }
  
  public void mapValues2(Map<String, Object> srcMap, Map<String, Object> targetMap, List<AttrToAttrMapping> attrMappings, String srcTimeZone) {
    String temp;
    AttributeM a;
    if (attrMappings != null && attrMappings.size() > 0) {
      for (AttrToAttrMapping am : attrMappings) {
        temp = am.getFromAttr();
        if (srcMap.containsKey(temp)) {
          a = getAttribute(am.getToAttr());
          if (a != null) {
            targetMap.put(a.getName(), AttributeM.convertValue2(srcMap.get(temp), a.getType(), srcTimeZone != null ? srcTimeZone : a.getSourceTimeZone()));
          } else {
            targetMap.put(temp, srcMap.get(temp));
          }
        }
      }
    } else {
      for (AttributeM attrM : attributes) {
        temp = attrM.getNameInSource();
        temp = temp != null && srcMap.containsKey(temp) ? temp : attrM.getName();
        if (temp != null && srcMap.containsKey(temp)) {
          targetMap.put(attrM.getName(), AttributeM.convertValue2(srcMap.get(temp), attrM.getType(), srcTimeZone != null ? srcTimeZone : attrM.getSourceTimeZone()));
        }
      }
    }
  }
  
  @JsonIgnore
  public List<String> getSourceColumnNames() {
    List<AttributeM> l = new ArrayList<AttributeM>();
    for (AttributeM attrM : attributes) {
      if (attrM.getSourceColNum() > -1) {
        l.add(attrM);
      }
    }
    Collections.sort(l, new Comparator<AttributeM>() {
      public int compare(AttributeM a1, AttributeM a2) {
        if (a1.getSourceColNum() < a2.getSourceColNum()) {
          return -1;
        } else if (a1.getSourceColNum() > a2.getSourceColNum()) {
          return 1;
        }
        return 0;
      }
    });
    
    List<String> names = new ArrayList<String>();
    
    for (int i=0; i<l.size(); i++) {
      names.add(l.get(i).getName());
    }
    
    return names;
  }
  
  public static ClassM mapToMetaData(Map<String, Object> map) {
    ClassM clsM = Util.fromMap(map, ClassM.class);
    return clsM;
  }
  
  public List<String> retrieveSystemAttrs() {
    List<AttributeM> attrs = getAttributes();
    List<String> systemAttrs = new ArrayList<String>();
    for (int i=0; i<attrs.size(); i++) {
      AttributeM attr = attrs.get(i);
      if (attr.isSystemAttr()) {
        systemAttrs.add(attr.getName());
      }
    }
    return systemAttrs;
  }

  public String getTableDbViewName() {
    return tableDbViewName;
  }

  public void setTableDbViewName(String tableDbViewName) {
    this.tableDbViewName = tableDbViewName;
  }
  
  public List<Map<String, Object>> getSchemaForDataStudio(boolean includeAllSysAttrs) {
    List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
    Map<String, Object> m;
    List<AttributeM> attrs = getAttributes();
    AttributeM attr;
    String conceptType;
    for (int i=0; i<attrs.size(); i++) {
      attr = attrs.get(i);
      if (includeAllSysAttrs || !attr.isSystemAttr() || "id".equals(attr.getName()) || "createdAt".equals(attr.getName())) {
        m = new LinkedHashMap<String, Object>();
        m.put("name",  attr.getName());
        m.put("displayName",  StringUtils.isNotBlank(attr.getDisplayName()) ? attr.getDisplayName() : attr.getName());
        conceptType = "DIMENSION";
        if (attr.isNumericType()) {
          m.put("type", "NUMBER");
          conceptType = "METRIC";
        } else if ("boolean".equalsIgnoreCase(attr.getType())) {
          m.put("type", "BOOLEAN");
        } else {
          m.put("type", "STRING");
        }
        m.put("conceptType", conceptType);
        l.add(m);
      }
    }
    
    return l;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isCustom() {
    return custom;
  }

  public void setCustom(boolean custom) {
    this.custom = custom;
  }

  public Map<String, Object> getUiProps() {
    return uiProps;
  }

  public void setUiProps(Map<String, Object> uiProps) {
    this.uiProps = uiProps;
  }
}
