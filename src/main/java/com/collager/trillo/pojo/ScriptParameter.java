package com.collager.trillo.pojo;

import java.util.Map;
import io.trillo.util.Proxy;

public class ScriptParameter extends RuntimeContext {
  
  private static final long serialVersionUID = 1L;

  public static ScriptParameter makeScriptParameter(Object inputData, Map<String, Object> stateMap) {
    return makeScriptParameter(inputData, stateMap, null, -1);
  }
  
  @SuppressWarnings("unchecked")
  public static ScriptParameter makeScriptParameter(Object inputData, Map<String, Object> stateMap, String taskName, long executionId) {
    ScriptParameter p = new ScriptParameter();
    p.setV(inputData);
    if (stateMap != null) {
      p.putAll(stateMap);
    }
    p.setTaskName(taskName);
    p.setExecutionId(executionId);
    Map<String, Object> loginResponse = Proxy.getLoginResponse();
    
    if (loginResponse.get("user") instanceof Map<?, ?>) {
      Map<String, Object> um = (Map<String, Object>) loginResponse.get("user");
      p.setIdOfUser(um.containsKey("id") ? Long.parseLong("" + um.get("id")) : -1);
      p.setUserId((String)um.get("userId"));
      p.setFirstName((String)um.get("firstName"));
      p.setLastName((String)um.get("lastName"));
      p.setEmail((String)um.get("email"));
      p.setUserOrgName((String)um.get("orgName"));
      p.setExternalId((String)um.get("externalId"));
      p.setRole((String)um.get("role"));
      p.setEmailVerified(Boolean.parseBoolean("" + um.get("emailVerified")));
      p.setTenantId((String)um.get("tenantId"));
      p.setTenantName((String)um.get("tenantName"));
      p.setUserOrgId(um.containsKey("orgId") ? Long.parseLong("" + um.get("orgId")) : -1);
    }
   
    p.setAppName(Proxy.getAppName());
    p.setOrgName(Proxy.getOrgName());
    return p;
  }

}
