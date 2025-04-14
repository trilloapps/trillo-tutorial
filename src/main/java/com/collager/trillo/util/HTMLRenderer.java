package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.collager.trillo.util.DataObject.Block;
import com.collager.trillo.util.DataObject.Cell;
import com.collager.trillo.util.DataObject.Field;
import com.collager.trillo.util.DataObject.Image;
import com.collager.trillo.util.DataObject.Page;
import com.collager.trillo.util.DataObject.Rect;
import com.collager.trillo.util.DataObject.Section;
import com.collager.trillo.util.DataObject.TOCItem;
import com.collager.trillo.util.DataObject.Table;
import com.collager.trillo.util.DataObject.TableRow;

public class HTMLRenderer {
  public static String generateHTML(Page page) {
    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"page-preview"
        + "\">\n");
    List<Map<String, Object>> tocItems = page.retreiveTocItems();
    if (tocItems != null) {
      sb.append(renderToc(tocItems, page));
    }   else {
      List<Rect> rl = page.retrieveRenderables();
      if (rl.size() == 0) {
        return formatText(page.retrievePageText());
      } else {
        sb.append(generateHTML(page, rl));
      }
    }
    sb.append("</div>\n");

    return sb.toString();
  }

  public static Object generateHTML(Page page, List<Rect> rl) {
    StringBuilder sb = new StringBuilder();

    int pid = 1;
    String title = StringUtils.isNotBlank(page.llmDerivedTitles) ? page.llmDerivedTitles :
        StringUtils.isNotBlank(page.title) ? page.title : "";
    if (title.length() > 0) {
      sb.append("<h3 class=\"cls-" + pid + " page-title\">" +  formatText(title) + "</h3>\n");
    }
    String type;
    for (Rect r : rl) {
      type = r.type;
      if (!"heading".equals(type)) {
        sb.append(generateSectionTitle(r, pid));
      }
      switch (type) {
        case "heading":
          sb.append(generateHeading(r, pid));
          break;
        case "paragraph":
        case "text":
        case "block" :
          sb.append(generateParagraph(r, pid));
          break;
        case "table":
          sb.append(generateTable(r, pid));
          break;
        case "image":
          sb.append(generateImage(r, pid));
          break;
        case "bulletPoints":
        case "list":
          sb.append(generateList(r, pid));
          break;
        case "formField":
          sb.append(generateFormField(r, pid));
          break;
        case "fieldSet":
          sb.append(generateFieldSet(r, pid));
          break;
        case "toc":
          sb.append(generateToc(r, pid));
          break;
        default:
          sb.append(generateParagraph(r, pid));
          break;
      }
      pid++;
    }
    return sb.toString();
  }

  private static Object generateSectionTitle(Rect r, int pid) {
    if (!(r instanceof Section)) {
      return "";
    }
    Section s = (Section)r;
    String t = s.title;
    if (StringUtils.isBlank(t)) {
      return "";
    }
    return "<h4 class=\"cls-" + pid + "\">" +  formatText(t) + "</h4>\n";
  }

  public static String generateHeading(Rect r, int pid) {
    return "<h4 class=\"cls-" + pid + "\">" +  formatText(r.text) + "</h4>\n";
  }

  public static String generateParagraph(Rect r, int pid) {
    return "<p class=\"cls-" + pid + "\">" +  formatText(r.text) + "</p>\n";
  }

  private static String generateList(Rect r, int pid) {
    if (!(r instanceof Section)) {
      return "";
    }
    Section s = (Section)r;
    if (s.items == null) {
      return "";
    }
    StringBuffer sb = new StringBuffer();
    sb.append("<div class=\"cls-" + pid + "\">\n");
    sb.append("<ul>\n");
    for (String str : s.items) {
      sb.append("  <li>" + formatText(str) + "</li>\n");
    }
    sb.append("</ul>\n");
    sb.append("</div>\n");
    return sb.toString();
  }

  public static String generateFormField(Rect r, int pid) {
    if (!(r instanceof Section)) {
      return "";
    }
    Section s = (Section)r;
    if (s.field == null) {
      return "";
    }
    List<Field> fields = new ArrayList<>();
    fields.add(s.field);
    return generateFields(fields, pid);
  }

  public static String generateFieldSet(Rect r, int pid) {
    if (!(r instanceof Section)) {
      return "";
    }
    Section s = (Section)r;
    if (s.fieldSet == null || s.fieldSet.fields == null) {
      return "";
    }
    return generateFields(s.fieldSet.fields, pid);
  }

  public static String generateFields(List<Field> fields, int pid) {
    StringBuffer sb = new StringBuffer();
    sb.append("<div class=\"cls-" + pid + "\">\n");
    sb.append("<table class=\"form-table\">\n");
    sb.append("  <tbody>\n");

    String trCls = "odd";
    for (Field f : fields) {
      sb.append("    <tr class=\"" + trCls + "\">\n");
      sb.append("      <td class=\"label\">" + formatLabel(f.name) + "</td>\n");
      sb.append("      <td class=\"value\">" + formatText(f.value == null ? "" : "" + f.value) + "</td>\n");
      sb.append("    </tr>\n");
      trCls = "odd".equals(trCls) ? "even" : "odd";
    }

    sb.append("  </tbody>\n");
    sb.append("</table>\n");
    sb.append("</div>\n");
    return sb.toString();
  }


  public static Object generateTable(Rect r, int pid) {
    if (!(r instanceof Table) && !(r instanceof Section)) {
      return "";
    }
    Table table = null;
    if (r instanceof Section) {
      Section s = (Section)r;
      table = s.table;
      if (table == null) {
        return "";
      }
    } else {
      table = (Table) r;
    }
    StringBuffer sb = new StringBuffer();
    sb.append("<div class=\"cls-" + pid + "\">\n");
    sb.append("<table>\n");
    if (table.header != null) {
      sb.append("  <thead>\n");
      sb.append("    <tr>\n");
      for (Cell cell : table.header.cells) {
        sb.append("      <th>" + formatText(cell.text) + "</th>\n");
      }
      sb.append("    </tr>\n");
      sb.append("  </thead>\n");
    }
    sb.append("  <tbody>\n");

    if (table.rows != null) {
      for (TableRow row : table.rows) {
        sb.append("    <tr>\n");
        if (row.cells != null) {
          for (Cell cell : row.cells) {
            sb.append("      <td>" + formatText(cell.text) + "</td>\n");
          }
        }
        sb.append("    </tr>\n");
      }
    }
    sb.append("  </tbody>\n");
    sb.append("</table>\n");
    sb.append("</div>\n");
    return sb.toString();
  }

  public static Object generateImage(Rect r, int pid) {
    Image img;
    if (r instanceof Block) {
      img = ((Block) r).image;
    } else if (r instanceof Section) {
      img = ((Section) r).image;
    } else {
      return "";
    }
    if (img == null) {
      return "";
    }
    String url = img.url;
    if (StringUtils.isBlank(url) || url.toLowerCase().trim().startsWith("data:")) {
      return "";
    }

    if ((img.imageWidth > -1 && img.imageWidth < 150) || (img.imageHeight > -1 && img.imageHeight < 100) || img.imageWidth > 2000) {
      return "";
    }
    String str = url.toLowerCase();
    if (str.startsWith("http://") || str.startsWith("https://")) {

    } else {
      url = "{{{SERVER_BASE_URL}}}" + url;
    }
    return "<div class=\"img-wrapper cls-" + pid + "\"><img src=\"" + url + "\"" +
        (img.imageWidth > -1 ? (" width=\"" + img.imageWidth) + "\"" : "") +
        (img.imageHeight > -1 ? (" height=\"" + img.imageHeight + "\"") : "") + " /></div>\n";

  }

  public static String generateToc(Rect r, int pid) {
    if (!(r instanceof Section)) {
      return "";
    }
    Section s = (Section)r;
    if (s.toc == null || s.toc.items == null) {
      return "";
    }
    return renderToc(s.toc.items, pid);
  }

  public static String renderToc(List<Map<String, Object>> tocItems, Page page) {
    StringBuilder sb = new StringBuilder();
    sb.append("<h3>Table of Content</h3>\n");
    if (tocItems != null) {
      for (Map<String, Object> m : tocItems) {
        if (m.containsKey("pageNumber") && m.containsKey("title")) {
          sb.append("<p>");
          sb.append("<div><strong>" + m.get("pageNumber") + "&nbsp;&nbsp;&nbsp;&nbsp;" + m.get("title") + "</strong></div>");
          if (m.get("summary") != null) {
            sb.append("<div>" + m.get("summary") + "</div>");
          }
          sb.append("</p>");
        }
      }
    }
    return sb.toString();
  }

  public static String renderToc(List<TOCItem> items, int pid) {
    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"cls-" + pid + "\">\n");
    if (items != null) {
      for (TOCItem item : items) {
        if (item.pageNumber > 0) {
          sb.append("<p>");
          sb.append("<div><strong>" +item.pageNumber + "&nbsp;&nbsp;&nbsp;&nbsp;" + (item.title != null ? item.title : "") + "</strong></div>");
          if (item.summary != null) {
            sb.append("<div>" + item.summary + "</div>");
          }
          sb.append("</p>");
        }
      }
    }
    sb.append("</div>\n");
    return sb.toString();
  }

  public static String formatText(String text) {
    if (StringUtils.isBlank(text)) return "";
    text = text.replaceAll("(\r\n|\n)", "<br/>");
    return text;
  }

  public static String formatLabel(String text) {
    String label = formatText(text);
    if (StringUtils.isNotBlank(label)) {
      label = label.trim();
      if (!label.endsWith(":")) {
        label = label + ":";
      }
    }
    return label;
  }
}
