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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import us.pserver.zerojs.JsonReader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/04/2016
 */
public class DefaultJsonReader extends AbstractObservable implements JsonReader {

  private final ReadableByteChannel channel;
  
  private final StringBuilder build;
  
  private final Charset charset;
  
  
  public DefaultJsonReader(ReadableByteChannel rbc) {
    this(rbc, Charset.forName("UTF-8"));
  }
  
  
  public DefaultJsonReader(ReadableByteChannel rbc, Charset cst) {
    if(rbc == null) {
      throw new IllegalArgumentException(
          "ReadableByteChannel must be not null"
      );
    }
    if(cst == null) {
      throw new IllegalArgumentException(
          "Charset must be not null"
      );
    }
    this.channel = rbc;
    this.build = new StringBuilder();
    this.charset = cst;
  }
  
  
  public ReadableByteChannel getReader() {
    return channel;
  }
  
  
  @Override
  public int read() throws IOException {
    ByteBuffer bytes = ByteBuffer.allocateDirect(4096);
    CharBuffer buffer;
    boolean escQuote = false;
    boolean escQuotes = false;
    int read = 0;
    int total = 0;
    while((read = channel.read(bytes)) > 0) {
      total += read;
      bytes.flip();
      buffer = charset.decode(bytes);
      while(buffer.hasRemaining()) {
        char ch = buffer.get();
        //System.out.print("("+ ch+ ") ");
        if(!escQuote && !escQuotes) {
          switch(ch) {
            case JsonToken.CHAR_START_OBJECT:
              //System.out.println("* start object");
              handlers.forEach(h->{h.startObject();});
              break;
            case JsonToken.CHAR_START_ARRAY:
              //System.out.println("* start array");
              handlers.forEach(h->{h.startArray();});
              break;
            case JsonToken.CHAR_END_OBJECT:
              //System.out.println("* notify value: "+ build);
              //System.out.println("* end object");
              notifyValue();
              handlers.forEach(h->{h.endObject();});
              break;
            case JsonToken.CHAR_END_ARRAY:
              //System.out.println("* notify value: "+ build);
              //System.out.println("* end array");
              notifyValue();
              handlers.forEach(h->{h.endArray();});
              break;
            case JsonToken.CHAR_COLON:
              //System.out.println("* notify name: "+ build);
              notifyName();
              break;
            case JsonToken.CHAR_COMMA:
              //System.out.println("* notify value: "+ build);
              notifyValue();
              break;
            case JsonToken.CHAR_QUOTE:
              //System.out.println("* escape quote");
              escQuote = !escQuote;
              break;
            case JsonToken.CHAR_QUOTES:
              //System.out.println("* escape quotes");
              escQuotes = !escQuotes;
              break;
            case ' ':
            case '\n':
            case '\t':
            case '\r':
              break;
            default:
              build.append(ch);
              break;
          }
        }
        else if(escQuote && JsonToken.CHAR_QUOTE == ch) {
          escQuote = !escQuote;
        }
        else if(escQuotes && JsonToken.CHAR_QUOTES == ch) {
          escQuotes = !escQuotes;
        }
        else {
          //System.out.println("* append");
          build.append(ch);
        }
      }//inner while
      bytes.clear();
    }//while
    return total;
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
