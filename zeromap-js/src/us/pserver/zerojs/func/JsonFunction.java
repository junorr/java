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

import java.util.ArrayDeque;
import java.util.Deque;
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
  
  private Deque<Character> tokens = new ArrayDeque<>();
  

  @Override
  public String apply(Node root) {
    if(root == null) {
      throw new IllegalArgumentException(
          "Invalid null Node"
      );
    }
    tokens.clear();
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
    else if(isArray(node)) {
      inspectArray(node);
    }
    else if(isObject(node)) {
      inspectObject(node);
    }
    else if(node.hasChilds()) {
      inspectKeyValue(node);
    }
    else {
      node.childs().forEach(this::inspect);
    }
  }
  
  
  private void inspectArray(Node node) {
    if(node.hasParent()) {
      handlers.forEach(h->h.name(node.value()));
    }
    tokens.addLast(JsonToken.START_ARRAY);
    handlers.forEach(JsonHandler::startArray);
    node.childs().forEach(this::inspect);
    handlers.forEach(JsonHandler::endArray);
    tokens.pollLast();
  }
  
  
  private void inspectObject(Node node) {
    boolean inarray = !tokens.isEmpty() && JsonToken.START_ARRAY == tokens.peekLast();
    if(inarray) {
      handlers.forEach(JsonHandler::startObject);
      tokens.addLast(JsonToken.START_OBJECT);
    }
    if(node.hasParent()) {
      handlers.forEach(h->h.name(node.value()));
    }
    tokens.addLast(JsonToken.START_OBJECT);
    handlers.forEach(JsonHandler::startObject);

    node.childs().forEach(this::inspect);
    handlers.forEach(JsonHandler::endObject);
    tokens.pollLast();
    
    if(inarray) {
      handlers.forEach(JsonHandler::endObject);
      tokens.pollLast();
    }
  }
  
  
  private void inspectKeyValue(Node node) {
    if(!tokens.isEmpty() && JsonToken.START_ARRAY == tokens.peekLast()) {
      tokens.addLast(JsonToken.START_OBJECT);
      handlers.forEach(JsonHandler::startObject);
      handlers.forEach(h->h.name(node.value()));
      node.childs().forEach(this::inspect);
      handlers.forEach(JsonHandler::endObject);
      tokens.pollLast();
    } else {
      handlers.forEach(h->h.name(node.value()));
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
