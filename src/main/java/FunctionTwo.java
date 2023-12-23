import com.collager.trillo.util.Api;
import com.collager.trillo.util.ServerlessFunction;

import java.util.Map;

public class FunctionTwo extends ServerlessFunction {
  
  @Api(httpMethod="post")
  public Object method3(Map<String, Object> parameters) {
    return parameters;
  }
  
  @Api(httpMethod="get")
  public Object method4(Map<String, Object> parameters) {
    return parameters;
  }
}
