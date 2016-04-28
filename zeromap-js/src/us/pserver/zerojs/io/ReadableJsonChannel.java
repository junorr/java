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
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import us.pserver.zerojs.func.NodeBiFunction;
import us.pserver.zerojs.handler.NodeBuilder;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.io.ReadableNodeChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/04/2016
 */
public class ReadableJsonChannel extends NodeBiFunction implements ReadableNodeChannel {

  private final ReadableByteChannel channel;
  
  private final Charset charset;
  
  
  public ReadableJsonChannel(ReadableByteChannel rbc) {
    this(rbc, Charset.forName("UTF-8"));
  }
  
  
  public ReadableJsonChannel(ReadableByteChannel rbc, Charset cst) {
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
    this.charset = cst;
  }
  
  
  @Override
  public int read(Node root) throws IOException {
    ByteBuffer bytes = ByteBuffer.allocateDirect(4096);
    this.clear();
    NodeBuilder nb = new NodeBuilder(root);
    this.addHandler(nb);
    CharBuffer buffer;
    int read = 0;
    int total = 0;
    while((read = channel.read(bytes)) > 0) {
      total += read;
      bytes.flip();
      buffer = charset.decode(bytes);
      buffer.chars().forEach(this::parse);
      bytes.clear();
    }
    this.handlers.remove(nb);
    return total;
  }


  @Override
  public int read(ByteBuffer dst) throws IOException {
    return channel.read(dst);
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
