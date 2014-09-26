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

package us.pserver.json;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/09/2014
 */
public class JsonArray extends Element {

  private List<Element> list;
  
  
  public JsonArray() {
    list = new LinkedList<>();
    super.setValue(list);
  }
  
  
  public JsonArray add(Element el) {
    if(el != null)
      list.add(el);
    return this;
  }
  
  
  public JsonArray add(Object o) {
    if(o != null)
      this.add(new Element(o));
    return this;
  }
  
  
  public Object[] toArray() {
    Object[] os = new Object[list.size()];
    for(int i = 0; i < list.size(); i++) {
      os[i] = list.get(i).getValue();
    }
    return os;
  }
  
  
  public List toList() {
    return Arrays.asList(toArray());
  }
  
  
  public List<Element> list() {
    return list;
  }
  
}
