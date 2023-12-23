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


  private static String DEFAULT_CONFIG_FILE = "./parameters/configuration.json";

  public static void main(String[] args) {
    File file = new File(args.length > 0 ? args[0] : DEFAULT_CONFIG_FILE);
    if (!file.exists()) {
      log.error("Missing '" + file.getPath() + "'");
      System.exit(-1);
    }
    Map<String, Object> configuration = Util.fromJSONFileAsMap(file);
    if (!verifyConfig(configuration, file.getPath())) {
      System.exit(-1);
    }

    String functionName = (String) configuration.get("functionName");
    String currentMethod = (String) configuration.get("currentMethod");
    log.info("Function name: " + functionName + ", method name: " + currentMethod);

    // find parameters of the function from the configuration files
    Map<String, Object> parameters = getParameters(configuration, currentMethod);
    if (parameters == null) {
      log.error("Missing parameters");
      System.exit(-1);
    }


    // the following will connect to the Trillo Workbench specified in the configuration file (url, userId, password).
    Proxy.setArgs(configuration);
    if (!Proxy.login()) {
      // login failed, it will print the logs
      return;
    }

    // execute function
    executeFunction(functionName, currentMethod, parameters);
    log.info("Done");
  }

  private static boolean verifyConfig(Map<String, Object> m, String configFilePath) {
    boolean verified = true;
    if (!m.containsKey("serverUrl")) {
      log.error("Missing 'serverUrl' in " + configFilePath);
      verified = false;
    }

    if (!m.containsKey("userId")) {
      log.error("Missing 'userId' in " + configFilePath);
      verified = false;
    }

    if (!m.containsKey("password")) {
      log.error("Missing 'password' in " + configFilePath);
      verified = false;
    }

    if (!m.containsKey("functionName")) {
      log.error("Missing 'functionName' in " + configFilePath);
      verified = false;
    }

    if (!m.containsKey("currentMethod")) {
      log.error("Missing 'currentMethod' in " + configFilePath);
      verified = false;
    }

    return verified;
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> getParameters(Map<String, Object> configuration,
                                                   String currentMethod) {
    if (!(configuration.get("methods") instanceof Map<?, ?>)) {
      return null;
    }
    Map<String, Object> methods = (Map<String, Object>) configuration.get("methods");


    return (Map<String, Object>) methods.get(currentMethod);
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
