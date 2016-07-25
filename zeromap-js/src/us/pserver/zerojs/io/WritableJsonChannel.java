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
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import us.pserver.zerojs.func.JsonFunction;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.io.WritableNodeChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/04/2016
 */
public class WritableJsonChannel extends JsonFunction implements WritableNodeChannel {
  
  private final WritableByteChannel channel;
  
  private final Charset charset;
  

  public WritableJsonChannel(WritableByteChannel wbc) {
    this(wbc, Charset.forName("UTF-8"));
  }


  public WritableJsonChannel(WritableByteChannel wbc, Charset cst) {
    super();
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
  }

  
  @Override
  public int write(Node node) throws IOException {
    return this.write(charset.encode(this.apply(node)));
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
