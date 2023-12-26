import java.util.ArrayList;
import java.util.Map;
import com.collager.trillo.model.DataIterator;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.ServerlessFunction;

public class DataIteratorLesson extends ServerlessFunction {
  
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
  
  @Api(httpMethod = "get")
  public Object iterate(Map<String, Object> parameters) {
    // instantiate a data iterator using SQL template.
    DataIterator dataIterator = DSApi.getDataIterator(1, 3, SQL_QUERY_TEMPLATE);
    // Set parameters into dataIterator's DataRequest.
    dataIterator.getDataRequest().setParams(parameters);
    // Initialize DataIterator.
    Result r = dataIterator.initialize();

    if (r.isFailed()) {
      LogApi.error("Failed to initialize DataIterator", "error", r.getMessage());
      return r;
    }

    Integer totalCount = dataIterator.getTotalItems();

    // print total number of records iterator will iterate through.
    LogApi.auditLogInfo("Total number of items : " + totalCount);
    ArrayList<Map<String, Object>> allItems = new ArrayList<>();

    // iterate through each record.
    while (dataIterator.hasNext()) {
      ArrayList<Map<String, Object>> items = (ArrayList<Map<String, Object>>) dataIterator.getPage();
      allItems.addAll(items);
    }
    return allItems;
  }
}
