/*
 * Copyright (c)  2020 Trillo Inc.
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO INC.
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 *
 */

package com.collager.trillo.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.collager.trillo.pojo.DocParams;
import com.collager.trillo.pojo.DocSchema;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.DataObject.Document;
import com.collager.trillo.util.DataObject.Page;

public class DocApi extends BaseApi {

  public static List<Map<String, Object>> getDocList(long folderId, String orderBy) {
    return remoteCallAsListOfMaps("DocApi", "getDocList", folderId, orderBy);
  }

  public static String getDocFolderPath(String folderId) {
    return remoteCallAsString("DocApi", "getDocFolderPath", folderId);
  }

  public static String getBucketPath(Map<String, Object> docObject) {
    return remoteCallAsString("DocApi", "getBucketPath", docObject);
  }

  public static Object getDocAIResult(long docId) {
    return remoteCall("DocApi", "getDocAIResult", docId);
  }

  public static Object getDocAIResults(long folderId, int start, int pageSize) {
    return remoteCall("DocApi", "getDocAIResults", folderId, start, pageSize);
  }

  public static Object getSummaryResult(long docId, String type) {
    return remoteCall("DocApi", "getSummaryResult", docId, type);
  }

  public static Object getSummaryResults(long folderId, int start, int pageSize) {
    return remoteCall("DocApi", "getSummaryResults", folderId, start, pageSize);
  }
  
  public static Object getOrCreateDocAndFolder(String folderName, String fileName, String content) {
    Map<String, Object> params = new LinkedHashMap<String, Object>();
    params.put("folderName", folderName);
    params.put("fileName", fileName);
    params.put("base64Content", content);
    return getOrCreateDocAndFolder(params);
  }

  public static Object getOrCreateDocAndFolder(Map<String, Object> params) {
    return remoteCall("DocApi", "getOrCreateDocAndFolder", params);
  }

  public static Result createDocFolder(Map<String, Object> folder) {
    return remoteCallAsResult("DocApi", "createDocFolder", folder);
  }

  public static Result retrieveUploadSignedUrl(Map<String, Object> params) {
    return remoteCallAsResult("DocApi", "retrieveUploadSignedUrl", params);
  }  
  
  public static Result saveDocObject(Map<String, Object> docObject) {
    return remoteCallAsResult("DocApi", "saveDocObject", docObject);
  }

  public static Result renameDoc(Map<String, Object> params) {
    return remoteCallAsResult("DocApi", "renameDoc", params);
  }

  public static Object executeWorkflow(Map<String, Object> params) {
    return remoteCall("DocApi", "executeWorkflow", params);
  }
  
  @SuppressWarnings("unchecked")
  public static Result getDocument(DocParams docParams, String docId) {
    Result result = remoteCallAsResult("DocApi", "getDocument", docParams, docId);
    if (result.isFailed()) {
      return result;
    }
    try {
      Document document = Util.fromMap((Map<String, Object>)result.getData(), Document.class);
      return Result.getSuccessResultWithData(document);
    } catch (Exception exc) {
      return Result.getFailedResult("Failed to make Document object, error: " + exc.getMessage());
    }
  }
  
  @SuppressWarnings("unchecked")
  public static Result getDocument(DocParams docParams, Map<String, Object> doc, String base64String) {
    Result result = remoteCallAsResult("DocApi", "getDocument", docParams, doc, base64String);
    if (result.isFailed()) {
      return result;
    }
    try {
      Document document = Util.fromMap((Map<String, Object>)result.getData(), Document.class);
      return Result.getSuccessResultWithData(document);
    } catch (Exception exc) {
      return Result.getFailedResult("Failed to make Document object, error: " + exc.getMessage());
    }
  }
  
  public static Result saveChunks(String docId, List<Map<String, Object>> chunks) {
    return remoteCallAsResult("DocApi", "saveChunks", docId, chunks);
  }
  
  public static Result extractText(String fileExtension, String base64String) {
    Result result = remoteCallAsResult("DocApi", "extractText", fileExtension,base64String);
    return result;
  }

