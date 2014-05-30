/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.tn3270.script;


/**
 *
 * @author juno
 */
public class Command {
  
  public static final String
    STR_APPEND = "append", 
    STR_CONNECT_3270 = "connect3270", 
    STR_CONTAINS = "contains", 
    STR_CURSOR = "cursor",
    STR_DELAY = "delay",
    STR_ELSE = "else",
    STR_END = "end",
    STR_ENDWHILE = "endwhile",
    STR_EQUALS = "equals",
    STR_FILE_GET = "fileget",
    STR_FILE_PUT = "fileput",
    STR_GET_FIELD = "getfield",
    STR_GET_SCREEN = "getscreen",
    STR_IF = "if",
    STR_KEY = "key",
    STR_NOT = "not",
    STR_PASS = "pass",
    STR_PRINT = "print",
    STR_SET_FIELD = "setfield",
    STR_VAR = "var",
    STR_WAIT = "wait",
    STR_WHILE = "while";
  
  
  private int argsSize;
  
  private String[] args;
  
  private CommandType type;
  
  
  public Command(CommandType cmd) {
    if(cmd == null) 
      throw new IllegalArgumentException(
          "Invalid Command Type: "+ cmd);
    if(argsSize < 0)
      throw new IllegalArgumentException(
          "Invalid number of arguments: "+ argsSize);
    type = cmd;
    argsSize = cmd.getArgsSize();
    args = new String[argsSize];
  }
  
  
  public int getArgsSize() {
    return argsSize;
  }
  
  
  public String[] getArgs() {
    return args;
  }
  
  
  public CommandType getType() {
    return type;
  }
  
  
  public Command setType(CommandType type) {
    if(type != null)
      this.type = type;
    return this;
  }
  
  
  public Command setArgsSize(int size) {
    if(size > 0) {
      argsSize = size;
      args = new String[argsSize];
    }
    return this;
  }
  
  
  public String getArg(int idx) {
    if(idx < 0 || idx >= args.length)
      return null;
    return args[idx];
  }
  
  
  public Command setArg(String arg, int idx) {
    if(idx >= 0 && idx < args.length) {
      args[idx] = arg;
    }
    return this;
  }
  
  
  public String toString() {
    String s = type.name()+"{";
    for(int i = 0; i < args.length; i++) {
      s += args[i];
      if(i < args.length -1)
        s += ", ";
    }
    return s + "}";
  }
  
}
