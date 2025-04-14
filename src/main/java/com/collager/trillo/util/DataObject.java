package com.collager.trillo.util;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jodd.util.StringUtil;

public class DataObject {

  public static final float CONFIDENCE_THREHOLD = 0.9f;

  public static float getAggregateConfidenceOfPages(List<Page> pl) {
    float total = 0;
    int n = 0;
    for (Page p: pl) {
      if (p.confidence > 0) {
        total += p.confidence;
        n++;
      }
    }
    if (n > 0) {
      return total / n;
    }
    return total;
  }

  public static float getAggregateConfidence(List<Rect> rl) {
    float total = 0;
    int n = 0;
    for (Rect r: rl) {
      if (r.confidence > 0) {
        total += r.confidence;
        n++;
      }
    }
    if (n > 0) {
      return total / n;
    }
    return total;
  }

  @JsonPropertyOrder({"text", "confidence", "fontSize", "relativeFontSize", "x1", "y1","x2", "y2", "width", "height"})
  @JsonInclude(Include.NON_DEFAULT)
  abstract public static class Rect {
    public String text = null;
    public String type = "text";
    public double x1 = -1;
    public double x2 = -1;
    public double y1 = -1;
    public double y2 = -1;
    public float confidence = -1.0f;
    public double width = 0;
    public double height = 0;
    public float fontSize = 0;
    public float relativeFontSize = 0;
    public boolean matched = false;
    public boolean hidden = false;

    @JsonIgnore
    public boolean renderable = true;

    public void copyTo(Rect t) {
      t.x1 = x1;
      t.x2 = x2;
      t.y1 = y1;
      t.y2 = y2;
      t.confidence = confidence;
      t.width = width;
      t.height = height;
      t.fontSize = fontSize;
      t.relativeFontSize = relativeFontSize;
      t.matched = matched;
      t.hidden = hidden;
    }

    public void union(Rect r) {
      if (r.x1 < 0 || r.y1 < 0) {
        return;
      }
      if (x1 < 0 || r.x1 < x1) {
        x1 = r.x1;
      }
      if (y1 < 0 || r.y1 < y1) {
        y1 = r.y1;
      }
      if (x2 < 0 || r.x2 > x2) {
        x2 = r.x2;
      }
      if (y2 < 0 || r.y2 > y2) {
        y2 = r.y2;
      }
      width = x2 - x1;
      height = y2 - y1;
    }

    public void incrementHeight(Rect r) {
      height += r.height;
      y2 = y1 + height;
    }

    public void updateX1(double x1) {
      this.x1 = x1;
      this.x2 = x1 + width;
    }

    public void updateY1(double y1) {
      this.y1 = y1;
      this.y2 = y1 + height;
    }

    public Rect makeRenderable() {
      Rect r = makeSimpleCopy();
      return r;
    }

    abstract public Rect makeSimpleCopy();
    abstract public String retrieveText();
  }

  @JsonPropertyOrder({"tableId", "text", "confidence", "fontSize", "relativeFontSize",
      "detectedBreak", "spaceCharWidth", "colSpan", "isNull", "x1", "y1","x2", "y2", "width", "height"})

  public static class Token extends Rect {
    public String detectedBreak;
    public double spaceCharWidth = 0;
    public boolean isNull = false;

    @JsonIgnore
    public boolean taken = false;

    public Token() {
      type = "token";
    }
    @JsonIgnore
    public int tableId = 0;

    public void updateRelativeFontSize(float minFontSize) {
      float sz = fontSize > 0 ? fontSize : (float)height;
      sz = sz / minFontSize;
      //float v = Math.round(sz * 2) / 2;
      //if (v > 3) v = 3;
      if (sz > 3) sz = 3;
      relativeFontSize = Math.round(sz * 100.0f) / 100.0f;;
    }

    public Token clone() {
      Token t = new Token();
      copyTo(t);
      return t;
    }

    public void copyTo(Token t) {
      super.copyTo(t);
      t.detectedBreak = detectedBreak;
      t.spaceCharWidth = spaceCharWidth;
      t.isNull = isNull;
    }

    public void mergeToken(Token token) {
      if (token.text != null) {
        text += " " + token.text;
      }
      union(token);
    }

    @Override
    public Token makeSimpleCopy() {
      Token t = new Token();
      t.text = text;
      t.type = type;
      return t;
    }

    @Override
    public String retrieveText() {
      if (StringUtils.isBlank(text)) {
        return "";
      }
      return text;
    }
  }

  @JsonPropertyOrder({"name", "text", "displayName",
      "x1", "y1","x2", "y2", "width", "height"})
  public static class Cell extends Rect {
    public String name = "";
    public String displayName;

    public Cell() {
      type = "cell";
    }

    public int numberOfChars() {
      int l = name != null ? name.length() : 0;
      l += text != null ? text.length() + 1 : 0;
      return l;
    }
    @Override
    public Cell makeSimpleCopy() {
      Cell c = new Cell();
      c.name = name;
      c.text = text;
      c.type = type;
      return c;
    }

    @Override
    public String retrieveText() {
      return (StringUtils.isBlank(name) ? "" : name + ": ") + text;
    }

    public void fixFormatting() {
      name = CoreDocUtil.fixFormatting(name);
      text = CoreDocUtil.fixFormatting(text);
    }
  }

  @JsonPropertyOrder({"name", "value", "displayName", "diplayName", "text", "pageNumber", "x1", "y1","x2", "y2", "width", "height"})
  public static class Field extends Rect {
    public String name = "";
    public int pageNumber;
    public String diplayName;
    public String displayName;
    public Object value = "";
    public Rect labelRect;

    public Field() {
      type = "formField";
    }

    public void copyTo(Field f) {
      super.copyTo(f);
      f.value = value;
    }

    public int numberOfChars() {
      int l = name != null ? name.length() : 0;
      l += value != null ? (value + "").length() + 1: 0;
      return l;
    }
    @Override
    public Field makeSimpleCopy() {
      Field f = new Field();
      f.name = name;
      f.text = text;
      f.value = value;
      f.diplayName = displayName;
      f.type = type;
      return f;
    }

    @Override
    public String retrieveText() {
      return name + ": " + text;
    }

    public void fixFormatting() {
      name = CoreDocUtil.fixFormatting(name);
      text = CoreDocUtil.fixFormatting(text);
    }
  }

  @JsonPropertyOrder({"name", "value", "displayName", "diplayName", "text", "pageNumber", "x1", "y1","x2", "y2", "width", "height"})
  public static class FieldSet extends Rect {
    public List<Field> fields = null;

    public FieldSet() {
      type = "fieldSet";
    }

    public void addField(Field field) {
      if (fields == null) {
        fields = new ArrayList<>();
      }
      union(field);
      fields.add(field);
    }

    public void copyTo(FieldSet fieldSet) {
      super.copyTo(fieldSet);
      if (fields != null) {
        fieldSet.fields = new ArrayList<>();
        Field f2;
        for (Field f : fields) {
          f2 = new Field();
          f.copyTo(f2);
          fieldSet.fields.add(f2);
        }
      }
    }

    public int numberOfChars() {
      int n = 0;
      if (fields != null) {
        for (Field f : fields) {
          n+= f.numberOfChars();
        }
        n += fields.size();
      }
      return n;
    }

    @Override
    public FieldSet makeSimpleCopy() {
      FieldSet fieldSet = new FieldSet();
      if (fields != null) {
        fieldSet.fields = new ArrayList<>();
        for (Field f : fields) {
          fieldSet.fields.add(f.makeSimpleCopy());
        }
      }
      return fieldSet;
    }

    @Override
    public String retrieveText() {
      if (fields != null) {
        String str = "";
        for (Field f : fields) {
          str += f.retrieveText() + "\n";
        }
        return str;
      }
      return "";
    }

    public void fixFormatting() {
      if (fields != null) {
        for (Field f : fields) {
          f.fixFormatting();
        }
      }
    }
  }

  public static class LabelRect extends Rect {

    public LabelRect() {
      type = "labelRect";
    }

