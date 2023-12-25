package lessons.New_function_using_Workbench_UI;

import java.util.Map;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.ServerlessFunction;

public class SampleFunc extends ServerlessFunction {

  @Api(httpMethod="post")
  public Object saveValue(Map<String, Object> parameters) {
    return parameters;
  }
  
  @Api(httpMethod="put")
  public Object updateValue(Map<String, Object> parameters) {
    return parameters;
  }
  
  @Api(httpMethod="get")
  public Object getValue(Map<String, Object> parameters) {
    return parameters;
  }
  
  @Api(httpMethod="delete")
  public Object removeOverheads(Map<String, Object> parameters) {
    return parameters;
  }

}
