import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.ServerlessFunction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FunctionToCall extends ServerlessFunction {


  @Api(httpMethod = "get")
  public Object queryRecordsFromDSById(Map<String, Object> parameters) {
    String id = "" + parameters.get("id");
    String tableName = "" + parameters.get("tableName");
    return DSApi.get("shared.common." + tableName, id);
  }

  @Api(httpMethod = "post")
  public Object saveManyRecordsInDs(Map<String, Object> parameters) {
    List<Map<String, Object>> records = (List<Map<String, Object>>) parameters.get("records");
    String tableName = "" + parameters.get("tableName");
    return DSApi.saveManyMapListIgnoreError("shared.common." + tableName, records);
  }
}