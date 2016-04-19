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

package us.pserver.zerojs.reader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import us.pserver.zerojs.exception.JsonReadException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/04/2016
 */
public class ChannelInput implements JsonInput {
  
  private final ReadableByteChannel channel;
  
  private final ByteBuffer bytes;
  
  private CharBuffer buffer;
  
  private final Charset charset;
  
  
  public ChannelInput(ReadableByteChannel ch) {
    this(ch, null);
  }
  
  
  public ChannelInput(ReadableByteChannel ch, Charset cs) {
    if(ch == null) {
      throw new IllegalArgumentException(
          "Channel must be not null"
      );
    }
    if(cs == null) {
      cs = Charset.forName("UTF-8");
    }
    this.channel = ch;
    this.charset = cs;
    this.buffer = null;
    this.bytes = ByteBuffer.allocate(1024);
  }
  

  @Override
  public boolean hasNext() {
    fillBuffer();
    return buffer != null && buffer.remaining() > 0;
  }
  
  
  private void fillBuffer() {
    if(buffer != null && buffer.remaining() > 0)
      return;
    try {
      channel.read(bytes);
      bytes.flip();
      buffer = charset.decode(bytes);
      bytes.clear();
    } catch(IOException e) {
      buffer = null;
    }
  }


  @Override
  public Character next() throws JsonReadException {
    if(!hasNext()) {
      throw new JsonReadException("No more data");
    }
    return buffer.get();
  }

  
  public static void main(String[] args) {
    ByteArrayInputStream bin = 
        new ByteArrayInputStream(
            "abcdefghijklmnopqrstuvwxyz".getBytes(
                Charset.forName("UTF-8")));
    ChannelInput iss = new ChannelInput(Channels.newChannel(bin));
    while(iss.hasNext()) {
      System.out.printf("-%s%n", iss.next());
    }
  }
  
}
