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

package us.pserver.zerojs.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.exception.JsonParseException;
import us.pserver.zerojs.mapper.NodeJsonMapper;
import us.pserver.zerojs.parse.JsonToken;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.io.WritableNodeChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/04/2016
 */
public class WritableJsonChannel implements WritableNodeChannel, JsonHandler {
  
  
  private final WritableByteChannel channel;
  
  private final Charset charset;

  private final CharBuffer buffer;
  
  private final NodeJsonMapper mapper;
  
  private boolean appendComma;
  
  private int writed;
  
  
  public WritableJsonChannel(WritableByteChannel wbc) {
    this(wbc, Charset.forName("UTF-8"));
  }


  public WritableJsonChannel(WritableByteChannel wbc, Charset cst) {
    if(wbc == null) {
      throw new IllegalArgumentException(
          "Writer must be not null"
      );
    }
    if(cst == null) {
      throw new IllegalArgumentException(
          "Charset must be not null"
      );
    }
    this.channel = wbc;
    this.charset = cst;
    this.buffer = CharBuffer.allocate(4096);
    this.mapper = new NodeJsonMapper();
    this.mapper.addHandler(this);
    this.appendComma = false;
    this.writed = 0;
  }


  private void append(char ch) throws JsonParseException {
    try {
      if(buffer.remaining() < 1) {
        this.flush();
      }
      buffer.put(ch);
    } catch(IOException e) {
      throw new JsonParseException(e.getMessage(), e);
    }
  }
  
  
  private void append(String str) throws JsonParseException {
    try {
      if(buffer.remaining() < str.length()) {
        this.flush();
      }
      buffer.put(str);
    } catch(IOException e) {
      throw new JsonParseException(e.getMessage(), e);
    }
  }


  private void flush() throws IOException {
    if(buffer.position() > 0) {
      buffer.flip();
      writed += channel.write(charset.encode(buffer));
      buffer.clear();
    }
  }
  
  
  @Override
  public void startObject() throws JsonParseException {
    append(JsonToken.CHAR_START_OBJECT);
  }


  @Override
  public void endObject() throws JsonParseException {
    append(JsonToken.CHAR_END_OBJECT);
  }


  @Override
  public void startArray() throws JsonParseException {
    append(JsonToken.CHAR_START_ARRAY);
  }


  @Override
  public void endArray() throws JsonParseException {
    append(JsonToken.CHAR_END_ARRAY);
  }


  @Override
  public void name(String str) {
    if(appendComma) {
      append(JsonToken.CHAR_COMMA);
      appendComma = false;
    }
    append(JsonToken.CHAR_QUOTES);
    append(str);
    append(JsonToken.CHAR_QUOTES);
    append(JsonToken.CHAR_COLON);
  }


  @Override
  public void value(String str) {
    if(appendComma) {
      append(JsonToken.CHAR_COMMA);
    }
    try {
      Double.parseDouble(str);
      append(str);
    } 
    catch(NumberFormatException e) {
      if(str.equalsIgnoreCase("true")
          || str.equalsIgnoreCase("false")) {
        append(str);
      }
      else {
        append(JsonToken.CHAR_QUOTES);
        append(str);
        append(JsonToken.CHAR_QUOTES);
      }
    }
    appendComma = true;
  }

  
  @Override
  public int write(Node node) throws IOException {
    writed = 0;
    this.mapper.map(node);
    this.flush();
    return writed;
  }


  @Override
  public int write(ByteBuffer src) throws IOException {
    return channel.write(src);
  }


  @Override
  public boolean isOpen() {
    return channel.isOpen();
  }


  @Override
  public void close() throws IOException {
    channel.close();
  }

}
