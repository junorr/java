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

package com.jpower.tn3270.script.executor;

import com.jpower.tn3270.Session;
import com.jpower.tn3270.script.Command;
import com.jpower.tn3270.script.CommandType;
import com.jpower.tn3270.script.Var;
import com.jpower.tn3270.script.VarMemory;
import com.jpower.tn3270.script.parser.CommandParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class FileGetExecutor extends Executor {
  
  public static final String STR_FILE_VAR = "FileGetExecutor.FILE";
  
  public static final FileGetExecutor instance = 
      new FileGetExecutor();

  
  private BufferedReader br;
  
  private File file;
  
  private String[] line;
  
  private int idx = 0;
  

  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(cmd.getType() != CommandType.FILE_GET) 
      return false;
    
    Var fvar = VarMemory.getInstance().get(cmd.getArg(0));
    if(fvar == null) fvar = new Var(STR_FILE_VAR, cmd.getArg(0));
    Var var = VarMemory.getInstance().get(cmd.getArg(1));
    if(var == null) var = new Var(cmd.getArg(1));
    Var param = VarMemory.getInstance().get(cmd.getArg(2));
    
    try {
      File f = new File(fvar.value());
      boolean updated = (file == null || !file.equals(f));
      if(updated) {
        file = f;
        br = new BufferedReader(new FileReader(file));
      }
      
      if(!updated && line != null && idx < line.length) {
        var.setValue(line[idx++]);
        return true;
      }
      
      String ln = br.readLine();
      if(ln == null) {
        var.setValue("EOF");
        return true;
      }
      
      if(param != null && !param.value().equals("\n")) {
        line = ln.split(param.value());
        idx = 0;
        if(line == null || line.length < 1)
          return false;
        var.setValue(line[idx++]);
      }
      else {
        var.setValue(ln);
      }
      
      VarMemory.getInstance().put(var);
      return true;
      
    } catch(IOException e) {
      return false;
    }
  }

  
  public static void main(String[] args) {
    FileGetExecutor fget = FileGetExecutor.instance;
    
    String sc = "fileget d:/apps/tn3270s/CPF.txt fg CSV";
    CommandParser cp = CommandParser.getInstanceFor(sc);
    Command cmd = cp.parse(sc);
    Executor ex = Executor.getInstanceFor(cmd.getType());
    ex.exec(cmd, null);
    System.out.println("* "+ VarMemory.getInstance().get("fg"));
    ex.exec(cmd, null);
    System.out.println("* "+ VarMemory.getInstance().get("fg"));
    ex.exec(cmd, null);
    System.out.println("* "+ VarMemory.getInstance().get("fg"));
    ex.exec(cmd, null);
    System.out.println("* "+ VarMemory.getInstance().get("fg"));
  }
  
}
