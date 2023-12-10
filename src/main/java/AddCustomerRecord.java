import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.Loggable;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.TrilloFunction;


import java.util.HashMap;
import java.util.Map;


public class AddCustomerRecord implements Loggable, TrilloFunction {

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

    LogApi.auditLogInfo("Start of AddCustomerRecord()");
    String className = "shared.common.Customer";
    Map<String, Object> entity = new HashMap<>();
    entity.put("name", "Lora");
    entity.put("email", "lora@gmail.com");
    entity.put("address", "3, woodland road, NY");
    entity.put("phone", "+16833707839");
    Object res = DSApi.save(className, entity);
    LogApi.auditLogInfo("End of AddCustomerRecord()");
    return res;
  }

}