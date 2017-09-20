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

package us.pserver.dbone.store;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import us.pserver.tools.io.ByteBufferInputStream;
import us.pserver.tools.io.ByteBufferOutputStream;
import us.pserver.tools.mapper.MappedValue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class SerializationService {

  public SerializationService() {}
  
  public ByteBuffer serialize(Serializable slz) throws StorageException {
    try (
        ByteBufferOutputStream bos = new ByteBufferOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
    ) {
      oos.writeObject(slz);
      return bos.toByteBuffer();
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
  public Object deserialize(ByteBuffer buf) throws StorageException {
    try (
        ByteBufferInputStream bis = new ByteBufferInputStream(buf);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ) {
      return ois.readObject();
    }
    catch(ClassNotFoundException | IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
}
