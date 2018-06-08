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

package us.pserver.tools.misc;

import java.util.function.Consumer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class Final<T> {

  private volatile T val;
  
  public Final(T val) {
    this.val = val;
  }
  
  public Final() {
    this(null);
  }
  
  public boolean isDefined() {
    return val != null;
  }
  
  public Final define(T v) {
    if(!tryDefine(v)) {
      throw new IllegalStateException("Final value already defined");
    }
    return this;
  }
  
  public boolean tryDefine(T v) {
    if(val == null) {
      synchronized(this) {
        if(val == null) {
          val = v;
          return true;
        }
        return false;
      }
    }
    return false;
  }
  
  public void ifDefined(Consumer<T> cs) {
    if(isDefined()) cs.accept(val);
  }
  
  public T get() {
    return val;
  }
  
}
