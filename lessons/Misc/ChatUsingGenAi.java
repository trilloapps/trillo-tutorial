import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.GCPGenApi;
import com.collager.trillo.util.TrilloFunction;

import java.util.Map;

public class ChatUsingGenAi implements TrilloFunction {

  public Object handle(ScriptParameter scriptParameter) {
    try {
      return _handle(scriptParameter);
    } catch (Exception e) {
      LogApi.error("Failed", e);
      return Result.getFailedResult(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private Object _handle(ScriptParameter scriptParameter) {
    Map<String, Object> functionParameters = (Map<String, Object>) scriptParameter.getV();

    // save the prompt sent in payload else return failed response
    if (!functionParameters.containsKey("messages")) {
      return Result.getFailedResult("'messages' is missing");
    }

    return chat(functionParameters);

  }

  private Result chat(Map<String, Object> params) {


    Result res = null;

    // call GCPGenApi to send messages
    LogApi.auditLogInfo("Sending request to chat...");
    res = (Result) GCPGenApi.chat(params);

    // in case of any error log it
    if (res.isFailed()) {
      LogApi.auditLogError("There is an error to get response from GCPGenApi Model. Error: " + res.getMessage());
      return Result.getFailedResult("There is an error to get response from GCPGenApi Model");
    }

    LogApi.auditLogInfo("Successfully fetched response", "response", res.getData().toString());
    //return the successful response
    return Result.getSuccessResultWithData(res.getData());

  }


}