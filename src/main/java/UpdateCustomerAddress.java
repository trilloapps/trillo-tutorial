import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.Loggable;
import com.collager.trillo.util.TrilloFunction;
import com.collager.trillo.util.LogApi;

public class UpdateCustomerAddress implements Loggable, TrilloFunction {

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

    LogApi.auditLogInfo("Start of UpdateCustomerAddress()");
    String className = "shared.common.Customer";
    String id = "2";
    String attrName = "address";
    String value = "7, woodland street, NY";
    Object res = DSApi.update(className, id, attrName, value);
    LogApi.auditLogInfo("Start of UpdateCustomerAddress()");
    return res;
  }

}