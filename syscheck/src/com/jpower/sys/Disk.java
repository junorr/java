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
 * @version 0.0 - 06/09/2012
 */
public class Disk implements Serializable {
  
  private String mount;
  
  private String dev;
  
  private String fs;
  
  private long used;
  
  private long free;
  
  
  public Disk() {
    mount = "";
    dev = "";
    fs = null;
    used = 0;
    free = 1;
  }
  
  
  public Disk(String local, String alias) {
    this.mount = local;
    this.dev = alias;
  }
  
  
  public String getMountPoint() {
    return mount;
  }
  
  
  public Disk setMountPoint(String s) {
    if(s != null && !s.trim().isEmpty())
      mount = s;
    return this;
  }
  
  
  public String getDevice() {
    return dev;
  }
  
  
  public Disk setDevice(String s) {
    if(s != null && !s.trim().isEmpty())
      dev = s;
    return this;
  }
  
  
  public long getUsedSpace() {
    return used;
  }
  
  
  public Disk setUsedSpace(long u) {
    if(u >= 0)
      used = u;
    return this;
  }
  
  
  public long getFreeSpace() {
    return free;
  }
  
  
  public Disk setFreeSpace(long f) {
    if(f >= 0)
      free = f;
    return this;
  }


  public String getFileSystem() {
    return fs;
  }


  public Disk setFileSystem(String fs) {
    this.fs = fs;
    return this;
  }
  
  
  public long getTotalSpace() {
    return used + free;
  }
  
  
  public double getUsedPerc() {
    return (double) ((int) ((used / ((double) getTotalSpace())) * 10000)) / 100;
  }
  
  
  @Override
  public String toString() {
    return "Disk ["+ dev + ", fs=" + fs + " ("+ mount + "), used: "+ getUsedPerc()+ "%]";
  }

}
