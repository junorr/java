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

package us.pserver.zerojs.handler;

import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.JsonParseException;
import us.pserver.zerojs.impl.JsonToken;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/04/2016
 */
public class NodeBuilder implements JsonHandler {
  
  public static final String ID_ARRAY = "@js-array";
  
  private final Node root;
  
  private Node current;
  
  private char lasttoken;
  
  
  public NodeBuilder(Node root) {
    if(root == null) {
      throw new IllegalArgumentException(
          "Invalid null root Node"
      );
    }
    this.root = root;
    current = this.root;
    lasttoken = 0;
  }
  
  
  public NodeBuilder() {
    this(new ONode("root"));
  }
  
  
  public Node getRoot() {
    return root;
  }
  

  @Override
  public void startObject() throws JsonParseException {
    //System.out.print("{");
    lasttoken = JsonToken.START_OBJECT;
    current = current.newChild(
        String.valueOf(JsonToken.START_OBJECT)
    );
  }


  @Override
  public void endObject() throws JsonParseException {
    //System.out.print("}");
    lasttoken = JsonToken.END_OBJECT;
    current = (current != root 
        ? current.parent().get() : current
    );
    current = (current != root 
        ? current.parent().get() : current
    );
    //System.out.println("* current="+ current.value());
  }


  @Override
  public void startArray() throws JsonParseException {
    //System.out.print("[");
    //System.out.print("* current="+ current.value());
    lasttoken = JsonToken.START_ARRAY;
    current = current.newChild(
        String.valueOf(JsonToken.START_ARRAY)
    );
    //System.out.println(", next="+ current.value());
  }


  @Override
  public void endArray() throws JsonParseException {
    //System.out.print("]");
    lasttoken = JsonToken.END_ARRAY;
    current = (current != root 
        ? current.parent().get() : current
    );
    //System.out.println("* current="+ current.value());
  }


  @Override
  public void name(String str) throws JsonParseException {
    //System.out.print(" '"+ str+ "':");
    if(lasttoken != JsonToken.START_OBJECT
        && current != root) {
      current = current.parent().get();
    }
    //System.out.print("* current="+ current.value());
    current = current.newChild(str);
    lasttoken = 0;
    //System.out.println(", next="+ current.value());
  }


  @Override
  public void value(String str) throws JsonParseException {
    //System.out.print(str);
    lasttoken = 0;
    current.newChild(str);
  }

}
