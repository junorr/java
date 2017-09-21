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

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/09/2017
 */
public class SharedBlock extends DefaultBlock {
  
  private final FileChannel channel;
  
  private FileLock lock;
  
  public SharedBlock(FileChannel fch, Region reg, ByteBuffer buf) {
    super(reg, buf);
    this.channel = NotNull.of(fch).getOrFail("Bad null FileChannel");
  }
  
  @Override
  public Block writeLock() throws StorageException {
    if(lock != null) {
      if(lock.isValid() && !lock.isShared()) return this;
      else this.releaseLock();
    }
    lock = StorageException.rethrow(()->
        channel.lock(region.offset(), region.length(), false)
    );
    return this;
  }
  
  @Override
  public Block readLock() throws StorageException {
    if(lock != null) {
      if(lock.isValid()) return this;
      else this.releaseLock();
    }
    lock = StorageException.rethrow(()->
        channel.lock(region.offset(), region.length(), false)
    );
    return this;
  }
  
  @Override
  public Block releaseLock() throws StorageException {
    if(lock != null) {
      StorageException.rethrow(lock::release);
      lock = null;
    }
    return this;
  }
  
  @Override
  public boolean isWriteLocked() {
    return lock != null && lock.isValid() && !lock.isShared();
  }

}