    @Override
    public Rect makeSimpleCopy() {
      return null;
    }
    @Override
    public String retrieveText() {
      return text;
    }
  }

  @JsonPropertyOrder({"name", "text", "x1", "y1","x2", "y2", "width", "height", "cells"})
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TableRow extends Rect {
    public List<Cell> cells = null;

    public TableRow() {
      type = "tableRow";
    }

    public TableRow clone() {
      TableRow t = new TableRow();
      copyTo(t);
      return t;
    }

    public void copyTo(TableRow t) {
      super.copyTo(t);
      t.cells = cells;
    }

    public Cell getCell(String name) {
      for (Cell cell : cells) {
        if (name.equals(cell.name)) {
          return cell;
        }
      }
      return null;
    }

    public Cell addCell(String name, String cellText) {
      if (cells == null) {
        cells = new ArrayList<Cell>();
      }
      Cell cell = new Cell();
      cell.name = name;
      cell.text = cellText;
      cells.add(cell);
      return cell;
    }

    public void addCell(Cell cell) {
      if (cells == null) {
        cells = new ArrayList<Cell>();
      }
      cells.add(cell);
    }

    public int numberOfChars() {
      int l = 0;
      for (Cell c : cells) {
        l += c.numberOfChars() + 1;
      }
      return l;
    }

    public Object getCellValue(String name) {
      Cell cell = getCell(name);
      if (cell != null) {
        return cell.text;
      }
      return null;
    }

    @Override
    public TableRow makeSimpleCopy() {
      TableRow r = new TableRow();
      if (cells != null) {
        for (Cell c : cells) {
          r.addCell(c.makeSimpleCopy());
        }
      }
      r.type = type;
      return r;
    }

    @Override
    public String retrieveText() {
      if (cells == null) {
        return "";
      }
      String str = "";
      for (Cell c : cells) {
        str += c.retrieveText() + "\n";
      }
      return str;
    }

    public void fixFormatting() {
      if (cells != null) {
        for (Cell c : cells) {
          c.fixFormatting();
        }
      }
    }
  }

  @JsonPropertyOrder({"name", "text", "x1", "y1","x2", "y2", "width", "height", "cells"})
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TableHeader extends Rect {

    public TableHeader() {
      type = "tableHeader";
    }

    public List<Cell> cells = null;


    public TableHeader clone() {
      TableHeader t = new TableHeader();
      copyTo(t);
      return t;
    }

    public void copyTo(TableHeader t) {
      super.copyTo(t);
      t.cells = cells;
    }

    public Cell getCell(String name) {
      for (Cell cell : cells) {
        if (name.equals(cell.name)) {
          return cell;
        }
      }
      return null;
    }

    public Cell addCell(String name, String cellText) {
      if (cells == null) {
        cells = new ArrayList<Cell>();
      }
      Cell cell = new Cell();
      cell.name = name;
      cell.text = cellText;
      cells.add(cell);
      return cell;
    }

    public void addCell(Cell cell) {
      if (cells == null) {
        cells = new ArrayList<Cell>();
      }
      cells.add(cell);
    }

    public int numberOfChars() {
      int l = 0;
      for (Cell c : cells) {
        l += c.numberOfChars() + 1;
      }
      return l;
    }

    @Override
    public TableHeader makeSimpleCopy() {
      TableHeader h = new TableHeader();
      if (cells != null) {
        for (Cell c : cells) {
          h.addCell(c.makeSimpleCopy());
        }
      }
      h.type = type;
      return h;
    }

    @Override
    public String retrieveText() {
      if (cells == null) {
        return "";
      }
      String str = "";
      for (Cell c : cells) {
        if (StringUtils.isNotBlank(c.name)) {
          str += c.name + "\n";
        }
      }
      return str;
    }
  }

  @JsonPropertyOrder({"name", "text", "csvTable", "columns", "columns2", "header", "rows", "x1", "y1","x2", "y2", "width", "height"})
  public static class Table extends Rect {
    public List<TableRow> rows = null;
    public TableHeader header = null;
    public List<String> columns = null;
    public boolean csvTable = false;

    public Table() {
      type = "table";
    }

    public int numberOfChars() {
      int l = 0;
      if (columns != null) {
        for (String s : columns) {
          if (s != null) {
            l += s.length() + 1;
          }
        }
      }
      if (rows != null) {
        for (TableRow r : rows) {
          l += r.numberOfChars() + 1;
        }
      }
      return l;
    }

    public void addRow(String[] sarr) {
      if (rows == null) {
        rows = new ArrayList<>();
      }
      TableRow row = new TableRow();
      int idx = 0;
      String name;
      for (String s : sarr) {
        name = columns != null && idx < columns.size() ? columns.get(idx) : "";
        row.addCell(name, s);
        idx++;
      }
      rows.add(row);
    }

    public void addRow(TableRow row) {
      if (rows == null) {
        rows = new ArrayList<>();
      }
      rows.add(row);
    }

    public String retrieveText() {
      String text = "";
      if (rows == null) {
        return text;
      }
      for (int i=0; i<rows.size(); i++) {
        if (text.length() > 0) {
          text += "\n";
        }
        text += rows.get(i).retrieveText();
      }
      return text;
    }

    @Override
    public Table makeSimpleCopy() {
      Table t = new Table();
      if (columns != null) {
        t.columns = new ArrayList<>(columns);
      }
      if (header != null) {
        t.header = header.makeSimpleCopy();
      }
      if (rows != null) {
        for (TableRow r : rows) {
          t.addRow(r);
        }
      }
      t.type = type;
      return t;
    }

    public void updateColumnNames() {
      if (header == null || header.cells == null) {
        return;
      }
      columns = new ArrayList<>();
      for (Cell c : header.cells) {
        columns.add(c.name);
      }
    }

    public void fixFormatting() {
      if (columns != null) {
        List<String> columns2 = new ArrayList<>();
        for (String str : columns) {
          columns2.add(CoreDocUtil.fixFormatting(str));
        }
        columns = columns2;
      }
      if (header != null && header.cells != null) {
        for (Cell c : header.cells) {
          c.fixFormatting();
        }
      }
      if (rows != null) {
        for (TableRow r : rows) {
          r.fixFormatting();
        }
      }
    }
  }

  @JsonPropertyOrder({"name", "value", "text", "entities", "x1", "y1","x2", "y2", "width", "height"})
  public static class Entity extends Rect {
    public String name = "";
    public Object value = "";
    public List<Entity> entities = null; // nested entities

    public Entity() {
      type = "entity";
    }

    public void addEntity(Entity entity) {
      if (entities == null) {
        entities = new ArrayList<Entity>();
      }
      entities.add(entity);
    }

    @Override
    public Entity makeSimpleCopy() {
      Entity e = new Entity();
      e.name = name;
      e.text = text;
      e.value = value;
      e.type = type;
      return e;
    }

    @Override
    public String retrieveText() {
      return (StringUtils.isNotBlank(name) ? name + ": " : "") + value;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"name", "text", "confidence", "fontSize", "relativeFontSize", "x1", "y1","x2", "y2", "width", "height"})
  public static class Line extends Rect {

    public Line() {
      type = "line";
    }

    @Override
    public Line makeSimpleCopy() {
      Line l = new Line();
      l.text = text;
      l.type = type;
      return l;
    }
    @Override
    public String retrieveText() {
      if (StringUtils.isBlank(text)) {
        return "";
      }
      return text;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"name", "text", "spaceCharWidth", "confidence", "fontSize", "relativeFontSize", "subBlocks", "image",
      "x1", "y1","x2", "y2", "width", "height"})
  public static class Block extends Rect {
    public int blockId;
    public String subType;
    public double spaceCharWidth = 0;
    public Image image = null;
    public int pageStart;
    public int pageEnd;
    public List<Block> subBlocks = new ArrayList<Block>();
    @JsonIgnore
    List<String> sl = null;

    public Block() {
      type = "block";
    }

    public List<String> retrieveTextList() {
      List<String> tl = new ArrayList<>();
      if (!StringUtils.isBlank(text)) {
        String str = text + "\n";
        tl.add(str);
      }

      for (Block sb : subBlocks) {
        tl.addAll(sb.retrieveTextList());
      }
      return tl;
    }

