package lessons.Read_File_from_Cloud_Storage_Bucket;
import java.util.Map;

import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.ServerlessFunction;
import com.collager.trillo.util.StorageApi;

public class ReadFileFromCloudStorageBucket extends ServerlessFunction {

  @Api(httpMethod = "post")
  public static Object readFileFromCloudStorageBucket(Map<String, Object> parameters) {
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

}
