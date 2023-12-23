package io.trillo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeConversionTester {
  
  public static void main(String[] args) {
    Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
   
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());    
    System.out.println("zonedDateTime: " + zonedDateTime);
    String str = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime);
    System.out.println("ISO_OFFSET_DATE_TIME: " + str);
    
    str = DateTimeFormatter.ISO_INSTANT.format(zonedDateTime);
    System.out.println("ISO_INSTANT: " + str); 
    
    DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    str = df.format(new Date());
    System.out.println("ISO_INSTANT: " + str); 
    
    df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    str = df.format(new Date());
    System.out.println("yyyy-MM-dd HH:mm:ss.SSS: " + str); 
  }
}

