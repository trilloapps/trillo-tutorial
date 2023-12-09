package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.List;

public class CallLogger {
  private int level = 2;
  private boolean collectCallLogs = false;
  private List<Object> logs = null;
  
  public boolean isDebugOn() {
    return level <= 1;
  }
  public void setDebugLevel(boolean debugOn) {
    if (debugOn) {
      setLevel(1);
    } else {
      unsetLevel(1);
    }
  }
  public boolean isInfoOn() {
    return level <= 2;
  }
  public void setInfoLevel(boolean infoOn) {
    if (infoOn) {
      setLevel(2);
    } else {
      unsetLevel(2);
    }
  }
  public boolean isWarningOn() {
    return level <= 3;
  }
  public void setWarningLevel(boolean warningOn) {
    if (warningOn) {
      setLevel(3);
    } else {
      unsetLevel(3);
    }
  }
  public boolean isErrorOn() {
    return level <= 4;
  }
  public void setErrorLevel(boolean errorOn) {
    if (errorOn) {
      setLevel(4);
    } else {
      unsetLevel(4);
    }
  }
  
  public void debug(String msg) {
    if (!isCollectCallLogs() || level > 1) {
      return;
    }
    addMsg("Debug", msg);
  }
  
  public void info(String msg) {
    if (!isCollectCallLogs() || level > 2) {
      return;
    }
    addMsg("Info", msg);
  }
  
  public void warn(String msg) {
    if (!isCollectCallLogs() || level > 3) {
      return;
    }
    addMsg("warn", msg);
  }
  
  public void error(String msg) {
    if (!isCollectCallLogs() || level > 4) {
      return;
    }
    addMsg("Error", msg);
  }
  
  public List<Object> getLogs() {
    return logs;
  }
  
  private void setLevel(int n) {
    if (level > n) {
      level = n;
    }
  }
  
  private void unsetLevel(int n) {
    if (level <= n) {
      level = n + 1;
    }
  }
  
  private void addMsg(String type, String msg) {
    if (logs == null) {
      logs = new ArrayList<Object>();
    }
    logs.add(type + " : " + msg);
  }
  
  public void setLogLevel(String logLevel) {
    if ("debug".equals(logLevel)) {
      setDebugLevel(true);
    } else if ("info".equals(logLevel)) {
      setInfoLevel(true);
    } else if ("warn".equals(logLevel)) {
      setWarningLevel(true);
    } else if ("error".equals(logLevel)) {
      setErrorLevel(true);
    }
  }
  
  public boolean isCollectCallLogs() {
    return collectCallLogs;
  }
  
  public void setCollectCallLogs(boolean collectCallLogs) {
    this.collectCallLogs = collectCallLogs;
  }
}
