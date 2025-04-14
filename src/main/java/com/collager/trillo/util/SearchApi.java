package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.collager.trillo.util.OCR.Line;
import com.collager.trillo.util.OCRTemplate.SearchRequest;
import com.collager.trillo.util.OCRTemplate.SearchRequestWithLines;
import com.collager.trillo.util.OCRTemplate.SearchResult;
import com.collager.trillo.util.OCRTemplate.TemplateTable;

public class SearchApi extends BaseApi {

  public static List<SearchResult> search(SearchRequestWithLines srwl) {
    List<Map<String, Object>> l = BaseApi.remoteCallAsListOfMaps("SearchApi", "search", srwl);
    List<SearchResult> sl = new ArrayList<SearchResult>();
    SearchResult sr;
    for (Map<String, Object> m : l) {
      sr = Util.fromMap(m, SearchResult.class);
      sl.add(sr);
    }
    return sl;
  }
  
  public static SearchResult search(SearchRequest req, List<Line> lines) {
    SearchRequestWithLines srwl = new SearchRequestWithLines();
    srwl.lines = lines;
    srwl.requests.add(req);
    List<SearchResult> rl = search(srwl);
    if (rl.size() > 0) {
      return rl.get(0);
    } else {
      return null;
    }
  }
  
  public static SearchResult search(List<Line> lines, String searchPhrase) {
    SearchRequest sr = new SearchRequest();
    sr.searchPhrases.add(searchPhrase);
    sr.start = 1;
    sr.end = lines.size();
    sr.occurrence = 1;
    return search(sr, lines);
  }
  
  public static int getStartLineIndex(TemplateTable tc, List<Line> lines) {
    SearchRequest req = tc.getSearchRequest();
    if (req == null) {
      return -1;
    }
    
    SearchResult res = SearchApi.search(req, lines);
    
    if (res == null) {
      return -1;
    }
    
    return res.lineIndex + (tc.searchedEndLineInclusive ? 0 : 1);
  }
  
  public static int getEndLineIndex(TemplateTable tc, List<Line> lines) {
    SearchRequest req = tc.getSearchRequest();
    if (req == null) {
      return -1;
    }

    SearchResult res = SearchApi.search(req, lines);
    
    if (res == null) {
      return -1;
    }
    
    return res.lineIndex + (tc.searchedStartLineInclusive ? 0 : 1);
  }

  public static int getLineIndex(TemplateTable tc, List<Line> lines) {
    SearchRequest req = tc.getSearchRequest();
    if (req == null) {
      return -1;
    }

    SearchResult res = SearchApi.search(req, lines);

    if (res == null) {
      return -1;
    }

    return res.lineIndex;
  }

  public static List<SearchResult> search(List<Line> lines, List<String> searchPhrases) {
    SearchRequestWithLines srwl = new SearchRequestWithLines();
    srwl.lines = lines;
    SearchRequest sr;
    for (String searchPhrase: searchPhrases) {
      sr = new SearchRequest();
      sr.searchPhrases.add(searchPhrase);
      sr.start = 1;
      sr.end = lines.size();
      sr.occurrence = 1;
      srwl.requests.add(sr);
    }

    List<SearchResult> rl = search(srwl);
    return rl;
  }

  public static List<SearchResult> search(SearchRequestWithLines srwl, int occurrence) {
    for (SearchRequest sr: srwl.requests) {
      sr.occurrence = occurrence;
    }

    List<SearchResult> rl = search(srwl);
    return rl;
  }

  public static SearchResult search(SearchRequest req, List<Line> lines, int occurrence) {
    req.occurrence = occurrence;
    SearchResult res = search(req, lines);
    return res;
  }

  public static SearchResult search(List<Line> lines, String searchPhrase, int occurrence) {
    SearchRequest sr = new SearchRequest();
    sr.searchPhrases.add(searchPhrase);
    sr.start = 1;
    sr.end = lines.size();
    sr.occurrence = occurrence;
    return search(sr, lines);
  }
}
