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

package us.pserver.jose.json.iterator;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/03/2017
 */
public abstract class ByteIteratorFactory {

  
  public static ByteIterator of(String src) {
    if(src == null || src.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Null/Empty String Source");
    }
    return new ByteBufferIterator(ByteBuffer.wrap(
        UTF8String.from(src).getBytes())
    );
  }
  
  
  public static ByteIterator of(ByteBuffer buffer) {
    return new ByteBufferIterator(buffer);
  }
  
  
  public static ByteIterator of(ReadableByteChannel channel) {
    return new ByteChannelIterator(channel);
  }
  
  
  public static ByteIterator of(InputStream input) {
    return of(Channels.newChannel(input));
  }
  
  
  public static ByteIterator of(ReadableByteChannel channel, int bufferSize) {
    return new ByteChannelIterator(channel, bufferSize);
  }
  
  
  public static ByteIterator of(InputStream input, int bufferSize) {
    return of(Channels.newChannel(input), bufferSize);
  }
  
}
