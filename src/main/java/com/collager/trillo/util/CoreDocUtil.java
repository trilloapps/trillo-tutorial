package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.collager.trillo.model.DataIterator;
import com.collager.trillo.pojo.DocWorkflow;
import com.collager.trillo.pojo.Result;
import com.collager.trillo.util.DataObject.Document;
import com.collager.trillo.util.DataObject.Image;
import com.collager.trillo.util.DataObject.Page;
import com.collager.trillo.util.DataObject.PageSummary;
import com.collager.trillo.util.DataObject.Paragraph;
import com.collager.trillo.util.DataObject.Token;
import com.fasterxml.jackson.core.type.TypeReference;

public class CoreDocUtil {

  public static final String DOC_AI_WORKFLOW_LOCK_GROUP = "DocAIWorkflow";

  public static final String CACHE_SEARCH_WORKFLOW_MAP_NAME = "SearchWorkflow";

  public static final String STATUS_ENQUEUED = "enqueued";
  public static final String STATUS_PROCESSING = "processing";
  public static final String STATUS_NONE = "";

  public static String normalizeDocFolderPath(String path) {
    path = StringUtils.removeStart(path, "/");
    path = StringUtils.removeEnd(path, "/");
    return path;
  }

  public static String getBucketPath(Map<String, Object> docObject) {
    if ("0".equals("" + docObject.get("id"))) {
      return "" + docObject.get("fileName");
    }
    if (docObject.containsKey("externalSource") && docObject.get("externalId") != null
        && !"unknown".equalsIgnoreCase("" + docObject.get("externalId"))) {
      return "" + docObject.get("externalId");
    }
    String userId = docObject.containsKey("userId") ? ("" + docObject.get("userId")).trim() : "";

    String path = "docs/" + (userId.length() > 0 ? userId + "/" : "") + docObject.get("id") + "."
        + docObject.get("fileType");
    return path;
  }

  public static String getBucketPathForRawDocContent(Map<String, Object> docObject,
      String subFolder) {
    if ("0".equals("" + docObject.get("id"))) {
      return "/tmp";
    }

    String userId = docObject.containsKey("userId") ? ("" + docObject.get("userId")).trim() : "";

    String path = "raw-docs/" + (StringUtils.isBlank(subFolder) ? "" : subFolder + "/")
        + (userId.length() > 0 ? userId + "/" : "") + docObject.get("id");
    return path;
  }

  public static String getBucketPathForImage(String docId, String subFolder) {

    String userId = AccessControlUtil.getCurrentUser_UserId();

    String path = "images/" + (StringUtils.isBlank(subFolder) ? "" : subFolder + "/")
        + (userId.length() > 0 ? userId + "/" : "") + docId;
    return path;
  }

  public static String getBucketPathForDownloads(String folderId, String fileName) {

    String userId = AccessControlUtil.getCurrentUser_UserId();

    String path = "downloads/" + (userId.length() > 0 ? userId + "/" : "") + "folder/" + folderId + "/" + fileName;

    return path;
  }

  public static String teantIdFromPath(String path) {
    if (path.startsWith("/")) {
      path = normalizeDocFolderPath(path);
    }
    if (path.length() > 0 && Character.isDigit(path.charAt(0))) {
      int idx = path.indexOf("/");
      if (idx > 0) {
        return path.substring(0, idx);
      }
    }
    return null;
  }

