import java.util.LinkedHashMap;
import java.util.Map;
import com.collager.trillo.gcp.utils.GCPRestApi;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import com.collager.trillo.util.DocApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.ServerlessFunction;
import com.collager.trillo.util.StorageApi;

public class DocumentAIProcessor extends ServerlessFunction {

  /*
   This function reads a file from the bucket and processes it using a Document AI Processor
   */
  @SuppressWarnings("unchecked")
  @Api(httpMethod = "post")
  public Object process(Map<String, Object> parameters) {
    if (!parameters.containsKey("bucketName")) {
      return Result.getFailedResult("Missing bucketName");
    }
    if (!parameters.containsKey("sourceFilePath")) {
      return Result.getFailedResult("Missing sourceFilePath");
    }
    String bucketName = "" + parameters.get("bucketName");
    String sourceFilePath = "" + parameters.get("sourceFilePath");

    // reads file content from the bucket
    Result result = StorageApi.readFromBucket(bucketName, sourceFilePath);

    Object content;
    if (result.isFailed()) {
      LogApi.error("Failed to read the file, error: " + result.getMessage());
      return result;
    } else {
      content = result.getData();
    }
            
    // Retrieves processor url
    String url = DocApi.getUrl("Invoice", false);
    
    if (url == null) {
      return Result.getFailedResult("Failed to find process url");
    }
    
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("skipHumanReview", true);
    Map<String, Object> inlineDocument = new LinkedHashMap<String, Object>();
    body.put("rawDocument", inlineDocument);
    inlineDocument.put("mimeType", "application/pdf");
    inlineDocument.put("content", content);
    
    // Using processor URL processes content
    Object res = GCPRestApi.post(url, body);
    Object data = null;
    if (res instanceof Result) {
      result = (Result)res;
      if (result.isFailed()) {
        LogApi.error("Failed to process file: " + result.getMessage());
        return result;
      }
      data = result.getData();
    } else {
      data = res;
    }

    if (!(data instanceof Map<?, ?>)) {
      LogApi.error("Failed to process file, invalid response type");
      return result;
    }

    Map<String, Object> responseDocument = (Map<String, Object>) data;
    if (responseDocument.get("document") instanceof Map<?, ?>) {
      responseDocument = (Map<String, Object>) responseDocument.get("document");
    }
    
    // return processor response
    return responseDocument;
    
  }
}
