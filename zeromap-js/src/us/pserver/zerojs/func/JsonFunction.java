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
import us.pserver.zerojs.impl.JsonToken;
import us.pserver.zeromap.Node;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/04/2016
 */
public class JsonFunction extends AbstractHandlerContainer implements Function<Node,String> {

  @Override
  public String apply(Node root) {
    if(root == null) {
      throw new IllegalArgumentException(
          "Invalid null Node"
      );
    }
    JsonBuilder jb = new JsonBuilder();
    this.addHandler(jb);
    this.inspect(root);
    this.handlers.remove(jb);
    return jb.toJson();
  }
  
  
  private void inspect2(Node node) {
    if(node == null) return;
    if(!node.hasChilds()) {
      System.out.println("* isValue: "+ node.value());
      handlers.forEach(h->h.value(node.value()));
    }
    else if(isArray(node)) {
      System.out.println("* isArray: "+ node.value());
      handlers.forEach(h->h.startArray());
      node.childs().forEach(this::inspect);
      handlers.forEach(h->h.endArray());
    }
    else if(isArray(node.firstChild().get())) {
      handlers.forEach(h->h.name(node.value()));
      node.childs().forEach(this::inspect);
    }
    else if(isObject(node) || (node.hasParent() 
        && isArray(node.parent().get()))) {
      if(node.hasParent()) {
        handlers.forEach(h->h.name(node.value()));
      }
      handlers.forEach(h->h.startObject());
      node.childs().forEach(this::inspect);
      handlers.forEach(h->h.endObject());
    }
    else {
      handlers.forEach(h->h.name(node.value()));
      node.childs().forEach(this::inspect);
    }
  }

  
  private void inspect(Node node) {
    if(node == null) return;
    if(!node.hasChilds()) {
      System.out.println("* isValue: "+ node.value());
      handlers.forEach(h->h.value(node.value()));
    }
    else if(isArray(node)) {
      System.out.println("* isArray: "+ node.value());
      handlers.forEach(h->h.startArray());
      node.childs().forEach(this::inspect);
      handlers.forEach(h->h.endArray());
    }
    else {
      if(node.hasParent()) {
        handlers.forEach(h->h.name(node.value()));
      }
    }
    else if(isObject(node) && isChildArray(node)) {
      
    }
  }

  
  private void iterate(Node node) {
    if(node == null) {
      return;
    }
    if(!node.hasChilds()) {
      handlers.forEach(h->h.value(node.value()));
      return;
    }
    boolean isarray = isArray(node);
    boolean isobj = isObject(node);
    handlers.forEach(h->{
      if(isarray) h.startArray();
      else if(isobj) h.startObject();
    });
    Iterator<Node> iter = node.childs().iterator();
    while(iter.hasNext()) {
      Node n = iter.next();
      if(!isArray(n)) {
        handlers.forEach(h->h.name(n.value()));
      }
      n.childs().forEach(this::iterate);
    }//while
    handlers.forEach(h->{
      if(isarray) h.startArray();
      else if(isobj) h.startObject();
    });
  }
  
  
  private boolean isArray(Node n) {
    return n.value().length() == 1
        && n.value().charAt(0) == JsonToken.START_ARRAY;
  }
  
  
  private boolean isChildArray(Node n) {
    return n.childs().size() == 1 
        && isArray(n.firstChild().get());
  }
  
  
  private boolean isObject(Node n) {
    if(n == null || isArray(n)) {
      return false;
    }
    if(!n.hasChilds()) {
      return false;
    }
    return n.childs().stream().allMatch(c->c.hasChilds());
  }
  
}
