import com.collager.trillo.pojo.Result;
import com.collager.trillo.pojo.ScriptParameter;
import com.collager.trillo.util.GCPGenApi;
import com.collager.trillo.util.LogApi;
import com.collager.trillo.util.MetaApi;
import com.collager.trillo.util.TrilloFunction;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

public class GenerateImageUsingGenAi implements TrilloFunction {
  private static String configFile = "DemoConfig.json";

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

    //fetch information from metadata files
    Map<String, Object> configs =
      MetaApi.getDomainFileAsMap("shared/domainMeta/files/" + configFile);

    // save the prompt sent in payload else return failed response
    if (!functionParameters.containsKey("prompt")) {
      return Result.getFailedResult("'prompt' is missing");
    }
    String prompt = MapUtils.getString(functionParameters, "prompt");

    // save the bucket and folder name to store the image from config file
    String bucket = MapUtils.getString(configs, "bucket", "trillo-tutorial");
    String defaultImageGenerateFolder = MapUtils.getString(configs, "defaultImageGenerateFolder", "GeneratedImages");

    return generateImage(prompt, bucket, defaultImageGenerateFolder);

  }

  private Result generateImage(String prompt, String gcsBucket, String gcsImageGenerateFolder) {


    Result res = null;

    // create a  folder path to store the bunch of images generated.
    String folderPath = "gs://" + gcsBucket + "/" + gcsImageGenerateFolder + "/";

    // call GCPGenApi to generate image by sending prompt, totalImageCount, and GCS location to store that image.
    LogApi.auditLogInfo("Sending request to generate image...");
    res = (Result) GCPGenApi.generateImageInBucket(prompt,
      2, folderPath);

    // in case of any error log it
    if (res.isFailed()) {
      LogApi.auditLogError("There is an error to get response from GenAI Model. Error: " + res.getMessage());
      return Result.getFailedResult("There is an error to get response from GenAI Model");
    }

    LogApi.auditLogInfo("Successfully saved image(s)", "response", res.getData().toString());
    //return the successful response
    return Result.getSuccessResultWithData(res.getData());

  }


}