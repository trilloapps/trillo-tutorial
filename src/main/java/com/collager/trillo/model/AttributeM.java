package com.collager.trillo.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({ "name", "type", "enumName",
  "displayName", "description"})
public class AttributeM extends BaseM {
  
  private static Pattern timezonePattern = Pattern.compile("\\b(?<!')([xXzZ])(?!')\\b");
  
  private String type = "string";
  private String nameInDS; // column, attribute 
  private String typeInDS;
  private boolean primaryKey = false;
  private boolean unique = false;
  private boolean indexed = false;
  private boolean tenant = false;
  private boolean persistent = true;
  private String expression = null; // expression used to compute this attribute, can also be a mustache template.
  private int length = -1; // -1 means that unknown
  private boolean required = false;
  private boolean tenantUnique; // unique within tenant
  private boolean hidden;
  private String name2;
  private boolean listHidden;
  private boolean dataManagerHidden;
  private boolean editable;
  private Object defaultValue;
  private boolean autoSequence = false;
  private boolean systemAttr;
  private boolean representsUser;
  private boolean representsOrdering = false;
  private boolean naming = false;
  private String className = null; // if the attribute represents a key of object of another class, this is the name of that class.
  private String alias = null;
  private boolean appKey = false;
  private String nameInSource = null; // name in the other source DB or API (used for mapping)
  private String sourceFormat = null;
  private String sourceTimeZone;
  private int sourceColNum = -1; // -1, unknown
  private int sourceStartPos = -1; // -1,unknown
  private int sourceEndPos = -1; // -1, unknown

  // If attribute represents the a key of another object, by default the other object is joined during query.
  // If this flag is set to true the joining is skipped.
  private boolean disableAutoJoin = false; 
  
  /*
   * Enum name is the key to enumeration set (defined is a property file) to convert
   * value to user friendly string.
   */
  private String enumName;
  
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getNameInDS() {
    return nameInDS;
  }

  public void setNameInDS(String nameInDS) {
    this.nameInDS = nameInDS;
  }
  
  public String getTypeInDS() {
    return typeInDS;
  }

  public void setTypeInDS(String typeInDS) {
    this.typeInDS = typeInDS;
  }
  
