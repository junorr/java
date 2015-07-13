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

package us.pserver.xprops.util;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/07/2015
 */
public class Valid<T> {

  private final T obj;
  
  
  public Valid(T obj) {
    this.obj = obj;
  }
  
  
  public T getOrFail(String msg) throws IllegalArgumentException {
    return getOrFail(new IllegalArgumentException(msg+ Objects.toString(obj)));
  }
  
  
  public T getOrFail(boolean test, String msg) throws IllegalArgumentException {
    if(!test)
      throw new IllegalArgumentException(msg);
    return obj;
  }
  
  
  public T getOrFail(Class cls) throws IllegalArgumentException {
    return getOrFail(new IllegalArgumentException(
        "Invalid "+ cls.getSimpleName()+ ": "+ obj));
  }


  public T getOrFail(IllegalArgumentException exc) throws IllegalArgumentException {
    if(obj == null || obj.toString().isEmpty())
      throw exc;
    return obj;
  }
  
  
  public T getOrFail(Throwable thr) throws IllegalArgumentException {
    if(obj == null || obj.toString().isEmpty())
      throw new IllegalArgumentException(thr);
    return obj;
  }
  
  
  public Valid testNull(String msg) throws IllegalArgumentException {
    getOrFail(msg);
    return this;
  }
  
  
  public Valid test(boolean test, String msg) throws IllegalArgumentException {
    if(test) getOrFail(msg);
    return this;
  }
  
  
  public Valid testNull(Class cls) throws IllegalArgumentException {
    getOrFail(cls);
    return this;
  }
  
  
  public Valid testNull(IllegalArgumentException exc) throws IllegalArgumentException {
    getOrFail(exc);
    return this;
  }
  
  
  public Valid testNull(Throwable thr) throws IllegalArgumentException {
    getOrFail(thr);
    return this;
  }
  
  
  public boolean isValid() {
    return obj != null;
  }
  
  
  public static <T> Valid<T> off(T obj) {
    return new Valid(obj);
  }
  
}