    // used for comparing two blocks if they have the same text
    public List<String> _getSL() {
      if (sl != null) {
        return sl;
      }
      sl = new ArrayList<String>();
      if (StringUtils.isBlank(text)) {
        return sl;
      }
      String[] sarr = text.split("\\s+");
      sl = new ArrayList<>(Arrays.asList(sarr));
      return sl;
    }

    public Block makeSimpleCopy() {
      Block b2 = new Block();
      b2.type = type;
      b2.text = text;
      if (image != null) {
        b2.image = image.makeSimpleCopy();
      }
      return b2;
    }

    public Block makeRenderable() {
      Block b = makeSimpleCopy();
      if (b.image != null) {
        b.image.url = BaseApi.getServerBaseUrl() + b.image.url;
      }
      return b;
    }

    @Override
    public String retrieveText() {
      if (StringUtils.isBlank(text)) {
        return "";
      }
      return text;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"name", "text", "confidence", "fontSize", "relativeFontSize", "x1", "y1","x2", "y2", "width", "height", "extra", "isList", "textWithTags"})
  public static class Paragraph extends Rect {
    public boolean extra = false;
    public boolean isList = false;
    public String textWithTags = null;
    @JsonIgnore
    public int pageNumber;
    public Image image = null;

    public Paragraph() {
      type = "paragraph";
    }

    public Paragraph makeSimpleCopy() {
      Paragraph p = new Paragraph();
      p.text = text;
      p.type = type;
      if (image != null) {
        p.image = image.makeSimpleCopy();
      }
      return p;
    }

    public Paragraph makeRenderable() {
      Paragraph p = makeSimpleCopy();
      if (p.image != null) {
        p.image.url = BaseApi.getServerBaseUrl() + p.image.url;
      }
      return p;
    }

    public Paragraph makeSimpleCopyWithBounds() {
      Paragraph p = makeSimpleCopy();
      p.text = text;
      p.type = type;
      p.confidence = confidence;
      p.x1 = x1;
      p.y1 = y1;
      p.x2 = x2;
      p.y2 = y2;
      p.width = width;
      p.height = height;
      return p;
    }

