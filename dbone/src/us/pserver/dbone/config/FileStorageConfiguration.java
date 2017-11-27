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

import java.nio.file.Paths;
import java.util.Objects;
import us.pserver.dbone.store.FileStorage;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/11/2017
 */
public class FileStorageConfiguration implements Configuration<FileStorage> {
  
  public static final String ENV_DBONE_DIRECTORY = "DBONE_DIRECTORY";
  
  public static final String USER_HOME = System.getProperty("user.home");
  
  public static final String DBONE_DIR_NAME = "/.dbone";
  
  
  private BufferAllocPolicy allocPolicy;
  
  private String storeDir;
  
  private int blockSize;
  
  
  public FileStorageConfiguration() {
    allocPolicy = DBOneConfiguration.DEFAULT_BUFFER_ALLOC_POLICY;
    storeDir = getEnvDBOneDirectory();
    blockSize = DBOneConfiguration.DEFAULT_BLOCK_SIZE;
    if(storeDir == null || storeDir.trim().isEmpty()) {
      storeDir = USER_HOME.concat(DBONE_DIR_NAME);
    }
  }
  
  
  private String getEnvDBOneDirectory() {
    try {
      return System.getenv(ENV_DBONE_DIRECTORY);
    }
    catch(SecurityException e) {
      return null;
    }
  }


  public BufferAllocPolicy getBufferAllocPolicy() {
    return allocPolicy;
  }


  public FileStorageConfiguration setBufferAllocPolicy(BufferAllocPolicy allocPolicy) {
    this.allocPolicy = allocPolicy;
    return this;
  }


  public String getStorageDirectory() {
    return storeDir;
  }


  public FileStorageConfiguration setStorageDirectory(String storeDir) {
    this.storeDir = storeDir;
    return this;
  }


  public int getBlockSize() {
    return blockSize;
  }


  public FileStorageConfiguration setBlockSize(int blockSize) {
    this.blockSize = blockSize;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + Objects.hashCode(this.allocPolicy);
    hash = 23 * hash + Objects.hashCode(this.storeDir);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FileStorageConfiguration other = (FileStorageConfiguration) obj;
    if (!Objects.equals(this.storeDir, other.storeDir)) {
      return false;
    }
    return this.allocPolicy == other.allocPolicy;
  }


  @Override
  public String toString() {
    return String.format(
        "FileStorageConfiguration{%n  - bufferAllocPolicy=%s;  - blockSize=%d;%n  - storageDirectory=%s;%n}",
        allocPolicy, blockSize, storeDir
    );
  }
  
  
  @Override
  public FileStorage create() {
    return new FileStorage(
        Paths.get(storeDir), 
        blockSize, 
        allocPolicy.getAllocFunction()
    );
  }
  
}
