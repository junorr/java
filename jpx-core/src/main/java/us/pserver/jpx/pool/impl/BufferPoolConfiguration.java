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

package us.pserver.jpx.pool.impl;

import java.util.Objects;
import us.pserver.jpx.pool.PoolConfiguration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/08/2018
 */
public class BufferPoolConfiguration implements PoolConfiguration {
  
  public static final long DEFAULT_MAX_MEM_ALLOCATION = 512 * 1024 * 1024;
  
  public static final int MIN_MEM_ALLOCATION = 4096;
  
  public static final int MIN_UNIT_BUFFER_SIZE = 128;
  
  private final PoolConfiguration cfg;
  
  private final long maxMemAlloc;
  
  private final int unitBufSize;
  
  
  public BufferPoolConfiguration(PoolConfiguration innerCfg, long maxMemAlloc, int unitBufSize) {
    if(maxMemAlloc < MIN_MEM_ALLOCATION) {
      throw new IllegalArgumentException(String.format("Bad max memory allocation: %d (< %d)", 
          maxMemAlloc, MIN_MEM_ALLOCATION
      ));
    }
    if(unitBufSize < MIN_UNIT_BUFFER_SIZE) {
      throw new IllegalArgumentException(String.format(
          "Bad unit buffer size: %d (< %d)", 
          unitBufSize, MIN_UNIT_BUFFER_SIZE
      ));
    }
    this.maxMemAlloc = maxMemAlloc;
    this.unitBufSize = unitBufSize;
    this.cfg = Objects.requireNonNull(innerCfg)
        .withMaxReferenceCount((int) (maxMemAlloc / unitBufSize));
  }
  
  
  public BufferPoolConfiguration(PoolConfiguration innerCfg) {
    this(innerCfg, DEFAULT_MAX_MEM_ALLOCATION, MIN_MEM_ALLOCATION);
  }
  
  
  public BufferPoolConfiguration() {
    this(new DefaultPoolConfiguration().withInitialSize(2), DEFAULT_MAX_MEM_ALLOCATION, MIN_MEM_ALLOCATION);
  }
  
  
  public long getMaxMemoryAllocation() {
    return maxMemAlloc;
  }
  
  
  public BufferPoolConfiguration withMaxMemmoryAllocation(long maxMemAlloc) {
    return new BufferPoolConfiguration(cfg, maxMemAlloc, unitBufSize);
  }
  
  
  public int getUnitBufferSize() {
    return unitBufSize;
  }
  
  
  public BufferPoolConfiguration withUnitBufferSize(int unitBufSize) {
    return new BufferPoolConfiguration(cfg, maxMemAlloc, unitBufSize);
  }
  
  
  @Override
  public int getMaxReferenceCount() {
    return cfg.getMaxReferenceCount();
  }


  @Override
  public PoolConfiguration withMaxReferenceCount(int max) {
    return new BufferPoolConfiguration(cfg.withMaxReferenceCount(max), maxMemAlloc, unitBufSize);
  }


  @Override
  public int getMinAvailableCount() {
    return cfg.getMinAvailableCount();
  }


  @Override
  public PoolConfiguration withMinAllocatedCount(int min) {
    return new BufferPoolConfiguration(cfg.withMinAllocatedCount(min), maxMemAlloc, unitBufSize);
  }


  @Override
  public double getDeallocationPercentage() {
    return cfg.getDeallocationPercentage();
  }


  @Override
  public PoolConfiguration withDeallocationPercentage(double prc) {
    return new BufferPoolConfiguration(cfg.withDeallocationPercentage(prc), maxMemAlloc, unitBufSize);
  }


  @Override
  public int getInitialSize() {
    return cfg.getInitialSize();
  }


  @Override
  public PoolConfiguration withInitialSize(int init) {
    return new BufferPoolConfiguration(cfg.withInitialSize(init), maxMemAlloc, unitBufSize);
  }

}
