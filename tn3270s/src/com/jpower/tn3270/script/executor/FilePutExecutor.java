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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class FilePutExecutor extends Executor {
  
  public static final String STR_FILE_VAR = "FilePutExecutor.FILE";
  
  public static final FilePutExecutor instance = 
      new FilePutExecutor();

  
  private PrintStream ps;
  
  private File file;
  

  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(cmd.getType() != CommandType.FILE_PUT) 
      return false;
    
    Var fvar = VarMemory.getInstance().get(cmd.getArg(0));
    if(fvar == null) fvar = new Var(STR_FILE_VAR, cmd.getArg(0));
    
    Var var = VarMemory.getInstance().get(cmd.getArg(1));
    if(var == null) var = Var.TEMP.setValue(cmd.getArg(1));
    
    Var param = VarMemory.getInstance().get(cmd.getArg(2));
    
    try {
      File f = new File(fvar.value());
      if(file == null || !file.equals(f)) {
        boolean append = f.exists();
        file = f;
        ps = new PrintStream(new FileOutputStream(f, append));
      }
      
      System.out.println("* FilePut.exec.print: '"+ var.value()+ "'");
      ps.print(var.value());
      if(param != null) 
        ps.print(param.value());
      
      ps.flush();
      return true;
      
    } catch(IOException e) {
      return false;
    }
  }
  
  
  public static void main(String[] args) {
    String sc = "fileput ./arq2.csv 100 CSV";
    
    Command cmd = CommandParser.getInstanceFor(sc).parse(sc);
    Executor ex = Executor.getInstanceFor(cmd.getType());
    System.out.println("* exec: "+ ex.exec(cmd, null));
    System.out.println("* "+ Var.TEMP);
  }
  
}
