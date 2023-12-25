import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.collager.trillo.model.DataRequest;
import com.collager.trillo.model.Exp;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.ServerlessFunction;

public class PaginatedList extends ServerlessFunction {

  @SuppressWarnings("unchecked")
  @Api(httpMethod="get")
  public Object page(Map<String, Object> parameters) {
    
    DataRequest dsr = new DataRequest();
    // fetch records of class customers
    dsr.setClassName("Customer");
    // page size is 3
    dsr.setSize(3);
    // start index is 0
    dsr.setStart(0);
    
    // create a filter expression which say id < 5
    Exp exp = new Exp();
    exp.setLhs("id");
    exp.setOp("<");
    exp.setRhs(5);
    dsr.setFilter(exp); // comment out this line to fetch all records
    Object r;
    
    List<Object> list = new ArrayList<Object>();
    Map<String, Object> response;
    int totalItems = -1;
    Result result;
    while (true) {
      // returns a Map or Result object (in case of error)
      // if successful, the map contains following keys.
      // totalItems
      // start
      // items of List<Object> type
      r = DSApi.getPage(dsr);
      if (r instanceof Map<?, ?>) {
        response = (Map<String, Object>) r;
        if (totalItems == -1) {
          // save totalITems
          totalItems = (Integer) response.get("totalItems");
        }
        list.add(response.get("items"));
        if (list.size() == totalItems) {
          break;
        }
        
      } else if (r instanceof Result) {
        // returns result if there is failure
        result = (Result) r;
        if (result.isFailed()) {
          LogApi.error("Failed: " + result.getMessage());
        } else {
          LogApi.info("Message: " + result.getMessage());
        }
        break;
      }
    }
    LogApi.info("Number of items fetched: " + list.size());
    return list;
  }
 
}