  public static Map<String, Object> getContent(Map<String, Object> docResult) {

    if (docResult.containsKey("content")) {
      Map<String, Object> content = getContent("" + docResult.get("content"));
      if (content.get("_pages") != null) {
        content.put("pages", content.get("_pages")); // for backward compatibility
        content.remove("_pages"); // removed, removing from the old data
      }
      content.remove("textPages"); // removed, removing from the old data
      content.remove("_entities"); // entities are internal and not needed by an external client
      if (content.get("summaryContent") instanceof String) {
        content.put("summaryContentFormatted", formatText((String)content.get("summaryContent")));
        //content.put("summaryJson", MarkdownToJson.toJson((String)content.get("summaryContent")));
      }
      makeMinimalPage(content);

      return content;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static void makeMinimalPage(Map<String, Object> content) {
    Page page;
    if (content.get("pages") instanceof List<?>) {
      List<PageSummary> pageSummaries = new ArrayList<>();
      List<Map<String, Object>> pages = (List<Map<String, Object>>) content.get("pages");
      for (Map<String, Object> pageMap : pages) {
        try {
          page = Util.fromMap(pageMap, Page.class);
          pageMap.put("pageText", page.retrieveRenderableText());
          if (page.pageSummary != null) {
            pageSummaries.add(page.pageSummary);
          } else {
            pageSummaries.add(new PageSummary());
          }
        } catch (Exception exc) {}
        pageMap.remove("blocks");
        pageMap.remove("lines");
        pageMap.remove("paragraphs");
        pageMap.remove("tokens");
        pageMap.remove("grid");
        pageMap.remove("content");
      }
      content.put("pageSummaries", pageSummaries);
    }
  }

  public static Map<String, Object> getContent(String contentStr) {
    try {
      return Util.fromJSONStringAsMap(contentStr);
    } catch(Exception exc) {
    }
    return null;
  }

  public static String formatText(String text) {
    if (text == null) return "";
    if (StringUtils.isBlank(text)) return text;
    text = text.replaceAll("(\r\n|\n)", "<br/>");
    return text;
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> makeDocAIResultPage(List<Map<String, Object>> docList, int start,
      int totalItems) {
    Map<String, Object> resultPage = new LinkedHashMap<String, Object>();

    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> attributes = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> rawContentList = new ArrayList<Map<String, Object>>();
    List<Object> ids = new ArrayList<Object>();

    resultPage.put("start", start);
    resultPage.put("totalItems", totalItems);
    resultPage.put("ids", ids);
    resultPage.put("items", items);
    resultPage.put("attributes", attributes);
    resultPage.put("rawContentList", rawContentList);

    Map<String, Object> a = new LinkedHashMap<String, Object>();
    a.put("name", "_file_");
    a.put("label", "File");
    attributes.add(a);


    Map<String, Object> content;
    Map<String, Object> item;
    for (Map<String, Object> doc : docList) {
      content = getContent(doc);
      if (content != null) {
        ids.add(doc.get("id"));
        rawContentList.add(content);
        Map<String, Object> entities = null;
        if (content.get("chunks") instanceof List<?>) {
          List<Map<String, Object>> chunks = (List<Map<String, Object>>) content.get("chunks");
          entities = new LinkedHashMap<String, Object>();
          for (Map<String, Object> m : chunks) {
            entities.put("" + m.get("name"), m.get("text"));
          }

          //mergeAttributes(attributes, entities);
        } else if (content.get("entities") instanceof Map<?, ?>) {
          entities = (Map<String, Object>) content.get("entities");
          entities.remove("generic_entities");
          if (entities != null) {
            mergeAttributes(attributes, entities);
          }
        }
        if (entities != null && entities.size() > 0) {
          entities.put("_file_", doc.get("fileName"));
          content.put("entities", entities);
          items.add(entities);
        } else {
          content.remove("entities");
          item = new LinkedHashMap<String, Object>();
          item.put("_file_", doc.get("fileName"));
          items.add(item);
        }
      }
    }

    return resultPage;
  }

  @SuppressWarnings("unchecked")
  public static void addPages(String docId, Map<String, Object> content) {
    Object r = DSApi.queryMany("DocPage", "docId = " + docId);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      if (l.size() > 0) {
        sortByPageNumber(l);
        List<Map<String, Object>> pageList = makePageList(l);
        content.put("pages", pageList);
        addFields(docId, pageList);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static void addTokens(String docId, Document document) {
    Object r = DSApi.queryMany("DocPageToken", "docId = " + docId);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      sortByPageNumber(l);
      Map<String, Object> m;
      Page page;
      int pageNumber;
      List<Token> tokens;
      for (int i=0; i<l.size(); i++) {
        m = l.get(i);
        if (m.containsKey("content")) {
          try {
            pageNumber = Util.asInt(m.get("pageNumber"), -1);
            if (pageNumber >= 0 && m.containsKey("content")) {
              page = document.retrievePageByFilePageNumber(pageNumber);
              if (page != null) {
                tokens = Util.fromJSONString("" + m.get("content"),  new TypeReference<ArrayList<Token>>(){});
                page.tokens = tokens;
              }
            }
          } catch (Exception exc) {
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static void addFields(String docId, List<Map<String, Object>> pageList) {
    Object r = DSApi.queryMany("DocField", "docId = " + docId);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      sortByPageNumber(l);

      Map<String, Object> m;
      Map<String, Object> pageMap;
      int pageNumber;

      for (int i=0; i<pageList.size(); i++) {
        pageMap = pageList.get(i);
        pageMap.put("fields",  new ArrayList<Map<String, Object>>());
      }
      List<Map<String, Object>> fields;
      for (int i=0; i<l.size(); i++) {
        m = l.get(i);
        pageNumber = Util.asInt(m.get("pageNumber"), -1);
        if (pageNumber >= 0) {
          pageMap = getPageMap(pageList, pageNumber);
          if (pageMap != null) {
            fields = (List<Map<String, Object>>) pageMap.get("fields");
            fields.add(m);
          }
        }
      }
    }
  }


  public static Map<String, Object> getPageMap(List<Map<String, Object>> pageList, int pageNumber) {
    Map<String, Object> pageMap;
    int pageNumber2;
    for (int i=0; i<pageList.size(); i++) {
      pageMap = pageList.get(i);
      pageNumber2 = Util.asInt(pageMap.get("pageNumber"), -1);
      if (pageNumber == pageNumber2) {
        return pageMap;
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static void addPages2(String docId, Map<String, Object> content) {
    Object r = DSApi.queryMany("DocPage", "docId = " + docId);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      sortByPageNumber(l);

      List<Map<String, Object>> pages = new ArrayList<>();
      Map<String, Object> m;
      Map<String, Object> p;
      for (int i=0; i<l.size(); i++) {
        m = l.get(i);
        if (m.containsKey("content")) {
          try {
            p = Util.fromJSONStringAsMap("" + m.get("content"));
          } catch (Exception exc) {
            p = new LinkedHashMap<>();
          }
          pages.add(p);
        }
      }
      content.put("pages", pages);
      addFields(docId, pages);
    }
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> getDocFieldsByPages(String docId) {
    Map<String, Object> minDocument = new LinkedHashMap<>();
    minDocument.put("id", docId);
    String query;
    query = "docId = " + docId;
    Object r = DSApi.queryMany("DocPage", query);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      if (l.size() > 0) {
        sortByPageNumber(l);
        List<Map<String, Object>> pageList = makeMinimalPageListForDocFields(l);
        addFields(docId, pageList);
        minDocument.put("pages", pageList);
      }
    }
    if ( minDocument.get("pages") == null) {
      minDocument.put("pages", new ArrayList<>());
    }
    return minDocument;
  }

  @SuppressWarnings("unchecked")
  public static Object getDocRecordsByFolderWithFieldSpecs(String folderId) {
    String sql = "select *" + " from DocField_tbl "
        + " where folderId = " + folderId + " order by docId, pageNumber";

    String totalCountQuery = "select count(*)" + " from DocField_tbl "
        + " where folderId = " + folderId;

    int start = 1;
    int pageSize = 1000;

    DataIterator dataIterator =
        DSApi.getDataIterator("shared", "common", start, pageSize, sql);
    dataIterator.setCountQuery(totalCountQuery);

    Result tempResult = dataIterator.initialize();

    if (tempResult.isFailed()) {
      LogApi.error("Failed to initialize DataIterator", "error", tempResult.getMessage());
      return tempResult;
    }

    Object res;
    List<Map<String, Object>> list =  new ArrayList<>();
    while (dataIterator.hasNext()) {
      res = dataIterator.getNext();
      if (res instanceof Map<?, ?>) {
        list.add((Map<String, Object>)res);
      }
    }

    Map<String, Object> fieldsWithSpecsForFolder = new LinkedHashMap<String, Object>();
    fieldsWithSpecsForFolder.put("records", makeRecords(list));
    fieldsWithSpecsForFolder.put("fieldSpecs", makeFieldSpecs(list));
    return fieldsWithSpecsForFolder;
  }

  private static List<Map<String, Object>> makeRecords(List<Map<String, Object>> list) {
    // assumes that there is one record per document type
    Map<String, Map<String, Object>> recordsTable = new LinkedHashMap<>();
    List<Map<String, Object>> records =  new ArrayList<>();
    Map<String, Object> record;
    String docId;
    for (Map<String, Object> m : list) {
      docId = "" + m.get("docId");
      record = recordsTable.get(docId);
      if (record == null) {
        record = new LinkedHashMap<String, Object>();
        record.put("_docId_", docId);
        recordsTable.put(docId, record);
        records.add(record);
      }
      record.put("" + m.get("name"), m.get("value"));
    }
    return records;
  }

  private static List<Map<String, Object>> makeFieldSpecs(List<Map<String, Object>> list) {
    List<Map<String, Object>> fieldSpecs =  new ArrayList<>();
    Map<String, Map<String, Object>> table = new LinkedHashMap<>();
    Map<String, Object> fieldSpec;
    String name;
    for (Map<String, Object> m : list) {
      name = "" + m.get("name");
      fieldSpec = table.get(name);
      if (fieldSpec == null) {
        fieldSpec = new LinkedHashMap<String, Object>();
        fieldSpec.put("name", name);
        fieldSpec.put("displayName", m.get("displayName"));
        fieldSpecs.add(fieldSpec);
        table.put(name, fieldSpec);
      }
    }
    return fieldSpecs;
  }

  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> getPages(String docId, int from, int to) {
    String query;
    if (to == 0) {
      query = "docId = " + docId  + " and pageNumber >= " + from;
    } else {
      query = "docId = " + docId  + " and pageNumber >= " + from + " and pageNumber <= " + to;
    }
    Object r = DSApi.queryMany("DocPage", query);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      if (l.size() > 0) {
        sortByPageNumber(l);
        List<Map<String, Object>> pageList = makePageList(l);
        addFields(docId, pageList);
        return pageList;
      }
    }
    return new ArrayList<>();
  }


  @SuppressWarnings("unchecked")
  public static void addChapters(String docId, Map<String, Object> content) {
    Object r = DSApi.queryMany("DocChapter", "docId = " + docId);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      if (l.size() > 0) {
        Collections.sort(l, new Comparator<Map<String, Object>>() {
          public int compare(Map<String, Object> n1, Map<String, Object> n2) {
            int i1 = Util.asInt(n1.get("chapterNumber"), -1);
            int i2 = Util.asInt(n2.get("chapterNumber"), -1);
            return Integer.compare(i1, i2);
          }
        });
        List<Map<String, Object>> chapterList = makeChapterList(l);
        content.put("chapters", chapterList);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static void addChapters2(String docId, Map<String, Object> content) {
    Object r = DSApi.queryMany("DocChapter", "docId = " + docId);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      Collections.sort(l, new Comparator<Map<String, Object>>() {
        public int compare(Map<String, Object> n1, Map<String, Object> n2) {
          int i1 = Util.asInt(n1.get("chapterNumber"), -1);
          int i2 = Util.asInt(n2.get("chapterNumber"), -1);
          return Integer.compare(i1, i2);
        }
      });

      List<Map<String, Object>> chapters = new ArrayList<>();
      Map<String, Object> m;
      Map<String, Object> m2;
      for (int i=0; i<l.size(); i++) {
        m = l.get(i);
        if (m.containsKey("content")) {
          try {
            m2 = Util.fromJSONStringAsMap("" + m.get("content"));
          } catch (Exception exc) {
            m2 = new LinkedHashMap<>();
          }
          chapters.add(m2);
        }
      }
      content.put("chapters", chapters);
    }
  }

  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> getChapters(String docId, int from, int to) {
    String query;
    if (to == 0) {
      query = "docId = " + docId  + " and chapterNumber >= " + from;
    } else {
      query = "docId = " + docId  + " and chapterNumber >= " + from + " and chapterNumber <= " + to;
    }
    Object r = DSApi.queryMany("DocChapter", query);
    if (r instanceof List<?>) {
      List<Map<String, Object>> l = (List<Map<String, Object>>) r;
      Collections.sort(l, new Comparator<Map<String, Object>>() {
        public int compare(Map<String, Object> n1, Map<String, Object> n2) {
          int i1 = Util.asInt(n1.get("chapterNumber"), -1);
          int i2 = Util.asInt(n2.get("chapterNumber"), -1);
          return Integer.compare(i1, i2);
        }
      });
      if (l.size() > 0) {
        List<Map<String, Object>> chapterList = makeChapterList(l);
        return chapterList;
      }
    }
    return new ArrayList<>();
  }


  private static List<Map<String, Object>> makePageList(List<Map<String, Object>> l) {
    List<Map<String, Object>> pageList = new ArrayList<>();
    List<Page> pages = new ArrayList<Page>();
    Map<String, Object> m;
    Page p;
    for (int i=0; i<l.size(); i++) {
      m = l.get(i);
      if (m.containsKey("content")) {
        try {
          p = Util.fromJSONString("" + m.get("content"), Page.class);
        } catch (Exception exc) {
          p = new Page();
        }
        pages.add(p);
      }
    }

    //DataObject.completeBlockAcrossPages(pages);

    for (int i=0; i<l.size(); i++) {
      m = l.get(i);
      if (m.containsKey("content")) {
        try {
          p = pages.get(i);
          m.put("title", p.title);
          m.put("altTitle", p.altTitle);
          m.put("llmDerivedTitles", p.llmDerivedTitles);
          m.put("llmDerivedAuthors", p.llmDerivedAuthors);
          m.put("geometryDerivedTitles", p.geometryDerivedTitles);
          m.put("geometryDerivedAuthors", p.geometryDerivedAuthors);
          m.put("docTitle", p.docTitle);
          m.put("docClass", p.docClass);
          m.put("pageText", p.retrieveRenderableText());
          m.put("pageTextFormatted", formatText((String) m.get("pageText")));
          m.put("tables", p.tables);
          if (p.fields != null) {
            m.put("fields", p.fields);
          }
          if (p.pageSummary != null) {
            p.pageSummary.textFormatted = formatText(p.pageSummary.text);
            m.put("pageSummary", p.pageSummary);
          }
          if (StringUtils.isAllBlank(p.html)) {
            p.html = HTMLRenderer.generateHTML(p);
          }
          if (StringUtils.isNotBlank(p.html)) {
            String html = p.html;
            String baseServerUrl = BaseApi.getServerBaseUrl();
            html = html.replaceAll("\\{\\{\\{SERVER_BASE_URL\\}\\}\\}",
                Util.removeLastCharIfMatches(baseServerUrl, '/'));
            html = html.replaceAll("/tn_embedded/", "/embedded/");
            m.put("html", html);
          }
          m.put("pageContent", p.retrieveRenderables());
          m.put("images", p.images);
          m.put("blocks", p.blocks);
          m.put("sections", p.sections);
          m.put("cleanParas", p.cleanParas);
          m.put("tags", p.tags);
          if (p.pageImageUrl != null) {
            m.put("pageImageUrl", p.pageImageUrl);
          }
          if (p.pageImageText != null) {
            m.put("pageImageText", p.pageImageText);
          }
          if (p.altPageImageUrl != null) {
            m.put("altPageImageUrl", p.altPageImageUrl);
          }
          if (p.altPageImageText != null) {
            m.put("altPageImageText", p.altPageImageText);
          }
          if (p.chapterId != null) {
            m.put("chapterId", p.chapterId);
          }
          if (p.createDate != null) {
            m.put("createDate", p.createDate);
          }
          if (p.authors != null) {
            m.put("authors", p.authors);
          }
          if (p.textWithMarkings != null) {
            m.put("textWithMarkings", p.textWithMarkings);
          }
          if (p.toc != null) {
            m.put("toc", p.toc);
          }
          m.put("tocVerified", p.tocVerified);
          if (p.curatedText != null) {
            m.put("curatedText", p.curatedText);
          }
          if (p.curatedPageContent != null) {
            m.put("curatedPageContent", p.curatedPageContent);
          }
          if (p.curatedToc != null) {
            m.put("curatedToc", p.curatedToc);
          } else if (p.toc != null) {
            m.put("curatedToc", p.toc);
          }
          m.put("imagePropertiesExtracted", p.imagePropertiesExtracted);
          pageList.add(m);
        } catch(Exception exc) {
          pageList.add(m);
        }
      } else {
        pageList.add(m);
      }
    }
    return pageList;
  }

  private static List<Map<String, Object>> makeChapterList(List<Map<String, Object>> l) {
    Map<String, Object> m;
    Map<String, Object> m2;
    for (int i=0; i<l.size(); i++) {
      m = l.get(i);
      if (m.containsKey("content")) {
        try {
          m2 = Util.fromJSONStringAsMap("" + m.get("content"));
          m.putAll(m2);
          m.remove("content");
        } catch (Exception exc) {

        }
      }
    }

    return l;
  }

  private static List<Map<String, Object>> makeMinimalPageListForDocFields(List<Map<String, Object>> l) {
    List<Map<String, Object>> pageList = new ArrayList<>();
    Map<String, Object> m;
    Map<String, Object> m2;
    Page p;
    for (int i=0; i<l.size(); i++) {
      m = l.get(i);
      if (m.containsKey("content")) {
        try {
          p = Util.fromJSONString("" + m.get("content"), Page.class);
        } catch (Exception exc) {
          p = new Page();
        }
        m2 = new LinkedHashMap<>();
        m2.put("id", m.get("id"));
        m2.put("pageNumber", m.get("pageNumber"));
        if (p.altPageImageUrl != null) {
          m2.put("altPageImageUrl", p.altPageImageUrl);
        }
        pageList.add(m2);
      }
    }
    return pageList;
  }

  @SuppressWarnings("unused")
  private static List<String> convertToStringList(String authors) {
    List<String> l;
    try {
      l = Util.fromJSONString(authors,  new TypeReference<ArrayList<String>>() {});
      return l;
    } catch (Exception exc) {
      l = new ArrayList<String>();
      l.add(authors);
      return l;
    }
  }

  protected static void mergeAttributes(List<Map<String, Object>> attributes, Map<String, Object> content) {
    Iterator<Entry<String, Object>> iter = content.entrySet().iterator();
    Entry<String, Object> e;
    Map<String, Object> a;
    String k;
    Object v;
    while (iter.hasNext()) {
      e = iter.next();
      k = e.getKey();
      v = e.getValue();
      if (v instanceof List<?> || v instanceof Map<?, ?> || "_text_".equals(k)) {
        continue;
      }
      if (!existsAttribute(attributes, k)) {
        a = new LinkedHashMap<String, Object>();
        a.put("name", k);
        if ("_file_".equals(k)) {
          a.put("label", "File");
        } else {
          a.put("label", k);
        }
        attributes.add(a);
      }
    }
  }

  protected static boolean existsAttribute(List<Map<String, Object>> attributes, String key) {
    for (Map<String, Object> a : attributes) {
      if (key.equals("" + a.get("name"))) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public static Result canEnqueue(String className, long id) {
    Object res = DSApi.get(className, "" + id);
    if (res instanceof Result) {
      return (Result) res;
    }
    Map<String, Object> m = (Map<String, Object>) res;

    String status = "" + m.get("status");

    if (STATUS_ENQUEUED.equals(status) || STATUS_PROCESSING.equals(status)) {
      return Result.getFailedResult(("DocFolder".equals(className) ? "Folder" : "Document") +
          (STATUS_ENQUEUED.equals(status) ? " is queued for processing" : " is being processed"));
    }
    return Result.getSuccessResult();
  }

  public static Result updateEnqueudStatusAndTaskExecId(String className, long id, long taskExecId) {
    Object res = DSApi.update(className, "" + id, "status", STATUS_ENQUEUED);
    if (res instanceof Result) {
      return (Result) res;
    }
    res = DSApi.update(className, "" + id, "taskExecId", taskExecId);
    if (res instanceof Result) {
      return (Result) res;
    }
    DSApi.commitTx();
    return Result.getSuccessResult();
  }

  @SuppressWarnings("unchecked")
  public static Result canProcess(String className, long id, long taskExecId) {
    Object res = DSApi.get(className, "" + id);
    if (res instanceof Result) {
      return (Result) res;
    }
    Map<String, Object> m = (Map<String, Object>) res;

    String status = "" + m.get("status");

    if (STATUS_PROCESSING.equals(status) && !"DocFolder".equals(className)) {
      return Result.getFailedResult(("DocFolder".equals(className) ? "Folder" : "Document") +
          (STATUS_ENQUEUED.equals(status) ? " is queued for processing" : " is being processed"));
    }

    if (STATUS_ENQUEUED.equals(status)) {
      // check taskExec id
      long taskExecId2 = Util.asLong(m.get("taskExecId"), -1);
      if (taskExecId != taskExecId2) {
        //return Result.getFailedResult(("DocFolder".equals(className) ? "Folder" : "Document") + 
        //  "has mis-match of taskExecId, " + taskExecId + " v/s " + taskExecId2);
      }
    }

    return Result.getSuccessResult();
  }

  public static Result updateProcessingStatus(String className, long id) {
    Object res = DSApi.update(className, "" + id, "status", STATUS_PROCESSING);
    if (res instanceof Result) {
      return (Result) res;
    }
    DSApi.commitTx();
    return Result.getSuccessResult();
  }

  public static Result resetStatus(String className, long id) {
    Object res = DSApi.update(className, "" + id, "status", "");
    if (res instanceof Result) {
      return (Result) res;
    }
    res = DSApi.update(className, "" + id, "taskExecId", -1);
    if (res instanceof Result) {
      return (Result) res;
    }
    DSApi.commitTx();
    return Result.getSuccessResult();
  }

  public static Result resetDocFolderAndDocStatus(String folderId) {

    long l = Util.asLong(folderId, -1);
    if (l < 0) {
      return Result.getBadRequestError("Invalid folder id");
    }

    String lockName = "DocFolder" + "." + folderId;
    Result res;
    try {
      UMApi.switchToPrivilegedMode();
      CacheApi.acquireLock(CoreDocUtil.DOC_AI_WORKFLOW_LOCK_GROUP, lockName);
      res = resetStatus("DocFolder", l);

      if (res .isFailed()) {
        return res;
      }

      String updateSql = "update Doc_tbl set status = '', taskExecId = -1 "
          + "where (status = '" + CoreDocUtil.STATUS_PROCESSING + "' or status = '" + CoreDocUtil.STATUS_ENQUEUED + "'"
          + ")  and folderId = " + folderId;


      Object res2 = DSApi.executeSqlWriteStatement(updateSql);

      if (res2 instanceof Result) {
        return (Result) res2;
      }
      return Result.getSuccessResult();
    } finally {
      UMApi.resetPrivilegedMode();
      CacheApi.releaseLock(CoreDocUtil.DOC_AI_WORKFLOW_LOCK_GROUP, lockName);
    }


  }

  @SuppressWarnings("unchecked")
  public static Object extractContent(Object data) {
    Map<String, Object> resMap;
    if (data instanceof Map<?, ?>) {
      resMap = (Map<String, Object>)data;
      if (resMap.get("messages") instanceof List<?>) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) resMap.get("messages");
        if (list.size() > 0) {
          if (list.get(0).containsKey("content")) {
            return list.get(0).get("content");
          }
        }
      }
      if (resMap.containsKey("content")) {
        return resMap.get("content");
      }
    }
    return data;
  }

  public static Object extractContentAndParseAsJson(Object data) {
    Object content = extractContent(data);
    return CoreDocUtil.parseJsonAsObjectOrList(content);
  }

  public static Object parseJsonAsObjectOrList(Object data) {
    try {
      // try parsing as Object
      return parseJsonAsObject(data);
    } catch (Exception exc) {
      return parseJsonAsList(data);
    }
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> parseJsonAsObject(Object data) {
    Map<String, Object> map;
    if (data instanceof Map<?, ?>) {
      map = (Map<String, Object>) data;
      return fixMap(map);
    } else if (data instanceof String){
      String str = _cleanJSON((String) data, '{', '}');
      try {
        map = Util.fromJSONStringAsMap(str);
        return fixMap(map);
      } catch (Exception exc) {
        //LogApi.error("Failed to parse as object, error: " + exc.getMessage() + "\nstring:\n" + data + "\nclean string: " + correctedJson);
        throw new RuntimeException("Invalid JSON");
      }
    }
    throw new RuntimeException("Invalid JSON");
  }

  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> parseJsonAsList(Object data) {
    List<Map<String, Object>> list;
    if (data instanceof List<?>) {
      list = (List<Map<String, Object>>) data;
      return fixList(list);
    } else if (data instanceof String){
      String str = _cleanJSON((String) data, '[', ']');
      try {
        list = Util.fromJSONStringAsListOfMap(str);
        return fixList(list);
      } catch (Exception exc) {
        LogApi.error("Failed to parse as list, error: " + exc.getMessage() + "\nstring:\n" + data);
        throw new RuntimeException("Invalid JSON");
      }
    }
    throw new RuntimeException("Invalid JSON");
  }

  public static Map<String, Object> fixMap(Map<String, Object> map) {
    Map<String, Object> map2 = new LinkedHashMap<String, Object>();
    Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
    Entry<String, Object> e;
    while (iter.hasNext()) {
      e = iter.next();
      map2.put(e.getKey().replaceAll("\\s", ""), e.getValue());
    }
    return map2;
  }

  public static List<Map<String, Object>> fixList(List<Map<String, Object>> list) {
    List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
    for (Map<String, Object> map : list) {
      list2.add(fixMap(map));
    }
    return list2;
  }


  // start and end characters are '{', '}' pairs or '[', ']' pairs.
  private static String _cleanJSON(String str, char startChar, char endChar) {

    // first remove any markup surrounding JSON

    int idx = str.indexOf(startChar);

    if (idx < 0) {
      throw new RuntimeException("Starting char is not found, char: '" + startChar + "'");
    }

    int idx2;
    if (startChar == '{') {
      idx2 = str.indexOf('[');
    } else {
      idx2 = str.indexOf('{');
    }
    if (idx2 >= 0 && idx2 < idx) {
      throw new RuntimeException("Starting char is not found, char: '" + startChar + "'");
    }

    str = str.substring(idx);

    // now end char
    idx = str.lastIndexOf(endChar);
    if (idx > 0) {
      str = str.substring(0, idx + 1);
    } else {
      throw new RuntimeException("End char is not found, char: '" + startChar + "'");
    }
    str = fixJson(str);
    return str;
  }

  public static String fixJson(String json) {
    StringBuilder fixedJson = new StringBuilder();

    for (int i = 0; i < json.length(); i++) {
      char current = json.charAt(i);

      // Check for the extra comma condition
      if (current == ',') {
        int j = i + 1;
        while (j < json.length() && Character.isWhitespace(json.charAt(j))) {
          j++;
        }
        if (j < json.length() && (json.charAt(j) == '}' || json.charAt(j) == ']')) {
          // Skip this comma
          continue;
        }
      }
      fixedJson.append(current);
    }

    return fixedJson.toString();
  }

  @SuppressWarnings("unchecked")
  public static String getPartText(Map<String, Object> contentWrapper) {
    if (contentWrapper.get("content") instanceof Map<?, ?>) {
      Map<String, Object> content = (Map<String, Object>) contentWrapper.get("content");
      if (content.get("parts") instanceof List<?>) {
        List<Map<String, Object>> parts = (List<Map<String, Object>>)content.get("parts");
        if (parts.size() > 0) {
          return "" + parts.get(0).get("text");
        }
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static String getGroudingText(Map<String, Object> answer) {
    String str = null;
    if (answer.get("groundingAttributions") instanceof List<?>) {
      List<Map<String, Object>> groundingAttributions = (List<Map<String, Object>>)answer.get("groundingAttributions");
      for (Map<String, Object> m : groundingAttributions) {
        Map<String, Object> content = (Map<String, Object>) m.get("content");
        if (content != null && content.get("parts") instanceof List<?>) {
          List<Map<String, Object>> parts = (List<Map<String, Object>>)content.get("parts");
          if (parts.size() > 0) {
            str = (str == null ? "" : "\n\n") + parts.get(0).get("text");
          }
        }
      }
    }
    return str;
  }

  @SuppressWarnings("unchecked")
  public static int getPassageId(Map<String, Object> answer) {
    if (answer.get("groundingAttributions") instanceof List<?>) {
      List<Map<String, Object>> groundingAttributions = (List<Map<String, Object>>)answer.get("groundingAttributions");
      if (groundingAttributions.size() > 0) {
        Map<String, Object> sourceId = (Map<String, Object>) groundingAttributions.get(0).get("sourceId");
        if (sourceId != null && sourceId.get("groundingPassage") instanceof Map<?, ?>) {
          Map<String, Object> groundingPassage = (Map<String, Object>)sourceId.get("groundingPassage");
          return Util.asInt(groundingPassage.get("passageId"), -1);
        }
      }
    }
    return -1;
  }

  public static String makeDisplayName(String name) {
    String displayName;
    displayName = name.replaceAll("_", " ");
    return displayName;
  }

  @SuppressWarnings("unchecked")
  public static String makeText(Object value) {
    String text = "";
    if (value instanceof List<?>) {
      List<String> sl = (List<String>) value;
      for (Object s: sl) {
        text += (text.length() > 0 ? "\n" : "") + s;
      }
    } else if (value instanceof String) {
      text = "" + value;
    }
    return text;
  }

  public static String getFixedName(String name) {
    name = name.replaceAll("'s ", "");
    String fixedName = Util.toValidIdentifier(name);
    if (fixedName.length() > 1) {
      fixedName = Util.removeLastCharIfMatches(fixedName, '_');
    }
    if (fixedName.length() > 1) {
      fixedName = Util.removeLeadingChars(fixedName, '_');
    }
    return fixedName;
  }

  public static void inferImageCaption(Document document) {
    try {
      if (document.pages == null) {
        return;
      }
      for (Page page: document.pages) {
        inferImageCaption(page);
      }
    } catch (Exception exc) {}
  }

  private static void inferImageCaption(Page page) {
    if (page.paragraphs == null || page.images == null) {
      return;
    }
    List<ParaInfo> pl = makeParaInfoList(page);
    if (pl.size() == 0) {
      return;
    }
    //System.out.println("\n-------\n");
    for (Image im : page.images) {
      updateDelta(im, pl);
      Collections.sort(pl, new ParaInfoComparator());
      im.altText = pl.get(0).paragraph.text;
      //System.out.println(im.altText + "\n");
    }
  }

  public static String templateWithPath(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("fileName cannot be null");
    }
    String extension = FileUtil.getFileExtension(fileName);
    if (StringUtils.isBlank(extension)) {
      fileName = fileName + ".txt";
    }
    if (fileName.startsWith("shared/domainMeta/")) {
      return fileName;
    } else {
      return "shared/domainMeta/docAI/templates/" + fileName;
    }
  }

  public static String workflowFileWithPath(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("fileName cannot be null");
    }
    String extension = FileUtil.getFileExtension(fileName);
    if (StringUtils.isBlank(extension)) {
      fileName = fileName + ".json";
    }
    if (fileName.startsWith("shared/domainMeta/")) {
      return fileName;
    } else {
      return "shared/domainMeta/docAI/workflows/" + fileName;
    }
  }

  public static String getFormattedDateSolr(Object object) {
    String dt = getFormattedDate(object);
    if (dt == null) {
      return null;
    }
    dt += "T00:00:00Z";
    return dt;
  }

  public static String getFormattedDate(Object object) {
    String s = object.toString();
    if (s.length() < 4) {
      return null;
    }
    String[] sl = s.split("-");
    String dt;
    if (sl.length == 1) {
      dt = sl[0] + "-01-" + "01";
    } else if (sl.length == 2) {
      dt = sl[0] + "-" + sl[1] + "-" + "01";
    } else if (sl.length > 2) {
      dt = sl[0] + "-" + sl[1] + "-" + sl[2];
    } else {
      return null;
    }
    return dt;
  }

  public static String conceptMarker() {
    return "\n\n__concepts__\n";
  }

  public static void removeConcepts(Map<String, Object> m) {
    if (m == null) {
      return;
    }
    if (m.get("content") instanceof String) {
      String content = "" + m.get("content");
      int idx = content.indexOf( "\n\n__concepts__\n");
      if (idx >= 0) {
        content = content.substring(0, idx);
        m.put("content", content);
      }
    }
  }

  public static long getDocTopFolderId(Map<String, Object> doc) {
    if (doc.containsKey("topFolderId")) {
      long id = Util.asLong(doc.get("topFolderId"), 0);
      if (id != 0) {
        return id;
      }
    }
    return Util.asLong(doc.get("folderId"), -1);
  }

  public static long getFolderTopParentId(Map<String, Object> folder) {
    if (folder.containsKey("topParentId")) {
      long id = Util.asLong(folder.get("topParentId"), 0);
      if (id != 0) {
        return id;
      }
    }
    return Util.asLong(folder.get("parentId"), -1);
  }

  public static long getFolderTopParentIdForDoc(Map<String, Object> folder) {
    long id;
    if (folder.containsKey("topParentId")) {
      id = Util.asLong(folder.get("topParentId"), 0);
      if (id != 0) {
        return id;
      }
    }
    id = Util.asLong(folder.get("parentId"), -1);
    if (id > 0) {
      return id;
    }
    id = Util.asLong(folder.get("id"), -1);
    return id;
  }

  public static void sortToc(List<Map<String, Object>> l) {
    sortByPageNumber(l);
  }

  // returns null if not extracted
  public static String extractBlock(String str, String blockMarker) {
    int idx1 = str.indexOf(blockMarker);
    int idx2 = str.indexOf(blockMarker, idx1 + blockMarker.length());
    if (idx1 < 0 || idx2 < 0) {
      return null;
    }
    return str.substring(idx1 + blockMarker.length(), idx2);
  }

  // returns original text if not removed
  public static String removeBlock(String str, String blockMarker) {
    int idx1 = str.indexOf(blockMarker);
    int idx2 = str.indexOf(blockMarker, idx1 + blockMarker.length());
    if (idx1 < 0 || idx2 < 0) {
      return str;
    }
    return str.substring(0, idx1) + str.substring(idx2 + blockMarker.length());
  }

  private static void sortByPageNumber(List<Map<String, Object>> l) {
    Collections.sort(l, new Comparator<Map<String, Object>>() {
      public int compare(Map<String, Object> n1, Map<String, Object> n2) {
        int i1 = Util.asInt(n1.get("pageNumber"), -1);
        int i2 = Util.asInt(n2.get("pageNumber"), -1);
        return Integer.compare(i1, i2);
      }
    });
  }

  public static void putInCacheSearchWorkflow(String folderId, DocWorkflow workflow) {
    LogApi.info("Putting workflow in cache, folderId: " + folderId);
    CacheApi.put(CACHE_SEARCH_WORKFLOW_MAP_NAME, folderId, workflow);
  }

  public static DocWorkflow getFromCacheSearchWorkflow(String folderId) {
    LogApi.info("Getting workflow from cache, folderId: " + folderId);
    Object o = CacheApi.get(CACHE_SEARCH_WORKFLOW_MAP_NAME, folderId);
    if (o != null) {
      if (o instanceof DocWorkflow) {
        return (DocWorkflow) o;
      } else {
        removeFromCacheSearchWorkflow(folderId);
      }
    }
    return null;
  }

  public static void removeFromCacheSearchWorkflow(String folderId) {
    LogApi.info("Removing workflow from cache, folderId: " + folderId);
    CacheApi.remove(CACHE_SEARCH_WORKFLOW_MAP_NAME, folderId);
  }
  
  public static boolean isInputJsonArray(Object value) {
    if (value instanceof String) {
      return ("" + value).trim().startsWith("[");
    } else if (value instanceof List<?>) {
      return true;
    }
    return false;
  }
  
  public static boolean isJsonType(String type) {
    return "json".equals(type) || "jsonAsHtml".equals(type);
  }
  
  public static String removeCodeBlocks(String text) {
    String pattern = "```(?:[a-zA-Z]+)?\\n(.*?)(?:```|$)"; // Non-greedy match, handles end of string.
    Pattern regex = Pattern.compile(pattern, Pattern.DOTALL);
    Matcher matcher = regex.matcher(text);
    StringBuilder result = new StringBuilder();

    while (matcher.find()) {
        result.append(matcher.group(1).trim()).append("\n"); // Append the captured group and a newline
    }

    if (result.length() > 0) {
        return result.toString().trim(); //trim to remove trailing whitespace.
    } else {
        return text; // Return original text if no code blocks found.
    }
  }
  
  public static Object convertToJSON(String str) {
    str = str.trim();
    if (str.startsWith("[")) {
      List<Map<String, Object>> l = Util.fromJSONStringAsListOfMap(str);
      return l;
    } else if (str.startsWith("{")) {
      Map<String, Object> m = Util.fromJSONStringAsMap(str);
      return m;
    }
    return null;
  }

  private static void updateDelta(Image im, List<ParaInfo> pl) {
    double d1, d2;
    boolean f1, f2;
    for (ParaInfo pi : pl) {
      f1 = overlapsX(im, pi);
      f2 = overlapsY(im, pi);
      if (f1 && f2) {
        pi.delta = Double.MAX_VALUE;
      } else if (f1) {
        if (pi.y2 < im.y) {
          pi.delta = im.y - pi.y2;
        } else {
          pi.delta = pi.y1 - im.y - im.height;
        }
      } else if (f2) {
        if (pi.x2 < im.x) {
          pi.delta = im.x - pi.x2;
        } else {
          pi.delta = pi.x1 - im.x - im.width;
        }
      } else {
        if (pi.x2 < im.x) {
          d1 = im.x - pi.x2;
        } else {
          d1 = pi.x1 - im.x - im.width;
        }
        if (pi.y2 < im.y) {
          d2 = im.y - pi.y2;
        } else {
          d2 = pi.y1 - im.y - im.height;
        }
        pi.delta = Math.min(d1, d2);
      }
    }

  }

  private static boolean overlapsX(Image im, ParaInfo pi) {
    if ((pi.x1 >= im.x && pi.x1 <= im.x + im.width)||
        (pi.x2 >= im.x && pi.x2 <= im.x + im.width)) {
      return true;
    }
    return false;
  }

  private static boolean overlapsY(Image im, ParaInfo pi) {
    if ((pi.y1 >= im.y && pi.y1 <= im.y + im.height)||
        (pi.y2 >= im.y && pi.y2 <= im.y + im.height)) {
      return true;
    }
    return false;
  }


  private static List<ParaInfo> makeParaInfoList(Page page) {
    List<ParaInfo> l = new ArrayList<ParaInfo>();
    ParaInfo pi;
    for (Paragraph p : page.paragraphs) {
      if (p.text == null || p.text.length() < 6) {
        continue;
      }
      if (Character.isLowerCase(p.text.charAt(0))) {
        continue;
      }
      pi = new ParaInfo();
      pi.paragraph = p;
      pi.x1 = p.x1;
      pi.x2 = p.x2;
      pi.y1 = p.y1;
      pi.y2 = p.y2;
      pi.textLength = p.text.length();
      l.add(pi);
    }
    return l;
  }

  static class ParaInfoComparator implements Comparator<ParaInfo> {

    @Override
    public int compare(ParaInfo bi1, ParaInfo bi2) {
      double d = Math.abs(bi1.delta-bi2.delta);
      if (d > 0.01) {
        return Double.compare(bi1.delta, bi2.delta);
      } else {
        return Integer.compare(bi1.textLength, bi2.textLength);
      }
    }

  }

  static String fixFormatting(String str) {
    if (str == null) {
      return str;
    }
    return str.replace("''", "'");
  }

  static class ParaInfo {
    double x1;
    double y1;
    double x2;
    double y2;
    double delta;
    int textLength;
    Paragraph paragraph;
  }
  
  public static String buildBucketUrl() {
    return "gs://" + Util.getBucketName() + "/";
  }
  
  public static String extractUrlFromImageUrl(String url) {
    Pattern pattern = Pattern.compile("path=([^&]+)");
    Matcher matcher = pattern.matcher(url);
    if (matcher.find()) {
      return matcher.group(1);
    } else {
      return null;
    }
  }
  
  public static ArrayList<Map<String, Object>> buildPartsList(Map<String, Object> objectMap,
      String prompt) {
    ArrayList<Map<String, Object>> parts = new ArrayList<>();
    Map<String, Object> filedata = new HashMap<>();
    filedata.put("fileData", objectMap);
    parts.add(filedata);

    Map<String, Object> text = new HashMap<>();
    text.put("text", prompt);
    parts.add(text);

    return parts;
  }
  
  public static ArrayList<Map<String, Object>> buildPartsList(List<Map<String, Object>> objectList,
      String prompt) {
    ArrayList<Map<String, Object>> parts = new ArrayList<>();
    
    for (Map<String, Object> objectMap : objectList) {
      Map<String, Object> filedata = new HashMap<>();
      filedata.put("fileData", objectMap);
      parts.add(filedata);
    }

    Map<String, Object> text = new HashMap<>();
    text.put("text", prompt);
    parts.add(text);

    return parts;
  }
}
