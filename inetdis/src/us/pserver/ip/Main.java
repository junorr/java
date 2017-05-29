/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.ip;

import java.util.List;
import java.util.stream.Collectors;
import us.pserver.tools.StringPad;


/**
 *
 * @author juno
 */
public class Main {
  
  
  public static String header() {
    return StringPad.of("Index").rpad(" ", 7)
        + StringPad.of("Name").rpad(" ", 12)
        + StringPad.of("IPv4").lpad(" ", 17)
        + StringPad.of("IPv6").lpad(" ", 42)
        + "\n"
        + StringPad.of(StringPad.of("").lpad("-", 5)).rpad(" ", 7)
        + StringPad.of(StringPad.of("").lpad("-", 10)).rpad(" ", 12)
        + StringPad.of(StringPad.of("").lpad("-", 15)).lpad(" ", 17)
        + StringPad.of(StringPad.of("").lpad("-", 40)).lpad(" ", 42);
  }
  
  
  public static String format(INet i) {
    if(i == null) return "";
    return StringPad.of("#".concat(String.valueOf(i.index()))).rpad(" ", 7)
        + StringPad.of(i.name()).rpad(" ", 12)
        + StringPad.of((i.ipv4String() != null ? i.ipv4String() : "-")).lpad(" ", 17)
        + StringPad.of((i.ipv6String() != null ? i.ipv6String() : "-")).lpad(" ", 42);
  }
  
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    INetDiscovery dis = INetDiscovery.create();
    if(args == null || args.length == 0) {
      System.out.println(header());
      List<INet> ls = dis.getInterfaces().stream()
          .sorted((a,b)->Integer.compare(a.index(), b.index()))
          .collect(Collectors.toList());
      for(INet i : ls) {
        System.out.println(format(i));
      }
    }
    else if(args[0].equals("-m") || args[0].equals("--main")) {
      System.out.println(header());
      System.out.println(format(dis.guessMainINet()));
    }
    else if(args[0].equals("-l") || args[0].equals("--local")) {
      System.out.println(header());
      System.out.println(format(dis.getLoopback()));
    }
    else if(args[0].equals("-n") || args[0].equals("--name") && args.length > 1) {
      String arg = args[1];
      System.out.println(header());
      System.out.println(format(dis.getInterfaces().stream()
          .filter(i->i.name().equals(arg))
          .findAny().orElse(null))
      );
    }
    else if(args[0].equals("-i") || args[0].equals("--index") && args.length > 1) {
      String arg = args[1];
      try {
        System.out.println(header());
        System.out.println(format(dis.getInterfaces().stream()
            .filter(i->i.index() == Integer.parseInt(arg))
            .findAny().orElse(null))
        );
      } catch(NumberFormatException e) {
        System.err.println("# Bad Index: "+ args[1]);
      }
    }
    else if(args[0].equals("-h") || args[0].equals("--help")) {
      System.out.println("* INetDiscovery usage: inetdis [opt] [arg]");
      System.out.println("    -i/--index <index>: Get INet by index");
      System.out.println("    -l/--local        : Get INet loopback");
      System.out.println("    -m/--main         : Get Main INet");
      System.out.println("    -n/--name <name>  : Get INet by name");
      System.out.println("    -h/--help         : Show this help usage");
    }
  }
  
}
