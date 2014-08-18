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

import java.util.Objects;
import java.util.Scanner;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullstr;
import us.pserver.log.LogProvider;
import us.pserver.redfs.RemoteFileSystem;
import us.pserver.rob.NetConnector;
import us.pserver.rob.container.Credentials;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/08/2014
 */
public class InteractiveFS {
  
  public static final String
      CD = "cd",
      CURRENT = "current",
      CRC32 = "crc32",
      GETFILE = "file",
      HOSTINFO = "hostinfo",
      LS = "ls",
      MKDIR = "mkdir",
      READ = "read",
      RM = "rm",
      RMDIR = "rmdir",
      UNZIP = "unzip",
      WRITE = "write",
      ZIP = "zip";

  private CLIFileSystem cli;
  
  private RemoteFileSystem rem;
  
  private NetConnector nc;
  
  private Scanner scn;
  
  
  public InteractiveFS(NetConnector conn, Credentials crd) {
    nullarg(NetConnector.class, conn);
    nullarg(Credentials.class, crd);
    nc = conn;
    rem = new RemoteFileSystem(nc, crd);
    scn = new Scanner(System.in);
    cli = new CLIFileSystem(rem);
  }
  
  
  public String readCommand() {
    String cmd = scn.nextLine();
    if(cmd == null || cmd.length() < 2)
      cmd = scn.nextLine();
    return cmd;
  }
  
  
  public String[] parseCommand(String str) {
    nullstr(str);
    return str.split(" ");
  }
  
  
  private void print(String name, String[] ss) {
    if(ss == null) {
      System.out.println("* "+ name+ "=null");
      return;
    }
    String s = "";
    for(int i = 0; i < ss.length; i++) {
      s += ss[i]+ " ";
    }
    System.out.println("* "+ name+ "["+ ss.length+ "]="+ s);
  }
  
  
  private void checkArgs(String[] cmds, int length) {
    if(cmds == null || cmds.length < length)
      throw new IllegalArgumentException(
          "Invalid command args [cmd="
          + (cmds != null ? cmds[0] : "null")
          + ", needed="+ length
          + ", passed="+ cmds.length + "]");
  }
  
  
  public String execCommand(String[] cmd) {
    checkArgs(cmd, 1);
    switch(cmd[0]) {
      case CD:
        checkArgs(cmd, 2);
        return cli.cd(cmd[1]);
      case CURRENT:
        return cli.current();
      case CRC32:
        checkArgs(cmd, 2);
        return cli.getCRC32(cmd[1]);
      case GETFILE:
        checkArgs(cmd, 2);
        return cli.getFile(cmd[1]);
      case HOSTINFO:
        return cli.getHostInfo();
      case LS:
        if(cmd.length > 1)
          return cli.ls(cmd[1]);
        else 
          return cli.ls();
      case MKDIR:
        checkArgs(cmd, 2);
        return cli.mkdir(cmd[1]);
      case READ:
        checkArgs(cmd, 3);
        return cli.readFile(cmd[1], cmd[2]);
      case RM: 
        checkArgs(cmd, 2);
        return cli.rm(cmd[1]);
      case RMDIR:
        checkArgs(cmd, 2);
        return cli.rmdir(cmd[1]);
      case UNZIP: 
        checkArgs(cmd, 3);
        return cli.unzip(cmd[1], cmd[2]);
      case WRITE:
        checkArgs(cmd, 3);
        return cli.writeFile(cmd[1], cmd[2]);
      case ZIP:
        checkArgs(cmd, 3);
        return cli.zip(cmd[1], cmd[2]);
      default:
        throw new IllegalArgumentException(
            "Invalid command ["+ cmd[0]+ "]");
    }
  }
  
  
  public void showHelp() {
    System.out.println("* Commands Available:");
    System.out.println("  - cd <dir>     : Change directory");
    System.out.println("  - current      : Return current directory");
    System.out.println("  - crc32 <file> : Return CRC32 for the remote file");
    System.out.println("  - file <file>  : Return file info");
    System.out.println("  - hostinfo     : Return remote host info");
    System.out.println("  - ls [dir]     : List contents of directory");
    System.out.println("  - mkdir <dir>  : Create new directory");
    System.out.println("  - read <r> <l> : Download the remote file to the local file");
    System.out.println("  - rm <file>    : Remove the remote file");
    System.out.println("  - rmdir <dir>  : Remove the remote directory");
    System.out.println("  - unzip <z> <o>: Unzip the file to the output file/dir");
    System.out.println("  - write <l> <r>: Upload the local file to the remote file");
    System.out.println("  - zip <o> <s>  : Compress into zip file remote files/dirs");
    System.out.println();
  }
  
  
  public void run() {
    String out = "redfs["
        + nc.getAddress()
        + "](";
    while(true) {
      System.out.print(out+ cli.current()+ ")> ");
      String cmd = readCommand();
      if(cmd.equalsIgnoreCase("help"))
        showHelp();
      else if(cmd.equalsIgnoreCase("exit"))
        break;
      else
        try {
          System.out.println(
              execCommand(parseCommand(cmd)));
        } catch(Exception e) {
          LogProvider.getSimpleLog().error(e, true);
        }
    }
    System.out.println("* exit now!");
    System.exit(0);
  }
  
  
  public static InteractiveFS login() {
    try {
      Scanner sc = new Scanner(System.in);
      System.out.println("* Login on remote host:");
      System.out.print("  Remote address [addr port]: ");
      String addr = sc.next();
      int port = sc.nextInt();
      sc.nextLine();
      System.out.println("* Connect on ["+ addr+ ":"+ port+ "]");
      NetConnector nc = new NetConnector(addr, port);
      System.out.print("  Use proxy [y/n]? ");
      String use = sc.nextLine();
      if(use.equalsIgnoreCase("y")) {
        System.out.print("  Proxy address [addr port]: ");
        addr = sc.next();
        port = sc.nextInt();
        sc.nextLine();
        nc.setProxyAddress(addr)
            .setProxyPort(port);
      }
      System.out.println("* Credentials:");
      System.out.print("  User name: ");
      String usr = sc.nextLine();
      System.out.print("  Password : ");
      Credentials cred = new Credentials(usr, 
          new StringBuffer(sc.nextLine()));
      System.out.println("* Connecting to remote ("+ nc+ ")...");
      return new InteractiveFS(nc, cred);
    } catch(Exception e) {
      LogProvider.getSimpleLog().error(e, true);
      return null;
    }
  }
  
  
  public static void main(String[] args) {
    //InteractiveFS ifs = login();
    NetConnector nc = new NetConnector()
        .setAddress("172.24.77.60")
        .setProxyAddress("172.24.75.19")
        .setProxyPort(6060);
    Credentials cred = new Credentials("juno", new StringBuffer("32132155"));
    InteractiveFS ifs = new InteractiveFS(nc, cred);
    if(ifs == null) return;
    ifs.run();
  }
  
}
