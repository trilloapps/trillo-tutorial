import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.Loggable;
import com.collager.trillo.util.TrilloFunction;

import java.util.Map;


public class SampleFunc implements Loggable, TrilloFunction {

  public Object handle(ScriptParameter scriptParameter) {

    // do the implementation inside _handle()
    try {
      return _handle(scriptParameter);
    } catch (Exception e) {
      log().error("Failed", e);
      return Result.getFailedResult(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private Object _handle(ScriptParameter scriptParameter) {
    Map<String, Object> functionParameters = (Map<String, Object>) scriptParameter.getV();

    return functionParameters;
  }

}