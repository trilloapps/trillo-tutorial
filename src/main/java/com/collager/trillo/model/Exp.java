package com.collager.trillo.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({ "op", "lhs", "subExps", "marker"})
public class Exp {
  
  private String marker = ""; // used for debugging, error at a level will return the marker to spot the error
  private String op; // =, >, <, >=, <=, not, and, or, anything else
  private String lhs;
  private Object rhs;
  private List<Exp> subExps = new ArrayList<Exp>();
  
  
  public String getMarker() {
    return marker;
  }
  public void setMarker(String marker) {
    this.marker = marker;
  }
  public String getOp() {
    return op;
  }
  public void setOp(String op) {
    this.op = op;
  }
  public String getLhs() {
    return lhs;
  }
  public void setLhs(String lhs) {
    this.lhs = lhs;
  }
  public Object getRhs() {
    return rhs;
  }
  public void setRhs(Object rhs) {
    this.rhs = rhs;
  }
  
  public List<Exp> getSubExps() {
    return subExps;
  }
  public void setSubExps(List<Exp> subExps) {
    this.subExps = subExps;
  }
  public Result validate() {
    return validate(this);
  }
  
  public static Result validate(Exp exp) {
    Result r;
    String op = exp.op != null ? exp.op.toLowerCase().trim() : "";
    if (op.isEmpty()) {
      return Result.getSuccessResult();
    }
    List<Exp> subExps = exp.subExps;
    switch (op) {
      case "or":
      case "and":
        if (subExps != null && subExps.size() > 0) {
          if (subExps.size() < 2) {
            return Result.getFailedResult("2 or more expressions required, op: \"" + exp.op + "\" and marker: " + exp.marker);
          }
          for (Exp e : subExps) {
            r = e.validate();
            if (r.isFailed()) return r;
          }
        } else {
          return Result.getFailedResult("invalid expression, op: \"" + exp.op + "\" and marker: " + exp.marker);
        }
        break;
      case "not":
        if (subExps != null && subExps.size() > 0) {
          if (subExps.size() != 1) {
            return Result.getFailedResult("Exactly 1 subExps needed, op: \"" + exp.op + "\" and marker: " + exp.marker);
          }
          r = subExps.get(0).validate();
          if (r.isFailed()) return r;
        } else {
          return Result.getFailedResult("invalid expression, op: \"" + exp.op + "\" and marker: " + exp.marker);
        }
        break;
      default : 
        if (subExps != null && subExps.size() > 0) {
          if (subExps.size() != 2) {
            return Result.getFailedResult("Exactly 2 subExps needed, op: \"" + exp.op + "\" and marker: " + exp.marker);
          }
          r = subExps.get(0).validate();
          if (r.isFailed()) return r;
          r = subExps.get(1).validate();
          if (r.isFailed()) return r;
        } else if (exp.lhs != null) {
          Object rhs = exp.rhs;
          /* if (rhs == null) {
            return Result.getFailedResult("rhs is required, op: \"" + exp.op + "\" and marker: " + exp.marker);
          } */
          if (!(rhs instanceof Number || rhs instanceof Boolean || rhs instanceof Character || rhs instanceof String
              || rhs == null)) {
            return Result.getFailedResult("Invalid value of rhs, op: \"" + exp.op + "\" and marker: " + exp.marker);
          }
        } else {
          return Result.getFailedResult("Invalid expression, op: \"" + exp.op + "\" and marker: " + exp.marker);
        }
        break;
    }
    return Result.getSuccessResult();
  }
  
  // assumes exp is validated
  public Map<String, Object> retrieveParameterizedQueryAndValues() {
    Map<String, Object> m = new LinkedHashMap<String, Object>();
    List<Object> values = new ArrayList<Object>();
    List<String> names = new ArrayList<String>();
   
    String sql = parameterizedQueryAndValues(names, values);
    
    m.put("names", names);
    m.put("values", values);
    m.put("sql", sql);
    
    return m;
  }
  
  public String parameterizedQueryAndValues(List<String> names, List<Object> values) {
    String sql = "";
    if (StringUtils.isBlank(op)) {
      return sql;
    }
    if (subExps == null || subExps.size() == 0) {
      names.add(lhs);
      values.add(rhs);
      sql = lhs + " " + op + " ?";
    } else {
      String s;
      if ("not".equalsIgnoreCase(op)) {
        s = subExps.get(0).parameterizedQueryAndValues(names, values);
        sql = "not (" + s + ")";
      } else {      
        for (Exp e : subExps) {
          s = "(" + e.parameterizedQueryAndValues(names, values) + ")";
          if (sql.isEmpty()) {
            sql = s;
          } else {
            sql += " " + op + " " + s;
          }
        }
      }
    }
    return sql;
  }
  
  public static Exp op(String op, String lhs, Object rhs) {
    Exp exp = new Exp();
    exp.op = op;
    exp.lhs = lhs;
    exp.rhs = rhs;
    return exp;
  }
  
  public static Exp and(Exp... subExpsArr) {
    Exp exp = new Exp();
    exp.op = "and";
    if (subExpsArr != null) {
      exp.subExps = Arrays.asList(subExpsArr);
    }
    return exp;
  }
  
  public static Exp or(Exp... subExpsArr) {
    Exp exp = new Exp();
    exp.op = "or";
    if (subExpsArr != null) {
      exp.subExps = Arrays.asList(subExpsArr);
    }
    return exp;
  }
  
  public static Exp not(Exp subExp) {
    Exp exp = new Exp();
    exp.op = "not";
    if (subExp != null) {
      exp.subExps.add(subExp);
    }
    return exp;
  }
  
  public Exp add(Exp... subExpsArr) {
    if (subExpsArr != null) {
      subExps.addAll(Arrays.asList(subExpsArr));
    }
    return this;
  }
  
  public static void main(String[] args) {
    File f = new File("TestExpression.json");
    List<Exp> expressions = Util.fromJSONSFile(f,  new TypeReference<ArrayList<Exp>>() {});
    Result r;
    for (Exp exp : expressions) {
      r = exp.validate();
      System.out.println(Util.asJSONPrettyString(exp));
      System.out.println(r.getMessage());
      
      if (r.isSuccess()) {
        Map<String, Object> parameterizedQueryAndValues = exp.retrieveParameterizedQueryAndValues();
        System.out.println(Util.asJSONPrettyString(parameterizedQueryAndValues));
      }
    }
  }
  
}
