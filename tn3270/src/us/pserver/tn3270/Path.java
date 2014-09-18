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

package us.pserver.tn3270;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/07/2013
 */
public class Path {

  private LinkedList<Task> tsks;
  
  private int taskDelay;
  
  
  public Path() {
    tsks = new LinkedList<>();
    taskDelay = 0;
  }
  
  
  public Path add(Task tsk) {
    if(tsk != null) {
      tsks.add(tsk);
    }
    return this;
  }
  
  
  public List<Task> tasks() {
    return tsks;
  }
  
  
  public Task findTaskByControl(String ctrlString) {
    if(ctrlString == null || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      if(tsk.controlField().getContent()
          .equals(ctrlString))
        return tsk;
    }
    return null;
  }
  
  
  public Task findTaskByField(String fldContent) {
    if(fldContent == null || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(fldContent);
      if(f != null) return tsk;
    }
    return null;
  }
  
  
  public Task findTaskByField(int row, int col, int len) {
    if(!Cursor.isValid(row, col) || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(row, col, len);
      if(f != null) return tsk;
    }
    return null;
  }
  
  
  public Field findField(String fldContent) {
    if(fldContent == null || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(fldContent);
      if(f != null) return f;
    }
    return null;
  }
  
  
  public Field findField(int row, int col, int len) {
    if(!Cursor.isValid(row, col) || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(row, col, len);
      if(f != null) return f;
    }
    return null;
  }
  
  
  public int getDelayBetweenTasks() {
    return taskDelay;
  }
  
  
  public Path setDelayBetweenTasks(int delay) {
    taskDelay = delay;
    return this;
  }
  
}
