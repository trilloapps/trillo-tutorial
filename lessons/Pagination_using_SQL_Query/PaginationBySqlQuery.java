import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.collager.trillo.model.DataRequest;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.ServerlessFunction;

public class PaginationBySqlQuery extends ServerlessFunction {
  
  private String SQL_QUERY_TEMPLATE = "SELECT " + 
      "  c.id AS customer_id, " + 
      "  c.firstName AS first_name, " + 
      "  c.lastName AS last_name, " + 
      "  li.id AS line_item_id, " + 
      "  li.description, " + 
      "  li.price " + 
      "FROM " + 
      "  Customer_tbl c " + 
      "JOIN " + 
      "  LineItem_tbl li ON c.id = li.customerId " + 
      "WHERE " + 
      "  c.id = {{{id}}}";
  
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
    dsr.setSql(SQL_QUERY_TEMPLATE);
    
    // we passed incoming parameters in dsr. Trillo Workbench uses them to process the template.
    dsr.setParams(parameters);
    
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
