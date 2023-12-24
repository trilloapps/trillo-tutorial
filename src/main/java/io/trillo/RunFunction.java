package io.trillo;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.BaseApi;
import com.collager.trillo.util.ServerlessFunction;
import com.collager.trillo.util.Util;
import io.trillo.util.Proxy;

public class RunFunction {
  
  private static Logger log = LoggerFactory.getLogger(RunFunction.class);
  
  // The Workbench url, its credentials, function name, function parameters are read from a file.
  // The file name is passed on the command line.
  // Alternatively, url, its credentials can also be read from the environment variable.
  // We strongly recommend using environment variables for credentials to avoid leaking.
  // This also avoids accidentally checking in configuration with the credentials to git.
  
  
  private static String DEFAULT_CONFIG_FILE = "./config/server.json";
  
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    File file = new File(args.length > 0 ? args[0] : DEFAULT_CONFIG_FILE);
    if (!file.exists()) {
      log.error("Missing config file, " + file.getPath());
      System.exit(-1);
    }
    
    // read Trillo Workbench configuration from config/server.json to get URL and function details reference
    Map<String, Object> config = loadFile(file);
    if (config == null) {
      System.exit(-1);
    }
    
    // verify config and add userId and password from the environment.
    if (!verifyAndUpdateConfig(config, file.getPath())) {
      System.exit(-1);
    }
    
    String functionDetailsFileName = (String) config.get("functionDetailsFile");
    File functionDetailsFile = new File(functionDetailsFileName);
    
    if (!functionDetailsFile.exists()) {
      log.error("Missing functionDetailsFile, " + functionDetailsFile.getPath());
      System.exit(-1);
    }
    
    Map<String, Object> functionDetails = loadFile(functionDetailsFile);
    if (functionDetails == null) {
      System.exit(-1);
    }
    if (!verifyFunctionDetails(functionDetails, functionDetailsFile.getPath())) {
      System.exit(-1);
    }
    
    String functionName = (String) functionDetails.get("functionName");
    String methodName = (String) functionDetails.get("methodName");
    log.info("Function name: " + functionName + ", method name: " + methodName);
    
    // find parameters of the function from the configuration files
    Map<String, Object> parameters = (Map<String, Object>) functionDetails.get("parameters");
   
    // the following will connect to the Trillo Workbench specified in the configuration file (url, userId, password).
    Proxy.setArgs(config);
    if (!Proxy.login()) {
      // login failed, it will print the logs
      return;
    }
    
    // execute function
    executeFunction(functionName, methodName, parameters);
    log.info("Done");
  }

  private static Map<String, Object> loadFile(File file) {
    try {
      return Util.fromJSONFileAsMap(file);
    } catch (Exception exc) {
      exc.printStackTrace(System.err);
      log.error("Failed to load file, " + file.getPath());
    }
    return null;
  }

  private static boolean verifyAndUpdateConfig(Map<String, Object> config, String configFilePath) {
    boolean verified = true;
    if (!config.containsKey("serverUrl")) {
      log.error("Missing 'serverUrl' in " + configFilePath);
      verified = false;
    }
    
 
    if (!config.containsKey("functionDetailsFile")) {
      log.error("Missing 'functionDetailsFile' in " + configFilePath);
      verified = false;
    }
    
    // we remove any userId or password defined in config to force their
    // specification via environment variables
    
    config.remove("userId");
    config.remove("password");
    
    // read environment variables
    String userId = System.getenv("TRILLO_WB_USER_ID");
    String password = System.getenv("TRILLO_WB_USER_PASSWORD");
    
    if (userId == null) {
      log.error("Trillo Workbench userId is not specified. Specify it using environment variable TRILLO_WB_USER_ID");
      verified = false;
    }
    
    if (password == null) {
      log.error("Trillo Workbench password is not specified. Specify it using environment variable TRILLO_WB_USER_PASSWORD");
      verified = false;
    }
    
    if (verified) {
      // if configuration is verified, update userId and password read from the environment
      config.put("userId", userId);
      config.put("password", password);
    }
    
    return verified;
  }
  
  private static boolean verifyFunctionDetails(Map<String, Object> functionDetails, String functionDetailsFile) {
    boolean verified = true;
    if (!functionDetails.containsKey("functionName")) {
      log.error("Missing 'functionName' in " + functionDetailsFile);
      verified = false;
    }
    
 
    if (!functionDetails.containsKey("methodName")) {
      log.error("Missing 'methodName' in " + functionDetailsFile);
      verified = false;
    }
    
    if (!functionDetails.containsKey("parameters")) {
      log.error("Missing 'parameters' in " + functionDetailsFile);
      verified = false;
    }
    
    if (!(functionDetails.get("parameters") instanceof Map<?, ?>)) {
      log.error("'parameters' is not a valid jsob object in " + functionDetailsFile);
      verified = false;
    }
    
    return verified;
  }
  
  
  private static void executeFunction(String functionName, String methodName, Map<String, Object> parameters) {
    Map<String, Object> functionParam= parameters;
    
    //String className = functionName.indexOf(".") > 0 ? functionName : "com.serverless.function." + functionName;
    String className = functionName;
    try {
      Object instance = Class.forName(className).newInstance();
      if (instance instanceof ServerlessFunction) {
        ServerlessFunction serverlessFunction = (ServerlessFunction) instance;
        ScriptParameter scriptParameter = ScriptParameter.makeScriptParameter(functionParam, null);
        serverlessFunction.setRuntimeContextState(scriptParameter);
        
        Object params = scriptParameter.getV();
        Object res = callMethod(instance, methodName, params);
        if (res instanceof String) {
          log.info("Result: \n" + res);
        } else {
          log.info("Result: \n" + BaseApi.asJSONPrettyString(res));
        }
      }
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      log.error("Failed to call function: " + functionName);
      e.printStackTrace();
    }
  }
  
  private static Object callMethod(Object receiver, String methodName, Object... params) {
    Class<?> cls = receiver.getClass();
    Method[] methods = cls.getMethods();
    Method toInvoke = null;
    methodLoop: for (Method method : methods) {
      if (!methodName.equals(method.getName())) {
        continue;
      }
      Class<?>[] paramTypes = method.getParameterTypes();
      if (params == null && paramTypes == null) {
        toInvoke = method;
        break;
      } else if (params == null || paramTypes == null || paramTypes.length != params.length) {
        continue;
      }

      for (int i = 0; i < params.length; ++i) {
        if (!paramTypes[i].isAssignableFrom(params[i].getClass())) {
          continue methodLoop;
        }
      }
      toInvoke = method;
      break;
    }
    if (toInvoke != null) {
      try {
        return toInvoke.invoke(receiver, params);
      } catch (Exception t) {
        t.printStackTrace();
        return Result.getFailedResult(t.toString());
      }
    }
    return Result.getFailedResult("Could not find the method to execute");
  }


}
