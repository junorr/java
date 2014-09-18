/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.bigjar;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import us.pserver.log.SimpleLog;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/01/2014
 */
public class BigJar {
  
  public static final String DEFAULT_DIR = "dist";
  
  public static final String LIB_DIR = "lib";
  
  public static final String BIGJAR = "bigjar";
  
  public static final String SPLASH = "SplashScreen-Image";
  
  public static final String COMMENT_KEY = "Packaged-By";
  
  public static final String COMMENT_VALUE = "BigJar - Jar Files Unifier";
  
  public static final String LOGFILE = "./bigjar.log";
  
  
  private String dir;
  
  private Path jarFile;
  
  private Path tempDir;
  
  private SimpleLog log;
  
  private Attributes attr, fattr;
  
  private String splash;
  
  private String custom;
  
  
  public BigJar(String dir) {
    Path p = Paths.get(dir).toAbsolutePath();
    if(dir == null || dir.isEmpty()
        || !Files.exists(p))
      throw new IllegalArgumentException(
          "Invalid directory ["+ p.toString()+ "]");
    this.dir = p.toString();
    jarFile = null;
    attr = null;
    fattr = null;
    custom = null;
    tempDir = Paths.get(dir, BIGJAR).toAbsolutePath();
    log = new SimpleLog(LOGFILE);
  }
  
  
  public BigJar() {
    this(DEFAULT_DIR);
  }
  
  
  public String getSplashScreen() {
    return splash;
  }
  
  
  public BigJar setSplashScreen(String str) {
    if(str == null || str.trim().isEmpty())
      throw new IllegalArgumentException("Invalid splash screen ["+ str+ "]");
    splash = str;
    return this;
  }
  
  
  public String getMFCustomAttributes() {
    return custom;
  }
  
  
  public BigJar setMFAttributesFile(String file) {
    if(file == null || file.isEmpty())
      return this;
    try (
        BufferedReader br = new BufferedReader(
            new FileReader(file));
        ) {
      fattr = new Attributes();
      while(true) {
        String line = br.readLine();
        if(line == null) break;
        if(line.contains(": ")) {
          String[] pair = line.split(": ");
          log.info("Setting MF Attribute {"+ pair[0]+ "="+ pair[1]+ "}");
          fattr.putValue(pair[0], pair[1]);
        }
      }
    }
    catch(IOException e) {
      log.error("Error setting file attributes: "+ e.getMessage());
    }
    return this;
  }  
  
  
  public BigJar setMFCustomAttributes(String str) {
    if(str == null || str.isEmpty() 
        || !str.contains(":"))
      throw new IllegalArgumentException(
          "Invalid attributes ["+ str+ "]");
    custom = str;
    return this;
  }
  
  
  private void putCustomAttributes(Manifest mf) {
    if(mf == null || custom == null)
      return;
    
    log.info("Setting Custom Manifest Attributes...");
    if(custom.contains(",")) {
      String[] ats = custom.split(",");
      for(String s : ats) {
        String[] at = s.split(":");
        mf.getMainAttributes().putValue(at[0], at[1]);
      }
    }
    else {
      String[] at = custom.split(":");
      mf.getMainAttributes().putValue(at[0], at[1]);
    }
  }
  
  
  public SimpleLog getSimpleLog() {
    return log;
  }
  
  
  private void createDir() throws IOException {
    Files.createDirectories(tempDir);
    if(!Files.exists(tempDir))
      throw new IOException(
          "Error creating directory ["+ tempDir+ "]");
  }
  
  
  private void deleteDir() throws IOException {
    new DirRemover(tempDir).remove();
  }
  
  
  private void checkJars(List<Path> jars) {
    if(jars == null || jars.isEmpty()
        || jarFile == null)
      throw new IllegalStateException(
          "Unnable to find jar files in directory ["+ dir+ "]");
  }
  
  
  private void unpackJars() {
    JarWalker fw = new JarWalker(dir);
    List<Path> list = fw.walk();
    jarFile = fw.getMainJar();
    this.checkJars(list);
    
    for(Path p : list) 
      unpackJar(p);
    unpackJar(jarFile);
  }
  
  
  public void unpackJar(Path p) {
    if(p == null || !Files.exists(p))
      throw new IllegalArgumentException("Invalid jar path ["+ p+ "]");
    
    try {
      JarInputStream jis = new JarInputStream(
          Files.newInputStream(p, 
              StandardOpenOption.READ));
      
      if(p == jarFile) {
        attr = jis.getManifest().getMainAttributes();
      }
      
      ZipEntry ze = jis.getNextEntry();
      while(ze != null) {
        Path dst = tempDir.resolve(ze.getName());
        if(ze.isDirectory()) {
          if(Files.exists(dst) && !Files.isDirectory(dst)) {
            log.warning("Deleting duplicate file: "+ dst.getFileName()+ "...");
            Files.delete(dst);
          }
          Files.createDirectories(dst);
        }
        else {
          if(!Files.exists(dst.getParent()))
            Files.createDirectories(dst.getParent());
          OutputStream os = Files.newOutputStream(dst, 
              StandardOpenOption.CREATE, 
              StandardOpenOption.WRITE);
          transfer(jis, os);
          os.flush();
          os.close();
        }
        jis.closeEntry();
        ze = jis.getNextEntry();
      }
      
    } catch(NoSuchFileException e) {
      log.error("Error extracting file: "+ e.toString());
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  void transfer(InputStream is, OutputStream os) throws IOException {
    byte[] bs = new byte[512];
    int r = 0;
    while((r = is.read(bs)) > 0) {
      os.write(bs, 0, r);
    }
  }
  
  
  private void makeBigJar() throws IOException {
    FileWalker fw = new FileWalker(tempDir);
    List<String> ents = fw.walk();
    
    try {
      Manifest mf = new Manifest();
      mf.getMainAttributes().putAll(attr);
      if(fattr != null)
        mf.getMainAttributes().putAll(fattr);
      mf.getMainAttributes().putValue(COMMENT_KEY, COMMENT_VALUE);
      if(splash != null) {
        log.info("SplashScreen-Image setted: "+ splash);
        mf.getMainAttributes().putValue(SPLASH, splash);
      }
      if(custom != null) {
        putCustomAttributes(mf);
      }
      
      JarOutputStream jos = new JarOutputStream(
          Files.newOutputStream(jarFile, 
              StandardOpenOption.CREATE, 
              StandardOpenOption.WRITE), mf);
      jos.setMethod(JarOutputStream.DEFLATED);
      jos.setLevel(8);
      
      for(String se : ents) {
        ZipEntry e = new ZipEntry(se);
        try {
          jos.putNextEntry(e);
        } catch(ZipException ze) {
          log.warning("Skiping existing entry: "+ se+ "...");
          continue;
        }
        if(!e.isDirectory()) {
          InputStream is = Files.newInputStream(
              tempDir.resolve(e.getName()), 
              StandardOpenOption.READ);
          transfer(is, jos);
          is.close();
        }
        jos.closeEntry();
      }
      
      jos.flush();
      jos.close();
      
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public void make() throws IOException {
    log.info("Creating temp dir ["+ BIGJAR+ "]");
    createDir();
    log.info("Unpacking Jar Files...");
    unpackJars();
    log.info("Creating BigJar ["
        + (jarFile.getParent() != null 
            ? jarFile.getParent()
                .getFileName().toString()+ "/" : "")
        + jarFile.getFileName().toString()+ "]...");
    makeBigJar();
    log.info("Cleaning up...");
    deleteDir();
    log.info("Done!");
  }
  
  
  public String getBaseDirectory() {
    return dir;
  }


  public void setBaseDirectory(String dir) {
    this.dir = dir;
  }

  
  public static void main(String[] args) {
    //args = new String[] { "c:/.local/java/j3270/dist" };
    ShellParser sp = new ShellParser();
    sp.setAppName("BIGJAR")
        .setAuthor("Juno Roesler")
        .setContact("juno@pserver.us")
        .setDescription("Jar Files Unifier")
        .setLicense("GNU/LGPL v3")
        .setYear("2014");
    
    Option opt = new Option()
        .setName("-h")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false)
        .setLongName("--help")
        .setDescription("Show this help text");
    sp.addOption(opt);
    
    opt = new Option()
        .setName("-s")
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setExclusive(false)
        .setMandatory(false)
        .setLongName("--splash")
        .setDescription("Specify splash screen image (path inside jar)");
    sp.addOption(opt);
    
    opt = new Option()
        .setName("-a")
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setExclusive(false)
        .setMandatory(false)
        .setLongName("--attributes")
        .setDescription("Custom attributes for Manifest (key1:val1,key2:val2,...)");
    sp.addOption(opt);
    
    opt = new Option()
        .setName("-m")
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setExclusive(false)
        .setMandatory(false)
        .setLongName("--manifest-adds")
        .setDescription("Copy custom attributes from <file> to Manifest");
    sp.addOption(opt);
    
    opt = Option.EMPTY_OPTION
        .setAcceptArgs(true)
        .setExclusive(false)
        .setMandatory(false)
        .setDescription("Directory with jars and libs to be unified");
    sp.addOption(opt);
    
    System.out.println(sp.createHeader(35));
    String usage = sp.createUsage();
    sp.parseArgs(args);
    if(!sp.parseErrors()) {
      System.out.println(usage);
      sp.printAllMessages(System.err);
      System.exit(1);
    }
    
    if(sp.isOptionPresent("-h")) {
      System.out.println(usage);
      System.exit(0);
    }
    
    BigJar bg;
    
    if(sp.isOptionPresent(Option.EMPTY)) {
      bg = new BigJar(sp.getOption(
          Option.EMPTY).getFirstArg());
    }
    else {
      bg = new BigJar();
    }
    
    if(sp.isOptionPresent("-s")) {
      bg.setSplashScreen(
          sp.getOption("-s").getFirstArg());
    }
    
    if(sp.isOptionPresent("-a")) {
      bg.setMFCustomAttributes(
          sp.getOption("-a").getFirstArg());
    }
    
    if(sp.isOptionPresent("-m")) {
      bg.getSimpleLog().info("File Attributes Detected");
      bg.setMFAttributesFile(
          sp.getOption("-m").getFirstArg());
    }
    
    try {
      bg.make();
    } catch(IOException e) {
      bg.getSimpleLog().fatal("Error creating BigJar: "+ e.toString());
      bg.getSimpleLog().fatal(e.toString());
    }
  }
  
}
