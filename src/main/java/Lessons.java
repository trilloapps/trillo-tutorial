import com.collager.trillo.model.DataIterator;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lessons extends ServerlessFunction {

  @Api(httpMethod = "get")
  public Object queryUsingDataIterator(Map<String, Object> parameters) {
    DataIterator dataIterator = DSApi.getDataIterator("shared.common.Customer", "", "firstName", 1, 10);
    Result r = dataIterator.initialize();

    if (r.isFailed()) {
      LogApi.error("Failed to initialize DataIterator", "error", r.getMessage());
      return r;
    }

    Integer totalCount = dataIterator.getTotalItems();

    LogApi.auditLogInfo("Total number of items : " + totalCount);
    ArrayList<Map<String, Object>> allItems = new ArrayList<>();

    while (dataIterator.hasNext()) {
      ArrayList<Map<String, Object>> items = (ArrayList<Map<String, Object>>) dataIterator.getPage();
      allItems.addAll(items);
    }
    return allItems;
  }

  @Api(httpMethod = "post")
  public Object readFileFromCloudStorageBucket(Map<String, Object> parameters) {
    if (!parameters.containsKey("bucketName")) {
      return Result.getFailedResult("Missing bucketName");
    }
    if (!parameters.containsKey("sourceFilePath")) {
      return Result.getFailedResult("Missing sourceFilePath");
    }
    String bucketName = "" + parameters.get("bucketName");
    String sourceFilePath = "" + parameters.get("sourceFilePath");

    return StorageApi.readFromBucket(bucketName, sourceFilePath);
  }

  @Api(httpMethod = "post")
  public Object generatingSignedUrlForDownload(Map<String, Object> parameters) {
    if (!parameters.containsKey("bucketName")) {
      return Result.getFailedResult("Missing bucketName");
    }
    if (!parameters.containsKey("sourceFilePath")) {
      return Result.getFailedResult("Missing sourceFilePath");
    }
    String bucketName = "" + parameters.get("bucketName");
    String sourceFilePath = "" + parameters.get("sourceFilePath");

    return StorageApi.getSignedUrl(bucketName, sourceFilePath);
  }

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

  @Api(httpMethod = "get")
  public Object queryDatasetBigQuery(Map<String, Object> parameters) {
    String query = "" + parameters.get("query");
    return BigQueryApi.getPage(query, 0, 10);
  }

  @Api(httpMethod = "post")
  public Object summarizeTextDocumentAi(Map<String, Object> parameters) {
    String text = "" + parameters.get("text");
    return GCPGenApi.summarizeText(text);
  }


}
