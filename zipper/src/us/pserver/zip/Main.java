/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zip;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Iterator;



/**
 *
 * @author juno
 */
public class Main {
  
  
  public static void doOp(Zipper zip, String arg, ShellParser sp, boolean doZip) {
    System.out.println("* "+ (doZip ? "Zipping" : "Unzipping")+ "...");
    
    String files[] = arg.split(",");
    if(files == null || files.length == 0)
      files = new String[] {arg};
    for(String f : files) {
      zip.add(f);
    }
    
    if(sp.getOption("-o").isPresent()) {
      zip.setOutput(Paths.get(
          sp.getOption("-o").getArg(0)));
    }
    
    if(doZip) zip.safeZip();
    else zip.safeUnzip();
    System.out.println("\n* Done!");
  }
  
  
  public static String fixSize(String str, int size) {
    if(size < 1) return str;
    if(str == null) str = "";
    int len = str.length();
    if(len <= size) return str;
    
    return str.substring(len - size);
  }
  
  
  public static void main(String[] args) {
    ShellParser sp = new ShellParser();
    Zipper zip = new Zipper();
    TextProgressBar pbar = new TextProgressBar();
    
    Option z = new Option("-z")
        .setLongName("--zip")
        .setArgsSeparator("=")
        .setAcceptArgs(true)
        .setExclusive(false)
        .setMandatory(false)
        .setDescription("Compress the especified files (\",\" separated)");
    
    Option u = new Option("-u")
        .setLongName("--unzip")
        .setArgsSeparator("=")
        .setAcceptArgs(true)
        .setExclusive(false)
        .setMandatory(false)
        .setDescription("Uncompress the especified files (\",\" separated)");
    
    Option o = new Option("-o")
        .setLongName("--output")
        .setArgsSeparator("=")
        .setAcceptArgs(true)
        .setExclusive(false)
        .setMandatory(false)
        .setDescription("File/Directory output for operation");
    
    Option p = new Option("-p")
        .setLongName("--progress")
        .setAcceptArgs(false)
        .setExclusive(false)
        .setMandatory(false)
        .setDescription("Show progress bar");
    
    Option v = new Option("-v")
        .setLongName("--verbose")
        .setAcceptArgs(false)
        .setExclusive(false)
        .setMandatory(false)
        .setDescription("Show processed files");
    
    Option l = new Option("-l")
        .setLongName("--list")
        .setArgsSeparator("=")
        .setAcceptArgs(true)
        .setExclusive(false)
        .setMandatory(false)
        .setDescription("List entries from zip file");
    
    Option h = new Option("-h")
        .setLongName("--help")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false)
        .setDescription("Show this help text");
    
    sp.setAppName("ZIPPER")
        .setAuthor("Juno Roesler")
        .setContact("juno@pserver.us")
        .setDescription("Java Zip utility")
        .setLicense("GNU/LGPL")
        .setYear("2013")
        .addOption(z)
        .addOption(u)
        .addOption(o)
        .addOption(p)
        .addOption(v)
        .addOption(l)
        .addOption(h);
    
    System.out.println(sp.createHeader(35));
    sp.parseArgs(args);
    
    if(sp.getOption("-h").isPresent()) {
      System.out.println(sp.createUsage());
      System.exit(0);
    }
    
    if(!sp.getOption("-z").isPresent() 
        && !sp.getOption("-u").isPresent()
        && !sp.getOption("-l").isPresent()) {
      System.out.println(sp.createUsage());
      System.out.println("# Required missing option (-z/-u/-l).");
      System.exit(1);
    }
    
    if(sp.getOption("-v").isPresent()) {
      pbar.setShowProcessedFiles(true);
    }
    
    if(sp.getOption("-p").isPresent()) {
      zip.addListener(pbar);
    }
    
    if(sp.getOption("-z").isPresent()) {
      String arg = sp.getOption("-z").getArg(0);
      doOp(zip, arg, sp, true);
    }
    
    else if(sp.getOption("-u").isPresent()) {
      String arg = sp.getOption("-u").getArg(0);
      doOp(zip, arg, sp, false);
    }
    
    else if(sp.getOption("-l").isPresent()) {
      String arg = sp.getOption("-l").getArg(0);
      String files[] = arg.split(",");
      if(files == null || files.length == 0)
        files = new String[] {arg};
      for(String f : files) {
        zip.add(f);
      }
      
      Iterator<Path> pi = zip.paths().iterator();
      while(pi.hasNext()) {
        Path pt = pi.next();
        System.out.println("* Listing entries...\n");
        System.out.println("["+pt+"]");
        zip.searchZipEntries();
        zip.remove(pt);
        DecimalFormat df = new DecimalFormat("#,###");
        for(int i = 0; i < zip.zipEntries().size(); i++) {
          ZipInfo zi = zip.zipEntries().get(i);
          System.out.printf("%3d. %40s", i+1, fixSize(zi.getEntry().getName(), 40));
          System.out.printf(" %18s\n", df.format(
              zi.getEntry().getCompressedSize())+ " bytes");
        }
      }
    }
  }
  
}
