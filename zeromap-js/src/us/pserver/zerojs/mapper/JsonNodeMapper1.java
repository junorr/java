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

package us.pserver.zerojs.mapper;

import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.exception.JsonParseException;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/04/2016
 */
public class JsonNodeMapper1 implements JsonHandler {
  
  private final Node root;
  
  private Node current;
  
  private JsonNodeMapper1 inner;
  
  public JsonNodeMapper1() {
    this(new ONode("root"));
  }
  
  
  public JsonNodeMapper1(Node root) {
    if(root == null) {
      throw new IllegalArgumentException(
          "Root Node must be not null"
      );
    }
    current = this.root = root;
    inner = null;
  }
  
  
  public Node getRoot() {
    return root;
  }

      
  @Override
  public void startObject() throws JsonParseException {
    if(inner != null) {
      inner.startObject();
    }
    else if(current != root) {
      inner = new JsonNodeMapper1(current);
    }
  }


  @Override
  public void endObject() throws JsonParseException {
    if(inner != null) {
      inner.endObject();
      current = inner.getRoot();
      inner = null;
    }
  }


  @Override
  public void startArray() throws JsonParseException {
  }


  @Override
  public void endArray() throws JsonParseException {
  }


  @Override
  public void name(String str) {
    if(inner != null) {
      inner.name(str);
    }
    else {
      current = (current != root ? current.parent() : current);
      current = current.newChild(str);
    }
  }


  @Override
  public void value(String str) {
    if(inner != null) {
      inner.value(str);
    }
    else {
      current.newChild(str);
    }
  }

}