  public String retrieveNameInDSOrName() {
    if (StringUtils.isBlank(nameInDS)) {
      return getName();
    }
    return nameInDS;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(boolean primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getEnumName() {
    return enumName;
  }

  public void setEnumName(String enumName) {
    this.enumName = enumName;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isUnique() {
    return unique;
  }

  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isIndexed() {
    return indexed;
  }

  public void setIndexed(boolean indexed) {
    this.indexed = indexed;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isTenant() {
    return tenant;
  }

  public void setTenant(boolean tenant) {
    this.tenant = tenant;
  }
  
  public boolean isPersistent() {
    return persistent;
  }

  public void setPersistent(boolean persistent) {
    this.persistent = persistent;
  }

  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isTenantUnique() {
    return tenantUnique;
  }

  public void setTenantUnique(boolean tenantUnique) {
    this.tenantUnique = tenantUnique;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
  
  public String getName2() {
    return name2;
  }

  public void setName2(String name2) {
    this.name2 = name2;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isListHidden() {
    return listHidden;
  }

  public void setListHidden(boolean listHidden) {
    this.listHidden = listHidden;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isDataManagerHidden() {
    return dataManagerHidden;
  }

  public void setDataManagerHidden(boolean dataManagerHidden) {
    this.dataManagerHidden = dataManagerHidden;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isSystemAttr() {
    return systemAttr;
  }

  public void setSystemAttr(boolean systemAttr) {
    this.systemAttr = systemAttr;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isRepresentsUser() {
    return representsUser;
  }

  public void setRepresentsUser(boolean representsUser) {
    this.representsUser = representsUser;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isRepresentsOrdering() {
    return representsOrdering;
  }

  public void setRepresentsOrdering(boolean representsOrdering) {
    this.representsOrdering = representsOrdering;
  }
 
  public String getNameInSource() {
    return nameInSource;
  }

  public void setNameInSource(String nameInSource) {
    this.nameInSource = nameInSource;
  }

  public String getSourceFormat() {
    return sourceFormat;
  }

  public void setSourceFormat(String sourceFormat) {
    this.sourceFormat = sourceFormat;
  }
 
  public String getSourceTimeZone() {
    return sourceTimeZone;
  }

  public void setSourceTimeZone(String sourceTimeZone) {
    this.sourceTimeZone = sourceTimeZone;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public int getSourceColNum() {
    return sourceColNum;
  }

  public void setSourceColNum(int sourceColNum) {
    this.sourceColNum = sourceColNum;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public int getSourceStartPos() {
    return sourceStartPos;
  }

  public void setSourceStartPos(int sourceStartPos) {
    this.sourceStartPos = sourceStartPos;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public int getSourceEndPos() {
    return sourceEndPos;
  }

  public void setSourceEndPos(int sourceEndPos) {
    this.sourceEndPos = sourceEndPos;
  }
  
  public String retrieveName2() {
    if (name2 != null) {
      return name2;
    }
    return getName();
  }
  
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isAutoSequence() {
    return autoSequence;
  }

  public void setAutoSequence(boolean autoSequence) {
    this.autoSequence = autoSequence;
  }

  public Object convertValue(Object v) {
    return convertValue(v, type);
  }
  
  public Object retrieveDefaultValue() {
    if (defaultValue == null) {
      return null;
    }
    String str = "" + defaultValue;
    return convertValue(str);
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isNaming() {
    return naming;
  }

  public void setNaming(boolean naming) {
    this.naming = naming;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public boolean isAppKey() {
    return appKey;
  }

  public void setAppKey(boolean appKey) {
    this.appKey = appKey;
  }

  @JsonInclude(Include.NON_DEFAULT)
  public boolean isDisableAutoJoin() {
    return disableAutoJoin;
  }

  public void setDisableAutoJoin(boolean disableAutoJoin) {
    this.disableAutoJoin = disableAutoJoin;
  }

  public static AttributeM findAttribute(List<AttributeM> attributes, String attrName) {
    for (AttributeM attrM : attributes) {
      if (attrName.equalsIgnoreCase(attrM.getName())) {
        return attrM;
      }
    }
    return null;
  }
  
  public static AttributeM findAttributeByNameInDS(List<AttributeM> attributes, AttributeM otherAttribute) {
    String nameInDSOrName = otherAttribute.retrieveNameInDSOrName();
    for (AttributeM attrM : attributes) {
      if (nameInDSOrName.equalsIgnoreCase(attrM.retrieveNameInDSOrName())) {
        return attrM;
      }
    }
    return null;
  }

  public void fixIt() {
    if (type == null) {
      type = "string";
      return;
    }
    if (!StringUtils.isAllLowerCase(type)) {
      type = type.toLowerCase();
    }
  }
  
  @JsonIgnore
  public boolean isNumericType() {
    return checkIfNumberType(type);
  }
  
  static public boolean checkIfNumberType(String t) {
    return checkIfIntegralType(t) || checkIfDecimalType(t);
   }
  
  static public boolean checkIfIntegralType(String t) {
    t = t.toLowerCase();
    switch (t) {
    
     case "short" :
     case "integer" :
     case "int" :
     case "long" : 
     case "biginteger" : return true;
     default: return false;
    }
   }
   
   static public boolean checkIfDecimalType(String t) {
     t = t.toLowerCase();
     switch (t) {
      case "decimal" :
      case "numeric" :
      case "float" :
      case "double" : return true;
      default: return false;
     }
   }
   
   static public boolean checkIfStringType(String t) {
     t = t.toLowerCase();
     switch (t) {
       case "char":
       case "string":
       case "longstring":
       case "varchar":
         return true;
       default:
         return false;
     }
   }
   
   public static Object convertValue(Object v, String type) {
     return convertValue(v, type, null);
   }
   
   public static Object convertValue(Object v, String type, boolean clean) {
     return convertValue(v, type, null, clean);
   }
   
   public static Object convertValue(Object v, String type, String sourceFormat) {
     return convertValue(v, type, sourceFormat, false);
   }

   public static Object convertValue(Object v, String type, String sourceFormat, boolean clean) {
    /* handle well known types and format */
    if (v == null) {
      return v;
    }
    String str = v + "";
    String tStr = str.trim();
    String t = type == null ? "string" : type.toLowerCase();
    try {
      switch (t) {
      case "char":
        return tStr.length() > 0 ? tStr.charAt(0) : null;
      case "string":
      case "longstring":
      case "varchar":
      case "text":
        return tStr;
      case "decimal":
        return new BigDecimal(tStr.length() > 0 ? toNumericString(tStr, clean) : "0");
      case "numeric":
        return new Double(tStr.length() > 0 ? toNumericString(tStr, clean) : "0");
      case "boolean":
        // treat 1 as true.
        // return v;
        return "true".equalsIgnoreCase(tStr) || "1".equals(tStr);
      case "byte":
        return tStr.length() > 0 ? tStr.getBytes()[0] : null;
      case "short":
        return new Short(tStr.length() > 0 ? toNumericString(tStr, clean) : "0");
      case "integer":
      case "int":
        return new Integer(tStr.length() > 0 ? toNumericString(tStr, clean) : "0");
      case "long":
      case "biginteger":
        return new Long(tStr.length() > 0 ? toNumericString(tStr, clean) : "0");
      case "float":
        return new Float(tStr.length() > 0 ? toNumericString(tStr, clean) : "0");
      case "double":
        return new Double(tStr.length() > 0 ? toNumericString(tStr, clean) : "0");
      case "date":
      case "time":
      case "datetime":
      case "timestamp":
        return convertDate(v, type, sourceFormat);
      default:
        return v;
      }
    } catch (NumberFormatException exc) {
      return 0;
    }
  }

  public static Object convertValue2(Object v, String type, String srcTimeZone) {
    /* handle well known types and format */
    if (v == null) {
      return v;
    }
    String t = type == null ? "string" : type.toLowerCase();
    Class<?> cls = null;
    Object v2;
    switch (t) {
    case "char":
      cls = Character.class;
      break;
    case "string":
    case "longstring":
    case "varchar":
      cls = String.class;
      break;
    case "decimal":
    case "numeric":
      cls = BigDecimal.class;
      break;
    case "double":
      cls = Double.class;
      break;
    case "float":
      cls = Float.class;
      break;
    case "boolean":
      cls = Boolean.class;
      break;
    case "byte":
      cls = Byte.class;
      break;
    case "short":
      cls = Short.class;
      break;
    case "integer":
    case "int":
      cls = Integer.class;
      break;
    case "long":
    case "biginteger":
      cls = Long.class;
      break;
    default:
      break;
    }
    if (cls != null) {
      try {
        v2 = ConvertUtils.convert(v, cls);
      } catch (Exception exc) {
        if (checkIfNumberType(t)) {
          // try by removing non-numeric characters
          v = toNumericString("" + v, true);
          v2 = ConvertUtils.convert(v, cls);
        } else {
          throw exc;
        }
      }
    } else {
      v2 = v;
    }
    
    if (v2 instanceof java.util.Date && srcTimeZone != null) {
      boolean hasTimeZone = false;
      if (v instanceof String) {
        Matcher m = timezonePattern.matcher((String)v);
        hasTimeZone = m.find();
      }
      if (!hasTimeZone) {
        TimeZone tz = TimeZone.getTimeZone(srcTimeZone);
        java.util.Date dt = (java.util.Date) v2;
        int offset = tz.getOffset(dt.getTime());
        dt.setTime(dt.getTime() - offset);
        return dt;
      }
    }
    
    return v2;
  }
  
  public static Object convertDate(Object v, String type, String sourceFormat) {
    if (v == null) {
      return v;
    }
    if (!(v instanceof Long)) {
      if (v instanceof Number) {
        v = ((Number)v).longValue();
      } else if (sourceFormat != null) {
        try {
          String str = v + "";
          if (StringUtils.isBlank(str)) {
            return null;
          } else {
            SimpleDateFormat formatter = new SimpleDateFormat(sourceFormat);
            java.util.Date date = formatter.parse(str);
            v = date.getTime();
          }
        } catch(Exception exc) {
          throw new RuntimeException("Failed to convert value: " + v + ", using format: " + sourceFormat);
        }
      }
    }
    String t = type == null ? "string" : type.toLowerCase();
    switch (t) {
    case "date":
      if (v instanceof Long) {
        return new Date((long) v);
      } else {
        return ((v instanceof String) && StringUtils.isBlank((CharSequence) v)) ? null : v;
      }
    case "time":
      if (v instanceof Long) {
        return new Time((long) v);
      } else {
        return ((v instanceof String) && StringUtils.isBlank((CharSequence) v)) ? null : v;
      }
    case "datetime":
    case "timestamp":
      if (v instanceof Long) {
        return new Timestamp((long) v);
      } else {
        return ((v instanceof String) && StringUtils.isBlank((CharSequence) v)) ? null : v;
      }
    default:
      return v;
    }
  }
  
  public static String toNumericString(String str, boolean clean) {
    if (clean) {
      str = str.replaceAll("[^\\d.-]", "");
    }
    return str;
  }

  public Object retrieveTimeValue() {
    String t = type == null ? "string" : type.toLowerCase();
    long v = System.currentTimeMillis();
    switch (t) {
    case "date":
      return new Date(v);
    case "time":
      return new Time(v);
    case "datetime":
    case "timestamp":
      return new Timestamp(v);
    default:
      return v;
    }
  }
  
  public Object retrieveNoTimeValue() {
    String t = type == null ? "string" : type.toLowerCase();
    switch (t) {
    case "date":
    case "time":
    case "datetime":
    case "timestamp":
      return null;
    default:
      return 0L;
    }
  }
  
  static public class AttrToAttrMapping {
    private String fromAttr;
    private String toAttr;
    public String getFromAttr() {
      return fromAttr;
    }
    public void setFromAttr(String fromAttr) {
      this.fromAttr = fromAttr;
    }
    public String getToAttr() {
      return toAttr;
    }
    public void setToAttr(String toAttr) {
      this.toAttr = toAttr;
    }
  }
  
}
