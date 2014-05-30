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

package com.jpower.tn3270.script;

import com.jpower.tn3270.script.executor.Executor;
import com.jpower.tn3270.Session;
import com.jpower.tn3270.script.executor.FileGetExecutor;
import com.jpower.tn3270.script.parser.CommandParser;
import com.jpower.tn3270.script.parser.WhileParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class ScriptProcessor {
  
  public static final String 
      VAR_TN3270_HOST = "tn3270.host",
      VAR_TN3270_PORT = "tn3270.port";
  
  public static final String DEFAULT_HOST = "172.17.78.220";

  public static final int DEFAULT_PORT = 8023;

  
  private VarMemory mem;
  
  private Session sess;
  
  private boolean verbose;
  
  private boolean simulate;
  
  private boolean simerrors;
  
  private FileGetExecutor fgetx;
  
  private PrintStream out;
  
  
  public ScriptProcessor() {
    mem = VarMemory.getInstance();
    mem.put(new Var(VAR_TN3270_HOST, DEFAULT_HOST))
        .put(new Var(VAR_TN3270_PORT, DEFAULT_PORT));
    sess = new Session();
    verbose = false;
    simulate = false;
    simerrors = false;
    setStdout(System.out);
  }
  
  
  public ScriptProcessor(PrintStream stdout) {
    this();
    setStdout(stdout);
  }
  
  
  public ScriptProcessor(Session s) {
    this();
    sess = s;
  }


  public boolean isVerbose() {
    return verbose;
  }


  public ScriptProcessor setVerbose(boolean verbose) {
    this.verbose = verbose;
    return this;
  }


  public boolean isSimulate() {
    return simulate;
  }


  public ScriptProcessor setSimulate(boolean simulate) {
    this.simulate = simulate;
    if(simulate) verbose = true;
    return this;
  }
  
  
  public boolean hasSimulationErrors() {
    return simerrors;
  }


  public Session getSession() {
    return sess;
  }


  public ScriptProcessor setSession(Session sess) {
    if(sess != null)
      this.sess = sess;
    return this;
  }


  public PrintStream getStdout() {
    return out;
  }


  public ScriptProcessor setStdout(PrintStream out) {
    if(out != null) {
      this.out = out;
      Executor.stdout = out;
    }
    return this;
  }
  
  
  private String removeStartSpaces(String str) {
    if(str == null || str.isEmpty())
      return str;
    
    char[] cs = str.toCharArray();
    int i = 0;
    for(i = 0; i < cs.length; i++) {
      if(cs[i] == ' ') continue;
      else break;
    }
    return str.substring(i);
  }
  
  
  private Command getCommand(String str) {
    if(str.startsWith(" "))
      str = this.removeStartSpaces(str);
    
    CommandParser cp = CommandParser.getInstanceFor(str);
    if(cp == null || cp.parse(str) == null)
      throw new IllegalArgumentException(
          "\nInvalid Command: '"+ str+ "'\n");
    
    return cp.parse(str);
  }
  
  
  private Executor getExecutor(Command cmd) {
    Executor e = Executor.getInstanceFor(cmd.getType());
    if(e instanceof FileGetExecutor) {
      if(fgetx == null) fgetx = (FileGetExecutor) e;
      else e = fgetx;
    }
    if(e == null)
      throw new IllegalArgumentException(
          "\nInvalid Command: "+ cmd.toString()+ "\n");
    return e;
  }
  
  
  private int execWhile(Command cmd, String[] commands, int idx) {
    if(cmd == null
        || commands == null
        || commands.length < 1
        || idx < 0
        || idx >= commands.length)
      return idx;
    
    Executor cond = this.getExecutor(cmd);
    if(cond == null) return idx;
    
    int origIdx = idx;
    int endIdx = 0;
    int nwhile = 0;
    
    for(int i = idx+1; (endIdx == 0 && i < commands.length); i++) {
      Command end = this.getCommand(commands[i]);
      if(end.getType() == CommandType.WHILE)
        nwhile++;
      else if(end.getType() == CommandType.ENDWHILE)
        if(nwhile == 0) endIdx = i;
        else nwhile--;
    }
    
    if(endIdx == 0) return idx;
    
    int size = (endIdx - origIdx) - 1;
    String[] cmds = new String[size];
    System.arraycopy(commands, origIdx + 1, cmds, 0, size);
    WhileParser wp = new WhileParser();
    
    while(cond.exec(cmd, sess)) {
      process(cmds);
      if(simulate) break;
      cmd = wp.parse(commands[origIdx]);
    }
    
    return endIdx;
  }
  
  
  private boolean exec(Executor e, Command c) {
    if(e == null || c == null)
      return false;
    
    boolean success = (!simulate ? e.exec(c, sess) : true);
    
    if(verbose && c.getType() != CommandType.PRINT)
      out.println("* Command ("+ c.toString()
          + ") successful? "+ success);
    else if(c.getType() != CommandType.IF
        && c.getType() != CommandType.WHILE
        && !success)
      out.println("* Command ("+ c.toString()
          + ") successful? "+ success);
    
    simerrors = !success || simerrors;
    return success;
  }
  
  
  public ScriptProcessor process(String[] commands) {
    if(commands == null || commands.length < 1)
      return this;
    
    simerrors = false;
    boolean ifok = false;
    boolean skip = false;
    for(int i = 0; i < commands.length; i++) {
      
      if(commands[i] != null && commands[i].contains("#"))
        continue;
      
      Command cmd = this.getCommand(commands[i]);
      if(cmd == null) continue;
      
      if(skip) {
        System.out.println("* SKIP: "+ cmd);
        if(cmd.getType() == CommandType.END)
          skip = false;
        continue;
      }
      
      if(cmd.getType() == CommandType.IF) {
        ifok = this.exec(this.getExecutor(cmd), cmd);
        if(!ifok) skip = true;
      }
      else if(cmd.getType() == CommandType.ELSE) {
        skip = ifok;
      }
      else if(cmd.getType() == CommandType.WHILE) {
        i = this.execWhile(cmd, commands, i);
      }
      else if(cmd.getType() != CommandType.END) {
        this.exec(this.getExecutor(cmd), cmd);
      }
    }//for
    
    return this;
  }
  
  
  public String[] readScript(File file) {
    if(file == null || !file.exists()) 
      return null;
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      List<String> list = new LinkedList<>();
      String line = null;
    
      while((line = br.readLine()) != null) {
        if(!line.equals("\n") && !line.trim().isEmpty())
          list.add(line);
      }
      String[] str = new String[list.size()];
      br.close();
      return list.toArray(str);
      
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public static void main(String[] args) {
    ScriptProcessor sp = new ScriptProcessor();
    sp.setVerbose(true).setStdout(System.out);
    //File f = new File("/media/storage/apps/tn3270s/CapturarCPF.txt");
    File f = new File("d:/cons_inv_agric.txt");
    System.out.println("* f = "+ f);
    System.out.println("* f.exists?: "+ f.exists());
    sp.setSimulate(false);
    sp.process(sp.readScript(f));
    sp.getSession().close();
  }
  
}
