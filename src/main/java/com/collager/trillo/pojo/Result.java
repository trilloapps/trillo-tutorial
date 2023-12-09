/*
 * Copyright (c)  2018 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */
package com.collager.trillo.pojo;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Result {
  public static final String SUCCESS = "success";
  public static final String FAILED = "failed";
  public static final String UNKNOWN = "unknown";
  
  // name of result for logging or recording (optonal)
  public String _name = null;
  
  private String status = UNKNOWN;
  private String message = null;
  private List<NamedMessage>namedMessages = null;
  private Map<String, Object> props = null;
  private Object data = null;
  private int code = 0;
  private String _rtag = "_r_";
  private List<Object> logs = null;
  static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


  public Result() {

  }

  public Result(String status) {
    this.status = status;
  }

  public Result(String status, String message) {
    this.status = status;
    this.message = message;
  }
  
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
    if (message == null && "failed".equals(status)) {
      message = "Operation failed, please see the errors below";
    }
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<NamedMessage> getNamedMessages() {
    return namedMessages;
  }

  public void addMessage(String name, String message) {
    if (namedMessages == null) {
      namedMessages = new ArrayList<NamedMessage>();
    }
    NamedMessage m = new NamedMessage(name, message);
    namedMessages.add(m);
  }
  
  public String getDetailMessage() {
    String s = message;
    if (s == null) {
      s = "";
    }
    if (namedMessages != null) {
      for (NamedMessage m : namedMessages) {
        s += (s.length() > 0 ? "<br/>" : "") + m.getNameAndMessage();
      }
    }
    return s;
  }
  
  
  public Map<String, Object> getProps() {
    return props;
  }

  public void setProps(Map<String, Object> props) {
    this.props = props;
  }
  
  public void addProp(String name, Object value) {
    if (props == null) {
      props = new HashMap<String, Object>();
    }
    props.put(name, value);
  }
  
  public Object getProp(String name) {
    if (props == null) {
      return null;
    }
    return props.get(name);
  }

  public boolean isFailed() {
    return FAILED.equals(status);
  }
  
  public boolean isSuccess() {
    return SUCCESS.equals(status);
  }
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public static Result makeResult(String status, String message) {
    Result r = new Result();
    r.setStatus(status);
    r.setMessage(message);
    return r;
  }
  
  public static Result makeResult(String status, String message, Object data) {
    Result r = new Result();
    r.setStatus(status);
    r.setMessage(message);
    r.setData(data);
    return r;
  }
  
  public static class NamedMessage {
    private String name;
    private String message;
    public NamedMessage(String name, String message) {
      this.name = name;
      this.message = message;
    }
    public String getName() {
      return name;
    }
    public String getMessage() {
      return message;
    }
    public String getNameAndMessage() {
      return name + " : " + message;
    }
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
  
  public String get_rtag() {
    return _rtag;
  }

  public void set_rtag(String _rtag) {
    // immutable, setter to keep JSON parser happy
  }

  public static Result getInternalError(Exception exc) {
    LOG.error(Arrays.toString(exc.getStackTrace()));
    Result result = new Result();
    result.setStatus(Result.FAILED);
    result.setMessage(exc.getMessage());
    result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    return result;
  }
  
  public static Result getBadRequestError() {
    //LOG.warn("Bad request");
    Result result = new Result();
    result.setStatus(Result.FAILED);
    result.setMessage("Bad request");
    result.setCode(HttpStatus.SC_BAD_REQUEST);
    return result;
  }
  
  public static Result getNotFoundError() {
    //LOG.warn("Not found");
    LOG.debug("Not found");
    Result result = new Result();
    result.setStatus(Result.FAILED);
    result.setMessage("Not found");
    result.setCode(HttpStatus.SC_NOT_FOUND);
    return result;
  }
  
  public static Result getNotYetImplementedError() {
    //LOG.info("Not yet implemented");
    Result result = new Result();
    result.setStatus(Result.FAILED);
    result.setMessage("Not yet implemented");
    result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    return result;
  }
  
  public static Result getBadRequestError(String msg) {
    //LOG.warn(msg);
    Result result = getBadRequestError();
    result.setMessage(msg);
    return result;
  }
  
  public static Result getNotFoundError(String msg) {
    //LOG.warn(msg);
    Result result = getNotFoundError();
    result.setMessage(msg);
    return result;
  }
  
  public static Result getUnauthorizedError() {
    //LOG.warn("Unauthorized");
    Result result = new Result();
    result.setStatus(Result.FAILED);
    result.setMessage("Unauthorized");
    result.setCode(HttpStatus.SC_UNAUTHORIZED);
    return result;
  }
  
  public static Result getUnauthorizedError(String msg) {
    //LOG.warn(msg);
    Result result = getUnauthorizedError();
    result.setMessage(msg);
    return result;
  }
  
  public static Result getSuccessResult() {
    return getSuccessResult("Success", null);
  }
  
  public static Result getSuccessResultWithData(Object data) {
    return getSuccessResult("Success", data);
  }
  
  public static Result getSuccessResult(String message) {
    return getSuccessResult(message, null);
  }
  
  public static Result getSuccessResult(String message, Object data) {
    Result result = new Result();
    result.setStatus(Result.SUCCESS);
    result.setMessage(message);
    result.setData(data);
    result.setCode(HttpStatus.SC_OK);
    return result;
  }
  
  public static Result getFailedResult(String message) {
    //LOG.warn(message);
    Result result = new Result();
    result.setStatus(Result.FAILED);
    result.setMessage(message);
    result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    return result;
  }
  
  public static Result getFailedResult(String message, int code) {
    //LOG.warn(message);
    Result result = new Result();
    result.setStatus(Result.FAILED);
    result.setMessage(message);
    result.setCode(code);
    return result;
  }

  public List<Object> getLogs() {
    return logs;
  }

  public void setLogs(List<Object> logs) {
    this.logs = logs;
  }
  
  public String getName() {
    return _name;
  }
  
  public void setName(String name) {
    _name = name;
  }
}
