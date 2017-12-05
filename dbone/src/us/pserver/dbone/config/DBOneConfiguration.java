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

package us.pserver.dbone.config;

import java.io.IOException;
import us.pserver.dbone.store.Region;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/11/2017
 */
public class DBOneConfiguration {
  
  public static final int DEFAULT_BLOCK_SIZE = 1024;
  
  public static final BufferAllocPolicy DEFAULT_BUFFER_ALLOC_POLICY = BufferAllocPolicy.ALLOCATE_DIRECT;
  
  public static final String ENV_DBONE_CONFIGURATION = "DBONE_CONFIG";
  
  public static final int MIN_BLOCK_SIZE = Region.BYTES + Integer.BYTES + 1;
  
  
  private int blockSize;
  
  private BufferAllocPolicy allocPolicy;
  
  private boolean transactional;
  
  private String fileConf;
  
  
  public DBOneConfiguration() {
    this.blockSize = DEFAULT_BLOCK_SIZE;
    this.allocPolicy = DEFAULT_BUFFER_ALLOC_POLICY;
    this.fileConf = getEnvDBOneFileConf();
    this.transactional = false;
  }


  private String getEnvDBOneFileConf() {
    try {
      return System.getenv(ENV_DBONE_CONFIGURATION);
    }
    catch(SecurityException e) {
      return null;
    }
  }


  public int getBlockSize() {
    return blockSize;
  }


  public DBOneConfiguration setBlockSize(int blockSize) {
    if(blockSize < MIN_BLOCK_SIZE) {
      throw new IllegalArgumentException(
          String.format("Bad block size (< %d)", MIN_BLOCK_SIZE)
      );
    }
    this.blockSize = blockSize;
    return this;
  }


  public BufferAllocPolicy getBufferAllocPolicy() {
    return allocPolicy;
  }


  public DBOneConfiguration setBufferAllocPolicy(BufferAllocPolicy allocPolicy) {
    this.allocPolicy = allocPolicy;
    return this;
  }


  public boolean isTransactionEnabled() {
    return transactional;
  }


  public DBOneConfiguration setTransactionEnabled(boolean transactional) {
    this.transactional = transactional;
    return this;
  }


  public String getFileConfiguration() {
    return fileConf;
  }


  public DBOneConfiguration setFileConfiguration(String fileConf) {
    this.fileConf = fileConf;
    return this;
  }
  
}
