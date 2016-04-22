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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/04/2016
 */
public class ChannelReader extends Reader {
  
  private final ReadableByteChannel channel;
  
  private final ByteBuffer bytes;
  
  private CharBuffer buffer;
  
  private final Charset charset;
  
  
  public ChannelReader(ReadableByteChannel ch) {
    this(ch, null);
  }
  
  
  public ChannelReader(ReadableByteChannel ch, Charset cs) {
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
    this.bytes = ByteBuffer.allocate(4096);
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
  public int read(char[] cbuf, int off, int len) throws IOException {
    fillBuffer();
    if(buffer == null || !buffer.hasRemaining()) {
      return -1;
    }
    int read = Math.min(len, buffer.remaining());
    buffer.get(cbuf, off, read);
    return read;
  }


  @Override
  public void close() throws IOException {
    channel.close();
    bytes.clear();
    buffer.clear();
  }
  

  public static void main(String[] args) throws IOException {
    ByteArrayInputStream bin = 
        new ByteArrayInputStream(
            "abcdefghijklmnopqrstuvwxyz".getBytes(
                Charset.forName("UTF-8")));
    ChannelReader iss = new ChannelReader(Channels.newChannel(bin));
    char[] cs = new char[1];
    while(iss.read(cs) > 0) {
      System.out.printf("-%s%n", cs[0]);
    }
  }

}