  public static Object getDocSchemaByTargetId(String id) {
    return remoteCall("DocApi", "getDocSchemaByTargetId", id);
  }

  public static Object search(Map<String, Object> params) {
    return remoteCall("DocApi", "search", params);
  }

  public static Result deleteDoc(long docId){
    return remoteCallAsResult("DocApi", "deleteDoc", docId);
  }
  
  public static Result createThumbnails(Map<String, Object> doc, Document document, int width, int height) {
    return remoteCallAsResult("DocApi", "createThumbnails", doc, document, width, height);
  }

  public static Result updateTitles(Document document, DocSchema docSchema) {

    return remoteCallAsResult("DocApi", "updateTitles", document, docSchema);
  }

  public static Result cleanPage(Document document, Page page, String inputTemplateFile, DocSchema docSchema) {
    
    return remoteCallAsResult("DocApi", "cleanPage", document, page, inputTemplateFile, docSchema);
  }

  public static String getPageTextUsingGridExtractor(Page page) {
    
    return remoteCallAsString("DocApi", "getPageTextUsingGridExtractor", page);
  }

  public static Map<String, Object> getTOC(Document document, DocSchema docSchema) {
    
//    return remoteCallAsMap("DocApi", "getTOC", document, docSchema);
    return null;
  }

  public static Result cleanChapters(Map<String, Object> doc, Document document, DocSchema docSchema) {
    
    return remoteCallAsResult("DocApi", "cleanChapters", doc, document, docSchema);
  }

  public static Object getDocWorkflowByTargetId(String targetId) {
    
    return remoteCall("DocApi", "getDocWorkflowByTargetId", targetId);
  }

  public static Object getDocWorkflowByTargetId(String workflowType, String targetId) {
    
    return remoteCall("DocApi", "getDocWorkflowByTargetId", workflowType, targetId);
  }

  public static Object getDocWorkflow(String id) {
    
    return remoteCall("DocApi", "getDocWorkflow", id);
  }

  public static Object listDocWorkflows(Map<String, Object> params) {
    
    return remoteCall("DocApi", "listDocWorkflows", params);
  }

  public static Object saveDocWorkflow(Map<String, Object> workflowObj) {
    
    return remoteCall("DocApi", "saveDocWorkflow", workflowObj);
  }

  public static Object getDocFolderTree(String orderBy) {
    return remoteCall("DocApi", "getDocFolderTree", orderBy);
  }

  public static Object getDocFolderAndDocList(long folderId, String orderBy, boolean deleted) {
    return remoteCall("DocApi", "getDocFolderAndDocList", folderId, orderBy, deleted);
  }

  public static List<Map<String, Object>> getDocFolderList(long folderId, String orderBy) {
    return remoteCallAsListOfMaps("DocApi", "getDocFolderList", folderId, orderBy);
  }

  public static Object getDocFolderParents(long folderId) {
    return remoteCall("DocApi", "getDocFolderParents", folderId);
  }

  public static String getDocFolderFullId(long folderId) {
    return remoteCallAsString("DocApi", "getDocFolderFullId", folderId);
  }

  public static Result createFolder(Map<String, Object> folder) {
    return remoteCallAsResult("DocApi", "createFolder", folder);
  }

  public static Result updateFolder(Map<String, Object> folder) {
    return remoteCallAsResult("DocApi", "updateFolder", folder);
  }

  public static Result renameFolder(Map<String, Object> params) {
    return remoteCallAsResult("DocApi", "renameFolder", params);
  }

  public static Result moveFolder(Map<String, Object> params) {
    return remoteCallAsResult("DocApi", "moveFolder", params);
  }

  public static Result deleteFolder(Map<String, Object> params) {
    return remoteCallAsResult("DocApi", "deleteFolder", params);
  }

  public static Result createDownloadSignedUrl(Map<String, Object> params) {
    return remoteCallAsResult("DocApi", "createDownloadSignedUrl", params);
  }

