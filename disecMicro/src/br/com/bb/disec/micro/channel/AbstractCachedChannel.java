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

package br.com.bb.disec.micro.channel;

import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/09/2016
 */
public abstract class AbstractCachedChannel {
  
  public static final String CRLF = "\r\n";
  
  
  protected final WritableByteChannel channel;
  
  protected ByteBuffer buffer;
  
  protected final Charset charset;
  
  
  public AbstractCachedChannel(WritableByteChannel chn) {
    if(chn == null) {
      throw new IllegalArgumentException("Bad Null Channel");
    }
    this.channel = chn;
    charset = Charset.forName("UTF-8");
    buffer = ByteBuffer.allocateDirect(4096);
  }
  
  
  public WritableByteChannel getSender() {
    return channel;
  }
  
  
  public AbstractCachedChannel flush() {
    if(buffer.position() > 0) {
      buffer.flip();
      UncheckedException.unchecked(()->channel.write(buffer));
      buffer.clear();
    }
    return this;
  }
  
  
  public void close() {
    flush();
    UncheckedException.unchecked(channel::close);
  }
  
      
  public AbstractCachedChannel write(String s) {
    if(s == null || s.isEmpty()) return this;
    return this.write(s.getBytes(charset));
  }
  
  
  protected AbstractCachedChannel write(byte[] bs) {
    int start = 0;
    int count = 0;
    while(start < bs.length) {
      count = Math.min(
          (bs.length-start), 
          buffer.remaining()
      );
      buffer.put(bs, start, count);
      start += count;
      if(buffer.remaining() == 0) {
        flush();
      }
    }
    return this;
  }
  
  
  public AbstractCachedChannel newLine() {
    return this.write(CRLF);
  }
  
  
  public abstract AbstractCachedChannel put(Object obj);
  
  public abstract AbstractCachedChannel put(String str);
  
  public abstract AbstractCachedChannel put(Number num);
  
  public abstract AbstractCachedChannel put(Boolean bol);
  
  public abstract AbstractCachedChannel put(Date dte);
  
}
