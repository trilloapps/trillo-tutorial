package com.collager.trillo.util;

import static com.collager.trillo.util.Util.convertToResult;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;
import com.collager.trillo.pojo.Result;

public class EmailApi extends BaseApi {

  private static String emailEndpoint = "/api/v1.1/email";

  public static Object sendEmail(String appName, String email, String subject, String content, String template,
                                 String fromAlias, Map<String, Object> templateParams) {
   return EmailApi.sendEmail(appName, email, subject, content, template, fromAlias, templateParams, null);
  }
  
  public static Object sendEmail(String appName, String email, String subject, String content,
      String template, String fromAlias, Map<String, Object> templateParams, String senderEmail) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("appName", appName);
    body.put("email", email);
    body.put("subject", subject);
    body.put("content", content);
    body.put("template", template);
    body.put("fromAlias", fromAlias);
    body.put("templateParams", templateParams);
    if (senderEmail != null) {
      body.put("templateParams", senderEmail);
    }
    Object obj =
        HttpRequestUtil.post(emailEndpoint + "/sendEmail", new JSONObject(body).toString());
    return convertToResult(obj);
  }

  public static Result sendEmail(
      final String toEmail,
      final String subject,
      String content) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("toEmail", toEmail);
    body.put("subject", subject);
    body.put("content", content);
    Object obj= HttpRequestUtil.post(emailEndpoint + "/sendEmail", new JSONObject(body).toString());
    return convertToResult(obj);
  }

  
  public static Result sendEmailMarkDownContent(String mailTo, String subject, String content) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("mailTo", mailTo);
    body.put("subject", subject);
    body.put("content", content);
    Object obj= HttpRequestUtil.post(emailEndpoint + "/sendEmailMarkDownContent", new JSONObject(body).toString());
    return convertToResult(obj);
  }

  public static Map<String, Object> getEmailProps(String templateName) {
    return remoteCallAsMap("EmailApi", "getEmailProps", templateName);
  }

  public static boolean emailTemplateExists(String templateName) {
    return remoteCallAsBoolean("EmailApi", "emailTemplateExists", templateName);
  }

  public static String getEmailTemplateContent(String templateName) {
    return remoteCallAsString("EmailApi", "getEmailTemplateContent", templateName);
  }

  public static String getProcessedEmailContentFromTemplate(String templateName,
      Map<String, Object> emailParams) {
    return remoteCallAsString("EmailApi", "getProcessedEmailContentFromTemplate", templateName, emailParams);
  }

  public static String getSubject(String templateName, String defaultSubject,
      Map<String, Object> emailParams) {
    return remoteCallAsString("EmailApi", "getSubject", templateName, defaultSubject, emailParams);
  }

  public static String getRedirectUrlWithPath(String templateName, String url) {
    return remoteCallAsString("EmailApi", "getRedirectUrlWithPath", templateName, url);
  }

  public static boolean sendEmailUsingFunction(String functionName,
      Map<String, Object> emailParams) {
    return remoteCallAsBoolean("EmailApi", "sendEmailUsingFunction", functionName, emailParams);
  }
}


