/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author juno
 */
public class TextFieldParser implements Serializable {
    
  private String line;
  
  private List<String> fields;
    
    
  public TextFieldParser() {
    line = null;
    fields = new LinkedList<>();
  }
  
  
  public TextFieldParser(String line) {
    this.line = line;
    fields = new LinkedList<>();
  }
  
  
  public TextFieldParser clear() {
    fields.clear();
    line = null;
    return this;
  }


  public String getLine() {
    return line;
  }


  public TextFieldParser setLine(String line) {
    this.line = line;
    fields.clear();
    return this;
  }
  
  
  public String nextField() {
    return field(1);
  }
  
  
  public String field(int idx) {
    if(fields.isEmpty() 
        || idx >= fields.size() || idx < 0)
      return null;
    return fields.get(idx);
  }
  
  
  public String pop() {
    if(fields.isEmpty()) return null;
    return fields.remove(0);
  }
  
  
  public String peak() {
    if(fields.isEmpty()) return null;
    return fields.get(0);
  }
  
  
  public boolean fieldContainsMany(String s, int times) {
    String f = peak();
    
    if(f == null || s == null) return false;
    if(times < 0) return false;
    if(times == 0 && !f.contains(s)) return true;
    
    int found = 0;
    int chars = s.length();
    for(int i = 0; i < f.length(); i += chars) {
      String part = f.substring(i, i + chars);
      if(part.equals(s)) found++;
    }
    
    return found == times;
  }
  
  
  public void discard(int num) {
    for(int i = 0; i < num; i++) {
      this.pop();
    }
  }
  
  
  public double doubleField(int idx) {
    String s = this.field(idx);
    if(s == null) return -1;
    
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      
      if(Character.isDigit(c) 
          || (sb.indexOf(".") < 0 && c == '.'))
        sb.append(c);
      else if(c == ',' && sb.indexOf(".") < 0) sb.append('.');
    }
    s = sb.toString();
    
    try {
      return Double.parseDouble(s);
      
    } catch(NumberFormatException ex) {
      return -1;
    }
  }
  
  
  public double doublePop() {
    if(fields.isEmpty()) return -1;
    double d = doubleField(0);
    fields.remove(0);
    return d;
  }


  public String fieldAsNumberString(int idx) {
    String s = this.field(idx);
    if(s == null) return null;
    
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      
      if(Character.isDigit(c) || c == '.')
        sb.append(c);
      else if(c == ',' && sb.indexOf(".") < 0) sb.append('.');
    }
    
    return sb.toString();
  }
  
  
  public String popAsNumberString() {
    if(fields.isEmpty()) return null;
    String d = fieldAsNumberString(0);
    fields.remove(0);
    return d;
  }
  
  
  public int size() {
    return fields.size();
  }
  
  
  public List<String> fields() {
    return fields;
  }


  public List<String> getFields() {
    return fields;
  }
  
  
  public void parse(String ... delimiters) {
    if(line == null || line.trim().isEmpty()
        || delimiters == null || delimiters.length == 0
        || delimiters[0] == null)
      return;
    
    StringBuilder sb = new StringBuilder();
    int chars = delimiters[0].length();
    
    for(int i = 0; i < line.length(); i += chars) {
      String s = line.substring(i, i + chars);
      
      boolean add = true;
      for(String d : delimiters) {
        if(s.equals(d)) {
          if(!sb.toString().isEmpty())
            fields.add(sb.toString());
          sb = new StringBuilder();
          add = false;
          break;
        }
      }
      
      if(add) sb.append(s);
    }
    
    if(!sb.toString().isEmpty())
      fields.add(sb.toString());
    
    line = null;
  }
    
  
  public static void main(String[] args) {
    TextFieldParser tp = new TextFieldParser();
    String line = "12:34:45:78:90";
    tp.setLine(line).parse(" ");
    
    System.out.println("line = ["+ line+ "]");
    System.out.println("tp.fieldContainsMany(':', 4): "+ tp.fieldContainsMany(":", 4));
    
    
    /*
    tp.setLine("/dev/xvda1       8256952 1639896   6197628  21% /");
    tp.parse(" ");
    List<String> l = tp.getFields();
    System.out.println("* Fields: "+ l.size());
    for(int i = 0; i < l.size(); i++) {
      System.out.println("   ["+ l.get(i) + "]");
    }
    * */
  }
  
}
