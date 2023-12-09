package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class OCR {
  
  public static final String type = "OCR";
  
  @SuppressWarnings("unchecked")
  public static OCRResult responseToOCRResult(Map<String, Object> response, int totalPages) {
    OCRResult ocrResult = new OCRResult();
    ocrResult.totalPages = totalPages;
    Map<String, Object> fta = (Map<String, Object>) response.get("fullTextAnnotation");
    
    if (fta == null) {
      return ocrResult;
    }
    List<Map<String, Object>> pages = (List<Map<String, Object>>) fta.get("pages");
    ocrResult.pages = pages;
    return ocrResult;
  }
  
  public static void merge1To2(OCRResult ocrResult1, OCRResult ocrResult2) {
    ocrResult1.pages.addAll(ocrResult2.pages);
  }
  
  public static class OCRResult {
    public List<Map<String, Object>> pages = new ArrayList<Map<String, Object>>(0);;
    public int totalPages = 0;
  }
  
  
  @JsonInclude(Include.NON_EMPTY)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Value {
    public List<Page> processedPages;
    public List<Map<String, Object>> data;
    public String templateName;
    public String error;
  }
  
  
  @JsonInclude(Include.NON_EMPTY)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PageCollection {
    public List<Page> pages;
    public String error;
  }
  
  @JsonInclude(Include.NON_EMPTY)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Page {
    public List<Line> lines;
    public int width = 0;
    public int height = 0;
    public String unit = "";
  }
  
  @JsonPropertyOrder({ "text"})
  public static class Line extends BasicRect {
   
    public void update(Word w) {
      updateText(w);
      if (words.size() == 0) {
        rotation = w.rotation;
      }
      words.add(w);
    }
    
    public void updateText(Word w) {
      DetectedBreak db = w.detectedBreak;
      if (text == null) text = "";
      
      if (db.isPrefix) {
        if ("SURE_SPACE".equals(db.type) || "EOL_SURE_SPACE".equals(db.type) || "LINE_BREAK".equals(db.type)
            || "HYPHEN".equals(db.type)) {
          text += "      ";
        } else  if ("SPACE".equals(db.type)) {
          text += " ";
        }
      }
      text += w.text;
      if (!db.isPrefix) {
        if ("SURE_SPACE".equals(db.type) || "EOL_SURE_SPACE".equals(db.type) || "LINE_BREAK".equals(db.type)
            || "HYPHEN".equals(db.type)) {
          text += "    ";
        } else if ("SPACE".equals(db.type)) {
          text += " ";
        }
      }
    }
    
    public void reUpdateWordsFromCells() {
      words.clear();
      if (cells != null) {
        for (Cell cell : cells) {
          words.addAll(cell.words);
        }
      }
      text = "";
      for (Word w : words) {
        updateText(w);
      }
    }
    
    public void printCells() {
      if (cells == null) {
        return;
      }
      Cell cell;
      for (int i=0; i<cells.size(); i++) {
        cell = cells.get(i);
        System.out.print((i > 0 ? "   :   " : "") + cell.text);
      }
      System.out.print("\n");
    }

    public void makeCells() {
      cells = new ArrayList<Cell>();
      Cell cell = new Cell();
      cells.add(cell);
      Word p = words.get(0);
      cell.update(p);
      Word c; 
      int n = words.size();
      for (int i=1; i<n; i++) {
        c = words.get(i);
        DetectedBreak db = p.detectedBreak;
        if ("SURE_SPACE".equals(db.type) || "EOL_SURE_SPACE".equals(db.type) || "LINE_BREAK".equals(db.type)
            || "HYPHEN".equals(db.type)) {
          cell = new Cell();
          cells.add(cell);
        }
        cell.update(c);
        p = c;
      }
    }

    public void update(Paragraph p) {
      if (text == null) text = "";
      text += (text.length() > 0 ? "     |     " : "") + p.text.trim();
    }
    
    public void computeBounds() {
      computeBounds(words);
      computeAngle();
      if (cells != null) {
        for (Cell cell : cells) {
          cell.computeBounds();
        }
      }
    }
    
    public void computeAngle()  {
      if (words.size() == 0) {
        angle = 0;
        return;
      }
      double sum = 0;
      for (Word w : words) {
        sum += w.angle;
      }
      angle = sum / words.size();
      if (Math.abs(angle) < 0.02) angle = 0;
    }
    
    public String getOverlappingValue(BasicRect br) {
      List<Word> wl = new ArrayList<Word>();
      boolean found = false;
      for (Word w : words) {
        if (overlapsAcross(br, w, 0.2, 0.3)) {
          found = true;
          wl.add(w);
        } else if (found) {
          // the next word did not overlap
          break;
        }
      }
      if (wl.size() == 1) {
        return wl.get(0).text.trim();
      } else if (wl.size() > 1) {
        Cell cell = makeCell(wl);
        return cell.text.trim();
      }
      return "";
    }
    
    public String text = null;
    public List<Word> words = new ArrayList<Word>();
    public List<Cell> cells = null;
    public boolean merged = false;
    public List<TokenizedWord> tokenizedWords = null;
  }
  
  @JsonPropertyOrder({ "rotation", "xa", "ya", "x1", "y1", "x2", "y2", "confidence"})
  public static class Block extends Rect {
    
    @SuppressWarnings("unchecked")
    public Block(Map<String, Object> block) {
      confidence = parseDouble(block.get("confidence"));
      Rect r = getRect((Map<String, Object>) block.get("boundingBox"));
      x1 = r.x1;
      y1 = r.y1;
      x2 = r.x2;
      y2 = r.y2;
      xa = r.xa;
      ya = r.ya;
      pt2X = r.pt2X;
      pt2Y = r.pt2Y;
      rotation = r.rotation;
      detectedBreak = getDetectedBreak(block);
      Paragraph paragraph;
      List<Map<String, Object>> paras = (List<Map<String, Object>>) block.get("paragraphs");
      for (Map<String, Object> p : paras) {
        paragraph = makeParagraph(p);
        paragraphs.add(paragraph);
      }
      
      //paragraphs = sort(paragraphs, Paragraph.class);
    }
    public List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  }
  
  @JsonPropertyOrder({ "rotation", "xa", "ya", "x1", "y1", "x2", "y2", "confidence"})
  public static class Paragraph extends Rect {
    
    @SuppressWarnings("unchecked")
    public Paragraph(Map<String, Object> paragraph) {
      confidence = parseDouble(paragraph.get("confidence"));
      Rect r = getRect((Map<String, Object>) paragraph.get("boundingBox"));
      List<Map<String, Object>> wl = (List<Map<String, Object>>) paragraph.get("words");
      x1 = r.x1;
      y1 = r.y1;
      x2 = r.x2;
      y2 = r.y2;
      xa = r.xa;
      ya = r.ya;
      pt2X = r.pt2X;
      pt2Y = r.pt2Y;
      rotation = r.rotation;
      detectedBreak = getDetectedBreak(paragraph);
      Word word;
      for (Map<String, Object> w : wl) {
        word = makeWord(w);
        words.add(word);
        update(word);
      }
    }
    public void update(Word w) {
      DetectedBreak db = w.detectedBreak;
      if (text == null) text = "";
      
      if (db.isPrefix) {
        if ("EOL_SURE_SPACE".equals(db.type) || "LINE_BREAK".equals(db.type)
            || "HYPHEN".equals(db.type)) {
          text += "\n";
        } else if ("SURE_SPACE".equals(db.type)) {
          text += "      ";
        } else if ("SPACE".equals(db.type)) {
          text += " ";
        }
      }
      text += w.text;
      if (!db.isPrefix) {
        if ("EOL_SURE_SPACE".equals(db.type) || "LINE_BREAK".equals(db.type)
            || "HYPHEN".equals(db.type)) {
          text += "\n";
        } else if ("SURE_SPACE".equals(db.type)) {
          text += "      ";
        } else if ("SPACE".equals(db.type)) {
          text += " ";
        }
      }
    }
    
    public List<Word> words = new ArrayList<Word>();
  }
  
  @JsonPropertyOrder({ "text", "words"})
  public static class Cell extends BasicRect {
   
    public void update(Word w) {
      DetectedBreak db = w.detectedBreak;
      if (text == null) text = "";
      if ("SPACE".equals(db.type) && db.isPrefix) text = text + " ";
      text += w.text;
      if ("SPACE".equals(db.type) && !db.isPrefix) text = text + " ";
      if (words.size() == 0) {
        rotation = w.rotation;
      }
      words.add(w);
    }
    
    public void merge(Cell cell) {
      text += "\\n" + cell.text;
      union(cell);
      if (words.size() > 0) {
        DetectedBreak db = new DetectedBreak();
        db.type = "LINE_BREAK";
        words.get(words.size() - 1).detectedBreak = db;
      }
      words.addAll(cell.words);
    }

    public void computeBounds() {
      computeBounds(words);
    }

    public String text = null;
    public List<Word> words = new ArrayList<Word>();
  }

  @JsonPropertyOrder({ "rotation", "xa", "ya", "x1", "y1", "x2", "y2", "confidence", "text"})
  public static class Word extends Rect {
    
    public Word() {
    }
    
    @SuppressWarnings("unchecked")
    public Word(Map<String, Object> word) {
      confidence = parseDouble(word.get("confidence"));
      Rect r = getRect((Map<String, Object>) word.get("boundingBox"));
      x1 = r.x1;
      y1 = r.y1;
      x2 = r.x2;
      y2 = r.y2;
      xa = r.xa;
      ya = r.ya;
      pt2X = r.pt2X;
      pt2Y = r.pt2Y;
      rotation = r.rotation;
      detectedBreak = getDetectedBreak(word);
      List<Map<String, Object>> symbols = (List<Map<String, Object>>) word.get("symbols");
      symbolBreaks = new ArrayList<DetectedBreak>(symbols.size());
      String text = "";
      DetectedBreak db;
      double sybConfidence;
      for (Map<String, Object> s : symbols) {
        sybConfidence = parseDouble(s.get("confidence"));
        if (sybConfidence < 0.4) {
          continue;
        }
        if (s.containsKey("text")) {
          text += s.get("text");
        }
        db = getDetectedBreak(s);
        symbolBreaks.add(db);
        detectedBreak = getGeaterBreak(db, detectedBreak);
      }
      
      this.text = text;
      computeAngle();
    }
   
    @JsonIgnore
    public List<DetectedBreak> symbolBreaks;
    @JsonIgnore
    public List<Map<String, Object>> symbols;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public static class Rect extends BasicRect {
    public double confidence;
    public DetectedBreak detectedBreak;
    public String text;
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public static class BasicRect {
    public double x1;
    public double y1;
    public double x2;
    public double y2;
    public double xa;
    public double ya;
    public double rotation = 0;
    public double angle = 0;
    public double pt2X = -1;
    public double pt2Y = -1;
    
    public BasicRect getUnion(BasicRect r2) {
      // assumes that rotation is same, else assumes this.rotation
      
      double tx1 = x1;
      double ty1 = y1;
      double tx2 = x2;
      double ty2 = y2;
      double tpt2X = pt2X;
      double tpt2Y = pt2Y;
     
      if (rotation == 0d) {
        if (r2.x1 < tx1) tx1 = r2.x1;
        if (r2.y1 < ty1) ty1 = r2.y1;
        if (r2.x2 > tx2) tx2 = r2.x2;
        if (r2.y2 > ty2) ty2 = r2.y2;
        if (r2.pt2X > tpt2X) tpt2X = r2.pt2X;
        if (r2.pt2Y < tpt2Y) tpt2Y = r2.pt2Y;
      } else if (rotation == 90d) {
        if (r2.x1 > tx1) tx1 = r2.x1;
        if (r2.y1 < ty1) ty1 = r2.y1;
        if (r2.x2 < tx2) tx2 = r2.x2;
        if (r2.y2 > ty2) ty2 = r2.y2;
        if (r2.pt2X > tpt2X) tpt2X = r2.pt2X;
        if (r2.pt2Y > tpt2Y) tpt2Y = r2.pt2Y;
      } else if (rotation == 180d) {
        if (r2.x1 > tx1) tx1 = r2.x1;
        if (r2.y1 > ty1) ty1 = r2.y1;
        if (r2.x2 < tx2) tx2 = r2.x2;
        if (r2.y2 < ty2) ty2 = r2.y2;
        if (r2.pt2X < tpt2X) tpt2X = r2.pt2X;
        if (r2.pt2Y > tpt2Y) tpt2Y = r2.pt2Y;
      } else if (rotation == 270d) {
        if (r2.x1 < tx1) tx1 = r2.x1;
        if (r2.y1 > ty1) ty1 = r2.y1;
        if (r2.x2 > tx2) tx2 = r2.x2;
        if (r2.y2 < ty2) ty2 = r2.y2;
        if (r2.pt2X < tpt2X) tpt2X = r2.pt2X;
        if (r2.pt2Y < tpt2Y) tpt2Y = r2.pt2Y;
      } 
      
      BasicRect br = new BasicRect();
      br.x1 = tx1;
      br.y1 = ty1;
      br.x2 = tx2;
      br.y2 = ty2;
      br.pt2X = tpt2X;
      br.pt2Y = tpt2Y;
      return br;
    }
    
    public void union(BasicRect r2) {
      // assumes that rotation is same, else assumes this.rotation
      BasicRect br = getUnion(r2);
      this.x1 = br.x1;
      this.x2 = br.x2;
      this.y1 = br.y1;
      this.y2 = br.y2;
      this.pt2X = br.pt2X;
      this.pt2Y = br.pt2Y;
      xa = (x1 + x2) / 2;
      ya = (y1 + y2) / 2;
    }
    
    public void computeBounds(List<? extends BasicRect> brl)  {
      if (brl.size() == 0) return;
      
      BasicRect br = brl.get(0);
      BasicRect br2 = new BasicRect();
      br2.x1 = br.x1;
      br2.y1 = br.y1;
      br2.x2 = br.x2;
      br2.y2 = br.y2;
      br2.pt2X = br.pt2X;
      br2.pt2Y = br.pt2Y;
      br2.rotation = br.rotation;
      
      for (int i=1; i<brl.size(); i++) {
        br2 = br2.getUnion(brl.get(i));
      }
      
      x1 = br2.x1;
      y1 = br2.y1;
      x2 = br2.x2;
      y2 = br2.y2;
      pt2X = br2.pt2X;
      pt2Y = br2.pt2Y;
      xa = (x1 + x2) / 2;
      ya = (y1 + y2) / 2;
    }
    
    public void computeAngle()  {
      double x1 = this.x1;
      double y1 = this.y1;
      double x2 = this.pt2X != -1 ? this.pt2X : this.x1;
      double y2 = this.pt2Y != -1 ? this.pt2Y : this.y1;

      if (rotation == 0 || rotation == 180) {
        angle = Math.atan2(y2-y1, x2-x1);
      } else {
        angle = Math.atan2(y2-y1, x2-x1);
      }
      //if (Math.abs(angle) < 0.005) angle = 0;
    }
    
    double getNewX(double newY2) {
      if (angle == 0) {
        return x1;
      }
      double newV = (newY2-y1) / Math.tan(angle) + x1;
      return newV;
    }
    
    double getNewY(double newX2) {
      if (angle == 0) {
        return y1;
      }
      double newV = ((newX2-x1) * Math.tan(angle)) + y1;
      return newV;
    }
  }
  
  @JsonInclude(Include.NON_DEFAULT)
  public static class DetectedBreak {
    public boolean isPrefix = false;
    public String type;
  }
  
  public static List<String> retrieveProcessedText(Page processedPage) {
    return retrieveProcessedText(processedPage.lines);
  }
  
  public static List<String> retrieveProcessedText(List<Line> lines) {
    List<String> tl = new ArrayList<String>();
    if (lines != null) {
      for (Line line : lines) {
        if (line.text != null) {
          tl.add(line.text.trim());
        }
      }
    }
    return tl;
  }
  
  public static Block makeBlock(Map<String, Object> block) {
    return new Block(block);
  }
  
  public static  Paragraph makeParagraph(Map<String, Object> paragraph) {
    return new Paragraph(paragraph);
  }
  
  public static Cell makeCell(List<Word> words) {
    Cell cell = new Cell();
    if (words.size() > 0) {
      Word w = words.get(0);
      cell.angle = w.angle;
      cell.pt2X = w.pt2X;
      cell.pt2Y = w.pt2Y;
      cell.rotation = w.rotation;
      cell.x1 = w.x1;
      cell.x2 = w.x2;
      cell.xa = w.xa;
      cell.y1 = w.y1;
      cell.y2 = w.y2;
      cell.ya = w.ya;
      cell.update(w);
      int n = words.size();
      for (int i=1; i<n; i++) {
        w = words.get(i);
        cell.update(w);
        cell.union(w);
      }
    }
    return cell;
  }
  
  public static  Word makeWord(Map<String, Object> word) {
    return new Word(word);
  }
  
  @SuppressWarnings("unchecked")
  public static Rect getRect(Map<String, Object> boundingPoly) {
    Rect rect = new Rect();
    double x1 = 0;
    double y1 = 0;
    double x2 = 0;
    double y2 = 0;
    double r;
    double cx = 0;
    double cy = 0;
    double pt2X = -1;
    double pt2Y = -1;
    int n = 1;
    List<Map<String, Object>> l = null;
    double m = 1.0d;
    if (boundingPoly.containsKey("vertices")) {
      l = ( List<Map<String, Object>>)boundingPoly.get("vertices");
    } else if (boundingPoly.containsKey("normalizedVertices")) {
      m = 100.0d;
      l = ( List<Map<String, Object>>)boundingPoly.get("normalizedVertices");
    } 
    if (l != null) {
      if (l.size() == 1) {
        x1 = x2 = parseDouble(l.get(0).get("x")) * m;
        y1 = y2 = parseDouble(l.get(0).get("y")) * m;
      } else if (l.size() == 2) {
        x1 = parseDouble(l.get(0).get("x")) * m;
        y1 = parseDouble(l.get(0).get("y")) * m;
        x2 = parseDouble(l.get(1).get("x")) * m;
        y2 = parseDouble(l.get(1).get("y")) * m;
      } else if (l.size() > 2) {
        x1 = parseDouble(l.get(0).get("x")) * m;
        y1 = parseDouble(l.get(0).get("y")) * m;
        x2 = parseDouble(l.get(2).get("x")) * m;
        y2 = parseDouble(l.get(2).get("y")) * m;
        pt2X = parseDouble(l.get(1).get("x")) * m;
        pt2Y = parseDouble(l.get(1).get("y")) * m;
      }
      
      n = l.size();
      for (int i=0; i<l.size(); i++) {
        cx += parseDouble(l.get(i).get("x")) * m;
        cy += parseDouble(l.get(i).get("y")) * m;
      }
      if (n == 0) {
        n = 1;
      }
    }
    
    cx = cx/n;
    cy = cy/n;
    
    if (x1 <= cx) {
      if (y1 <= cy) {
        r = 0d;
      } else {
        r = 270d;
      }
    } else {
      if (y1 <= cy) {
        r = 90d;
      } else {
        r = 180d;
      }
    }
    
    
    rect.x1 = x1;
    rect.y1 = y1;
    rect.x2 = x2;
    rect.y2 = y2;
    rect.xa = cx;
    rect.ya = cy;
    rect.pt2X = pt2X;
    rect.pt2Y = pt2Y;
    rect.rotation = r; 
    
    return rect;
  }
  
  public static DetectedBreak getGeaterBreak(DetectedBreak db1, DetectedBreak db2) {
    String breakType1 = db1.type;
    String breakType2 = db2.type;
    if (StringUtils.isBlank(breakType2)) {
      return db1;
    } else if (StringUtils.isBlank(breakType1)) {
      return db2;
    }
    
    if ("LINE_BREAK".equals(breakType1)) {
      return db1;
    } else if ("LINE_BREAK".equals(breakType2)) {
      return db2;
    }
    
    if ("HYPHEN".equals(breakType1)) {
      return db1;
    } else if ("HYPHEN".equals(breakType2)) {
      return db2;
    }
    
    if ("EOL_SURE_SPACE".equals(breakType1)) {
      return db1;
    } else if ("EOL_SURE_SPACE".equals(breakType2)) {
      return db2;
    }
    
    if ("SURE_SPACE".equals(breakType1)) {
      return db1;
    } else if ("SURE_SPACE".equals(breakType2)) {
      return db2;
    }
    
    if ("SPACE".equals(breakType1)) {
      return db1;
    } else if ("SPACE".equals(breakType2)) {
      return db2;
    }
    
    return db2;
  }
  
  @SuppressWarnings("unchecked")
  public static DetectedBreak getDetectedBreak(Map<String, Object> map) {
    Map<String, Object> property = (Map<String, Object>) map.get("property");
    Map<String, Object> detectedBreak = null; 
    DetectedBreak db = new DetectedBreak();
    if (property != null) {
      detectedBreak = (Map<String, Object>) property.get("detectedBreak");
      if (detectedBreak != null) {
        db.type = "" + detectedBreak.get("type");
        if (detectedBreak.get("isPrefix") instanceof Boolean) {
          db.isPrefix = (boolean) detectedBreak.get("isPrefix");
        }
      }
    }
    return db;
  }
  
  public static boolean overlapsXInOrder(BasicRect current, BasicRect prev) {
    if (current.rotation == 270) {
      if (current.ya > prev.y1) {
        return false;
      }
    } else if (current.rotation == 90) {
      if (prev.ya > current.y1) {
        return false;
      }
    }
    return overlapsX(current, prev);
  }
  
  public static boolean overlapsYInOrder(BasicRect current, BasicRect prev) {
    if (current.rotation == 0) {
      if (current.xa < prev.x1) {
        return false;
      }
    } else if (current.rotation == 180) {
      if (prev.xa < current.x1) {
        return false;
      }
    }
    return overlapsY(current, prev);
  }
  
  public static boolean overlaps(BasicRect c, BasicRect p) {
    
    if (c.rotation != p.rotation) {
      return false;
    }
    
    if (c.rotation == 0 || c.rotation == 180) {
      return overlapsY(c, p);
    } else {
      return overlapsX(c, p);
    }
  }
  
  public static boolean overlapsAcross(BasicRect c, BasicRect p) {
    
    if (c.rotation != p.rotation) {
      return false;
    }
    
    if (c.rotation == 0 || c.rotation == 180) {
      return overlapsX(c, p);
    } else {
      return overlapsY(c, p);
    }
  }
  
  public static boolean overlapsX(BasicRect r1, BasicRect r2) {
    double lt1 = r1.x1; //Math.min(r1.x1, r1.x2);
    double lt2 = r2.x1; //Math.min(r2.x1, r2.x2);;
    if (r1.angle != 0) {
      double newX1 = r1.getNewX(r2.y1); 
      //double newX2 = r1.getNewX(r2.y2); 
      lt1 = newX1; //Math.min(newX1, newX2);
    }
    double w1 = Math.abs(r1.x2 - r1.x1);
    double w2 = Math.abs(r2.x2 - r2.x1);
    double rt1 = lt1 + w1;
    double rt2 = lt2 + w2;
    double lx = Math.max(lt1, lt2);
    double rx = Math.min(rt1, rt2);
    
    // we need to compare narrower rectangle and wider rectangle overlap
    double reqOverlap1 = (Math.min(w1, w2)) * 0.9; // narrow
    double reqOverlap2 = (Math.max(w1, w2)) * 0.7; // wider
    double overlap = rx - lx;
    if (overlap > reqOverlap1 || overlap > reqOverlap2) {
      return true;
    }
    return false;
  }
  
  public static boolean overlapsY(BasicRect r1, BasicRect r2) {
    double tt1 = r1.y1; //Math.min(r1.y1, r1.y2);
    double tt2 = r2.y1; //Math.min(r2.y1, r2.y2);
    if (r1.angle != 0) {
      double newY1 = r1.getNewY(r2.x1); 
      //double newY2 = r1.getNewY(r2.x2); 
      tt1 = newY1; //Math.min(newY1, newY2);
    }
    double h1 = Math.abs(r1.y2 - r1.y1);
    double h2 = Math.abs(r2.y2 - r2.y1);
    double bt1 = tt1 + h1;
    double bt2 = tt2 + h2;
    double ty = Math.max(tt1, tt2);
    double by = Math.min(bt1, bt2);
    // we need to compare shorter rectangle and taller rectangle overlap
    double reqOverlap1 = (Math.min(h1, h2)) * 0.9; // shorter
    double reqOverlap2 = (Math.max(h1, h2)) * 0.5; // taller
    double overlap = by - ty;
    if (overlap > reqOverlap1 || overlap > reqOverlap2) {
      return true;
    }
    return false;
  }
  
  public static boolean overlapsAcross(BasicRect c, BasicRect p, 
      double smPercent, double lgPercent) {
    
    if (c.rotation != p.rotation) {
      return false;
    }
    
    if (c.rotation == 0 || c.rotation == 180) {
      return overlapsX(c, p, smPercent, lgPercent);
    } else {
      return overlapsY(c, p, smPercent, lgPercent);
    }
  }
  
  public static boolean overlapsX(BasicRect r1, BasicRect r2,
      double smPercent, double lgPercent) {
    double lt1 = r1.x1; //Math.min(r1.x1, r1.x2);
    double lt2 = r2.x1; //Math.min(r2.x1, r2.x2);;
    if (r1.angle != 0) {
      double newX1 = r1.getNewX(r2.y1); 
      //double newX2 = r1.getNewX(r2.y2); 
      lt1 = newX1; //Math.min(newX1, newX2);
    }
    double w1 = Math.abs(r1.x2 - r1.x1);
    double w2 = Math.abs(r2.x2 - r2.x1);
    double rt1 = lt1 + w1;
    double rt2 = lt2 + w2;
    double lx = Math.max(lt1, lt2);
    double rx = Math.min(rt1, rt2);
    
    // we need to compare narrower rectangle and wider rectangle overlap
    double reqOverlap1 = (Math.min(w1, w2)) * lgPercent; // narrow
    double reqOverlap2 = (Math.max(w1, w2)) * smPercent; // wider
    double overlap = rx - lx;
    if (overlap > reqOverlap1 || overlap > reqOverlap2) {
      return true;
    }
    return false;
  }
  
  public static boolean overlapsY(BasicRect r1, BasicRect r2, 
      double smPercent, double lgPercent) {
    double tt1 = r1.y1; //Math.min(r1.y1, r1.y2);
    double tt2 = r2.y1; //Math.min(r2.y1, r2.y2);
    if (r1.angle != 0) {
      double newY1 = r1.getNewY(r2.x1); 
      //double newY2 = r1.getNewY(r2.x2); 
      tt1 = newY1; //Math.min(newY1, newY2);
    }
    double h1 = Math.abs(r1.y2 - r1.y1);
    double h2 = Math.abs(r2.y2 - r2.y1);
    double bt1 = tt1 + h1;
    double bt2 = tt2 + h2;
    double ty = Math.max(tt1, tt2);
    double by = Math.min(bt1, bt2);
    // we need to compare shorter rectangle and taller rectangle overlap
    double reqOverlap1 = (Math.min(h1, h2)) * 0.9; // shorter
    double reqOverlap2 = (Math.max(h1, h2)) * 0.5; // taller
    double overlap = by - ty;
    if (overlap > reqOverlap1 || overlap > reqOverlap2) {
      return true;
    }
    return false;
  }
  
  public static boolean matches(String source, String target) {
    return source.trim().equals(target.trim());
  }
  
  public static boolean partiallyMatches(String source, String target) {
    return target.trim().startsWith(source.trim());
  }
  
  public static double parseDouble(Object v) {
    if (v == null) return 0;
    return Double.parseDouble("" + v);
  }
  
  public static class TokenizedWord {
    public String text;
    public boolean stopWord;
    public int lineIndex;
    public int wordIndex; // index of the original word
    public int index; // index of this TokenizedWord in its container list
  }
}
