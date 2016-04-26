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

import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.JsonParseException;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/04/2016
 */
public class JsonNodeMapper implements JsonHandler {
  
  private final Node root;
  
  private Node current;
  
  private boolean inarray;
  
  private boolean inobj;
  
  
  public JsonNodeMapper() {
    this(new ONode("root"));
  }
  
  
  public JsonNodeMapper(Node root) {
    if(root == null) {
      throw new IllegalArgumentException(
          "Root Node must be not null"
      );
    }
    current = this.root = root;
    inarray = false;
    inobj = false;
  }
  
  
  public Node getRoot() {
    return root;
  }

      
  @Override
  public void startObject() throws JsonParseException {
    if(current != root) {
      inobj = true;
      //System.out.println("* start object");
    }
  }


  @Override
  public void endObject() throws JsonParseException {
    if(current != root) {
      inobj = false;
      current = current.parent();
      //System.out.println("* end object, current: "+ current.value());
    }
  }


  @Override
  public void startArray() throws JsonParseException {
    inarray = true;
    //System.out.println("* start array");
  }


  @Override
  public void endArray() throws JsonParseException {
    inarray = false;
    current = (current.hasParent() 
        ? current.parent() : current);
    //System.out.println("* end array, current: "+ current.value());
  }


  @Override
  public void name(String str) {
    current = current.newChild(str);
    //System.out.println("* name="+ str+ ", current: "+ current.value());
  }


  @Override
  public void value(String str) {
    current.newChild(str);
    current = (!inarray && current.hasParent() 
        ? current.parent() : current);
    //System.out.println("* value="+ str+ ", current: "+ current.value());
  }

}
