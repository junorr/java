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
  
  private int inarray;
  

  @Override
  public String apply(Node root) {
    if(root == null) {
      throw new IllegalArgumentException(
          "Invalid null Node"
      );
    }
    inarray = 0;
    JsonBuilder jb = new JsonBuilder();
    this.addHandler(jb);
    this.inspect(root);
    this.handlers.remove(jb);
    return jb.toJson();
  }
  
  
  private void inspect(Node node) {
    if(node == null) return;
    if(!node.hasChilds()) {
      handlers.forEach(h->h.value(node.value()));
    }
    else {
      if(inarray <= 0) {
        if(node.hasParent()) {
          handlers.forEach(h->h.name(node.value()));
        }
        if(isArray(node)) {
          inarray++;
          handlers.forEach(h->h.startArray());
          node.childs().forEach(this::inspect);
          handlers.forEach(h->h.endArray());
          inarray--;
        }
        else if(isObject(node)) {
          handlers.forEach(h->h.startObject());
          node.childs().forEach(this::inspect);
          handlers.forEach(h->h.endObject());
        }
        else {
          
        }
      }
      else {
        handlers.forEach(JsonHandler::startObject);
        node.childs().forEach(this::inspect);
        handlers.forEach(JsonHandler::endObject);
      }
    }
  }
  
  
  private void inspect2(Node node) {
    if(node == null) return;
    //System.out.println("JsonFunction.inspect: node="+ node.value());
    if(node.hasChilds() && node.hasParent()) {
      //System.out.println("* name: "+ node.value());
      if(inarray > 0) {
        handlers.forEach(h->h.startObject());
        handlers.forEach(h->h.name(node.value()));
        handlers.forEach(h->h.endObject());
      } else {
        handlers.forEach(h->h.name(node.value()));
      }
    }
    if(!node.hasChilds()) {
      //System.out.println("* value: "+ node.value());
      handlers.forEach(h->h.value(node.value()));
    }
    else if(isArray(node)) {
      //System.out.println("* array: "+ node.value());
      inarray++;
      handlers.forEach(h->h.startArray());
      node.childs().forEach(this::inspect);
      handlers.forEach(h->h.endArray());
      inarray--;
    }
    else if(isObject(node)) {
      //System.out.println("* object: "+ node.value());
      handlers.forEach(h->h.startObject());
      node.childs().forEach(this::inspect);
      handlers.forEach(h->h.endObject());
    }
    else {
      node.childs().forEach(this::inspect);
    }
  }
  
  
  private boolean isArray(Node n) {
    return n.childs().size() > 1
        && (n.childs().stream().anyMatch(c -> !c.hasChilds())
        ||  n.childs().stream().distinct().count() != n.childs().size());
  }
  
  
  private boolean isObject(Node n) {
    return !isArray(n) && n.hasChilds() 
        && n.childs().stream().allMatch(Node::hasChilds);
  }
  
}
