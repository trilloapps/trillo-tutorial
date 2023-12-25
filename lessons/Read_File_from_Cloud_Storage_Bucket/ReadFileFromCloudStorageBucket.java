import java.util.Map;
import org.bson.internal.Base64;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.ServerlessFunction;
import com.collager.trillo.util.StorageApi;

public class ReadFileFromCloudStorageBucket extends ServerlessFunction {

  /*
   * The following function is an example of reading a file from cloud storage bucket. If result is
   * success then its 'data' attribute gives the content of file 
   * as byte array (or base64 encoded string due to JSON serialization).
   */
  @Api(httpMethod = "post")
  public static Object readFile(Map<String, Object> parameters) {
    if (!parameters.containsKey("bucketName")) {
      return Result.getFailedResult("Missing bucketName");
    }
    if (!parameters.containsKey("sourceFilePath")) {
      return Result.getFailedResult("Missing sourceFilePath");
    }
    String bucketName = "" + parameters.get("bucketName");
    String sourceFilePath = "" + parameters.get("sourceFilePath");

    Result result = StorageApi.readFromBucket(bucketName, sourceFilePath);

    if (result.isFailed()) {
      LogApi.error("Failed to read the file, error: " + result.getMessage());
    } else {
      byte[] byteArray;
      if (result.getData() instanceof byte[]) {
        byteArray = (byte[]) result.getData();
      } else {
        byteArray = Base64.decode((String) result.getData());
      }
      LogApi.info("Number of bytes read: " + byteArray.length);
    }
    return result;
  }
}