  public static Result createUploadSignedUrl(String apiHost, String providerName, String folderId, long duration) {
    return remoteCallAsResult("DocApi", "createUploadSignedUrl", apiHost, providerName, folderId, duration);
  }

  public static Result createDownloadSignedUrl(String apiHost, String providerName, String docId, long duration, String baseUri) {
    return remoteCallAsResult("DocApi", "createDownloadSignedUrl", apiHost, providerName, docId, duration, baseUri);
  }

  public static Object saveDocObjectWithFunction(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocObjectWithFunction", params);
  }

  public static Object saveDocWithContent(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocWithContent", params);
  }

  public static Object retrieveSignedUrl(Map<String, Object> params) {
    return remoteCall("DocApi", "retrieveSignedUrl", params);
  }

  public static Object saveDocGetSignedUrl(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocGetSignedUrl", params);
  }

  public static Object autoComplete(Map<String, Object> params) {
    return remoteCall("DocApi", "autoComplete", params);
  }

  public static Result updateDocFolderProperties(Map<String, Object> properties) {
    return remoteCallAsResult("DocApi", "updateDocFolderProperties", properties);
  }

  public static Result renameDoc(long docId, String newName) {
    return remoteCallAsResult("DocApi", "renameDoc", docId, newName);
  }

  public static Result copyDoc(long docId, long newParentFolderId, String newName) {
    return remoteCallAsResult("DocApi", "copyDoc", docId, newParentFolderId, newName);
  }

  public static Result moveDoc(long docId, long newParentFolderId) {
    return remoteCallAsResult("DocApi", "moveDoc", docId, newParentFolderId);
  }

  public static Result deleteManyDocs(List<Object> list, boolean permanent) {
    return remoteCallAsResult("DocApi", "deleteManyDocs", list, permanent);
  }

  public static Result restoreManyDocs(List<Object> list) {
    return remoteCallAsResult("DocApi", "restoreManyDocs", list);
  }

  public static Result retrieveSignedUrl(String docId, String contentType, boolean asAttachment, long duration) {
    return remoteCallAsResult("DocApi", "retrieveSignedUrl", docId, contentType, asAttachment, duration);
  }

  public static Result retrieveUploadSignedUrl(String folderId, String fileName, String contentType) {
    return remoteCallAsResult("DocApi", "retrieveUploadSignedUrl", folderId, fileName, contentType);
  }

  public static Object getDocAIResult(long docId, boolean addPages, boolean addChapters) {
    return remoteCall("DocApi", "getDocAIResult", docId, addPages, addChapters);
  }

  public static Result saveEmbedding(Map<String, Object> doc, Document document) {
    return remoteCallAsResult("DocApi", "saveEmbedding", doc, document);
  }

  public static Object chat(Map<String, Object> params) {
    return remoteCall("DocApi", "chat", params);
  }

  public static Object generateJson(Map<String, Object> params) {
    return remoteCall("DocApi", "generateJson", params);
  }

  public static Object bulkUpload(Map<String, Object> params) {
    return remoteCall("DocApi", "bulkUpload", params);
  }

  public static Object matchDocs(Map<String, Object> params) {
    return remoteCall("DocApi", "matchDocs", params);
  }

  public static Object getDocFolders(Map<String, Object> params) {
    return remoteCall("DocApi", "getDocFolders", params);
  }

  public static Object getDocs(Map<String, Object> params) {
    return remoteCall("DocApi", "getDocs", params);
  }

  public static Object getTags(Map<String, Object> params) {
    return remoteCall("DocApi", "getTags", params);
  }

  public static Object getDocSchema(String id) {
    return remoteCall("DocApi", "getDocSchema", id);
  }

  public static Object listDocSchemas(Map<String, Object> params) {
    return remoteCall("DocApi", "listDocSchemas", params);
  }

  public static Object saveDocSchema(Map<String, Object> schema) {
    return remoteCall("DocApi", "saveDocSchema", schema);
  }

