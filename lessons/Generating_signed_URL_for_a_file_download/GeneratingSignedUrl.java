import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.*;

public class GeneratingSignedUrl extends ServerlessFunction {

  /*
   Generates signed URL for generating a file. Normally this API is invoked by a client
   to generate a signed URL to download a file.
   */
  @Api(httpMethod = "post")
  public Object generate(Map<String, Object> parameters) {
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
}
