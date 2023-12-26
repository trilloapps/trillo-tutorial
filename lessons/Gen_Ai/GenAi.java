import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.*;

import java.util.HashMap;
import java.util.Map;

public class GenAi extends ServerlessFunction {

  @Api(httpMethod = "post")
  public Object summarizeText(Map<String, Object> parameters) {
    String text = "" + parameters.get("text");
    return GCPGenApi.summarizeText(text);
  }

  @Api(httpMethod = "post")
  public Object chat(Map<String, Object> parameters) {

    // check if messages is present in input parameters
    if (!parameters.containsKey("messages")) {
      return Result.getFailedResult("'messages' is missing");
    }
    Result res = null;

    // call GCPGenApi to send messages
    LogApi.auditLogInfo("Sending request to chat...");
    res = (Result) GCPGenApi.chat(parameters);

    // in case of any error log it
    if (res.isFailed()) {
      LogApi.auditLogError("There is an error to get response from GCPGenApi Model. Error: " + res.getMessage());
      return Result.getFailedResult("There is an error to get response from GCPGenApi Model");
    }

    LogApi.auditLogInfo("Successfully fetched response");
    //return the successful response

    Map<String, Object> responseMessages = new HashMap<>();
    responseMessages.put("messages", ((Map<String, Object>) res.getData()).get("messages"));
    return Result.getSuccessResultWithData(responseMessages);

  }

}
