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

package us.pserver.test.internal;

import us.pserver.dbone.store.Region;
import us.pserver.dbone.store.Storage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import us.pserver.dbone.store.StorageException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/11/2017
 */
public class MockStorage implements Storage {
  
  private final Map<Region,ByteBuffer> map;
  
  private final int blockSize;
  
  private int index;
  
  
  public MockStorage(int blockSize) {
    if(blockSize < 1) {
      throw new IllegalArgumentException("Bad block size: "+ blockSize);
    }
    map = new HashMap<>();
    this.blockSize = blockSize;
    index = 0;
  }
  
  
  private Region region() {
    Region r = Region.of(index, blockSize);
    index += blockSize;
    return r;
  }
  

  @Override
  public Region put(ByteBuffer buf) throws StorageException {
    Region reg = region();
    map.put(reg, buf);
    return reg;
  }


  @Override
  public ByteBuffer get(Region reg) throws StorageException {
    return map.get(reg);
  }


  @Override
  public long size() throws StorageException {
    return map.entrySet().stream()
        .map(e->e.getValue().remaining())
        .reduce(0, (t,r)->t+r);
  }


  @Override
  public void close() throws StorageException {}

}