  public static Object resetDocWorkflow(String id, boolean resetPrompts) {
    return remoteCall("DocApi", "resetDocWorkflow", id, resetPrompts);
  }

  public static Object getPrompt(String id) {
    return remoteCall("DocApi", "getPrompt", id);
  }

  public static Object getPrompt(String parentId, String name) {
    return remoteCall("DocApi", "getPrompt", parentId, name);
  }

  public static Object listOfPrompts(String parentId, String name) {
    return remoteCall("DocApi", "listOfPrompts", parentId, name);
  }

  public static Object savePrompt(Map<String, Object> promptContent) {
    return remoteCall("DocApi", "savePrompt", promptContent);
  }

  public static Object activatePrompt(Map<String, Object> paramObj) {
    return remoteCall("DocApi", "activatePrompt", paramObj);
  }

  public static Object getPromptSuggestions(String targetType, String targetId, String input) {
    return remoteCall("DocApi", "getPromptSuggestions", targetType, targetId, input);
  }

  public static Object getDocConversations(String docId) {
    return remoteCall("DocApi", "getDocConversations", docId);
  }

  public static Object getFolderConversations(String folderId) {
    return remoteCall("DocApi", "getFolderConversations", folderId);
  }

  public static Object getConversation(String id) {
    return remoteCall("DocApi", "getConversation", id);
  }

  public static Object getFolderMultiTurnConversations(String folderId) {
    return remoteCall("DocApi", "getFolderMultiTurnConversations", folderId);
  }

  public static Object getMultiTurnConversation(String id) {
    return remoteCall("DocApi", "getMultiTurnConversation", id);
  }

  public static Object getFolderStatus(String folderId) {
    return remoteCall("DocApi", "getFolderStatus", folderId);
  }

  public static Object getDocStatus(String docId) {
    return remoteCall("DocApi", "getDocStatus", docId);
  }

  public static Object resetStatus(String folderId) {
    return remoteCall("DocApi", "resetStatus", folderId);
  }

  public static Object getDoc(long docId) {
    return remoteCall("DocApi", "getDoc", docId);
  }

  public static Object getDoc(String folderId, String fileName) {
    return remoteCall("DocApi", "getDoc", folderId, fileName);
  }

  public static Object getDocument(long docId) {
    return remoteCall("DocApi", "getDocument", docId);
  }

  public static Object getPages(long docId, int from, int to) {
    return remoteCall("DocApi", "getPages", docId, from, to);
  }

  public static Map<String, Object> getDocFolderByName(long parentId, String folderName) {
    return remoteCallAsMap("DocApi", "getDocFolderByName", parentId, folderName);
  }

  public static Object getDocMetadata(long docId) {
    return remoteCall("DocApi", "getDocMetadata", docId);
  }

  public static Object getChapters(long docId, int from, int to, boolean excludeHeroImageAndSummary) {
    return remoteCall("DocApi", "getChapters", docId, from, to, excludeHeroImageAndSummary);
  }

  public static Object getChapters2(long docId, int from, int to, boolean excludeHeroImageAndSummary) {
    return remoteCall("DocApi", "getChapters2", docId, from, to, excludeHeroImageAndSummary);
  }

  public static Object getChapter(String chapterId) {
    return remoteCall("DocApi", "getChapter", chapterId);
  }

  public static Object getPageBySegmentId(String segmentId) {
    return remoteCall("DocApi", "getPageBySegmentId", segmentId);
  }

  public static List<Map<String, Object>> processUsingVectorDB(String query, List<Integer> folderIds, List<Integer> docIds, int pageSize, String type) {
    return remoteCallAsListOfMaps("DocApi", "processUsingVectorDB", query, folderIds, docIds, pageSize, type);
  }

  public static List<Map<String, Object>> searchContent(String query, String type, List<Map<String, Object>> docs) {
    return remoteCallAsListOfMaps("DocApi", "searchContent", query, type, docs);
  }

  public static Object getChapterByPageId(String pageId) {
    return remoteCall("DocApi", "getChapterByPageId", pageId);
  }

