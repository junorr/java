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
 * @version 0.0 - 22/08/2012
 */
public class SysProcess implements Serializable {
  
  private int pid;
  
  private String user;
  
  private String name;
  
  private double cpu;
  
  private double mem;
  
  private String uptime;
  
  
  public SysProcess() {
    pid = -1;
    user = null;
    name = null;
    cpu = 0;
    mem = 0;
    uptime = null;
  }

  
  public int getPid() {
    return pid;
  }
  
  
  public SysProcess setPid(int pid) {
    if(pid > 0)
      this.pid = pid;
    return this;
  }

  
  public String getUser() {
    return user;
  }

  
  public SysProcess setUser(String user) {
    if(user != null && !user.trim().isEmpty())
      this.user = user;
    return this;
  }

  
  public String getName() {
    return name;
  }

  
  public SysProcess setName(String name) {
    if(name != null && !name.trim().isEmpty())
      this.name = name;
    return this;
  }

  
  public double getCpu() {
    return cpu;
  }

  
  public SysProcess setCpu(double cpu) {
    if(cpu >= 0)
      this.cpu = cpu;
    return this;
  }

  
  public double getMem() {
    return mem;
  }

  
  public SysProcess setMem(double mem) {
    if(mem >= 0)
      this.mem = mem;
    return this;
  }

  
  public String getUptime() {
    return uptime;
  }

  
  public SysProcess setUptime(String uptime) {
    if(uptime != null && !uptime.trim().isEmpty())
      this.uptime = uptime;
    return this;
  }
  
  
  public String toString() {
    return "[PID: "+ pid+ " - "+ name+ ", cpu: "+ cpu+ "%, mem: "+ mem+ "%, user: "+ user+"]";
  }
  
}
