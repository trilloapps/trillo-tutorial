import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.DSApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.Loggable;
import com.collager.trillo.util.TrilloFunction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class FetchOrdersByQuery implements Loggable, TrilloFunction {

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

    LogApi.auditLogInfo("Start of FetchOrdersByQuery()");

    String tableName = "Order_tbl";

    String lastMonthDate = getCurrentDateMinusOneMonth();

    Object res = DSApi.queryMany("SELECT * FROM " + tableName + " where orderDate > " + lastMonthDate +
      " and totalAmount > 3000");

    LogApi.auditLogInfo(res.toString());

    LogApi.auditLogInfo("Start of FetchOrdersByQuery()");
    return res;
  }

  private static String getCurrentDateMinusOneMonth(){
    // Get the current date
    LocalDate currentDate = LocalDate.now();

    // Subtract 1 month
    LocalDate lastMonthDate = currentDate.minusMonths(1);

    // Define the desired date format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Format the dates
    String lastMonthDateString = lastMonthDate.format(formatter);

    return lastMonthDateString;

  }

}