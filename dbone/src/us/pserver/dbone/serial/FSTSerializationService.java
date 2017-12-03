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

package us.pserver.dbone.serial;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.nustaq.serialization.FSTConfiguration;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.io.ByteBufferInputStream;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class FSTSerializationService implements SerializationService {
  
  private final FSTConfiguration conf;
  
  public FSTSerializationService() {
    this.conf = FSTConfiguration.createDefaultConfiguration();
  }

  @Override
  public byte[] serialize(Object obj) throws StorageException {
    try (
        ByteBufferOutputStream bos = new ByteBufferOutputStream();
    ) {
      conf.encodeToStream(bos, obj);
      return bos.toByteArray();
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
  @Override
  public Object deserialize(byte[] buf) throws StorageException {
    try (
        ByteBufferInputStream bis = new ByteBufferInputStream(ByteBuffer.wrap(buf));
        ) {
      return conf.decodeFromStream(bis);
    }
    catch(Exception e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
}
