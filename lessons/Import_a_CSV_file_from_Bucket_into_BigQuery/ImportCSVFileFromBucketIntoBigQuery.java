package lessons.Import_a_CSV_file_from_Bucket_into_BigQuery;

import java.util.List;
import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.*;

public class ImportCSVFileFromBucketIntoBigQuery extends ServerlessFunction {


  @Api(httpMethod = "post")
  public Object importCSVFileFromBucketIntoBigQuery(Map<String, Object> parameters) {
    if(!parameters.containsKey("datasetName")){
      return Result.getFailedResult("Missing datasetName");
    }
    if(!parameters.containsKey("tableName")){
      return Result.getFailedResult("Missing tableName");
    }
    if(!parameters.containsKey("sourceUri")){
      return Result.getFailedResult("Missing sourceUri");
    }
    if(!parameters.containsKey("schema")){
      return Result.getFailedResult("Missing schema");
    }
    String datasetName = "" + parameters.get("datasetName");
    String tableName = "" + parameters.get("tableName");
    String sourceUri = "" + parameters.get("sourceUri");
    List<Map<String, Object>> schema = (List<Map<String, Object>>) parameters.get("schema");
    int numberOfRowsToSkip = 1;


    Result res = BigQueryApi.importCSVbyURIIntoTable(datasetName, tableName, sourceUri, schema, numberOfRowsToSkip);

    if(res.isSuccess()){
      return Result.getSuccessResult("Successfully loaded data");
    }
    return res;

  }
 
}
