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

package us.pserver.tools;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public class NotNull<T> {

  private T obj;
  
  
  NotNull(T t) {
    obj = t;
  }
  
  
  public static <T> NotNull<T> of(T obj) {
    return new NotNull<T>(obj);
  }
  
  
  public boolean isNull() {
    return obj == null;
  }
  
  
  public T get() {
    return obj;
  }
  
  
  public T getOrFail() throws NullPointerException {
    fail();
    return obj;
  }
  
  
  public void fail() throws NullPointerException {
    if(obj == null) {
      throw new NullPointerException("Invalid Null Object");
    }
  }
  
}
