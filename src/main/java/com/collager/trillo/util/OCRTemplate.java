package com.collager.trillo.util;
import java.util.ArrayList;
import java.util.List;
import com.collager.trillo.util.OCR.Cell;
import com.collager.trillo.util.OCR.Line;
import com.collager.trillo.util.OCR.Word;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class OCRTemplate {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Template {
    public String name;
    public String label;
    public List<String> searchPhrases;
    public int startLineForTitleSearch;
    public int endLineForTitleSearch;
    public List<TemplatePage> templatePages;
    
    public TemplatePage findTemplatePage(int pageNumber) {
      if (templatePages == null) {
        return null;
      }
      
      for (TemplatePage tp : templatePages) {
        if (tp.pageNumber == pageNumber) {
          return tp;
        }
      }
      
      return null;
    }
  }
  
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TemplatePage {
    public String name;
    public String label;
    public List<String> searchPhrases;
    public int startLineForTitleSearch;
    public int endLineForTitleSearch;
    public List<TemplateTable> templateTables;
    public int pageNumber = -1;
  }
  
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TemplateTable {
    public String name;
    public String label;
    public List<String> searchPhrases; // for title
    public int startLineForTitleSearch;
    public int endLineForTitleSearch;
    public int startLineForColumnsSearch;
    public int endLineForColumnsSearch;
    
    public List<TemplateColumn> templateColumns;
    
    // search phrase which will help decide starting row of data
    public String searchPhrasesStartLineIndex = null; 
    public int startLineSearchOccurrence = 1;
    public int startLineForStartIndexSearch = 1;
    public int endLineForStartIndexSearch = -1;
    public boolean searchedStartLineInclusive = false; // the line deduced by search, should it be included in the data
    
    // search phrase which will help decide end row of data
    public String searchPhrasesEndLineIndex = null; 
    public int endLineSearchOccurrence = 1;
    public int startLineForEndIndexSearch = 1;
    public int endLineForEndIndexSearch = -1;
    public boolean searchedEndLineInclusive = false; // the line deduced by search, should it be included in the data
    
    public String getDiaplyName() {
      if (label != null) return label;
      return name;
    }
    
    public SearchRequest getSearchRequest() {
      if (searchPhrasesStartLineIndex == null) {
        return null;
      }
      SearchRequest req = new SearchRequest();
      req.searchPhrases.add(searchPhrasesEndLineIndex);
      req.occurrence = endLineSearchOccurrence;
      req.start = startLineForEndIndexSearch;
      req.end = endLineForEndIndexSearch;
      return req;
    }
  }
  
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TemplateColumn {
    public String name;
    public String label;
    public List<String> searchPhrases;
    public int occurrence = 1; // occurrence of the search phrase for the match
    public String type;
    public String searchResultLineText = null; // for debugging
    @JsonIgnore
    public SearchResult searchResult;
    public boolean required = false;
    public double leftStretch = 0; // percentage of width
    public double rightStretch = 0;  // percentage of width
  }
  
  public static class SearchRequest {
    public int start;
    public int end;
    public String label;
    public List<String> searchPhrases = new ArrayList<String>();
    public int occurrence = 1; // 1 means first occurrence
  }
  
  public static class SearchResult {
    public int lineIndex = -1;
    public Line line;
    public Cell cell;
    public List<Word> wl = null;
  }
  
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TemplateList {
    public String name;
    public String label;
    public List<TemplateListEntry> templates;
  }
  
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TemplateListEntry {
    public String templateName;
    public List<String> searchPhrases;
    public int startLineForSearch;
    public int endLineForSearch;
    public int pageNumber;
    public String parser;
  }
  
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SearchRequestWithLines {
    public List<Line> lines;
    public List<SearchRequest> requests = new ArrayList<SearchRequest>();
  }
}
