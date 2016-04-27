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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.JsonParseException;
import us.pserver.zerojs.impl.JsonToken;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/04/2016
 */
public class JsonBuilder implements JsonHandler {

  private final StringBuilder buffer;
  
  private boolean appendComma;
  
  
  public JsonBuilder() {
    buffer = new StringBuilder();
    appendComma = false;
  }
  
  
  public String toJson() {
    return buffer.toString();
  }
  
  
  public CharBuffer toCharBuffer() {
    CharBuffer cb = CharBuffer.allocate(buffer.length());
    cb.put(buffer.toString());
    return cb;
  }
  
  
  public ByteBuffer toByteBuffer(Charset cst) {
    if(cst == null) {
      throw new IllegalArgumentException(
          "Invalid null Charset"
      );
    }
    return cst.encode(toJson());
  }
  
  
  public ByteBuffer toByteBuffer() {
    return toByteBuffer(Charset.forName("UTF-8"));
  }
  
  
  @Override
  public String toString() {
    return toJson();
  }
  
  
  @Override
  public void startObject() throws JsonParseException {
    buffer.append(JsonToken.CHAR_START_OBJECT);
  }


  @Override
  public void endObject() throws JsonParseException {
    buffer.append(JsonToken.CHAR_END_OBJECT);
  }


  @Override
  public void startArray() throws JsonParseException {
    buffer.append(JsonToken.CHAR_START_ARRAY);
  }


  @Override
  public void endArray() throws JsonParseException {
    buffer.append(JsonToken.CHAR_END_ARRAY);
  }


  @Override
  public void name(String str) throws JsonParseException {
    if(appendComma) {
      buffer.append(JsonToken.CHAR_COMMA);
      appendComma = false;
    }
    buffer.append(JsonToken.CHAR_QUOTES);
    buffer.append(str);
    buffer.append(JsonToken.CHAR_QUOTES);
    buffer.append(JsonToken.CHAR_COLON);
  }


  @Override
  public void value(String str) throws JsonParseException {
    if(appendComma) {
      buffer.append(JsonToken.CHAR_COMMA);
    }
    try {
      Double.parseDouble(str);
      buffer.append(str);
    } 
    catch(NumberFormatException e) {
      if(str.equalsIgnoreCase("true")
          || str.equalsIgnoreCase("false")) {
        buffer.append(str);
      }
      else {
        buffer.append(JsonToken.CHAR_QUOTES);
        buffer.append(str);
        buffer.append(JsonToken.CHAR_QUOTES);
      }
    }
    appendComma = true;
  }

}
