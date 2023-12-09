package com.collager.trillo.util;

import com.collager.trillo.pojo.ScriptParameter;

public interface TrilloFunction {
  public Object handle(ScriptParameter params);
}
