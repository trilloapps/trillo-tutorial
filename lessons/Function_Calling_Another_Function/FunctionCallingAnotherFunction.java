import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.*;

import java.util.Map;
import java.util.UUID;

public class FunctionCallingAnotherFunction extends ServerlessFunction {

  /*
   This function calls another function in 2 way- synchronously and asynchronously.
   First calls FunctionToCall.queryRecordsFromDSById() method to query records for an id synchronously using
   FuncApi.executeFunctionWithMethod()

   Then it calls FunctionToCall.saveManyRecordsInDs() method to save bulk records asynchronously using
   FuncApi.executeFunctionWithMethod()
   */

  @Api(httpMethod = "post")
  public Object saveAndQueryRecordInDS(Map<String, Object> parameters) {


    Result resForQuery = FuncApi.executeFunctionWithMethod("FunctionToCall", "queryRecordsFromDSById", parameters);
    if(resForQuery.isSuccess()){
      LogApi.auditLogInfo(resForQuery.getData().toString());
      String taskName = "sampleTask";
      String taskType = "background";
      String sourceUid = UUID.randomUUID().toString();
      String functionName = "FunctionToCall";
      String methodName = "queryRecordsFromDSById";
      Result resSave = FuncApi.createTaskBySourceUid2(taskName, taskType, sourceUid, functionName, methodName, parameters);
      return resSave;
    }
    return resForQuery;

  }
}