package lessons.Write_a_workflow;

import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.Api;
import lessons.Read_File_from_Cloud_Storage_Bucket.ReadFileFromCloudStorageBucket;
import lessons.Using_a_Document_AI_Processor.UsingDocumentAiProcessor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class WriteWorkflow {

  @Api(httpMethod = "post")
  public Object workflowForDataProcessing(Map<String, Object> parameters) {

    //In this workflow, we will read a file from bucket, extract the content, then summarize the text and return

    //read a file from GCP bucket
    Object res = ReadFileFromCloudStorageBucket.readFileFromCloudStorageBucket(parameters);
    if(res instanceof Result){
      if(((Result)res).isSuccess()){
        String text = decodeBase64("" + ((Result)res).getData());
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);

        //summarize text
        return UsingDocumentAiProcessor.summarizeTextDocumentAi(params);
      }
    }
    return  Result.getFailedResult("Failed");
  }

  public static String decodeBase64(String base64Text) {
    byte[] decodedBytes = Base64.getDecoder().decode(base64Text);
    return new String(decodedBytes, StandardCharsets.UTF_8);
  }

}
