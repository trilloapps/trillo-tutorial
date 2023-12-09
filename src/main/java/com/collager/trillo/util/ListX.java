package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListX extends ArrayList<MapX> {
  
  private static final long serialVersionUID = 1L;
  
//an M object can be given a name which will be used for debugging,
 // logging, in error message
 private String _name = null;
 
 public ListX() {}
 
 public ListX(String name) {
   _name = name;
 }
 
 public ListX(List<Map<String, Object>> sourceList) {
   addListOfMaps(sourceList);
 }
 
 public ListX(List<Map<String, Object>> sourceList, String name) {
   addListOfMaps(sourceList);
   _name = name;
 }
 
 public void name(String name) {
   this._name = name;
 }
 
 public String name() {
   return _name ;
 }
 
 public void addListOfMaps(List<Map<String, Object>> l) {
   for (Map<String, Object> m : l) {
     if (m instanceof MapX) {
       l.add((MapX) m);
     } else {
       add(new MapX(m));
     }
   }
 }
}