  public static Object getChapterByImageId(String imageId) {
    return remoteCall("DocApi", "getChapterByImageId", imageId);
  }

  public static Object getChapterBySegmentId(String segmentId) {
    return remoteCall("DocApi", "getChapterBySegmentId", segmentId);
  }

  public static Object getDocProperties(String id) {
    return remoteCall("DocApi", "getDocProperties", id);
  }

  public static Result saveDocProperties(Map<String, Object> properties) {
    return remoteCallAsResult("DocApi", "saveDocProperties", properties);
  }

  public static Object getRecentlyAccessedFolders() {
    return remoteCall("DocApi", "getRecentlyAccessedFolders");
  }

  public static Object saveFieldsTables(String docId, Map<String, Object> docMap) {
    return remoteCall("DocApi", "saveFieldsTables", docId, docMap);
  }

  public static Object getDocFields(String docId) {
    return remoteCall("DocApi", "getDocFields", docId);
  }

  public static Object getDocRecordsByFolder(String folderId) {
    return remoteCall("DocApi", "getDocRecordsByFolder", folderId);
  }

  public static Object saveDocFields(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocFields", params);
  }

  public static Object retrieveQA(String question) {
    return remoteCall("DocApi", "retrieveQA", question);
  }

  public static Object saveQA(Map<String, Object> params) {
    return remoteCall("DocApi", "saveQA", params);
  }

  public static Object deleteQA(String id) {
    return remoteCall("DocApi", "deleteQA", id);
  }

  public static Object getFieldsTables(String docId) {
    return remoteCall("DocApi", "getFieldsTables", docId);
  }

  public static Result saveThumbState(Map<String, Object> params) {
    return remoteCallAsResult("DocApi", "saveThumbState", params);
  }

  public static Object getTagsByType(String folderId, String tagType, int start, int pageSize) {
    return remoteCall("DocApi", "getTagsByType", folderId, tagType, start, pageSize);
  }

  public static Object getTagsByTypeAndValue(String folderId, String tagType, String tagValue, int start, int pageSize) {
    return remoteCall("DocApi", "getTagsByTypeAndValue", folderId, tagType, tagValue, start, pageSize);
  }

  public static Object deleteConversation(String id) {
    return remoteCall("DocApi", "deleteConversation", id);
  }

  public static Object deleteMultiTurnConversation(String id) {
    return remoteCall("DocApi", "deleteMultiTurnConversation", id);
  }

  public static Object downLoadContent(String id, String type, String format) {
    return remoteCall("DocApi", "downLoadContent", id, type, format);
  }

  public static Object createDefaultSchema() {
    return remoteCall("DocApi", "createDefaultSchema");
  }

  public static Object saveDocPage(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocPage", params);
  }

  public static Object saveDocChapter(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocChapter", params);
  }

  public static Object saveDocPageToc(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocPageToc", params);
  }

  public static Object verifyDocPageToc(Map<String, Object> params, boolean verified) {
    return remoteCall("DocApi", "verifyDocPageToc", params, verified);
  }

  public static Object removeDocPageToc(Map<String, Object> params) {
    return remoteCall("DocApi", "removeDocPageToc", params);
  }

  public static Object saveDocNumberOfFrontPages(Map<String, Object> params) {
    return remoteCall("DocApi", "saveDocNumberOfFrontPages", params);
  }

  public static Object saveContent(String folderId, String subDocFolderPath, String fileName,
      String contentType, byte[] fileBytes) {
    return remoteCall("DocApi", "saveContent", folderId, subDocFolderPath, fileName, contentType, fileBytes);
  }

  public static Map<String, Object> getDocFolderByPath(String path) {
    return remoteCallAsMap("DocApi", "getDocFolderByPath", path);
  }

  public static Map<String, Object> getData(String docId) {
    return remoteCallAsMap("DocApi", "getData", docId);
  }

  public static Map<String, Object> htmlToJson(String docId) {
    return remoteCallAsMap("DocApi", "htmlToJson", docId);
  }
}