package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapX extends LinkedHashMap<String, Object> {
  private static final long serialVersionUID = 1L;
  
  // an M object can be given a name which will be used for debugging,
  // logging, in error message
  protected String name = null;
  
  public MapX() {}
  
  public MapX(String name) {
    this.name = name;
  }
  
  public MapX(Map<String, Object> sourceMap) {
    if (sourceMap == null) {
      throw new RuntimeException("sourceMap is null");
    }
    putAll(sourceMap);
  }
  
  public MapX(Map<String, Object> sourceMap, String name) {
    if (sourceMap == null) {
      throw new RuntimeException("sourceMap is null");
    }
    putAll(sourceMap);
    this.name = name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name ;
  }
  
  public String getString(String key) {
    Object o = get(key);
    if (o == null) {
      return null;
    }
    if (o instanceof String) {
      return (String) o;
    }
    String s = "" + o.toString();
    return s;
  }
  
  public String getString(String key, String defaultValue) {
    if (containsKey(key)) {
      return getString(key);
    }
    return defaultValue;
  }
  
  public boolean getBoolean(String key) {
    Object o = get(key);
    if (o == null) {
      return false;
    }
    if (o instanceof Boolean) {
      return (boolean) o;
    }
    String s = "" + o.toString();
    if ("0".equals(s) || "false".equalsIgnoreCase(s)) {
      return false;
    }
    return true;
  }
  
  public boolean getBoolean(String key, boolean defaultValue) {
    if (containsKey(key)) {
      return getBoolean(key);
    }
    return defaultValue;
  }
  
  public int getInt(String key) {
    Object o = get(key);
    if (o == null) {
      return Integer.MIN_VALUE;
    }
    if (o instanceof Integer) {
      return (int) o;
    }
    if (o instanceof Number) {
      return ((Number) o).intValue();
    }
    String s = "" + o.toString();
    return Integer.parseInt(s);
  }
  
  public int getInt(String key, int defaultValue) {
    if (containsKey(key)) {
      return getInt(key);
    }
    return defaultValue;
  }

  public char getChar(String key) {
    Object o = get(key);
    if (o == null) {
      return Character.MIN_VALUE;
    }
    if (o instanceof Character) {
      return (Character) o;
    }
    return Character.MIN_VALUE;
  }

  public char getChar(String key, char defaultValue) {
    if (get(key) instanceof Character) {
      return getChar(key);
    }
    return defaultValue;
  }

  public long getLong(String key) {
    Object o = get(key);
    if (o == null) {
      return Long.MIN_VALUE;
    }
    if (o instanceof Long) {
      return (long) o;
    }
    if (o instanceof Number) {
      return ((Number) o).longValue();
    }
    String s = "" + o.toString();
    return Long.parseLong(s);
  }
  
  public long getLong(String key, long defaultValue) {
    if (containsKey(key)) {
      return getLong(key);
    }
    return defaultValue;
  }
  
  public short getShort(String key) {
    Object o = get(key);
    if (o == null) {
      return Short.MIN_VALUE;
    }
    if (o instanceof Short) {
      return (short) o;
    }
    if (o instanceof Number) {
      return ((Number) o).shortValue();
    }
    String s = "" + o.toString();
    return Short.parseShort(s);
  }
  
  public short getShort(String key, short defaultValue) {
    if (containsKey(key)) {
      return getShort(key);
    }
    return defaultValue;
  }
  
  public float getFloat(String key) {
    Object o = get(key);
    if (o == null) {
      return Float.NaN;
    }
    if (o instanceof Float) {
      return (float) o;
    }
    if (o instanceof Number) {
      return ((Number) o).floatValue();
    }
    String s = "" + o.toString();
    return Float.parseFloat(s);
  }
  
  public float getFloat(String key, float defaultValue) {
    if (containsKey(key)) {
      return getFloat(key);
    }
    return defaultValue;
  }
  
  public double getDouble(String key) {
    Object o = get(key);
    if (o == null) {
      return Double.NaN;
    }
    if (o instanceof Double) {
      return (double) o;
    }
    if (o instanceof Number) {
      return ((Number) o).doubleValue();
    }
    String s = "" + o.toString();
    return Double.parseDouble(s);
  }
  
  public double getDouble(String key, double defaultValue) {
    if (containsKey(key)) {
      return getDouble(key);
    }
    return defaultValue;
  }
  
  public MapX getMapX(String key) {
    Object o = get(key);
    if (o == null) {
      return null;
    }
    if (o instanceof MapX) {
      return (MapX) o;
    }
    throw new RuntimeException("Not a MapX type");
  }
  
  public MapX getCreateMapX(String key) {
    if (containsKey(key)) {
      return getMapX(key);
    }
    return new MapX();
  }
  
  public ListX getListX(String key) {
    Object o = get(key);
    if (o == null) {
      return null;
    }
    if (o instanceof ListX) {
      return (ListX) o;
    }
    throw new RuntimeException("Not a ListX type");
  }
  
  public ListX getCreateListX(String key) {
    if (containsKey(key)) {
      return getListX(key);
    }
    return new ListX();
  }
  
 //returns missing keys in the map
 public List<String> getMissingKeys(String ...keys) {
   List<String> l = new ArrayList<String>();
   for (String key : keys) {
     if (!containsKey(key)) {
       l.add(key);
     }
   }
   return l;
 }
 

 // returns number of keys copied
 public int copyValues(MapX targetMap, String ...keys) {
   int n = 0;
   for (String key : keys) {
     if (containsKey(key)) {
       targetMap.put(key, get(key));
       n++;
     }
   }
   return n;
 }
 
}
