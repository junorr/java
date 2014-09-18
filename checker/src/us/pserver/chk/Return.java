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
public class Return {

  private Object obj;
  
  private Throwable thr;
  
  
  public Return() {
    obj = null;
    thr = null;
  }
  
  
  public Return(Object o) {
    obj = o;
    thr = null;
  }
  
  
  public Return(Throwable t) {
    obj = null;
    thr = t;
  }
  
  
  public boolean hasValue() {
    return obj != null;
  }
  
  
  public boolean isNull() {
    return obj == null;
  }
  
  
  public boolean hasError() {
    return thr != null;
  }
  
  
  public Object getValue() {
    return obj;
  }
  
  
  public Return setValue(Object o) {
    obj = o;
    return this;
  }
  
  
  public Throwable getError() {
    return thr;
  }
  
  
  public Return setError(Throwable t) {
    thr = t;
    return this;
  }
  
  
  public boolean isValueOf(Class c) {
    nullarg(Class.class, c);
    if(obj == null) return false;
    return c.isAssignableFrom(obj.getClass());
  }
  
  
  public boolean isErrorOf(Class<? extends Throwable> c) {
    nullarg(Class.class, c);
    if(thr == null) return false;
    return c.isAssignableFrom(thr.getClass());
  }
  
  
  public <T> T valueOfType(Class<? extends T> c) {
    nullarg(Class.class, c);
    if(!isValueOf(c)) return null;
    return c.cast(obj);
  }
  
  
  public <T extends Throwable> T errorOfType(Class<? extends T> c) {
    nullarg(Class.class, c);
    if(!isErrorOf(c)) return null;
    return c.cast(thr);
  }
  
}
