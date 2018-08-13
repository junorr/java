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

import us.pserver.jpx.pool.PoolConfiguration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/08/2018
 */
public class DefaultPoolConfiguration implements PoolConfiguration {
  
  public static final int DEFAULT_INIT_SIZE = 1;
  
  public static final int DEFAULT_MAX_REF_COUNT = 50;
  
  public static final int DEFAULT_MIN_AVL_COUNT = 5;
  
  public static final double DEFAULT_DEALLOC_PERCENTAGE = 0.75;
  
  
  private final int maxRefCount;
  
  private final int minAvlCount;
  
  private final double deallocPerc;
  
  private final int initSize;


  public DefaultPoolConfiguration(int initSize, int maxRefCount, int minAvlCount, double deallocPerc) {
    this.maxRefCount = maxRefCount;
    this.minAvlCount = minAvlCount;
    this.deallocPerc = deallocPerc;
    this.initSize = initSize;
  }
  
  public DefaultPoolConfiguration() {
    this(DEFAULT_INIT_SIZE, DEFAULT_MAX_REF_COUNT, DEFAULT_MIN_AVL_COUNT, DEFAULT_DEALLOC_PERCENTAGE);
  }

  @Override
  public int getMaxReferenceCount() {
    return maxRefCount;
  }


  @Override
  public PoolConfiguration withMaxReferenceCount(int maxRefCount) {
    return new DefaultPoolConfiguration(initSize, maxRefCount, minAvlCount, deallocPerc);
  }


  @Override
  public int getMinAvailableCount() {
    return minAvlCount;
  }


  @Override
  public PoolConfiguration withMinAllocatedCount(int minAvlCount) {
    return new DefaultPoolConfiguration(initSize, maxRefCount, minAvlCount, deallocPerc);
  }


  @Override
  public double getDeallocationPercentage() {
    return deallocPerc;
  }


  @Override
  public PoolConfiguration withDeallocationPercentage(double deallocPerc) {
    return new DefaultPoolConfiguration(initSize, maxRefCount, minAvlCount, deallocPerc);
  }


  @Override
  public int getInitialSize() {
    return initSize;
  }


  @Override
  public PoolConfiguration withInitialSize(int iniSize) {
    return new DefaultPoolConfiguration(initSize, maxRefCount, minAvlCount, deallocPerc);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + this.maxRefCount;
    hash = 29 * hash + this.minAvlCount;
    hash = 29 * hash + (int) (Double.doubleToLongBits(this.deallocPerc) ^ (Double.doubleToLongBits(this.deallocPerc) >>> 32));
    hash = 29 * hash + this.initSize;
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
    final DefaultPoolConfiguration other = (DefaultPoolConfiguration) obj;
    if (this.maxRefCount != other.maxRefCount) {
      return false;
    }
    if (this.minAvlCount != other.minAvlCount) {
      return false;
    }
    if (Double.doubleToLongBits(this.deallocPerc) != Double.doubleToLongBits(other.deallocPerc)) {
      return false;
    }
    if (this.initSize != other.initSize) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "PoolConfiguration{" + "\n  - maxRefCount=" + maxRefCount + "\n  - minAvlCount=" + minAvlCount + "\n  - deallocPerc=" + deallocPerc + "\n  - initSize=" + initSize + "\n}";
  }
  
}
