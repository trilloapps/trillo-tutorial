import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.Loggable;
import com.collager.trillo.util.TrilloFunction;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

public class FetchProducts implements Loggable, TrilloFunction {

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

    LogApi.auditLogInfo("Start of FetchProducts()");

    if(!functionParameters.containsKey("id")){
      return Result.getFailedResult("Missing `id` parameter value in the request");
    }

    String id = MapUtils.getString(functionParameters, "id");

    String className = "shared.common.Customer";

    Object res = DSApi.queryMany(className, " id = '" + id + "' ");

    LogApi.auditLogInfo(res.toString());

    LogApi.auditLogInfo("Start of FetchProducts()");
    return res;
  }

}