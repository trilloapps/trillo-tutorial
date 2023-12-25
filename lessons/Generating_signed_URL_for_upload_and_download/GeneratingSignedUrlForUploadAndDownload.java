package lessons.Generating_signed_URL_for_upload_and_download;

import java.util.Map;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.*;

public class GeneratingSignedUrlForUploadAndDownload extends ServerlessFunction {


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

}
