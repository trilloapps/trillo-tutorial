package io.trillo;

import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.BaseApi;
import com.collager.trillo.util.ServerlessFunction;
import com.collager.trillo.util.TrilloFunction;
import com.collager.trillo.util.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import io.trillo.util.Proxy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RunFunction {
  
  private static Logger log = LoggerFactory.getLogger(RunFunction.class);
  
  // The server, its credentials, function name, function parameters are read from a file.
  // The file name is passed on the command line.
  // We recommend storing on files inside a directory called config (this directory is not checked in).
  // This will avoid accidentally checking in configuration with the credentials to git.
  
  // see the sample generic file for the example of a config file.
  
  private static String DEFAULT_CONFIG_FILE = "sample_run_function_config.json";
  
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    File file = new File(args.length > 0 ? args[0] : DEFAULT_CONFIG_FILE);
    if (!file.exists()) {
      System.err.println("Missing '" + file.getPath() + "'");
      System.exit(-1);
    }
    Map<String, Object> m = Util.fromJSONSFile(file,  new TypeReference<Map<String, Object>>() {});
    if (!verifyConfig(m, file.getPath())) {
      System.exit(-1);
    }
    
    log.info("Starting, server url: " + m.get("serverUrl"));
    Proxy.setArgs(m);
    
    /* create a function under com.serverless.function, or any other java package name
     * (avoiding com.collager and io.trillo packages).
     * Pass function name (including package name if it is not under com.serverless.function),
     * and parameters to passed in a JSON file. Provide the full path name of the
     * file. As a practice you can use ./input_files folder for creating the input file.
     * If the function requires no parameter, pass null.
     */
    
    String functionName = "" + m.get("functionName");
    log.info("Function name: " + functionName);
    String parameterFile = m.get("parameterFile") instanceof String ? (String) m.get("parameterFile") : null;
    Map<String, Object> parameters = m.get("parameters") instanceof Map<?, ?> ? (Map<String, Object>) m.get("parameters") : null;
    executeFunction(functionName, parameterFile, parameters);
    log.info("Done");
  }

  private static boolean verifyConfig(Map<String, Object> m, String configFilePath) {
    boolean verified = true;
    if (!m.containsKey("serverUrl")) {
      System.err.println("Missing 'serverUrl' in " + configFilePath);
      verified = false;
    }
    
    if (!m.containsKey("userId")) {
      System.err.println("Missing 'userId' in " + configFilePath);
      verified = false;
    }
    
    if (!m.containsKey("password")) {
      System.err.println("Missing 'password' in " + configFilePath);
      verified = false;
    }
    
    if (!m.containsKey("functionName")) {
      System.err.println("Missing 'functionName' in " + configFilePath);
      verified = false;
    }
    
    if (!verified) {
      return verified;
    }
    
    if (!m.containsKey("orgName")) {
      System.err.println("Missing 'orgName' in the trillo_config.json, defaulting to 'cloud'");
      m.put("orgName", "cloud");
    }
    
    if (!m.containsKey("appName")) {
      System.err.println("Missing 'appName' in the trillo_config.json, defaulting to 'main'");
      m.put("appName", "main");
    }
    
    return verified;
  }

  private static void executeFunction(String functionName, String parameterFileName, Map<String, Object> parameters) {
    Map<String, Object> functionParam = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(parameterFileName)) {
      File file = new File(parameterFileName);
      functionParam = Util.fromJSONSFile(file,  new TypeReference<Map<String, Object>>() {});
    } else {
      functionParam = parameters;
    }
    if (!Proxy.isLoggedIn()) {
      if (!Proxy.login()) {
        // login failed, it will print the logs
        return;
      }
    }
    //String className = functionName.indexOf(".") > 0 ? functionName : "com.serverless.function." + functionName;
    String className = functionName;
    try {
      Object instance = Class.forName(className).newInstance();
      if (instance instanceof TrilloFunction) {
        ScriptParameter scriptParameter = ScriptParameter.makeScriptParameter(functionParam, null);
        if (instance instanceof ServerlessFunction) {
          ((ServerlessFunction) instance).setRuntimeContextState(scriptParameter);
        }
        Object res = ((TrilloFunction) instance).handle(scriptParameter);
        if (res instanceof String) {
          log.info("Result: \n" + res);
        } else {
          log.info("Result: \n" + BaseApi.asJSONPrettyString(res));
        }
      }
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      log.error("Failed to call function: " + functionName, e);
    }
  }

}
