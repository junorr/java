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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/08/2012
 */
public class Cpu implements Serializable {
  
  private double user;
  
  private double sys;
  
  private double idle;
  
  private double iowait;
  
  private double hardi;
  
  private double softi;
  
  private int cores;
  
  private String arch;
  
  private String mode;
  
  private int threads;
  
  private String id;
  
  private double mhz;
  
  private List<SysProcess> prcs;
  
  
  public Cpu() {
    cores = 1;
    user = 0;
    sys = 0;
    idle = 0;
    iowait = 0;
    prcs = new LinkedList<>();
  }

  public double getUser() {
    return user;
  }

  public Cpu setUser(double user) {
    if(user >= 0)
      this.user = user;
    return this;
  }

  public double getSys() {
    return sys;
  }

  public Cpu setSys(double sys) {
    if(sys >= 0)
      this.sys = sys;
    return this;
  }

  public double getIddle() {
    return idle;
  }

  public Cpu setIddle(double iddle) {
    if(iddle >= 0)
      this.idle = iddle;
    return this;
  }

  public double getIowait() {
    return iowait;
  }

  public Cpu setIowait(double iowait) {
    if(iowait >= 0)
      this.iowait = iowait;
    return this;
  }
  
  
  public double getHardInterruption() {
    return hardi;
  }
  
  
  public Cpu setHardInterruption(double d) {
    if(d >= 0)
      hardi = d;
    return this;
  }
  
  
  public double getSoftInterruption() {
    return softi;
  }
  
  
  public Cpu setSoftInterruption(double d) {
    if(d >= 0)
      softi = d;
    return this;
  }
  
  
  public int getCores() {
    return cores;
  }
  
  
  public Cpu setCores(int num) {
    if(num > 0)
      cores = num;
    return this;
  }

  public String getArchitecture() {
    return arch;
  }

  public Cpu setArchitecture(String arch) {
    if(arch != null && !arch.trim().isEmpty())
      this.arch = arch.trim();
    return this;
  }

  public String getMode() {
    return mode;
  }

  public Cpu setMode(String mode) {
    if(mode != null && !mode.trim().isEmpty())
      this.mode = mode.trim();
    return this;
  }

  public int getThreadsPerCore() {
    return threads;
  }

  public Cpu setThreadsPerCore(int threads) {
    if(threads > 0)
      this.threads = threads;
    return this;
  }

  public String getId() {
    return id;
  }

  public Cpu setId(String manufact) {
    if(manufact != null && !manufact.trim().isEmpty())
      this.id = manufact.trim();
    return this;
  }

  public double getMhz() {
    return mhz;
  }

  public Cpu setMhz(double mhz) {
    if(mhz > 0)
      this.mhz = mhz;
    return this;
  }
  
  
  public double getUsedCpu() {
    return user + sys + hardi + softi + iowait;
  }
  
  
  public double getIddleCpu() {
    return 100 - getUsedCpu();
  }


  public List<SysProcess> getProcesses() {
    return prcs;
  }


  public void setProcesses(List<SysProcess> prcs) {
    this.prcs = prcs;
  }

  
  public String toString() {
    return "[CPU: mode: "+ mode+", ID: "+ id+ "]\n"
        +  "[CPU: arch: "+ arch+ ", MHz: "+ mhz+ ", cores: "+ cores+ ", threads/core: "+ threads+ "]\n"
        +  "[CPU: usedCPU: "+ getUsedCpu()+ "% (us: "+ user+ "%, sy: "+ sys+ "%), iddleCPU: "+ getIddleCpu()+ "%]\n"
        +  "[CPU: wa: "+ iowait+ "%, id: "+ idle+ "%, hardi: "+ hardi+ "%, softi: "+ softi+ "%]";
  }
  
}
