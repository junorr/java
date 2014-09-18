/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;
import com.jpower.sys.security.Access;
import com.jpower.sys.security.Password;
import com.jpower.sys.security.User;
import com.jpower.sys.security.UserDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;



/**
 *
 * @author juno
 */
public class Main {
  
  public static void main(String[] args) {
    ShellParser sp = new ShellParser();
    
    Option opt = new Option("-s")
        .setLongName("--start")
        .setDescription("Start Syscheck Module")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-k")
        .setLongName("--kill")
        .setDescription("Stop Syscheck Module")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-u")
        .setLongName("--user")
        .setDescription("Add and Configure New User")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-l")
        .setLongName("--list")
        .setDescription("List all users")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-e")
        .setLongName("--email")
        .setDescription("Select a user by your e-mail for other operations")
        .setAcceptArgs(true)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-d")
        .setLongName("--delete")
        .setDescription("Deletes a user by your e-mail (-e/--email option)")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-r")
        .setLongName("--reconfigure")
        .setDescription("Reconfigure permissions of a user by your e-mail (-e/--email option)")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-p")
        .setLongName("--permissions")
        .setDescription("List permissions of a user by your e-mail (-e/--email option)")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-w")
        .setLongName("--pwd")
        .setDescription("Tests the given password (--pwd <password>) for a user")
        .setAcceptArgs(true)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    opt = new Option("-h")
        .setLongName("--help")
        .setDescription("Show this help text")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    sp.addOption(opt);
    
    sp.setAppName("SYSCHECK")
        .setDescription("PowerStat Module")
        .setAuthor("Juno Roesler")
        .setContact("juno.rr@gmail.com")
        .setLicense("GNU/LGPL v2")
        .setHeaderComment("http://powerstat.us")
        .setYear("2013");
    
    System.out.println();
    System.out.println(sp.createHeader(40));
    
    Syscheck sys = new Syscheck();
    sp.parseArgs(args);
    
    if(sp.getOption("-h").isPresent()) {
      System.out.println(sp.createUsage());
      System.exit(0);
    }
    
    if(!sp.parseErrors()) {
      System.out.println(sp.createUsage());
      sp.printAllMessages(System.out);
      System.exit(1);
    }
    
    if(sp.getOption("-s").isPresent()) {
      if(sys.getPid() > 0) {
        Log.logger().info("Restarting Syscheck...");
        sys.deletePid();
      }
      try { Thread.sleep(2500); }
      catch(InterruptedException e) {}
      sys.startServices();
    }
    
    else if(sp.getOption("-k").isPresent()) {
      sys.deletePid();
    }
    
    else if(sp.getOption("-u").isPresent()) {
      UserDAO dao = new UserDAO();
      User u = dao.getUserFromStdin();
      if(u == null) {
        System.out.println("# Error reading user");
      } else {
        dao.saveUser(u);
        Log.logger().info("OK. User added: "+ u);
      }
    }
    
    else if(sp.getOption("-l").isPresent()) {
      UserDAO dao = new UserDAO();
      List<User> us = dao.listAll();
      System.out.println("* Number of Users: "+ us.size());
      for(User u : us)
        System.out.println("  - "+ u);
    }
    
    else if(sp.getOption("-d").isPresent()
        && sp.getOption("-e").isPresent()) {
      UserDAO dao = new UserDAO();
      User u = dao.queryUserByEmail(
          sp.getOption("-e").getArg(0));
      if(u == null) {
        System.out.println("# No user found!");
      } else {
        dao.deleteUser(u);
        Log.logger().info("* OK. User deleted: "+ u);
      }
    }
    
    else if(sp.getOption("-r").isPresent()
        && sp.getOption("-e").isPresent()) {
      try (
          BufferedReader reader = new BufferedReader(
              new InputStreamReader(System.in))) {
        
        UserDAO dao = new UserDAO();
        User u = dao.queryUserByEmail(
            sp.getOption("-e").getArg(0));
        if(u == null) {
          System.out.println("# No User found!");
        } else {
          dao.deleteUser(u);
          u.clearAccess();
          dao.addPermissions(u, reader);
          dao.saveUser(u);
          Log.logger().info("OK. User Permissions reconfigured: "+ u);
        }
      } catch(IOException e) {
        Log.logger().error(e.toString());
      }
    }
    
    else if(sp.getOption("-p").isPresent()
        && sp.getOption("-e").isPresent()) {
      UserDAO dao = new UserDAO();
      User u = dao.queryUserByEmail(
          sp.getOption("-e").getArg(0));
      if(u == null) {
        System.out.println("# No user found!");
      } else {
        System.out.println("* "+ u);
        System.out.println("* Permissions: "+ u.getAccess().size());
        for(Access a : u.getAccess())
          System.out.println("  - "+ a);
      }
    }
    
    else if(sp.getOption("-w").isPresent()
        && sp.getOption("-e").isPresent()) {
      UserDAO dao = new UserDAO();
      User u = dao.queryUserByEmail(
          sp.getOption("-e").getArg(0));
      String pass = sp.getOption("-w").getArg(0);
      if(u == null) {
        System.out.println("# Invalid e-mail: '"+ sp.getOption("-e").getArg(0));
      } 
      else {
        if(u.getPassword().validate(new Password(pass)))
          System.out.println("* OK. Password passed.");
        else
          System.out.println("# Error. Invalid Password.");
      }
    }
    
    else {
      System.out.println(sp.createUsage());
      System.out.println("# No Option informed.");
    }
  }
  
}
