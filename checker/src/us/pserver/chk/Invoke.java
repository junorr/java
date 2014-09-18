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

package us.pserver.chk;

import static us.pserver.chk.Checker.nullarg;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 04/07/2014
 */
public class Invoke {

  @FunctionalInterface
  public static interface Invocable {
    public Object invoke() throws Throwable;
  }
  

  @FunctionalInterface
  public static interface Void {
    public void invoke() throws Throwable;
  }
  
  
  public static Return call(Invocable ivk) {
    Return rt = new Return();
    try {
      rt.setValue(ivk.invoke());
    } catch(Throwable t) {
      rt.setError(t);
    }
    return rt;
  }
  
  
  public static Return call(Void vdi) {
    Return rt = new Return();
    try {
      vdi.invoke();
    } catch(Throwable t) {
      rt.setError(t);
    }
    return rt;
  }
  
  
  public static boolean isSuccessful(Void vdi) {
    try {
      vdi.invoke();
      return true;
    } catch(Throwable e) {
      return false;
    }
  }
  
  
  public static Object unchecked(Invocable ivk) throws RuntimeException {
    try {
      return ivk.invoke();
    } catch(Throwable t) {
      throw new RuntimeException(t);
    }
  }
  
  
  public static void unchecked(Void vdi) throws RuntimeException {
    try {
      vdi.invoke();
    } catch(Throwable t) {
      throw new RuntimeException(t);
    }
  }
  
}
