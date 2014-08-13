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

package us.pserver.rob;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/08/2014
 */
public class MethodChain {

  private List<RemoteMethod> meths;
  
  private Iterator<RemoteMethod> iter;
  
  private RemoteMethod curr, lastadd;
  
  
  public MethodChain() {
    meths = new LinkedList<>();
    iter = null;
    curr = null;
    lastadd = null;
  }
  
  
  public MethodChain add(RemoteMethod rm) {
    if(rm != null) {
      meths.add(rm);
      lastadd = rm;
    }
    return this;
  }
  
  
  public RemoteMethod add(String objname, String method) {
    if(objname != null && method != null) {
      RemoteMethod rm = new RemoteMethod(objname, method);
      meths.add(rm);
      lastadd = rm;
      return rm;
    }
    return null;
  }
  
  
  public RemoteMethod add(String method) {
    if(method != null) {
      RemoteMethod rm = new RemoteMethod()
          .method(method);
      meths.add(rm);
      lastadd = rm;
      return rm;
    }
    return null;
  }
  
  
  public RemoteMethod lastAdded() {
    return lastadd;
  }
  
  
  public List<RemoteMethod> methods() {
    return meths;
  }
  
  
  public MethodChain rewind() {
    iter = null;
    curr = null;
    return this;
  }
  
  
  public RemoteMethod current() {
    if(curr == null) return next();
    return curr;
  }
  
  
  public RemoteMethod next() {
    if(iter == null)
      iter = meths.iterator();
    if(iter.hasNext())
      curr = iter.next();
    else
      curr = null;
    return curr;
  }
  
  
  public boolean hasNext() {
    if(iter == null)
      iter = meths.iterator();
    return iter.hasNext();
  }


  @Override
  public String toString() {
    return "MethodChain{ methods = " + meths.size() + " }";
  }
  
  
  public String stringChain() {
    StringBuffer sb = new StringBuffer();
    meths.forEach(rm->sb.append(rm.toString()));
    return sb.toString();
  }
  
}
