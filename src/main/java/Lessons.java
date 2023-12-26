import com.collager.trillo.model.DataIterator;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Lessons extends ServerlessFunction {

  
  @Api(httpMethod = "post")
  public Object workflowForDataProcessing(Map<String, Object> parameters) {

    
    return  Result.getFailedResult("Failed");
  }

  public static String decodeBase64(String base64Text) {
    byte[] decodedBytes = Base64.getDecoder().decode(base64Text);
    return new String(decodedBytes, StandardCharsets.UTF_8);
  }


}
