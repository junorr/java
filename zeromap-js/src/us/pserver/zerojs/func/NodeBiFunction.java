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

import java.util.function.BiFunction;
import us.pserver.zerojs.handler.NodeBuilder;
import us.pserver.zerojs.impl.AbstractHandlerContainer;
import us.pserver.zerojs.impl.JsonToken;
import us.pserver.zeromap.Node;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/04/2016
 */
public class NodeBiFunction extends AbstractHandlerContainer implements BiFunction<String,Node,Node> {

  private final StringBuilder build;
  
  private boolean escQuote, escQuotes;
  
  
  public NodeBiFunction() {
    build = new StringBuilder();
  }
  
  
  @Override
  public Node apply(String json, Node root) {
    if(json == null || json.isEmpty()) {
      throw new IllegalArgumentException(
          "Invalid empty json String"
      );
    }
    if(root == null) {
      throw new IllegalArgumentException(
          "Invalid null root Node"
      );
    }
    this.clear();
    NodeBuilder nb = new NodeBuilder(root);
    this.addHandler(nb);
    json.chars().forEach(this::parse);
    this.handlers.remove(nb);
    return nb.getRoot();
  }
  
  
  protected void clear() {
    escQuote = escQuotes = false;
    if(build.length() > 0) {
      build.delete(0, build.length());
    }
  }

  
  protected void parse(int ch) {
    if(!escQuote && !escQuotes) {
      switch(ch) {
        case JsonToken.START_OBJECT:
          //System.out.println("* start object");
          handlers.forEach(h->{h.startObject();});
          break;
        case JsonToken.START_ARRAY:
          //System.out.println("* start array");
          handlers.forEach(h->{h.startArray();});
          break;
        case JsonToken.END_OBJECT:
          //System.out.println("* notify value: "+ build);
          //System.out.println("* end object");
          notifyValue();
          handlers.forEach(h->{h.endObject();});
          break;
        case JsonToken.END_ARRAY:
          //System.out.println("* notify value: "+ build);
          //System.out.println("* end array");
          notifyValue();
          handlers.forEach(h->{h.endArray();});
          break;
        case JsonToken.COLON:
          //System.out.println("* notify name: "+ build);
          notifyName();
          break;
        case JsonToken.COMMA:
          //System.out.println("* notify value: "+ build);
          notifyValue();
          break;
        case JsonToken.QUOTE:
          //System.out.println("* escape quote");
          escQuote = !escQuote;
          break;
        case JsonToken.QUOTES:
          //System.out.println("* escape quotes");
          escQuotes = !escQuotes;
          break;
        case ' ':
        case '\n':
        case '\t':
        case '\r':
          break;
        default:
          build.append((char)ch);
          break;
      }
    }
    else if(escQuote && JsonToken.QUOTE == ch) {
      escQuote = !escQuote;
    }
    else if(escQuotes && JsonToken.QUOTES == ch) {
      escQuotes = !escQuotes;
    }
    else {
      //System.out.println("* append");
      build.append((char)ch);
    }
  }
  
  
  private void fixQuotes() {
    if(build.length() > 0 && 
        (build.toString().endsWith("\"") 
        || build.toString().endsWith("'"))) {
      build.deleteCharAt(build.length() -1);
    }
  }
  
  
  private void notifyName() {
    if(build.length() > 0) {
      fixQuotes();
      String str = build.toString();
      handlers.forEach(h->h.name(str));
      build.delete(0, build.length());
    }
  }
  
  
  private void notifyValue() {
    if(build.length() > 0) {
      fixQuotes();
      String str = build.toString();
      handlers.forEach(h->h.value(str));
      build.delete(0, build.length());
    }
  }
  
}
