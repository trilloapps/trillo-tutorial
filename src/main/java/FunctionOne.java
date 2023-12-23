import java.util.Map;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.ServerlessFunction;

public class FunctionOne extends ServerlessFunction {

  @Api(httpMethod="post")
  public Object method1(Map<String, Object> parameters) {
    return parameters;
  }

  @Api(httpMethod="get")
  public Object method2(Map<String, Object> parameters) {
    return parameters;
  }
}
