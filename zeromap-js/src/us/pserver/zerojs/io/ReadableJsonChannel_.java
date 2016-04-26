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
import java.nio.channels.ReadableByteChannel;
import us.pserver.zerojs.mapper.JsonNodeMapper;
import us.pserver.zerojs.parse.JsonParser;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.io.ReadableNodeChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/04/2016
 */
public class ReadableJsonChannel_ implements ReadableNodeChannel {
  
  private final ReadableByteChannel channel;
  
  private final JsonParser parser;
  
  private final JsonNodeMapper handler;
  
  
  public ReadableJsonChannel_(ReadableByteChannel rbc) {
    if(rbc == null) {
      throw new IllegalArgumentException(
          "ReadableByteChannel must be not null"
      );
    }
    this.channel = rbc;
    this.parser = JsonParser.defaultParser(channel);
    this.handler = new JsonNodeMapper();
    this.parser.addHandler(handler);
  }

  
  @Override
  public int read(Node root) throws IOException {
    if(root == null) {
      throw new IllegalArgumentException(
          "Root Node must be not null"
      );
    }
    int read = this.parser.parse();
    root.add(handler.getRoot());
    return read;
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
