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
public class Task {

  private LinkedList<Field> flds;
  
  private Key key;
  
  private Field ctrl;
  
  
  public Task() {
    flds = new LinkedList<>();
    key = null;
    ctrl = null;
  }
  
  
  public Task(Field ctrlField, Key key) {
    this();
    this.key = key;
    this.ctrl = ctrlField;
  }
  
  
  public Task add(Field fld) {
    if(fld != null) {
      flds.add(fld);
    }
    return this;
  }
  
  
  public List<Field> fields() {
    return flds;
  }
  
  
  public Task setControlField(Field ctrl) {
    this.ctrl = ctrl;
    return this;
  }
  
  
  public Task setKey(Key key) {
    this.key = key;
    return this;
  }
  
  
  public Key key() {
    return key;
  }
  
  
  public Field controlField() {
    return ctrl;
  }
  
  
  public Field findField(String content) {
    if(content == null)
      return null;
    for(Field fld : flds) {
      if(fld.getContent().equals(content))
        return fld;
    }
    return null;
  }
  
  
  public Field findField(int row, int col, int len) {
    if(!Cursor.isValid(row, col) || len < 1)
      return null;
    for(Field fld : flds) {
      if(fld.getRow() == row
          && fld.getColumn() == col
          && fld.getLength() == len)
        return fld;
    }
    return null;
  }
  
}
