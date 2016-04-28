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

package us.pserver.zerojs.func;

import java.util.Iterator;
import java.util.function.Function;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.handler.JsonBuilder;
import us.pserver.zerojs.impl.AbstractHandlerContainer;
import us.pserver.zeromap.Node;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/04/2016
 */
public class JsonFunction extends AbstractHandlerContainer implements Function<Node,String> {

  @Override
  public String apply(Node t) {
    if(t == null) {
      throw new IllegalArgumentException(
          "Invalid null Node"
      );
    }
    JsonBuilder jb = new JsonBuilder();
    this.addHandler(jb);
    this.notifyStart(t);
    this.iterate(t);
    this.notifyEnd(t);
    this.handlers.remove(jb);
    return jb.toJson();
  }

  
  private void iterate(Node node) {
    if(node == null) {
      return;
    }
    Iterator<Node> iter = node.childs().iterator();
    while(iter.hasNext()) {
      Node n = iter.next();
      this.handlers.forEach(h->h.name(n.value()));
      if(isObject(n)) {
        this.handlers.forEach(JsonHandler::startObject);
        iterate(n);
        this.handlers.forEach(JsonHandler::endObject);
      }
      else if(isArray(n)) {
        this.handlers.forEach(JsonHandler::startArray);
        for(Node c : n.childs()) {
          if(c.hasChilds()) {
            iterate(c);
          } 
          else {
            this.handlers.forEach(h->h.value(c.value()));
          }
        }
        this.handlers.forEach(JsonHandler::endArray);
      }
      else if(n.childs().size() == 1 
          && !n.firstChild().hasChilds()) {
        this.handlers.forEach(h->h.value(n.firstChild().value()));
      }
    }
  }
  
  
  private void notifyStart(Node node) {
    if(isArray(node)) {
      this.handlers.forEach(JsonHandler::startArray);
    }
    else {
      this.handlers.forEach(JsonHandler::startObject);
    }
  }
  
  
  public void notifyEnd(Node node) {
    if(isArray(node)) {
      this.handlers.forEach(JsonHandler::endArray);
    }
    else {
      this.handlers.forEach(JsonHandler::endObject);
    }
  }
  
  
  private boolean isArray(Node n) {
    if(n == null) {
      return false;
    }
    if(n.childs().size() <= 1) {
      return false;
    }
    return n.childs().stream().anyMatch(
        (c) -> (!c.hasChilds())
    );
  }
  
  
  private boolean isObject(Node n) {
    if(n == null || isArray(n)) {
      return false;
    }
    if(!n.hasChilds()) {
      return false;
    }
    return n.childs().stream().allMatch(
        (c) -> (c.hasChilds())
    );
  }
  
}
