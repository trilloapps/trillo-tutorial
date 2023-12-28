import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.ServerlessFunction;

public class AccessRuntimeContext extends ServerlessFunction {

  /*
   The super class ServerlessFunction has reference to the runtime context.
   It provides a set of accessor method to read the values from the context.
   The following example prints a few properties from the runtime context.
   */

  @Api(httpMethod = "get")
  public Object printRuntimeContext(Map<String, Object> parameters) {
    LogApi.info("User id: " + this.getUserId());
    LogApi.info("'id' of user, this is internal database id: " + this.getIdOfUser());
    LogApi.info("Email: " + this.getEmail());
    LogApi.info("Name: " + this.getFullName());
    return Result.getSuccessResult();
  }
}