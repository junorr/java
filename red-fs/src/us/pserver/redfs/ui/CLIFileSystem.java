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

import java.util.List;
import us.pserver.log.LogProvider;
import us.pserver.log.SimpleLog;
import us.pserver.redfs.RemoteFile;
import us.pserver.redfs.RemoteFileSystem;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.container.Credentials;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/08/2014
 */
public class CLIFileSystem {

  private RemoteFileSystem rem;
  
  private SimpleLog log;
  
  
  public CLIFileSystem(RemoteFileSystem rem) {
    //nullarg(RemoteFileSystem.class, rem);
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
  
  
  public String getFile(String rpt) {
    try {
      RemoteFile rf = rem.getFile(rpt);
      if(rf == null) return "# No such file ["+ rpt+ "]";
      return format(rf);
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  private String format(RemoteFile rf) {
    if(rf == null) return null;
      return tabl(rf.getPath(), 35)
          + rept(" ", 3)
          + tabl(rf.getOwner(), 16)
          + rept(" ", 3)
          + tabr(rf.getSize().toString(), 12)
          + rept(" ", 3)
          + tabl(rf.getLastModifiedDate().toString(), 28);
  }
  
  
  private String fileHeader() {
    return tabl("File", 35)
        + tabl(" ", 3)
        + tabl("Owner", 16)
        + tabl(" ", 3)
        + tabl("Size", 12)
        + tabl(" ", 3)
        + tabl("Modified", 28)
        + "\n" 
        + rept("-", 35)
        + rept(" ", 3)
        + rept("-", 16)
        + rept(" ", 3)
        + rept("-", 12)
        + rept(" ", 3)
        + rept("-", 28);
  }
  
  
  public String current() {
    try {
      return format(rem.current());
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String ls() {
    try {
      StringBuffer sb = new StringBuffer();
      List<RemoteFile> ls = rem.ls();
      sb.append("ls [")
          .append(rem.current().getPath())
          .append("]: ")
          .append(ls.size())
          .append(" files\n")
          .append(fileHeader())
          .append("\n");
      ls.forEach(r->sb.append(format(r)).append("\n"));
      return sb.toString();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String ls(String f) {
    if(f == null)
      return "# No such file ["+ f+ "]";
    try {
      StringBuffer sb = new StringBuffer();
      List<RemoteFile> ls = rem.ls(f);
      sb.append("ls [")
          .append(rem.current().getPath())
          .append("]: ")
          .append(ls.size())
          .append(" files\n")
          .append(fileHeader())
          .append("\n");
      ls.forEach(r->sb.append(format(r)).append("\n"));
      return sb.toString();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public String proto2() {
    try {
      return rem.getHostInfo().toString();
    } catch(MethodInvocationException e) {
      log.error(e, true);
      return null;
    }
  }
  
  
  public static void main(String[] args) {
    Credentials cr = new Credentials("juno", new StringBuffer("32132155"));
    RemoteFileSystem rfs = new RemoteFileSystem(
        //new NetConnector().setAutoCloseConnetcion(false));
        new NetConnector()
            //.setProxyAddress("172.24.75.19")
            //.setProxyPort(6060)
            .setAddress("172.24.77.60"), cr);
    
    CLIFileSystem cfs = new CLIFileSystem(rfs);
    System.out.println("* getHostInfo = "+ cfs.getHostInfo());
    String f = "c:/users/juno/Downloads/pic.png";
    System.out.println("* getFile ["+ f+ "]");
    System.out.println(cfs.getFile(f));
    System.out.println(cfs.ls());
    System.out.println(cfs.ls(f));
    rfs.closeConnection();
  }
  
}
