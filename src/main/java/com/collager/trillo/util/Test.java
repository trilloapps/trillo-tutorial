package com.collager.trillo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class Test {

  public static void main(String[] args) {
   
    /*
    DateTime date = new DateTime();
    System.out.println(date.toString());
    System.out.println(date.getMillis());
    date = date.minusDays(100);
    System.out.println(date.toString());
    System.out.println(date.getMillis());
    */
    
  /*  int numberOfMonths = 12;
    LocalDate currentDate = LocalDate.now();
    LocalDate date = currentDate.minusMonths(numberOfMonths);
    LocalDate startDate = date;
    
    String from, to;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    String startDateStr = startDate.format(df);
    System.out.println("Start date: " + startDateStr);
    
    for (int monthN = 0; monthN < numberOfMonths; monthN++) {
      from = date.format(df);
      date = date.plusMonths(1);
      to = date.format(df);
      System.out.println("From: " + from + ", To: " + to);
    } */
    
    String dateStr = "12 Feb 2021";
    dateStr = normalizeDate(dateStr);
    System.out.println(dateStr);
    
    dateStr = "Mar-21-2021";
    dateStr = normalizeDate(dateStr);
    System.out.println(dateStr);
    
    
    dateStr = "02 21 2021";
    dateStr = normalizeDate(dateStr);
    System.out.println(dateStr);
    
  }
  
  public static String normalizeDate(String str) {
    
    if (StringUtils.isBlank(str)) {
      return null;
    }
    
    String[] sl = str.split("[-\\s]");
    
    if (sl.length < 3) {
      return str;
    }
    
    int idx = getAlphaMonthIndex(sl);
    
    String mon, day, year;
    String dateStr;
    String format;
    if (idx >= 0) {
      mon = sl[idx];
      if (idx == 0) {
        day = sl[1];
      } else {
        day = sl[0];
      }
      year = sl[2];
    } else {
      mon = sl[0];
      day = sl[1];
      year = sl[2];
      mon = fixAlphaAsNumber(mon);
    }
    
    day = fixAlphaAsNumber(day);
    year = fixAlphaAsNumber(year);
    
    dateStr = mon + "-" + day + "-" + year;
    
    format = ((mon.length() > 2) ? "MMM" : "MM") + "-" + "dd-" + ((year.length() > 2) ? "yyyy" : "yy");
    
    try {
      SimpleDateFormat parser = new SimpleDateFormat(format);
      Date date = parser.parse(dateStr);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      String formattedDate = formatter.format(date);
      return formattedDate;
    } catch (Exception exc) {
      exc.printStackTrace();
    }
    
    return str;
  }

  private static String fixAlphaAsNumber(String day) {
    day = day.replaceAll("I", "1");
    day = day.replaceAll("O", "0");
    day = day.replaceAll("o", "0");
    day = day.replaceAll("Q", "0");
    return day;
  }

  private static int getAlphaMonthIndex(String[] sl) {
    String s;
    for (int i=0; i<sl.length; i++) {
      s = sl[i];
      if (s.length() > 2) {
        if (Character.isAlphabetic(s.charAt(0))) {
          return i;
        }
      }
    }
    
    return -1;
  }

}
