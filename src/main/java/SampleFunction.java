import com.collager.trillo.util.Api;
import com.collager.trillo.util.ServerlessFunction;

import java.util.Map;

public class SampleFunction extends ServerlessFunction {

  @Api(httpMethod="post")
  public Object postMethodChangeMe(Map<String, Object> parameters) {
    return parameters;
  }


  @Api(httpMethod="get")
  public Object getMethodChangeMe(Map<String, Object> parameters) {
    return parameters;
  }

}
