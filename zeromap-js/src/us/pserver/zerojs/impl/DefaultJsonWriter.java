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

package us.pserver.zerojs.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import us.pserver.zerojs.JsonWriter;
import us.pserver.zerojs.mapper.NodeJsonMapper;
import us.pserver.zeromap.Node;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/04/2016
 */
public class DefaultJsonWriter extends AbstractObservableHandler implements JsonWriter {
  
  private final Node root;
  
  private final NodeJsonMapper mapper;
  
  
  public DefaultJsonWriter(Node root, Writer writer) {
    if(writer == null) {
      throw new IllegalArgumentException(
          "Writer must be not null"
      );
    }
    if(root == null) {
      throw new IllegalArgumentException(
          "Root Node must be not null"
      );
    }
    this.root = root;
    mapper = new NodeJsonMapper(writer);
  }
  
  
  @Override
  public Node getRoot() {
    return root;
  }
  
  
  @Override
  public Writer getWriter() {
    return mapper.getWriter();
  }
  
  
  private void iterate(Node node) throws IOException {
    if(node == null) {
      return;
    }
    Iterator<Node> iter = node.childs().iterator();
    while(iter.hasNext()) {
      Node n = iter.next();
      mapper.name(n.value());
      if(isObject(n)) {
        mapper.startObject();
        iterate(n);
        mapper.endObject();
      }
      else if(isArray(n)) {
        mapper.startArray();
        for(Node c : n.childs()) {
          if(c.hasChilds()) {
            iterate(c);
          } else {
            mapper.value(c.value());
          }
        }
        mapper.endArray();
      }
      else if(n.childs().size() == 1 
          && !n.firstChild().hasChilds()) {
        mapper.value(n.firstChild().value());
      }
    }
  }
  
  
  private boolean isArray(Node n) {
    if(n == null) {
      return false;
    }
    if(n.childs().size() <= 1) {
      return false;
    }
    for(Node c : n.childs()) {
      if(!c.hasChilds()) {
        return true;
      }
    }
    return false;
  }
  
  
  private boolean isObject(Node n) {
    if(n == null || isArray(n)) {
      return false;
    }
    if(!n.hasChilds()) {
      return false;
    }
    for(Node c : n.childs()) {
      if(!c.hasChilds()) {
        return false;
      }
    }
    return true;
  }
  

  @Override
  public void write() throws IOException {
    mapper.startObject();
    iterate(root);
    mapper.endObject();
  }

}
