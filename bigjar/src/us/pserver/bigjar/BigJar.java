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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
  
  public static final String META_INF = "META-INF";
  
  public static final String MANIFEST = "MANIFEST.MF";
  
  public static final String JAR_CFM = "jar cfm ";
  
  public static final String JAR_XF = "jar xf ";
  
  public static final String JAR_C_OPT = " -C ";
  
  public static final String AND = " & ";
  
  public static final String CD = "cd ";
  
  public static final String DOT = " .";
  
  public static final String SPACE = " ";
  
  public static final String SLASH = "/";
  
  public static final String CMD_C = "cmd /c ";
  
  public static final String LOGFILE = "./bigjar.log";
  
  
  private String dir;
  
  private Path jarFile;
  
  private Path tempDir;
  
  private SimpleLog log;
  
  
  public BigJar(String dir) {
    Path p = Paths.get(dir).toAbsolutePath();
    if(dir == null || dir.isEmpty()
        || !Files.exists(p))
      throw new IllegalArgumentException(
          "Invalid directory ["+ p.toString()+ "]");
    this.dir = p.toString();
    jarFile = null;
    tempDir = Paths.get(dir, BIGJAR).toAbsolutePath();
    log = new SimpleLog(LOGFILE);
  }
  
  
  public BigJar() {
    this(DEFAULT_DIR);
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
  
  
  private void deleteMetaInf() {
    new DirRemover(tempDir
        .resolve(META_INF)).remove();
  }
  
  
  private void checkJars(List<Path> jars) {
    if(jars == null || jars.isEmpty()
        || jarFile == null)
      throw new IllegalStateException(
          "Unnable to find jar files in directory ["+ dir+ "]");
  }
  
  
  private void unpackJars() {
    FileWalker fw = new FileWalker(dir);
    List<Path> list = fw.walk();
    jarFile = fw.getMainJar();
    this.checkJars(list);
    
    list.forEach(this::unpackJar);
    unpackJar(jarFile);
  }
  
  
  public void unpackJar(Path p) {
    StringBuilder cmd = new StringBuilder()
        .append(CD)
        .append(tempDir.toString())
        .append(AND)
        .append(JAR_XF)
        .append(p.toString());
    runCommand(cmd.toString());
  }
  
  
  private void makeBigJar() throws IOException {
    Files.delete(jarFile);
    if(Files.exists(jarFile))
      throw new IOException(
          "Unnable to delete jar file ["+ jarFile.toString()+ "]");
    StringBuilder cmd = new StringBuilder()
        .append(CD)
        .append(dir)
        .append(AND)
        .append(JAR_CFM)
        .append(jarFile.getFileName().toString())
        .append(SPACE)
        .append(BIGJAR).append(SLASH)
        .append(META_INF).append(SLASH)
        .append(MANIFEST)
        .append(JAR_C_OPT)
        .append(BIGJAR)
        .append(DOT);
    runCommand(cmd.toString());
  }
  
  
  public void make() throws IOException {
    log.info("Creating temp dir ["+ BIGJAR+ "]");
    createDir();
    log.info("Unpacking Jar Files...");
    unpackJars();
    log.info("Creating BigJar ["+ jarFile.toString()+ "]...");
    makeBigJar();
    log.info("Cleaning up...");
    deleteDir();
    log.info("Done!");
  }
  
  
  private void runCommand(String cmd) {
    try {
      Process p = Runtime.getRuntime().exec(CMD_C+ cmd);
      dump(p.getInputStream());
      p.waitFor();
    } catch(IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  
  private void dump(InputStream in) throws IOException {
    byte[] bs = new byte[128];
    int read = 1;
    while((read = in.read(bs)) > 0) {
      System.out.write(bs, 0, read);
    }
  }
  
  
  public String getBaseDirectory() {
    return dir;
  }


  public void setBaseDirectory(String dir) {
    this.dir = dir;
  }

  
  public static void main(String[] args) {
    BigJar bigjar;
    if(args != null && args.length > 0)
      bigjar = new BigJar(args[0]);
    else
      bigjar = new BigJar();
    
    System.out.println();
    System.out.println("    BIGJAR: Jar Files Unifier");
    System.out.println("---------------------------------");
    System.out.println(" Copyright (C) 2014 Juno Roesler");
    System.out.println();
    try {
      bigjar.make();
    } catch(IOException e) {
      bigjar.getSimpleLog().fatal("Error creating BigJar: "+ e.toString());
      bigjar.getSimpleLog().fatal(e.toString());
    }
  }
  
}
