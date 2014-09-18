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

package com.jpower.sys;

import java.io.Serializable;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 29/09/2012
 */
public class Memory implements Serializable {

  private int 
      total,
      used,
      free,
      cached;
  
  
  public Memory() {
    total = used = free = cached = 0; 
  }


  public int getTotal() {
    return total;
  }


  public Memory setTotal(int total) {
    this.total = total;
    return this;
  }


  public int getUsed() {
    return used;
  }


  public Memory setUsed(int used) {
    this.used = used;
    return this;
  }


  public int getFree() {
    return free;
  }


  public Memory setFree(int free) {
    this.free = free;
    return this;
  }


  public int getCached() {
    return cached;
  }


  public Memory setCached(int cached) {
    this.cached = cached;
    return this;
  }
  
  
  public double freePerc() {
    return ((double) free) / total * 100.0;
  }
  
  
  public double usedPerc() {
    return ((double) used) / total * 100.0;
  }
  
  
  public double cachedPerc() {
    return ((double) cached) / total * 100.0;
  }

  
  public String toString() {
    return "[Mem: total: "+ total
        + ", used: "+ used
        + ", cached: "+ cached
        + ", free: "+ free+ "]";
  }
  
}
