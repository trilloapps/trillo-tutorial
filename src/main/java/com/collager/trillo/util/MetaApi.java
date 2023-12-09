package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.collager.trillo.model.ClassM;
import com.collager.trillo.model.DataSourceM;
import com.collager.trillo.pojo.Result;

public class MetaApi extends BaseApi {
  private static String metaEndpoint = "/api/v1.1/meta";

  public static String getAppDataDir() {
    return remoteCallAsString("CoreMetaApi", "getAppDataDir");
  }

  public static ClassM getClass(String className) {
    return getClassM(className);
  }

  @SuppressWarnings("unchecked")
  @Deprecated
  public static ClassM getClassM(String className) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getClassM?className=" + className);
    if (res instanceof Map<?, ?>) {
      Map<String, Object> m = (Map<String, Object>) res;
      return Util.fromMap(m, ClassM.class);
    }
    return null;
  }

  public static String getLocalPath(String bucketPath) {
    return remoteCallAsString("CoreMetaApi", "getLocalPath");
  }

  public static List<Map<String, Object>> getSchemaForDataStudio(String className,
                                                                 boolean includeAllSysAttrs) {
    return remoteCallAsListOfMaps("CoreMetaApi", "getSchemaForDataStudio",
      className, includeAllSysAttrs);
  }

  public static String getDomainFileAsString(String fileName) {
    return remoteCallAsString("CoreMetaApi", "getDomainFileAsString",
      fileName);
  }

  public static Map<String, Object> getDomainFileAsMap(String fileName) {
    return remoteCallAsMap("CoreMetaApi", "getDomainFileAsMap",
      fileName);
  }

  public static List<Map<String, Object>> getDomainFileAsList(String fileName) {
    return remoteCallAsListOfMaps("CoreMetaApi", "getDomainFileAsList",
      fileName);
  }

  public static boolean existsDomainFile(String fileName) {
    return remoteCallAsBoolean("CoreMetaApi", "existsDomainFile",
      fileName);
  }

  public static boolean existsFunction(String functionName) {
    return remoteCallAsBoolean("CoreMetaApi", "existsFunction",
        functionName);
  }
  
  public static boolean existsClass(String className) {
    return remoteCallAsBoolean("CoreMetaApi", "existsClass",
        className);
  }
  
  
    @SuppressWarnings("unchecked")
  public static List<ClassM> getClasses(String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getClasses?filter=" + filter);
    List<ClassM> cl = new ArrayList<ClassM>();
    if (res instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) res;
      for (Map<String, Object> m : l) {
        cl.add(Util.fromMap(m, ClassM.class));
      }
    }
    return cl;
  }
  
  public static List<ClassM> getClasses(String dsName, String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getClasses?filter=" + filter + "&dsName=" + dsName);
    return convertToClassList(res);
  }
  
  public static List<ClassM> getClasses(String dsName, String schemaName, String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getClasses?filter=" + filter + "&dsName=" + dsName + "&schemaName=" + schemaName);
    return convertToClassList(res);
  }
  
  @SuppressWarnings("unchecked")
  public static List<String> getClassNames(String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getClassNames?filter=" + filter);
    if (res instanceof List<?>) {
      return (List<String>) res;
    }
    return new ArrayList<String>();
  }
  
  @SuppressWarnings("unchecked")
  public static List<String> getClassNames(String dsName, String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getClassNames?filter=" + filter + "&dsName=" + dsName);
    if (res instanceof List<?>) {
      return (List<String>) res;
    }
    return new ArrayList<String>();
  }
  
  @SuppressWarnings("unchecked")
  public static List<String> getClassNames(String dsName, String schemaName, String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getClassNames?filter=" + filter + "&dsName=" + dsName + "&schemaName=" + schemaName);
    if (res instanceof List<?>) {
      return (List<String>) res;
    }
    return new ArrayList<String>();
  }


  
  public static List<DataSourceM> getDataSources(String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getDataSources?filter=" + filter);
    return convertToDataSourceList(res);
  }
  
  @SuppressWarnings("unchecked")
  public static List<String> getDataSourceNames(String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getDataSourceNames?filter=" + filter);
    if (res instanceof List<?>) {
      return (List<String>) res;
    }
    return new ArrayList<String>();
  }
  
  public static List<DataSourceM> getDataSources(String appName, String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getDataSources?filter=" + filter + "&appName=" + appName);
    return convertToDataSourceList(res);
  }
  
  @SuppressWarnings("unchecked")
  public static List<String> getDataSourceNames(String appName, String filter) {
    Object res = HttpRequestUtil.get(metaEndpoint + "/getDataSourceNames?filter=" + filter + "&appName=" + appName);
    if (res instanceof List<?>) {
      return (List<String>) res;
    }
    return new ArrayList<String>();
  }

  
  public static Object saveClass(String className, ClassM clsM) {
    Object res = HttpRequestUtil.post(metaEndpoint + "/saveClass", clsM);
    return res;
  }
  
  public static Object updateClassVisibility(String className, String visibility) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("className", className);
    body.put("visibility", visibility);
    Object res = HttpRequestUtil.post(metaEndpoint + "/updateClassVisibility", new JSONObject(body).toString());
    return res;
  }
  


  public static Object getKeyValue(String key) {
    return remoteCall("CoreMetaApi", "getKeyValue", key);
  }
  
  public static Object getKeyValue(String key, String type) {
    return remoteCall("CoreMetaApi", "getKeyValue", key, type);
  }

  public static Object makeLogicalClassFromData(String className, Map<String, Object> data) {
    return remoteCall("CoreMetaApi", "makeLogicalClassFromData", className, data);
  }

  public static String getScript(String functionName) {
    return remoteCallAsString("CoreMetaApi", "getScript", functionName);
  }

  public static String getScriptFlavor(String functionName) {
    return remoteCallAsString("CoreMetaApi", "getScriptFlavor", functionName);
  }
  
  public static Object makeClassFromData(String className, String tableName, String pkAttrName, Map<String, Object> data) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("className", className);
    body.put("tableName", tableName);
    body.put("pkAttrName", pkAttrName);
    body.put("data", data);
    Object res = HttpRequestUtil.post(metaEndpoint + "/makeClassFromData", new JSONObject(body).toString());
    return res;
  }
  
  public static Object makeClassFromData(String className, String tableName, String pkAttrName, 
      Map<String, Object> data, Map<String, String> attrTypeByNames) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("className", className);
    body.put("tableName", tableName);
    body.put("pkAttrName", pkAttrName);
    body.put("data", data);
    body.put("attrTypeByNames", attrTypeByNames);
    Object res = HttpRequestUtil.post(metaEndpoint + "/makeClassFromData", new JSONObject(body).toString());
    return res;
  }
  
  public static Object makeClassFromData(String className, String tableName, String pkAttrName,
      Map<String, Object> data, boolean skipSaving) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("className", className);
    body.put("tableName", tableName);
    body.put("pkAttrName", pkAttrName);
    body.put("data", data);
    body.put("skipSaving", skipSaving);
    Object res = HttpRequestUtil.post(metaEndpoint + "/makeClassFromData", new JSONObject(body).toString());
    return res;
  }
  
  public static Object makeClassFromData(String className, String tableName, String pkAttrName, 
      Map<String, Object> data, Map<String, String> attrTypeByNames, boolean skipSaving) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("className", className);
    body.put("tableName", tableName);
    body.put("pkAttrName", pkAttrName);
    body.put("data", data);
    body.put("attrTypeByNames", attrTypeByNames);
    body.put("skipSaving", skipSaving);
    Object res = HttpRequestUtil.post(metaEndpoint + "/makeClassFromData", new JSONObject(body).toString());
    return res;
  }
  
  public static Object makeClassFromData(String className, String tableName, String pkAttrName, 
      Map<String, Object> data, Map<String, String> attrTypeByNames, boolean skipSaving, boolean addSystemAttrs) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("className", className);
    body.put("tableName", tableName);
    body.put("pkAttrName", pkAttrName);
    body.put("data", data);
    body.put("attrTypeByNames", attrTypeByNames);
    body.put("skipSaving", skipSaving);
    body.put("addSystemAttrs", addSystemAttrs);
    Object res = HttpRequestUtil.post(metaEndpoint + "/makeClassFromData", new JSONObject(body).toString());
    return res;
  }
  
  @SuppressWarnings("unchecked")
  private static List<ClassM> convertToClassList(Object res) {
    List<ClassM> cl = new ArrayList<ClassM>();
    if (res instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) res;
      for (Map<String, Object> m : l) {
        cl.add(Util.fromMap(m, ClassM.class));
      }
    }
    return cl;
  }
  
  @SuppressWarnings("unchecked")
  private static List<DataSourceM> convertToDataSourceList(Object res) {
    List<DataSourceM> dsList = new ArrayList<DataSourceM>();
    if (res instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) res;
      for (Map<String, Object> m : l) {
        dsList.add(Util.fromMap(m, DataSourceM.class));
      }
    }
    return dsList;
  }

  public static Object getMetadata(String id) {
    return remoteCallAsString("MetaApi", "getMetadata", id);
  }

  public static Object createMetadata(Map<String, Object> md) {
    return remoteCall("MetaApi", "createMetadata", md);
  }

  public static Object saveMetadata(String appName, String dsName, String schemaName, ClassM clsM) {
    return remoteCall("MetaApi", "saveMetadata", appName,
      dsName, schemaName, clsM);
  }

  public static Object saveMetadata(Map<String, Object> md) {
    return remoteCall("MetaApi", "saveMetadata", md);
  }

  public static Result createFunctionSysTask(String mdId) {
    return remoteCallAsResult("MetaApi", "createFunctionSysTask", mdId);
  }

  public static Result createFunctionSysTask(String orgName, String name) {
    return remoteCallAsResult("MetaApi", "createFunctionSysTask", orgName, name);
  }
}


