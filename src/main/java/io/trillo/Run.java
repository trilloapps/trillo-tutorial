package io.trillo;

import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.BaseApi;
import com.collager.trillo.util.TrilloFunction;
import com.collager.trillo.util.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import io.trillo.util.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Run {

  private static final String ABSOLUTE_PATH = "/Users/saqib/sw/github/trillo-tutorial";
  private static Logger log = LoggerFactory.getLogger(Run.class);
  
  /* 
   * Use the following constants to define the parameters or pass on the command line.
   */
  
  public static void main(String[] args) {
    File file = new File(ABSOLUTE_PATH + "/local_trillo_config2.json");
   if (!file.exists()) {
      System.err.println("Missing tillo_config.json file, create it from sample_trillo_config.json inside the program directory");
      System.exit(-1);
    }
    Map<String, Object> m = Util.fromJSONSFile(file,  new TypeReference<Map<String, Object>>() {});
    if (!verifyConfig(m)) {
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
    executeFunction("SampleFunc", ABSOLUTE_PATH + "/input_files/InputParam.json");
    log.info("Done");
  }

  private static boolean verifyConfig(Map<String, Object> m) {
    boolean verified = true;
    if (!m.containsKey("serverUrl")) {
      System.err.println("Missing 'serverUrl' in the trillo_config.json");
      verified = false;
    }
    
    if (!m.containsKey("userId")) {
      System.err.println("Missing 'userId' in the trillo_config.json");
      verified = false;
    }
    
    if (!m.containsKey("password")) {
      System.err.println("Missing 'password' in the trillo_config.json");
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

  private static void executeFunction(String functionName, String parameterFileName) {
    Map<String, Object> functionParam = new HashMap<String, Object>();
    if (parameterFileName != null) {
      File file = new File(parameterFileName);
      functionParam = Util.fromJSONSFile(file,  new TypeReference<Map<String, Object>>() {});
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
        Object res = ((TrilloFunction) instance).handle(scriptParameter);
        log.info("Result: \n" + BaseApi.asJSONPrettyString(res));
      }
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      log.error("Failed to call function: " + functionName, e);
    }
  }
}
