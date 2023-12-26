import java.util.Map;

import com.collager.trillo.util.Api;
import com.collager.trillo.util.BigQueryApi;
import com.collager.trillo.util.ServerlessFunction;

public class QueryDatasetBigQuery extends ServerlessFunction {

  @Api(httpMethod = "get")
  public Object getBQPage(Map<String, Object> parameters) {
    String query = "" + parameters.get("query");
    return BigQueryApi.getPage(query, 0, 10);
  }
 
}
