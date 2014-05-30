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
import us.pserver.log.SLogV2;

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
  
  public static final String CMD_C = "cmd /c ";
  
  public static final String LOGFILE = "./bigjar.log";
  
  
  private String dir;
  
  private Path jarFile;
  
  private SLogV2 log;
  
  
  public BigJar(String dir) {
    Path p = Paths.get(dir).toAbsolutePath();
    if(dir == null || dir.isEmpty()
        || !Files.exists(p))
      throw new IllegalArgumentException(
          "Invalid directory ["+ p.toString()+ "]");
    this.dir = p.toString();
    jarFile = null;
    log = new SLogV2(LOGFILE);
  }
  
  
  public BigJar() {
    this(DEFAULT_DIR);
  }
  
  
  public SLogV2 getSLogV2() {
    return log;
  }
  
  
  private void createDir() throws IOException {
    Files.createDirectories(Paths.get(dir, BIGJAR));
  }
  
  
  private void deleteDir() throws IOException {
    new DirRemover(Paths.get(dir, BIGJAR))
        .remove();
  }
  
  
  private void unpackLib() {
    List<Path> list = new FileWalker(
        Paths.get(dir, LIB_DIR).toString())
        .walk();
    for(Path p : list) {
      runCommand(CD+ Paths.get(dir, BIGJAR).toString()
          + AND+ JAR_XF+ p.toString());
    }
  }
  
  
  private void deleteMetaInf() {
    new DirRemover(Paths.get(dir, BIGJAR, META_INF))
        .remove();
  }
  
  
  private void unpackJar() {
    List<Path> list = new FileWalker(dir)
        .walk();
    for(Path p : list) {
      jarFile = p;
      runCommand(CD+ Paths.get(dir, BIGJAR).toString()
          + AND+ JAR_XF+ p.toString());
    }
  }
  
  
  private void makeBigJar() throws IOException {
    Files.delete(jarFile);
    runCommand(CD+ dir+ AND+ JAR_CFM
        + jarFile + " "
        + Paths.get(dir, BIGJAR, 
            META_INF, MANIFEST).toString()
        + JAR_C_OPT+ BIGJAR+ DOT);
  }
  
  
  public void make() throws IOException {
    log.info("Creating temp dir ["+ BIGJAR+ "]");
    createDir();
    log.info("Unpacking libs...");
    unpackLib();
    log.info("Unpacking Jar File...");
    unpackJar();
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


  public String getDir() {
    return dir;
  }


  public void setDir(String dir) {
    this.dir = dir;
  }

  
  public static void main(String[] args) {
    BigJar bigjar;
    if(args != null && args.length > 0)
      bigjar = new BigJar(args[0]);
    else
      bigjar = new BigJar();
    
    System.out.println();
    System.out.println(" BIGJAR: Jar Files Unifier");
    System.out.println("---------------------------");
    try {
      bigjar.make();
      bigjar.getSLogV2().stop();
    } catch(IOException e) {
      bigjar.getSLogV2().fatal("Error creating BigJar: "+ e.toString());
      bigjar.getSLogV2().fatal(e.toString());
    }
  }
  
}
