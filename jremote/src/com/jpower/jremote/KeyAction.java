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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 24/09/2012
 */
public class KeyAction extends AbstractAction {

  private int key;
  
  private int mod;
  
  
  public KeyAction() {
    key = -1;
    mod = 0;
  }
  
  
  public KeyAction(TYPE t, int key, int mod) {
    this.type(t);
    this.key = key;
    this.mod = mod;
  }


  public int getKey() {
    return key;
  }


  public KeyAction setKey(int key) {
    this.key = key;
    return this;
  }


  public int getModifiers() {
    return mod;
  }


  public KeyAction setModifiers(int mod) {
    this.mod = mod;
    return this;
  }
  

  @Override
  public Action execute(Robot r) {
    if(r == null) throw new IllegalArgumentException(
        "Invalid Robot instance: "+ r);
    if(key != -1) {
      try {
        if(type == TYPE.PRESS_ACTION)
          r.keyPress(key);
        else
          r.keyRelease(key);
      } catch(IllegalArgumentException ex) {
        System.out.println("# "+ ex.getMessage()+ ": "+ key);
      }
    }
    return this;
  }

}
