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
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.List;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.JsonReader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/04/2016
 */
public class DefaultJsonReader implements JsonReader {

  private final List<JsonHandler> handlers;
  
  private final Reader reader;
  
  private final StringBuilder build;
  
  
  public DefaultJsonReader(Reader rdr) {
    if(rdr == null) {
      throw new IllegalArgumentException(
          "Reader must be not null"
      );
    }
    this.handlers = new LinkedList<>();
    this.reader = rdr;
    this.build = new StringBuilder();
  }
  
  
  @Override
  public void read() throws IOException {
    char[] buffer = new char[1024];
    boolean escQuote = false;
    boolean escQuotes = false;
    int read = 0;
    while((read = reader.read(buffer)) > 0) {
      for(int i = 0; i < read; i++) {
        char ch = buffer[i];
        //System.out.print("("+ ch+ ") ");
        if(!escQuote && !escQuotes) {
          switch(ch) {
            case JsonTokens.START_OBJECT:
              //System.out.println("* start object");
              handlers.forEach(h->{h.startObject();});
              break;
            case JsonTokens.START_ARRAY:
              //System.out.println("* start array");
              handlers.forEach(h->{h.startArray();});
              break;
            case JsonTokens.END_OBJECT:
              //System.out.println("* notify value: "+ build);
              //System.out.println("* end object");
              notifyValue();
              handlers.forEach(h->{h.endObject();});
              break;
            case JsonTokens.END_ARRAY:
              //System.out.println("* notify value: "+ build);
              //System.out.println("* end array");
              notifyValue();
              handlers.forEach(h->{h.endArray();});
              break;
            case JsonTokens.COLON:
              //System.out.println("* notify name: "+ build);
              notifyName();
              break;
            case JsonTokens.COMMA:
              //System.out.println("* notify value: "+ build);
              notifyValue();
              break;
            case JsonTokens.QUOTE:
              //System.out.println("* escape quote");
              escQuote = !escQuote;
              break;
            case JsonTokens.QUOTES:
              //System.out.println("* escape quotes");
              escQuotes = !escQuotes;
              break;
            case ' ':
              break;
            default:
              build.append(ch);
              break;
          }
        }
        else if(escQuote && JsonTokens.QUOTE == ch
            && i < read -1 && 
            (buffer[i+1] == JsonTokens.COMMA
            || buffer[i+1] == JsonTokens.COLON
            || buffer[i+1] == JsonTokens.END_ARRAY
            || buffer[i+1] == JsonTokens.END_OBJECT)) {
          //System.out.println("* end escaping quote");
          escQuote = !escQuote;
        }
        else if(escQuotes && JsonTokens.QUOTES == ch
            && i < read -1 && 
            (buffer[i+1] == JsonTokens.COMMA
            || buffer[i+1] == JsonTokens.COLON
            || buffer[i+1] == JsonTokens.END_ARRAY
            || buffer[i+1] == JsonTokens.END_OBJECT)) {
          //System.out.println("* end escaping quotes");
          escQuotes = !escQuotes;
        }
        else {
          //System.out.println("* append");
          build.append(ch);
        }
      }
    }
  }
  
  
  //@Override
  public void readOld() throws IOException {
    CharBuffer buffer = CharBuffer.allocate(1024);
    while(reader.read(buffer) > 0) {
      buffer.flip();
      while(buffer.remaining() > 0) {
        char ch = buffer.get();
        switch(ch) {
          case ' ':
            break;
          case JsonTokens.START_OBJECT:
            handlers.forEach(h->{h.startObject();});
            break;
          case JsonTokens.START_ARRAY:
            handlers.forEach(h->{h.startArray();});
            break;
          case JsonTokens.END_OBJECT:
            notifyValue();
            handlers.forEach(h->{h.endObject();});
            break;
          case JsonTokens.END_ARRAY:
            notifyValue();
            handlers.forEach(h->{h.endArray();});
            break;
          case JsonTokens.COLON:
            notifyName();
            break;
          case JsonTokens.COMMA:
            notifyValue();
            break;
          case JsonTokens.QUOTE:
          case JsonTokens.QUOTES:
            if(build.length() > 0) {
              build.append(ch);
            }
            break;
          default:
            build.append(ch);
            break;
        }
      }
      buffer.clear();
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
  
  
  @Override
  public JsonReader addHandler(JsonHandler jsh) {
    if(jsh != null) {
      handlers.add(jsh);
    }
    return this;
  }


  @Override
  public boolean removeHandler(JsonHandler jsh) {
    return handlers.remove(jsh);
  }


  @Override
  public List<JsonHandler> getHandlers() {
    return handlers;
  }

}
