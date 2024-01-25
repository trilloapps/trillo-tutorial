package com.collager.trillo.op;

import java.util.Map;
import com.collager.trillo.pojo.Result;

@FunctionalInterface
public interface Worker {
  Result perfrom(Map<String, Object> params);
}
