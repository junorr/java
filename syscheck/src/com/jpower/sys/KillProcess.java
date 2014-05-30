/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

import java.io.File;

/**
 *
 * @version 0.0 - 22/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class KillProcess {
  
  public static final String KEY_PID = "pid_to_kill";
  
  private Config conf;
  
  
  public KillProcess(File f) {
    if(f == null || !f.exists())
      throw new IllegalArgumentException(
          "Invalid file: "+ f);
    conf = new Config(f);
  }
  
  
  public void kill(int pid) {
    conf.put(KEY_PID, pid);
    conf.save();
  }

}