    @Override
    public String retrieveText() {
      if (StringUtils.isBlank(text)) {
        return "";
      }
      return text;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"title", "summaryObj", "text", "textFormatted", "confidence"})
  public static class PageSummary {
    public String title;
    public String text;
    public String textFormatted;
    public float confidence = -1.0f;
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"pageNumber", "imageNumber", "fileName", "url", "tnUrl", "imageWidth", "imageHeight",
      "scaledWidth", "scaledHeight", "x", "y", "width", "height", "properties"})
  public static class Image {
    public int pageNumber = 0;
    public int imageNumber = 0;
    public String fileName = null;
    public String url = null;
    public String tnUrl = null;
    public String altText = null;
    public Map<String, Object> properties = null;

    // these are normalized dimensions (on a scale of 0-1).
    public double x = 0;
    public double y = 0;
    public double width = 0;
    public double height = 0;

    public int imageWidth = 0;
    public int imageHeight = 0;
    public float scaledWidth = 0;
    public float scaledHeight = 0;
    public Image makeSimpleCopy() {
      Image im = new Image();
      im.pageNumber = pageNumber;
      im.imageNumber = imageNumber;
      im.fileName = fileName;
      im.url = url;
      im.tnUrl = tnUrl;
      im.altText = altText;
      im.imageWidth = imageWidth;
      im.pageNumber = pageNumber;
      im.imageHeight = imageHeight;
      im.scaledWidth = scaledWidth;
      im.scaledHeight = scaledHeight;
      return im;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"url", "boundingBox"})
  public static class URL {
    public String hyperLink;

    // bounding box for the URL text.
    public double x = 0;
    public double y = 0;
    public double width = 0;
    public double height = 0;

  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"pageNumber", "pageClass", "docTitle", "title", "altTitle",
      "llmDerivedTitles", "llmDerivedAuthors", "geometryDerivedTitles", "geometryDerivedAuthors",
      "docClass", "width", "height", "confidence",
      "pageSummary", "pageContent", "textWithMarkings", "sections", "grids", "blocks", "fields", "tables",
      "paragraphs", "lines", "images",  "html", "toc", "tags", "authors", "createDate",
      "curatedText", "curatedToc", "curatedPageContent", "tocVerified"})
  public static class Page  {
    public int pageNumber;
    public String chapterId;
    public String pageClass = "";
    public String docTitle = null;
    public String title = null;
    public String altTitle;
    public String llmDerivedTitles;
    public String llmDerivedAuthors;
    public String geometryDerivedTitles;
    public String geometryDerivedAuthors;
    public String docClass;

    // page dimensions (dimensions are on the scale of 0 to 1).
    public double width = 1.0d;
    public double height = 1.0d;
    public String unit = "pixels";
    public float confidence = -1.0f;

    public PageSummary pageSummary = new PageSummary();
    public List<Section> sections;
    public List<Section> htmlSections;
    public List<Grid> grids;
    public List<Block> blocks;
    public List<Block> cleanBlocks;
    public List<Paragraph> cleanParas;
    public List<Field> fields;
    public List<Table> tables = null;
    public List<Paragraph> paragraphs;
    public List<Line> lines;
    public List<Image> images;
    public List<Token> tokens = new ArrayList<>();
    public List<URL> urls;
    public boolean ignore = false;
    public String html;
    public String authors;
    public String createDate;
    public String pageImageUrl = null;
    public String pageImageText = null;
    public String altPageImageUrl = null;
    public String altPageImageText = null;
    public String pageText = null;
    public Object pageContent = null;
    public String textWithMarkings;

    public Map<String, Object> toc;
    public Map<String, Object> tags;
    public List<Object> concepts;
    public List<Map<String, Object>> questions = null;

    public int filePageNumber = -1;
    public String spread = null; // "left" means left side of spread, "right" means right side of spread

    public String curatedText = null;
    public Map<String, Object> curatedToc = null;
    public List<Map<String, Object>> curatedPageContent = null;
    public boolean tocVerified = false;

    public boolean cleaned = false;
    public boolean imagePropertiesExtracted = false;

    @JsonIgnore
    public boolean renderableUpdated = false;

    public String retrieveRenderableText() {
      String text = "";
      List<Rect> rl = retrieveRenderables();
      String str;
      for (Rect r : rl) {
        str = r.retrieveText();
        if (StringUtils.isBlank(str)) {
          continue;
        }
        text += (text.length() > 0 ? "\n\n" : "") + str.trim();
      }

      if (text.length() > 0) {
        return text;
      }

      text = retrievePageText();
      return text;
    }

    public List<Rect> retrieveIndexables() {
      List<Rect> rl = new ArrayList<Rect>();
      if (sections != null && sections.size() > 0) {
        for (Section s : sections) {
          if ("image".equals(s.type)) {
            continue;
          }
          rl.add(s.makeSimpleCopy());
        }
        //rl = combineHeadersAndParagraphs(rl);
        rl = combineHeaders(rl);
        //rl = combineSmallBlocksForIndexing(rl);
      } else {
        updateRenderable();
        if (cleanParas != null) {
          for (Paragraph p : cleanParas) {
            rl.add(p.makeSimpleCopy());
          }
        } else if (cleanBlocks != null) {
          for (Block b : cleanBlocks) {
            rl.add(b.makeSimpleCopy());
          }
        } else {
          if (blocks != null) {
            for (Block b: blocks) {
              if (!b.renderable || StringUtils.isBlank(b.text)) {
                continue;
              }
              rl.add(b.makeSimpleCopy());
            }
          }
        }

        rl = combineSmallBlocksForIndexing(rl);

        if (tables != null) {
          for (Table t: tables) {
            rl.add(t.makeSimpleCopy());
          }
        }
        if (fields != null) {
          FieldSet fieldSet = new FieldSet();
          for (Field f : fields) {
            fieldSet.addField(f.makeSimpleCopy());
          }
          rl.add(fieldSet);
        }
      }

      return rl;
    }

    private List<Rect> combineHeaders(List<Rect> rl) {
      Section prev = null;
      Section current;
      List<Rect> rl2 = new ArrayList<>();
      for (Rect r : rl) {
        if (!(r instanceof Section)) {
          rl2.add(r);
        }
        current = (Section) r;
        if (prev != null) {
          if (r instanceof Section) {
            if ("heading".equals(current.type)) {
              // add text from prev so it can applied in the next cycle
              current.text = prev.text + "\n" + current.text;
            } else {
              // apply to title so it can be used in the indexing text
              current.title =  prev.text + (StringUtil.isBlank(current.title) ? "" : "\n" + current.title);
            }
            prev = null;
          }
        }
        if ("heading".equals(current.type)) {
          prev = current;
        } else {
          rl2.add(current);
        }
      }
      return rl2;
    }

    @SuppressWarnings("unused")
    private List<Rect> combineHeadersAndParagraphs(List<Rect> rl) {
      Section prevHeading = null;
      Section paragraph = null;
      Section current;
      List<Rect> rl2 = new ArrayList<>();
      for (Rect r : rl) {
        if (!(r instanceof Section)) {
          rl2.add(r);
          continue;
        }
        current = (Section) r;
        if (prevHeading != null) {
          if ("heading".equals(current.type)) {
            // add text from prev so it can applied in the next cycle
            current.text = prevHeading.text + "\n" + current.text;
          } else {
            // apply to title so it can be used in the indexing text
            current.title =  prevHeading.text + (StringUtil.isBlank(current.title) ? "" : "\n" + current.title);
          }
          prevHeading = null;
        }
        if ("heading".equals(current.type)) {
          if (paragraph != null) {
            rl2.add(paragraph);
            paragraph = null;
          }
          prevHeading = current;
        } else if ("paragraph".equals(current.type)) {
          if (paragraph != null) {
            int n1 = paragraph.text.length();
            int n2 = current.text.length();
            if ((n1 + n2 > 500) && (n1 > 100) && (n2 > 100)) {
              // new paragraphs
              rl2.add(paragraph);
              paragraph = current;
            } else {
              // add text to the previPragraph 
              paragraph.text = paragraph.text + "\n\n" + current.text;
            }
          } else {
            paragraph = current;
          }
        } else {
          // table or bullet points points
          if (paragraph != null) {
            rl2.add(paragraph);
            paragraph = null;
          }
          rl2.add(current);
        }
      }
      return rl2;
    }

    public List<Rect> retrieveRenderables() {
      List<Rect> rl = new ArrayList<Rect>();
      if (sections != null && sections.size() > 0) {
        for (Section s : sections) {
          if (!"annotation".equals(s.type) && !"photoLabel".equals(s.type)) {
            rl.add(s);
          }
        }
        if (blocks != null) {
          for (Block b : blocks) {
            if ("image".equals(b.type)) {
              insertRect(rl, b);
            }
          }
        }
      } else {
        //updateRenderable();
        if (cleanParas != null) {
          for (Paragraph p : cleanParas) {
            if (p.extra) {
              //continue;
            }
            rl.add(p);
          }
          if (blocks != null) {
            for (Block b : blocks) {
              if ("image".equals(b.type)) {
                insertRect(rl, b);
              }
            }
          }
        } else if (cleanBlocks != null) {
          if (blocks != null) {
            for (Block b : cleanBlocks) {
              rl.add(b);
            }
            for (Block b : blocks) {
              if ("image".equals(b.type)) {
                insertRect(rl, b);
              }
            }
          }
        } else {
          if (blocks != null) {
            for (Block b: blocks) {
              if (!b.renderable) {
                continue;
              }
              rl.add(b);
            }
          }
        }

        if (tables != null) {
          for (Table t: tables) {
            insertRect(rl, t);
          }
        }

        if (fields != null) {
          FieldSet fieldSet = new FieldSet();
          for (Field f : fields) {
            fieldSet.addField(f.makeSimpleCopy());
          }
          insertRect(rl, fieldSet);
        }
      }

      List<Rect> rl2 = new ArrayList<Rect>();

      for (Rect r : rl) {
        rl2.add(r.makeRenderable());
      }

      return rl;
    }

    private void insertRect(List<Rect> rects, Rect r) {
      Rect r2;
      int idx = -1;
      for (int i=0; i<rects.size(); i++) {
        r2 = rects.get(i);
        if (r.x2 < r2.x1) {
          idx = i;
          break;
        } else if (r.y2 < r2.y1) {
          idx = i;
          break;
        } else if (r.x1 < r2.x2) {
          if (r.y1 < r2.y1) {
            idx = i;
            break;
          }
        }
      }
      if (idx < 0) {
        rects.add(r);
      } else {
        rects.add(idx, r);
      }
    }

    private void updateRenderable() {
      if (renderableUpdated) {
        return;
      }
      renderableUpdated = true;
      if (blocks != null) {
        for (Block b: blocks) {
          if (overlapsWithImageTableFields(b)) {
            b.renderable = false;
          }
        }
      }
    }


    private List<Rect> combineSmallBlocksForIndexing(List<Rect> rl) {
      List<Rect> rl2 = new ArrayList<Rect>();

      Rect prev = null;
      int st = 0;
      for (; st<rl.size(); st++) {
        if (StringUtils.isNotBlank(rl.get(st).text)) {
          prev = rl.get(st);
          break;
        }
      }
      if (prev == null) {
        return rl2;
      }
      Rect current;
      String currentText;
      boolean prevLarge = isLargeText(prev.text);
      boolean currentLarge;
      boolean prevVeryLarge;
      boolean currentVerySmall;
      for (int i=st+1; i<rl.size(); i++) {
        current = rl.get(i);
        currentText = current.text;
        if (StringUtils.isBlank(currentText)) {
          continue;
        }
        currentLarge = isLargeText(currentText);
        prevVeryLarge = isVeryLargeText(prev.text);
        currentVerySmall = isSmallText(currentText);
        if (!(prevLarge && currentLarge) && (!prevVeryLarge ||  currentVerySmall)) {
          prev.text += "\n\n" + currentText;
          current.text = null; // will be removed
        } else {
          prev = current;
          prevLarge = currentLarge;
        }
      }

      for (Rect r: rl) {
        if (r.text != null) {
          rl2.add(r);
        }
      }

      return rl2;
    }

    private boolean isSmallText(String text) {
      if (text == null) {
        return true;
      }
      return text.length() < 20;
    }

    private boolean isLargeText(String text) {
      if (text == null) {
        return false;
      }
      return text.length() > 120;
    }

    private boolean isVeryLargeText(String text) {
      if (text == null) {
        return false;
      }
      return text.length() > 500;
    }

    public String retrieveCleanPageText() {
      List<String> sl = null;
      String text = "";
      if (grids instanceof List<?>) {
        sl = retrieveGridsTextList();
      } else if (blocks instanceof List<?>) {
        sl = retrieveCleanBlocksTextList();
      } else if (paragraphs instanceof List<?>) {
        sl = retrieveParagraphsTextList();
      }

      if (sl != null && sl.size() > 0) {
        text = String.join("\n", sl);
      }
      return text;
    }

    public String retrievePageText() {
      List<String> sl = null;
      String text = "";
      if (grids instanceof List<?>) {
        sl = retrieveGridsTextList();
      } else if (blocks instanceof List<?>) {
        sl = retrieveBlocksTextList();
      } else if (paragraphs instanceof List<?>) {
        sl = retrieveParagraphsTextList();
      }

      if (sl != null && sl.size() > 0) {
        text = String.join("\n", sl);
      }
      return text;
    }

    public List<String> retrieveSegments() {
      List<String> sl = null;
      if (grids instanceof List<?>) {
        sl = retrieveGridsTextList();
      } else if (blocks instanceof List<?>) {
        sl = retrieveBlocksTextList();
      } else if (paragraphs instanceof List<?>) {
        sl = retrieveParagraphsTextList();
      } else {
        sl = new ArrayList<>();
      }
      return sl;
    }

    public List<String> retrieveGridsTextList() {
      List<String> tl = new ArrayList<String>();
      if (grids == null) {
        return tl;
      }
      for (Grid g : grids) {
        tl.addAll(g.retrieveTextList());
      }
      return tl;
    }

    public List<String> retrieveBlocksTextList() {
      List<String> tl = new ArrayList<String>();
      if (blocks == null) {
        return tl;
      }
      for (Block b : blocks) {
        if ((b.confidence < 70.0d  && b.confidence != -1 )|| b.text == null) {
          continue;
        }
        if ("image".equals(b.type)) {
          continue;
        }
        tl.add(b.text + (b.text.length() > 50 ? "\n" : ""));
      }
      return tl;
    }

    public List<String> retrieveCleanBlocksTextList() {
      List<String> tl = new ArrayList<String>();
      if (blocks == null) {
        return tl;
      }
      for (Block b : blocks) {
        if ((b.confidence < 70.0d  && b.confidence != -1 )|| b.text == null) {
          continue;
        }
        if ("image".equals(b.type)) {
          continue;
        }
        if (overlapsWithImageTableFields(b)) {
          continue;
        }
        tl.add(b.text + (b.text.length() > 50 ? "\n" : ""));
      }
      return tl;
    }

    public List<String> retrieveParagraphsTextList() {
      List<String> tl = new ArrayList<String>();
      if (paragraphs == null) {
        return tl;
      }
      for (Paragraph p : paragraphs) {
        tl.add(p.text);
      }
      return tl;
    }


    public String retrieveEmbeddedImageUrls() {
      String embeddedUrls = "";

      if (images != null) {
        for (Image img : images) {
          if (StringUtils.isNotBlank(img.url)) {
            embeddedUrls += (embeddedUrls.length() > 0 ? "," : "") + img.url;
          }
        }
      }
      return embeddedUrls;
    }

    public String retrieveUrls() {
      return "";
    }

    public void removeCsvTable() {
      if (tables == null || tables.size() == 0) {
        return;
      }
      Table table;
      for (int i=tables.size() - 1; i>=0 ; i--) {
        table = tables.get(i);
        if (table.csvTable) {
          tables.remove(table);
        }
      }
    }

    public Image retrieveBestPageImage() {
      Image im = null;
      if (images == null) {
        return null;
      }
      for (Image im2 : images) {
        if (im != null && im2 != null) {
          if (im2.width > im.width && (im2.width * im2.height > im.width * im.height)) {
            im = im2;
          }
        } else if (im == null) {
          im = im2;
        }
      }
      return im;
    }

    @SuppressWarnings("unchecked")
    public String retrieveTagsText() {
      Iterator<Entry<String, Object>> iter;
      Entry<String, Object> e;
      List<String> sl;
      String type;
      String str;
      String tagsText = "";
      if (tags != null) {
        iter = tags.entrySet().iterator();
        while (iter.hasNext()) {
          e = iter.next();
          type = e.getKey();
          if (e.getValue() instanceof List<?>) {
            sl = (List<String>) e.getValue();
            str = String.join(", ", sl);
            if (tagsText.length() > 0) {
              tagsText += "\n";
            }
            tagsText += type + ": " + str;
          }
        }
      }

      return tagsText;
    }

    public Image getImageByUrl(String url) {
      if (images == null) {
        return null;
      }
      int idx = url.lastIndexOf('&');
      if (idx > 0) {
        url = url.substring(0, idx);
      }
      for (Image im : images) {
        if (im.url != null) {
          if (im.url.startsWith(url)) {
            return im;
          }
        }
      }
      return null;
    }

    public boolean overlapsWithImageTableFields(Rect rect) {
      Rectangle2D r1 = new Rectangle2D.Double(rect.x1, rect.y1, rect.width, rect.height);
      Rectangle2D r2;
      for (Block b : blocks) {
        if (b == rect) {
          continue;
        }
        r2 =  new Rectangle2D.Double(b.x1, b.y1, b.width, b.height);
        if (!"image".equals(b.type)) {
          continue;
        }
        if (r2.intersects(r1)) {
          return true;
        }
      }
      if (tables != null) {
        for (Table t : tables) {
          r2 =  new Rectangle2D.Double(t.x1, t.y1, t.width, t.height);
          if (r2.intersects(r1)) {
            return true;
          }
        }
      }
      if (fields != null && fields.size() > 0) {
        for (Field f : fields) {
          r2 =  new Rectangle2D.Double(f.x1, f.y1, f.width, f.height);
          if (r2.intersects(r1)) {
            return true;
          }
        }
      }
      return false;
    }

    public String retrieveConceptsText() {
      if (concepts == null) {
        return null;
      }
      String str = "";
      for (Object o : concepts) {
        str += o + "\n";
      }
      return str;
    }

    public String getAuthors() {
      if (StringUtils.isNotBlank(authors)) {
        return authors;
      } else if (StringUtils.isNotBlank(llmDerivedAuthors)) {
        return llmDerivedAuthors;
      } else if (StringUtils.isNotBlank(geometryDerivedAuthors)) {
        return geometryDerivedAuthors;
      }
      return null;
    }

    public void combineFields() {
      if (sections == null) {
        return;
      }
      List<Section> sections2 = new ArrayList<>();
      Section fieldSection = null;
      for (Section s : sections) {
        if ("formField".equals(s.type)) {
          if (fieldSection != null) {
            fieldSection.addFieldSection(s);
          } else {
            sections2.add(s);
            fieldSection = s;
          }
        } else {
          fieldSection = null;
          sections2.add(s);
        }
      }
      sections = sections2;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> retreiveTocItems() {
      List<Map<String, Object>> l;
      if (curatedToc != null && curatedToc.get("toc") instanceof List<?>) {
        l = (List<Map<String, Object>>) curatedToc.get("toc");
        if (l.size() > 0) {
          return l;
        }
      }
      if (toc != null && toc.get("toc") instanceof List<?>) {
        l = (List<Map<String, Object>>) toc.get("toc");
        if (l.size() > 0) {
          return l;
        }
      }
      return null;
    }

    public List<Image> retrieveImagesWithProperties() {
      List<Image> indexableImages = new ArrayList<>();
      if (images != null) {
        for (Image im : images) {
          if (im.properties != null) {
            indexableImages.add(im);
          }
        }
      }
      return indexableImages;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonPropertyOrder({"id", "title", "startingPageNumber", "numberOfPages", "pages", "pageNumbers", "authors", "createDate",
      "curatedText"})
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Chapter {
    public String id;
    public String title;
    public int startingPageNumber;
    public List<Integer> pageNumbers = new ArrayList<>();
    public List<Page> pages;
    public int numberOfPages;
    public String authors;
    public String createDate;
    public String chapterImageUrl = null;
    public String chapterImageThumnailUrl = null;
    public String chapterImageText = null;
    public String textWithMarkings = null;
    public String summary = null;
    public List<Paragraph> paragraphs = null;
    public String curatedText = null;
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonPropertyOrder({"id", "docKey", "type", "fileName", "title", "shortTitle", "theme", "docClass", "confidence",
      "reviewRequired", "summaryObj", "summaryContent",
      "pages", "entities", "chunks", "previewImages", "metadata", "overallProcessingStatus",
      "severity", "logs", "rawDocExtracted", "rawDocExtractor",
      "processor", "summarized", "_file_", "_entities", "createDate", "chaptersCleaned"})
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Document {

    public static final int CRITICAL = 1;
    public static final int ERROR = 2;
    public static final int WARNING = 3;
    public static final int INFO = 4;

    public static final int NOT_PROCESSED = 0;
    public static final int PROCESSED = 1;
    public static final int PARTIALLY_PROCESSED = 2;
    public static final int FAILED = 3;

    public String id;
    public String docKey = null;
    public String type;
    public String fileName = null;
    public String title = null;
    public String shortTitle = null;
    public String theme = null;
    public String docClass;
    public float confidence = -1.0f;
    public boolean reviewRequired = false;

    public String _file_ = null; // backward compatibility, remove it after _file_ usage is replaced by fileName
    public Map<String, Object> summaryObj = null;
    public String summaryContent = null;
    public List<Chapter> chapters;
    public List<Page> pages = null;
    public List<Entity> entities = null;
    public List<Chunk> chunks = null;
    public List<Image> previewImages = null;
    public Map<String, Object> metadata = null;

    public int overallProcessingStatus = 0;
    public int severity = 0;
    public List<String> logs = new ArrayList<String>();
    public int rawDocExtracted = 0;
    public String rawDocExtractor = null;
    public String processor = null;
    public String createDate;
    public boolean tocLocked = false;
    public String sourceUrl = null;
    public String sourceTitle = null;
    public boolean chaptersCleaned = false;
    public int numberOfFrontPages = -1;
    public boolean isCuratedNumberOfFrontPages = false;
    public boolean requiesNumberOfFrontPages = false;

    public String retrievePagesText(int numberOfPages) {
      List<String> tl = new ArrayList<>();
      if (pages != null) {
        int n = 0;
        for (Page p : pages) {
          if (tl.size() > 0) {
            tl.add("\f");
          }
          tl.addAll(p.retrieveSegments());
          if (numberOfPages != -1 && n >= numberOfPages) {
            break;
          }
        }
      }
      return String.join(" ", tl);
    }

    public void addPage(Page page) {
      if (pages == null) {
        pages = new ArrayList<Page>();
      }
      pages.add(page);
    }

    public void addEntity(Entity entity) {
      if (entities == null) {
        entities = new ArrayList<Entity>();
      }
      entities.add(entity);
    }

    public void addPreviewImage(Image image) {
      if (previewImages == null) {
        previewImages = new ArrayList<Image>();
      }
      previewImages.add(image);
    }

    public void sortPeviewImagesByPageNumber() {
      if (previewImages != null) {
        Collections.sort(previewImages, new Comparator<Image>() {
          public int compare(Image i1, Image i2) {
            return Integer.compare(i1.pageNumber, i2.pageNumber);
          }
        });
      }
    }

    public Map<String, Object> asMap() {
      Map<String, Object> m = new LinkedHashMap<String, Object>();
      m.put("fileName", fileName);
      m.put("_file_", _file_);
      m.put("pages", pages);
      m.put("entities", entities);
      m.put("previewImages", previewImages);
      return m;
    }

    public void logMessage(String action, int sev, String message, boolean auditIt) {
      logMessage(action, sev, message, null, auditIt);
    }

    public void logMessage(String action, int sev, String message, Object details, boolean auditIt) {
      if (sev < severity || severity == 0) {
        severity = sev;
      }
      computeOverallProcessingStatus();
      String str = "(sev: " + sev + ") - " + action + " - " + message + "\n";
      if (details != null) {
        try {
          if (details instanceof String || details.getClass().isPrimitive()) {
            str += details + "\n";
          } else {
            str += Util.asJSONPrettyString(details) + "\n";
          }
        } catch (Exception exc) {}
      }
      logs.add(str);
      if (auditIt) {
        if (sev == CRITICAL || sev == ERROR) {
          LogApi.auditError(action, message, details, null);
        } else if (sev == WARNING) {
          LogApi.auditWarn(action, message, details, null);
        } else if (sev == INFO) {
          LogApi.auditInfo(action, message, details, null);
        }
      }
    }

    public void computeOverallProcessingStatus() {
      int temp;
      if (severity == 0) {
        temp = 0;
      } else if (severity == ERROR || severity == CRITICAL) {
        temp = FAILED;
      } else if (severity == WARNING) {
        temp = PARTIALLY_PROCESSED;
      } else {
        temp = PROCESSED;
      }

      // if overallProcessingStatus is larger, it means it has been set by the system to override the computed value
      if (temp > overallProcessingStatus) {
        overallProcessingStatus = temp;
      }
    }

    public void clearLogs() {
      logs = new ArrayList<String>();
    }

    public boolean hasTables() {
      if (pages == null) {
        return true;
      }
      for (Page p : pages) {
        if (p.tables == null || p.tables.size() > 0) {
          return true;
        }
      }
      return false;
    }

    public void removeCsvTable() {
      if (pages == null) {
        return;
      }
      for (Page p : pages) {
        p.removeCsvTable();
      }

    }

    public boolean hasGrids() {
      if (pages == null) {
        return false;
      }
      for (Page p : pages) {
        if (p.grids != null && p.grids.size() > 0) {
          return true;
        }
      }
      return false;
    }

    public boolean hasBlocks() {
      if (pages == null) {
        return true;
      }
      for (Page p : pages) {
        if (p.blocks == null || p.blocks.size() > 0) {
          return true;
        }
      }
      return false;
    }

    public Page retrievePageByPageNumber(int pageNumber) {
      if (pages == null) {
        return null;
      }
      for (Page p : pages) {
        if (p.pageNumber == pageNumber) {
          return p;
        }
      }
      return null;
    }

    public Page retrievePageByFilePageNumber(int filePageNumber) {
      if (pages == null) {
        return null;
      }
      for (Page p : pages) {
        if (p.filePageNumber == filePageNumber) {
          return p;
        }
      }
      return null;
    }

    public List<Page> retrieveRelatedPagesByFilePageNumber(int filePageNumber) {
      List<Page> relatedPages = new ArrayList<>();
      if (pages == null) {
        return null;
      }
      for (Page p : pages) {
        if (p.filePageNumber == filePageNumber) {
          relatedPages.add(p);
        }
      }
      return relatedPages;
    }

    public String retrievePagePreviewImageUrl(int pageNumber) {
      if (previewImages == null) {
        return null;
      }
      int idx = pageNumber - 1;
      if (idx >= previewImages.size() || idx < 0) {
        return null;
      }
      return previewImages.get(idx).url;
    }

    public Image retrieveBestDocImage() {
      Image im = null;
      Image im2;
      if (previewImages != null && previewImages.size() > 0) {
        return previewImages.get(0);
      }
      for (Page page: pages) {
        im2 = page.retrieveBestPageImage();
        if (im != null && im2 != null) {
          if (im2.width > im.width && (im2.width * im2.height > im.width * im.height)) {
            im = im2;
          }
        } else if (im == null) {
          im = im2;
        }
      }
      return im;
    }

    @JsonIgnore
    public List<String> get_pageImageUrls_() {
      if (previewImages == null) {
        return null;
      }
      List<String> _pageImageUrls_ = new ArrayList<>();
      for (Image im : previewImages) {
        _pageImageUrls_.add(im.url);
      }
      return _pageImageUrls_;
    }

    public void set_pageImageUrls_(List<String> _pageImageUrls_) {
    }

    public void completeBlockAcrossPages() {
      if (pages == null) {
        return;
      }
      DataObject.completeBlockAcrossPages(pages);
    }

    public int getStartingPageIndexAfterFrontPages() {
      if (pages == null) {
        return 0;
      }
      int lastTocPageIndex = 0;
      int n = pages.size() > 6 ? 6 : pages.size();
      List<Map<String, Object>> tocItems;
      for (int i=0; i<n; i++) {
        tocItems = pages.get(i).retreiveTocItems();
        if (tocItems != null && tocItems.size() > 0) {
          lastTocPageIndex = i;
        }
      }
      return lastTocPageIndex;
    }

    public List<Map<String, Object>> makeDocumentToc() {
      List<Map<String, Object>> docTocList = new ArrayList<>();
      Page page;
      if (pages != null) {
        int n = pages.size() > 6 ? 6 : pages.size();
        List<Map<String, Object>> tocItems;
        for (int i=0; i<n; i++) {
          page = pages.get(i);
          tocItems = page.retreiveTocItems();
          if (tocItems != null && tocItems.size() > 0) {
            docTocList.addAll(tocItems);
          }
        }
      }
      if (docTocList.size() > 0) {
        CoreDocUtil.sortToc(docTocList);
      }
      return docTocList;
    }

    public boolean tocVerified() {
      Page page;
      if (pages != null) {
        int n = pages.size() > 6 ? 6 : pages.size();
        List<Map<String, Object>> tocItems;
        for (int i=0; i<n; i++) {
          page = pages.get(i);
          tocItems = page.retreiveTocItems();
          if (tocItems != null && tocItems.size() > 0) {
            if (!page.tocVerified) {
              return false;
            }
          }
        }
      }
      return true;
    }
    
    public boolean hasSummaries() {
      if (pages == null) {
        return false;
      }
      for (Page p :pages) {
        if (p.pageSummary != null && p.pageSummary.text != null) {
          return true;
        }
      }
      return false;
    }
    
    public List<Field> retrieveAllFields() {
      List<Field> fl = new ArrayList<>();
      if (pages != null) {
        for (Page p : pages) {
          if (p.fields != null) {
            fl.addAll(p.fields);
          }
        }
      }
      return fl;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"pageNumber", "name", "docTitle", "title", "text", "json", "html", "semanticSeach"})
  public static class Chunk {
    public int pageNumber = 1;
    public String name;
    public String docTitle = null;
    public String title = null;
    public String text = null;
    public String json = null;
    public String html = null;
    public boolean semanticSeach = false; // semantic search if true, else term search using lucene
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"name", "text", "confidence", "fontSize", "relativeFontSize", "tokens", "x1", "y1","x2", "y2", "width", "height"})
  public static class Row extends Rect {
    //public int cluster = -1;
    public List<Token> tokens;
    @JsonIgnore
    List<String> sl = null;
    public Row() {
      tokens = new ArrayList<Token>();
      type = "row";
    }
    public Row(List<Token> tokens) {
      this.tokens = tokens;
      for (Token token : tokens) {
        if (token != null && !token.isNull) {
          union(token);
        }
      }
    }
    public Row(List<Token> tokens, boolean skipNull) {
      this.tokens = new ArrayList<>();
      for (Token token : tokens) {
        if (token != null && !token.isNull) {
          this.tokens.add(token);
          union(token);
        } else if (!skipNull) {
          this.tokens.add(token);
        }
      }
    }
    public Row(Token[] tarr, boolean skipNull) {
      this.tokens = new ArrayList<>();
      for (Token token : tarr) {
        if (skipNull && (token == null || token.isNull)) {
          continue;
        }
        tokens.add(token);
        if (token != null) {
          union(token);
        }
      }
    }

    public void addToken(Token token) {
      if (token == null || token.isNull) {
        return;
      }
      tokens.add(token);
      union(token);
    }

    public void mergeRow(Row row) {
      Token target = tokens.get(tokens.size() - 1);
      for (Token token : row.tokens) {
        target.mergeToken(token);
      }
      x1 = target.x1;
      x2 = target.x2;
      y1 = target.y1;
      y2 = target.y2;
      width = target.width;
      height = target.height;
    }
    public void mergeTokens() {
      if (tokens.size() <= 1) return;

      Token target = tokens.get(0);

      for (int i=1; i<tokens.size(); i++) {
        target.mergeToken(tokens.get(i));
      }

      x1 = target.x1;
      x2 = target.x2;
      y1 = target.y1;
      y1 = target.y1;
      width = target.width;
      height = target.height;
    }

    public boolean endOfSentence() {
      if (tokens.size() == 0) {
        return true;
      }
      Token t = tokens.get(tokens.size() - 1);
      if (t.text != null) {
        if (t.text.endsWith(".")) {
          return true;
        }
      }
      return false;
    }

    public String retrieveText() {
      String str = "";

      for (Token t : tokens) {
        if (t != null) {
          str += (str.length() > 0 ? " " : "") + t.text;
        }
      }

      return str;
    }

    // used for comparing two blocks if they have the same text
    public List<String> _getSL() {
      if (sl != null) {
        return sl;
      }
      sl = new ArrayList<String>();
      String str = retrieveText();
      if (StringUtils.isBlank(str)) {
        return sl;
      }
      String[] sarr = str.split("\\s+");
      sl = new ArrayList<>(Arrays.asList(sarr));
      return sl;
    }
    @Override
    public Rect makeSimpleCopy() {
      return null;
    }
  }

  @JsonPropertyOrder({"name", "text", "gridType", "nCols", "canBeTreatedAsSingleColumn", "isMostlyText", "isTable", "percentWidth", "blocks",
      "rows", "subgrids", "confidence", "fontSize", "relativeFontSize", "tokens", "x1", "y1","x2", "y2", "width", "height"})
  public static class Grid extends Rect {
    public static final String ROW = "row";
    public static final String COLUMN = "column";

    public String name = "";
    public String gridType = null;
    public int nCols = 0;
    public boolean canBeTreatedAsSingleColumn = false;
    public boolean isMostlyText = false;
    public boolean isTable = false;
    public int percentWidth = 0;

    public List<Block> blocks = null;
    public List<Row> rows = new ArrayList<Row>();
    public List<Grid> subGrids = new ArrayList<Grid>();
    public boolean canAddTopMargin;
    @JsonIgnore
    List<String> sl = null;

    public Grid() {
      type = "grid";
    }

    public Grid(List<List<Token>> grid) {
      if (grid == null) {
        return;
      }
      Row row;
      for (int i=0; i<grid.size(); i++) {
        row = new Row(grid.get(i));
        rows.add(row);
        union(row);
      }
    }

    public Grid(List<List<Token>> grid, boolean skipNull) {
      if (grid == null) {
        return;
      }
      Row row;
      for (int i=0; i<grid.size(); i++) {
        row = new Row(grid.get(i), skipNull);
        if (row.tokens.size() > 0) {
          rows.add(row);
          union(row);
        }
      }
    }

    public Grid(Token[][] arr) {
      Row row;
      for (int i=0; i<arr.length; i++) {
        row = new Row(arr[i], false);
        rows.add(row);
        union(row);
      }
    }

    public void addRow(Row row) {
      rows.add(row);
      union(row);
    }

    public void addAllRows(List<Row> rows) {
      for (Row row : rows) {
        addRow(row);
      }
    }

    public void addSubGrid(Grid subGrid) {
      subGrids.add(subGrid);
      union(subGrid);
    }

    public void addAllSubGrid(List<Grid> subGrids) {
      for (Grid grid : subGrids) {
        addSubGrid(grid);
      }
    }

    public void addBlock(Block block) {
      if (blocks == null) {
        blocks = new ArrayList<>();
      }
      blocks.add(block);
      union(block);
    }

    public List<Double> retrieveRowMargins() {
      List<Double> margins = new ArrayList<Double>();
      if (rows.size() <= 1) {
        return margins;
      }
      Row prevRow = rows.get(0);
      Row row;
      for (int i=1; i<rows.size(); i++) {
        row = rows.get(i);
        margins.add(row.y1 - prevRow.y2);
        prevRow = row;
      }
      return margins;
    }

    public Grid makeEmptyGrid() {
      Grid grid = new Grid();
      grid.name = name;
      grid.confidence = confidence;
      grid.text = "";
      grid.percentWidth = percentWidth;
      grid.gridType = gridType;
      grid.nCols = nCols;
      grid.isMostlyText = isMostlyText;
      grid.canBeTreatedAsSingleColumn = canBeTreatedAsSingleColumn;

      return grid;
    }

    public void mergeGrid(Grid grid) {
      int idx = 0;
      if (blocks != null && grid.blocks != null) {
        if (blocks.size() > 0 && grid.blocks.size() > 0) {
          Block last = blocks.get(blocks.size() - 1);
          Block first = grid.blocks.get(0);
          if (!last.text.endsWith(".")) {
            last.text += " " + first.text;
            idx++;
            last.union(first);
          }
        }
      }
      if (blocks == null) {
        blocks = new ArrayList<>();
      }
      Block temp;
      if (grid.blocks != null) {
        for (int i=idx; i<grid.blocks.size(); i++) {
          temp = grid.blocks.get(i);
          blocks.add(temp);
          union(temp);
        }
      }
    }

    public List<String> retrieveTextList() {
      List<String> tl = new ArrayList<>();
      if (blocks != null && blocks.size() != 0) {
        for (Block b : blocks) {
          if ("image".equals(b.type)) {
            continue;
          }
          tl.addAll(b.retrieveTextList());
        }
      } else {
        if (rows != null && rows.size() != 0) {
          String t = "";
          for (Row r : rows) {
            t += r.retrieveText() + "\n";
          }
          tl.add(t);
        }
      }
      return tl;
    }

    public String retrieveText() {
      List<String> tl = retrieveTextList();
      String str = String.join("\n", tl);
      return str;
    }

    public List<String> _getSL() {
      if (sl != null) {
        return sl;
      }
      sl = new ArrayList<String>();
      String[] sarr;
      List<String> tl = retrieveTextList();
      for (String s : tl) {
        sarr = s.split("\\s+");
        sl.addAll(new ArrayList<>(Arrays.asList(sarr)));
      }
      return sl;
    }

    @Override
    public Rect makeSimpleCopy() {
      return null;
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"type", "subTitle", "text", "items", "field", "table"})
  public static class Section extends Rect {
    public String title = null;
    public List<String> items = null;
    public Field field = null;
    public FieldSet fieldSet = null;
    public Table table = null;
    public String htmlTag = null;
    public TOC toc = null;
    public Image image;

    // The following attributes are used for cleaning sections (removing irrelevant sections)
    @JsonIgnore
    public int seq;
    @JsonIgnore
    public boolean keep;
    @JsonIgnore
    public List<String> tokens = null;

    public Section() {
      type = "text";
    }

    @Override
    public Rect makeSimpleCopy() {
      Section s = new Section();
      s.type = type;
      s.text = text;
      if (items != null) {
        s.items = new ArrayList<>(items);
      }
      if (field != null) {
        s.field = field.makeSimpleCopy();
      }
      if (fieldSet != null) {
        s.fieldSet = fieldSet.makeSimpleCopy();
      }
      if (table != null) {
        s.table = table.makeSimpleCopy();
      }
      if (toc != null) {
        s.toc = toc;
      }
      return s;
    }

    public void addFieldSection(Section s) {
      if (fieldSet == null) {
        // convert formField to fieldSet (for multiple successive fields)
        fieldSet = new FieldSet();
        fieldSet.fields = new ArrayList<>();
        if (field != null) {
          fieldSet.fields.add(field);
          field = null;
        }
        type = "fieldSet";
      }
      if (s.field != null) {
        fieldSet.fields.add(s.field);
      }

    }

    public String retrieveText() {
      String t = StringUtils.isBlank(title) ? "" : title + "\n";
      switch(type) {
        case "table" :
          if (table != null) {
            return t + table.retrieveText();
          }
          break;
        case "formField" :
          if (field != null) {
            return t + field.retrieveText();
          }
          break;
        case "fieldSet" :
          if (fieldSet != null) {
            return t + fieldSet.retrieveText();
          }
          break;
        case "bulletPoints" :
        case "list" :
          if (items != null) {
            return t + String.join("\n", items);
          }
          break;
        case "toc" :
          if (toc != null) {
            return t + toc.retrieveText();
          }
          break;

        default: break;
      }

      if (StringUtils.isBlank(text)) {
        return "";
      }
      return t + text;
    }

    public void fixFormatting() {
      switch(type) {
        case "table" :
          if (table != null) {
            table.fixFormatting();
          }
          break;
        case "formField" :
          if (field != null) {
            field.fixFormatting();
          }
          break;
        case "fieldSet" :
          if (fieldSet != null) {
            fieldSet.fixFormatting();
          }
          break;
        case "bulletPoints" :
        case "list" :
          if (items != null) {
            List<String> items2 = new ArrayList<>(items.size());
            for (String s : items) {
              if (s != null) {
                items2.add(s.replace("''", "'"));
              }
            }
            items = items2;
          }
          break;
        case "toc" :
          if (toc != null) {
            toc.fixFormatting();
          }
          break;

        default:
          if (text != null) {
            text = CoreDocUtil.fixFormatting(text);
          }
      }
    }

    public void initSectionTokens() {
      String t = retrieveText();
      if (StringUtils.isBlank(t)) {
        tokens = new ArrayList<>();
      } else {
        String[] arr = t.split("\\s+");
        tokens = Arrays.asList(arr);
      }
    }
  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"type", "items"})
  public static class TOC extends Rect {
    public List<TOCItem> items = null;

    public TOC() {
      type = "toc";
    }

    public void addItem(TOCItem item) {
      if (items == null) {
        items = new ArrayList<>();
      }
      items.add(item);
    }

    @Override
    public TOC makeSimpleCopy() {
      TOC t = new TOC();
      if (items != null) {
        for (TOCItem item : items) {
          t.addItem(item.makeSimpleCopy());
        }
      }
      return t;
    }

    @Override
    public String retrieveText() {
      String text = "";
      if (items != null) {
        for (TOCItem item : items) {
          text = (text.length() > 0 ? "\n\n" : "") + item.retrieveText();
        }
      }
      return text;
    }

    public void fixFormatting() {
      if (items != null) {
        for (TOCItem item : items) {
          item.fixFormatting();
        }
      }
    }

  }

  @JsonInclude(Include.NON_DEFAULT)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPropertyOrder({"type", "pageNumber", "title", "summary"})
  public static class TOCItem extends Rect {
    public int pageNumber = 0;
    public String title = null;
    public String summary;

    public TOCItem() {
      type = "tocItem";
    }

    @Override
    public TOCItem makeSimpleCopy() {
      TOCItem item = new TOCItem();
      item.type = type;
      item.pageNumber = pageNumber;
      item.title = title;
      item.summary = summary;
      return item;
    }

    @Override
    public String retrieveText() {
      String text = pageNumber + "   " + (StringUtils.isBlank(title) ? "" : title);
      if (StringUtils.isNotBlank(summary)) {
        text += "\n" + summary;
      }
      return text;
    }

    public void fixFormatting() {
      if (title != null) {
        title = CoreDocUtil.fixFormatting(title);
      }
      if (summary != null) {
        summary = CoreDocUtil.fixFormatting(summary);
      }
    }

  }

  public static void completeBlockAcrossPages(List<Page> pages) {
    Page prevPage = pages.get(0);
    Page currentPage;
    Block b1, b2;
    for (int i=1; i<pages.size(); i++) {
      currentPage = pages.get(i);
      b1 = getParaBlockEndingWithPartialSentence(prevPage);
      b2 = getParaBlockBeginnningWithPartialSentence(currentPage);
      if (b1 != null && b2 != null) {
        b1.text = b1.text + " " + b2.text;
        b2.text = b1.text;
      }
      prevPage = currentPage;
    }
  }

  public static Block getParaBlockBeginnningWithPartialSentence(Page page) {
    if (page.blocks == null) {
      return null;
    }
    Block b;
    int n = 0;
    for (int i=0; i<page.blocks.size(); i++) {
      b = page.blocks.get(i);
      if (isParagraph(b)) {
        if (Character.isLowerCase(b.text.charAt(0))) {
          return b;
        }
      }
      n++;
      if (n > 5) {
        break; // try 3 times else break
      }
    }
    return null;
  }

  public static Block getParaBlockEndingWithPartialSentence(Page page) {
    if (page.blocks == null) {
      return null;
    }
    Block b;
    int n = 0;
    for (int i = page.blocks.size()-1; i >= 0; i--) {
      b = page.blocks.get(i);
      if (isParagraph(b)) {
        if (!endsWithPunc(b.text)) {
          return b;
        }
      }

      n++;
      if (n > 5) {
        break; // try 3 times else break
      }
    }
    return null;
  }

  public static boolean endsWithPunc(String text) {

    if (text == null || text.isEmpty()) {
      return false; // Return false for null or empty text
    }

    String str = text.trim();

    // Get the last character of the string
    char lastChar = str.charAt(str.length() - 1);

    // Check if the last character is a punctuation mark
    return lastChar == '.' || lastChar == '!' || lastChar == '?';
  }

  public static boolean isParagraph(Block b) {
    if (b.text == null) {
      return false;
    }
    String[] sentences = b.text.split("(?<=[.!?])\\s*");
    if (sentences.length > 1) {
      return true;
    }
    return false;
  }
}
