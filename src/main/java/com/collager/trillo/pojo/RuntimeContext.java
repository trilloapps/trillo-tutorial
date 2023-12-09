package com.collager.trillo.pojo;

import java.util.HashMap;
import java.util.Map;
import com.collager.trillo.util.MapX;

public class RuntimeContext extends MapX {
  
  private static final long serialVersionUID = 1L;
  
  private long idOfUser;
  private String userId = "guest";
  private String firstName = "";
  private String lastName = "";
  private String email = "";
  private String externalId = "";
  private String role = "";
  private String userOrgName = "";
  private boolean emailVerified = false;
  private String tenantId = "";
  private String tenantName = "";
  private long userOrgId = 0;
  private String pictureUrl = "";
  private String orgName = "";
  private String appName = "";
  private String taskName = "";
  private long executionId = -1;
  private String mobilePhone;
  private Object v = new HashMap<String, Object>(1);
 
  public long getIdOfUser() {
    return idOfUser;
  }
  public void setIdOfUser(long idOfUser) {
    this.idOfUser = idOfUser;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName == null ? "" : firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName == null ? "" : lastName;
  }
  public String getFullName() {
    String s = "";
    if (firstName != null || lastName != null) {
      if (firstName != null) {
        s = firstName;
      }
      if (lastName != null) {
        s += (s.length() > 0 ? " " : "") + lastName;
      }
    } else {
      s = userId;
    }
    return s;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email == null ? "" : email;
  }
  public String getOrgName() {
    return orgName;
  }
  public void setOrgName(String orgName) {
    this.orgName = orgName == null ? "" : orgName;
  }
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName == null ? "" : appName;
  }
  public String getExternalId() {
    return externalId;
  }
  public void setExternalId(String externalId) {
    this.externalId = externalId == null ? "" : externalId;
  }
  public String getRole() {
    return role;
  }
  public void setRole(String role) {
    this.role = role == null ? "" : role;
  }
  public String getUserOrgName() {
    return userOrgName;
  }
  public void setUserOrgName(String userOrgName) {
    this.userOrgName = userOrgName == null ? "" : userOrgName;
  }
  public boolean isEmailVerified() {
    return emailVerified;
  }
  public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
  }
  public String getTenantId() {
    return tenantId;
  }
  public void setTenantId(String tenantId) {
    this.tenantId = tenantId == null ? "" : tenantId;
  }
  public String getTenantName() {
    return tenantName;
  }
  public void setTenantName(String tenantName) {
    this.tenantName = tenantName == null ? "" : tenantName;
  }
  public long getUserOrgId() {
    return userOrgId;
  }
  public void setUserOrgId(long userOrgId) {
    this.userOrgId = userOrgId;
  }
  public String getPictureUrl() {
    return pictureUrl;
  }
  public void setPictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl == null ? "" : pictureUrl;
  }
  public Object getV() {
    return v;
  }
  public void setV(Object v) {
    this.v = v;
  }
  public String getTaskName() {
    return taskName;
  }
  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }
  public long getExecutionId() {
    return executionId;
  }
  public void setExecutionId(long executionId) {
    this.executionId = executionId;
  }
  
  public String getMobilePhone() {
    return mobilePhone;
  }
  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }
  public Map<String, Object>toMap() {
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("idOfUser", idOfUser);
    if (userId != null) m.put("userId",  userId);
    if (firstName != null) m.put("firstName",  firstName);
    if (lastName != null) m.put("lastName",  lastName);
    if (email != null) m.put("email",  email);
    if (orgName != null) m.put("orgName",  orgName);
    if (appName != null) m.put("appName",  appName);
    if (externalId != null) m.put("externalId",  externalId);
    if (role != null) m.put("role",  role);
    if (userOrgName != null) m.put("userOrgName",  userOrgName);
     m.put("emailVerified",  emailVerified);
    if (tenantId != null) m.put("tenantId",  tenantId);
    if (tenantName != null) m.put("tenantName",  tenantName);
    m.put("userOrgId",  userOrgId);
    if (pictureUrl != null) m.put("pictureUrl",  pictureUrl);
    if (v != null) m.put("v", v);
    if (taskName != null) m.put("taskName", taskName);
    m.put("executionId", executionId);
    return m;
  }
}
