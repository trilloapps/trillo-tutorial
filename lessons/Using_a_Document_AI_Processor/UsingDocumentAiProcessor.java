package lessons.Using_a_Document_AI_Processor;

import com.collager.trillo.util.Api;
import com.collager.trillo.util.GCPGenApi;

import java.util.Map;

public class UsingDocumentAiProcessor {

  @Api(httpMethod = "post")
  public static Object summarizeTextDocumentAi(Map<String, Object> parameters) {
    String text = "" + parameters.get("text");
    return GCPGenApi.summarizeText(text);
  }
}
