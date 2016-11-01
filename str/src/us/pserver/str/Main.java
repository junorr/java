/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.str;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;


/**
 *
 * @author juno
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ShellParser sp = new ShellParser();
    sp.setAppName("Str");
    sp.setAuthor("Juno Roesler");
    sp.setContact("juno@pserver.us");
    sp.setDescription("String Utils Functions");
    sp.setLicense("GNU/LGPL");
    sp.setYear("2014");
    
    Option o;
    o = Option.EMPTY_OPTION;
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("The text with will work to");
    sp.addOption(o);
    
    //load
    o = new Option();
    o.setName("-F");
    o.setLongName("--file");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Loads text from file");
    sp.addOption(o);
    
    //find
    o = new Option();
    o.setName("-f");
    o.setLongName("--find");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Finds string in the text");
    sp.addOption(o);
    
    //findAll
    o = new Option();
    o.setName("-a");
    o.setLongName("--all");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Finds all occurrences of the string");
    sp.addOption(o);
    
    //charAt
    o = new Option();
    o.setName("-A");
    o.setLongName("--charAt");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Returns the character at index");
    sp.addOption(o);
    
    //last
    o = new Option();
    o.setName("-l");
    o.setLongName("--last");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Finds the last occurrence of the string");
    sp.addOption(o);
    
    //length
    o = new Option();
    o.setName("-L");
    o.setLongName("--length");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(false);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Return the length of the string");
    sp.addOption(o);
    
    //upper
    o = new Option();
    o.setName("-u");
    o.setLongName("--upper");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(false);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Return the Upper Case of the string");
    sp.addOption(o);
    
    //lower
    o = new Option();
    o.setName("-w");
    o.setLongName("--lower");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(false);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Return the Lower Case of the string");
    sp.addOption(o);
    
    //equals
    o = new Option();
    o.setName("-e");
    o.setLongName("--equals");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the string is equals to");
    sp.addOption(o);
    
    //notEquals
    o = new Option();
    o.setName("-n");
    o.setLongName("--notEquals");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the string is NOT equals to");
    sp.addOption(o);
    
    //equalsics
    o = new Option();
    o.setName("-E");
    o.setLongName("--eqIgnoreCase");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the string is equals ignoring case");
    sp.addOption(o);
    
    //contains
    o = new Option();
    o.setName("-c");
    o.setLongName("--contains");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the text contains the string");
    sp.addOption(o);
    
    //containsics
    o = new Option();
    o.setName("-C");
    o.setLongName("--ctIgnoreCase");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the text contains ignoring case");
    sp.addOption(o);
    
    //startsWith
    o = new Option();
    o.setName("-s");
    o.setLongName("--starts");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the string starts with");
    sp.addOption(o);
    
    //startsWithIcs
    o = new Option();
    o.setName("-S");
    o.setLongName("--startsIgnoreCase");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the string starts with ignoring case");
    sp.addOption(o);
    
    //endsWith
    o = new Option();
    o.setName("-d");
    o.setLongName("--ends");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the string ends with");
    sp.addOption(o);
    
    //endsWithIcs
    o = new Option();
    o.setName("-D");
    o.setLongName("--endsIgnoreCase");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Verifies if the string ends with ignoring case");
    sp.addOption(o);
    
    //split
    o = new Option();
    o.setName("-p");
    o.setLongName("--split");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Splits the string by the (a)rgument and return (n)umber segment (a n)");
    sp.addOption(o);
    
    //replace
    o = new Option();
    o.setName("-r");
    o.setLongName("--replace");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Replaces the (s)tring with (a)rgument (s a)");
    sp.addOption(o);
    
    //subs
    o = new Option();
    o.setName("-b");
    o.setLongName("--subs");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Substring on (o)ffset with (l)ength (o l)");
    sp.addOption(o);
    
    //trim
    o = new Option();
    o.setName("-t");
    o.setLongName("--trim");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(false);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Trims the spaces in the string");
    sp.addOption(o);
    
    //concat
    o = new Option();
    o.setName("-T");
    o.setLongName("--concat");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(true);
    o.setExclusive(false);
    o.setMandatory(false);
    o.setDescription("Concats the text with the string");
    sp.addOption(o);
    
    o = new Option();
    o.setName("-h");
    o.setLongName("--help");
    o.setArgsSeparator(" ");
    o.setAcceptArgs(false);
    o.setExclusive(true);
    o.setMandatory(false);
    o.setDescription("Show the help");
    sp.addOption(o);
    
    
    //System.out.println(sp.createHeader(40));
    sp.parseArgs(args);
    
    if(!sp.parseErrors()) {
      System.out.println(sp.createHeader(40));
      sp.printWarningMessages(System.out);
      System.out.flush();
      sp.printErrorMessages(System.err);
      System.err.flush();
      System.exit(1);
    }
    
    if(sp.isOptionPresent("-h")) {
      System.out.println(sp.createHeader(40));
      System.out.println(sp.createUsage());
      System.exit(0);
    }
    
    Strings st = new Strings();
    if(sp.isOptionPresent("-F")) {
      st.load(sp.getOption("-F").getFirstArg());
      //System.out.println("* str from file");
    }
    else if(sp.isOptionPresent(Option.EMPTY)) {
      //System.out.println("* str from empty opt");
      st.setString(sp.getOption(Option.EMPTY).getFirstArg());
    }
    else {
      //System.out.println("* str from system.in");
      st.load(System.in);
    }
    
    try {
    
      if(sp.isOptionPresent("-f")) {
        o = sp.getOption("-f");
        if(o.arguments().size() > 1) {
          System.out.println(st.find(o.getFirstArg(), o.getAsInt(1)));
        } else {
          System.out.println(st.find(o.getFirstArg()));
        }
      }
      else if(sp.isOptionPresent("-a")) {
        o = sp.getOption("-a");
        int[] is = st.findAll(o.getFirstArg());
        if(is.length == 0) {
          System.out.print("-1");
        }
        for(int i = 0; i < is.length; i++) {
          System.out.print(is[i]);
          if(i < is.length -1)
            System.out.print(",");
        }
        System.out.println();
      }
      else if(sp.isOptionPresent("-A")) {
        o = sp.getOption("-A");
        System.out.println(st.charAt(o.getAsInt(0)));
      }
      else if(sp.isOptionPresent("-l")) {
        o = sp.getOption("-l");
        System.out.println(st.last(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-L")) {
        System.out.println(st.length());
      }
      else if(sp.isOptionPresent("-u")) {
        System.out.println(st.upper());
      }
      else if(sp.isOptionPresent("-w")) {
        System.out.println(st.lower());
      }
      else if(sp.isOptionPresent("-e")) {
        o = sp.getOption("-e");
        System.out.println(st.equals(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-E")) {
        o = sp.getOption("-E");
        System.out.println(st.equalsics(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-n")) {
        o = sp.getOption("-n");
        System.out.println(st.notEquals(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-c")) {
        o = sp.getOption("-c");
        System.out.println(st.contains(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-C")) {
        o = sp.getOption("-C");
        System.out.println(st.containsics(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-s")) {
        o = sp.getOption("-s");
        System.out.println(st.startsWith(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-S")) {
        o = sp.getOption("-S");
        System.out.println(st.startsWithIcs(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-d")) {
        o = sp.getOption("-d");
        System.out.println(st.endsWith(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-D")) {
        o = sp.getOption("-D");
        System.out.println(st.endsWithIcs(o.getFirstArg()));
      }
      else if(sp.isOptionPresent("-p")) {
        o = sp.getOption("-p");
        String[] ss = st.split(o.getFirstArg());
        if(o.arguments().size() > 1) {
          int i = o.getAsInt(1);
          if(i < 0 || i >= ss.length) {
            throw new IllegalStateException(
                "Invalid segment number: "+ o.getArg(1));
          }
          System.out.println(ss[i]);
        } else {
          for(int i = 0; i < ss.length; i++) {
            System.out.print(ss[i]);
            if(i < ss.length -1)
              System.out.print(",");
          }
          System.out.println();
        }
      }
      else if(sp.isOptionPresent("-r")) {
        o = sp.getOption("-r");
        System.out.println(st.replace(o.getArg(0), o.getArg(1)));
      }
      else if(sp.isOptionPresent("-b")) {
        o = sp.getOption("-b");
        if(o.arguments().size() > 1) {
          System.out.println(st.subs(o.getAsInt(0), o.getAsInt(1)));
        } else {
          System.out.println(st.subs(o.getAsInt(0)));
        }
      }
      else if(sp.isOptionPresent("-B")) {
        o = sp.getOption("-B");
        
      }
      else if(sp.isOptionPresent("-t")) {
        System.out.println(st.trim());
      }
      else if(sp.isOptionPresent("-T")) {
        o = sp.getOption("-T");
        System.out.println(st.concat(o.getFirstArg()));
      }
    
    }
    catch(Exception e) {
      System.out.println(sp.createHeader(40));
      System.out.println("# "+ e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
      
    System.exit(0);
  }
  
}
