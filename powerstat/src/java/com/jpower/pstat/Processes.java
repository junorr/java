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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.pstat;

import com.jpower.sys.CpuTaker;
import com.jpower.sys.KillProcess;
import com.jpower.sys.SysProcess;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/01/2013
 */
@ManagedBean
@SessionScoped
public class Processes implements Serializable {
  
  public static final File KILL_FILE = 
      new File("/opt/syscheck/processKiller.conf");
  
  private CpuTaker ctk;
  
  private List<SysProcess> prcs;
  
  private SysProcess selected;
  
  private KillProcess klr;
  
  
  public Processes() {
    ctk = new CpuTaker();
    klr = new KillProcess(KILL_FILE);
    prcs = null;
    selected = null;
    this.update();
  }
  
  
  public List<SysProcess> getPrcs() {
    return prcs;
  }
  
  
  public void setSelected(SysProcess sp) {
    selected = sp;
  }
  
  
  public SysProcess getSelected() {
    return selected;
  }
  
  
  public String update() {
    ctk.take();
    prcs = ctk.getProcesses();
    return "new-processes.xhtml";
  }
  
  
  public String kill() {
    if(selected == null)
      return null;
    klr.kill(selected.getPid());
    return this.update();
  }

}
