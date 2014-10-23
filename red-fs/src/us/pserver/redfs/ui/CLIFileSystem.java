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

package us.pserver.redfs.ui;

import com.jpower.date.SimpleDate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.listener.SimpleListener;
import us.pserver.log.LogProvider;
import us.pserver.log.SimpleLog;
import us.pserver.redfs.IOData;
import us.pserver.redfs.RFile;
import us.pserver.redfs.RemoteFileSystem;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.container.Credentials;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/08/2014
 */
public class CLIFileSystem {

  private RemoteFileSystem rem;
  
  private SimpleLog log;
  
  
  public CLIFileSystem(RemoteFileSystem rem) {
    nullarg(RemoteFileSystem.class, rem);
    this.rem = rem;
    log = LogProvider.getSimpleLog();
  }
  
  
  public String getHostInfo() {
    try {
      return rem.getHostInfo().toString();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  private String tabl(String str, int tabsize) {
    if(str == null || tabsize < 1) return null;
    if(str.length() > tabsize) {
      return "." + str.substring((str.length() - tabsize + 1));
    }
    return str + rept(" ", (tabsize - str.length()));
  }
  
  
  private String tabr(String str, int tabsize) {
    if(str == null || tabsize < 1) return null;
    if(str.length() > tabsize) {
      return str.substring(0, tabsize);
    }
    return rept(" ", (tabsize - str.length())) + str;
  }
  
  
  private String rept(String str, int size) {
    if(str == null || size < 1)
      return "";
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < size; i++) {
      sb.append(str);
    }
    return sb.toString();
  }
  
  
  public String proto() {
    try {
      return rem.getHostInfo().toString();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String getFile(String fl) {
    if(fl == null || fl.isEmpty())
      return "# No such file ["+ fl+ "]";
    try {
      RFile rf = rem.getFile(fl);
      return format(rf);
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  

  private String format(RFile rf) {
    if(rf == null) return null;
      return (rf.isDirectory() ? "d " : "f ")
          + tabl(rf.getPath(), 35)
          + rept(" ", 3)
          + tabl(rf.getOwner(), 16)
          + rept(" ", 3)
          + tabr(rf.getSize().toString(), 12)
          + rept(" ", 3)
          + tabr(SimpleDate.from(rf.getLastModifiedDate()).toString(), 20);
  }
  
  
  private String fileHeader() {
    return rept(" ", 2)
        + tabl("File", 35)
        + tabl(" ", 3)
        + tabl("Owner", 16)
        + tabl(" ", 3)
        + tabr("Size", 12)
        + tabl(" ", 3)
        + tabr("Last Modified", 20)
        + "\n" 
        + rept(" ", 2)
        + rept("-", 35)
        + rept(" ", 3)
        + rept("-", 16)
        + rept(" ", 3)
        + rept("-", 12)
        + rept(" ", 3)
        + rept("-", 20);
  }
  
  
  public String current() {
    try {
      return rem.current().getPath();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String ls() {
    try {
      List<RFile> ls = rem.ls();
      RFile curr = rem.current();
      return format(ls, curr);
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String ls(String f) {
    if(f == null || f.isEmpty())
      return "# No such file ["+ f+ "]";
    try {
      RFile curr = new RFile(f);
      List<RFile> ls = rem.ls(f);
      return format(ls, curr);
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  private String format(List<RFile> ls, RFile curr) {
    nullarg(List.class, ls);
    nullarg(RFile.class, curr);
    StringBuffer sb = new StringBuffer();
    sb.append("ls [")
        .append(curr.getPath())
        .append("]: ")
        .append(ls.size())
        .append(" files\n")
        .append(fileHeader())
        .append("\n");
    ls.forEach(r->sb.append(format(r)).append("\n"));
    return sb.toString();
  }
  
  
  public String cd(String dir) {
    if(dir == null || dir.isEmpty())
      return "# No such directory ["+ dir+ "]";
    try {
      rem.cd(dir);
      return rem.current().getPath();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String rm(String fl) {
    if(fl == null || fl.isEmpty())
      return "# No such file ["+ fl+ "]";
    try {
      rem.rm(fl);
      return fl;
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String osrm(String fl) {
    if(fl == null || fl.isEmpty())
      return "# No such file ["+ fl+ "]";
    try {
      return rem.osrm(fl);
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String rmdir(String dir) {
    if(dir == null || dir.isEmpty())
      return "# No such directory ["+ dir+ "]";
    try {
      rem.rmdir(dir);
      return dir;
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String mkdir(String dir) {
    if(dir == null || dir.isEmpty())
      return "# Invalid directory ["+ dir+ "]";
    try {
      rem.mkdir(dir);
      return dir;
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String zip(String zip, String ... srcs) {
    if(zip == null || zip.isEmpty())
      return "# Invalid file ["+ zip+ "]";
    try {
      rem.zip(zip, srcs);
      RFile rf = rem.getFile(zip);
      if(rf == null) return "# Error creating zip file ["+ zip+ "]";
      return rf.getPath();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String unzip(String zip, String out) {
    if(zip == null || zip.isEmpty())
      return "# Invalid file ["+ zip+ "]";
    if(out == null || out.isEmpty())
      return "# Invalid file ["+ out+ "]";
    try {
      RFile rz = rem.getFile(zip);
      if(rz == null) return "# No such file ["+ zip+ "]";
      rem.unzip(zip, out);
      RFile ro = rem.getFile(out);
      if(ro == null) return "# Error unziping file ["+ out+ "]";
      return ro.getPath();
    } catch(MethodInvocationException e) {
      //log.error(e, true);
      e.printStackTrace();
      return null;
    }
  }
  
  
  public String readFile(String remote, String local) {
    if(remote == null || remote.isEmpty())
      return "# Invalid file ["+ remote+ "]";
    if(local == null || local.isEmpty())
      return "# Invalid file ["+ local+ "]";
    try {
      RFile rf = rem.getFile(remote);
      if(rf == null) return "# No such file ["+ remote+ "]";
      if(rf.isDirectory()) return "# Can not read directory ["+ remote+ "]";
      IOData data = new IOData()
          .setRFile(rf)
          .setPath(IO.p(local))
          .addListener(new SimpleListener());
      rem.readFile(data);
      return local;
    } catch(IOException | MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String writeFile(String local, String remote) {
    if(remote == null || remote.isEmpty())
      return "# Invalid file ["+ remote+ "]";
    if(local == null || local.isEmpty())
      return "# Invalid file ["+ local+ "]";
    try {
      Path p = IO.p(local);
      if(!Files.exists(p))
        return "# No such file ["+ local+ "]";
      IOData data = new IOData()
          .setPath(p)
          .setRFile(new RFile(remote))
          .addListener(new SimpleListener());
      rem.writeFile(data);
      RFile rf = rem.getFile(remote);
      if(rf == null) return "# Error writing file ["+ local+ "]";
      return format(rf);
    } catch(IOException | MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String getCRC32(String file) {
    if(file == null || file.isEmpty())
      return "# Invalid file ["+ file+ "]";
    try {
      RFile rf = rem.getFile(file);
      if(rf == null) return "# No such file ["+ file+ "]";
      StringBuffer sb = new StringBuffer()
          .append("CRC32 [")
          .append(rf.getName())
          .append("]: ")
          .append(String.valueOf(
              rem.getCRC32(rf)));
      return sb.toString();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public static void main(String[] args) {
    Credentials cr = new Credentials("juno", new StringBuffer("32132155"));
    RemoteFileSystem rfs = new RemoteFileSystem(
        new NetConnector()
            .setProxyAddress("172.24.75.19")
            .setProxyPort(6060)
            .setAddress("172.24.77.60"), 
        cr);
    
    CLIFileSystem cfs = new CLIFileSystem(rfs);
    System.out.println("* getHostInfo:");
    System.out.println("  "+ cfs.getHostInfo());
    /*
    String f = "c:/users/juno/Downloads/pic.png";
    System.out.println("* getFile ["+ f+ "]");
    System.out.println(cfs.getFile(f));
    System.out.println(cfs.getCRC32(f));
    System.out.println(cfs.ls(f));
    System.out.println("\n");
    System.out.println("* cd [.local]="+ cfs.cd(".local"));
    System.out.println(cfs.ls());
    System.out.println("* mkdir [testdir]="+ cfs.mkdir("testdir"));
    System.out.println(cfs.ls());
    System.out.println("* rmdir [testdir]="+ cfs.rmdir("testdir"));
    System.out.println(cfs.ls());
    System.out.println("* unzip [cp_resid.zip > cp_resid.pdf]="+ cfs.unzip("cp_resid.zip", "cp_resid.pdf"));
    System.out.println("* rm [cp_resid.zip]="+ cfs.rm("cp_resid.zip"));
    System.out.println(cfs.ls());
    System.out.println("* zip [cp_resid.zip, cp_resid.pdf]="+ cfs.zip("cp_resid.zip", "cp_resid.pdf"));
    System.out.println(cfs.ls());
    String f1 = "c:/.local/cp_resid.zip";
    String f2 = "c:/.local/remote/cp_resid.zip";
    System.out.println("* writeFile ["+ f1+ " >> "+ f2+ "]="+ cfs.writeFile(f1, f2));
    System.out.println("* rm [cp_resid.zip]="+ cfs.rm("cp_resid.zip"));
    System.out.println(cfs.ls());
        */
    String f1 = "c:/.local/cp_resid.zip";
    String f2 = "c:/.local/remote/cp_resid.zip";
    System.out.println("* readFile ["+ f2+ " >> "+ f1+ "]="+ cfs.readFile(f2, f1));
    System.out.println(cfs.ls());
    rfs.closeConnection();
  }
  
}
