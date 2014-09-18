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

import com.jpower.tn3270.Cursor;
import com.jpower.tn3270.Session;
import com.jpower.tn3270.script.Command;
import com.jpower.tn3270.script.CommandType;
import com.jpower.tn3270.script.Var;
import java.io.PrintStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public abstract class Executor {
  
  
  public static PrintStream stdout = System.out;
  
  
  public abstract boolean exec(Command cmd, Session sess);
  
  
  public void checkCommand(Command cmd) {
    if(cmd == null)
      throw new IllegalArgumentException("Invalid Command: "+ cmd);
  }

  
  public void checkSession(Session sess) {
    if(sess == null)
      throw new IllegalArgumentException("Invalid Session: "+ sess);
    if(!sess.isConnected())
      throw new IllegalArgumentException("Session is not connected");
  }

  
  public Cursor getCursor(Command cmd) {
    if(cmd.getArgsSize() < 2) return null;
    Var row = new Var("row", cmd.getArg(0));
    Var col = new Var("col", cmd.getArg(1));
    if(!Cursor.isValid(row.getInt(), col.getInt()))
      return null;
    return new Cursor(row.getInt(), col.getInt());
  }
  
  
  public static Executor getInstanceFor(CommandType cmd) {
    if(cmd == null) return null;
    
    switch(cmd) {
      case APPEND:
        return new AppendExecutor();
      case CONNECT_3270:
        return new Connect3270Executor();
      case CONTAINS:
        return new ContainsExecutor();
      case CURSOR:
        return new CursorExecutor();
      case DELAY:
        return new DelayExecutor();
      case EQUALS:
        return new EqualsExecutor();
      case FILE_GET:
        return new FileGetExecutor();
      case FILE_PUT:
        return new FilePutExecutor();
      case GET_FIELD:
        return new GetFieldExecutor();
      case GET_SCREEN:
        return new GetScreenExecutor();
      case IF:
        return new IfExecutor();
      case NOT:
        return new NotExecutor();
      case KEY:
        return new KeyExecutor();
      case PASS:
        return new PassExecutor();
      case PRINT:
        return new PrintExecutor(stdout);
      case SET_FIELD:
        return new SetFieldExecutor();
      case VAR:
        return new VarExecutor();
      case WAIT:
        return new WaitExecutor();
      case WHILE:
        return new IfExecutor();
      default:
        return null;
    }
  }
  
}
