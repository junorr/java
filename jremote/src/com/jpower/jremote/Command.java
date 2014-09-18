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

package com.jpower.jremote;

import java.awt.Robot;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class Command implements Serializable {
  
  private LinkedList<KeyAction> keys;
  
  private LinkedList<MouseAction> mouse;
  
  
  public Command() {
    keys = new LinkedList<>();
    mouse = new LinkedList<>();
  }
  
  
  public Command(MouseAction ma, KeyAction ... keys) {
    this();
    mouse.add(ma);
    for(int i = 0; i < keys.length; i++) {
      this.keys.add(keys[i]);
    }
  }
  
  
  public boolean isEmpty() {
    return (keys == null || keys.isEmpty()) 
        && (mouse == null || mouse.isEmpty());
  }


  public LinkedList<KeyAction> getKeys() {
    return keys;
  }
  
  
  public void add(KeyAction key) {
    keys.add(key);
  }


  public LinkedList<MouseAction> getMouse() {
    return mouse;
  }


  public void add(MouseAction mouse) {
    this.mouse.add(mouse);
  }
  
  
  public void reset() {
    keys.clear();
    mouse.clear();
  }
  
  
  public Command execute(Robot r) {
    if(r == null) throw new IllegalArgumentException(
        "Invalid Robot instance: "+ r);
    
    for(MouseAction m : mouse) {
      m.execute(r);
    }
    for(KeyAction k : keys) {
      k.execute(r);
    }
    return this;
  }
  
  
  public String toString() {
    return "[Command: keys = "
        + keys.size()
        + ", mouse = "
        + mouse.size()+ "]";
  }

}
