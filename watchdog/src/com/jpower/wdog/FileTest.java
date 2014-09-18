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

package com.jpower.wdog;

import java.util.Date;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/08/2012
 */
public class FileTest {
  
  public static void main(String[] args) {
    ChangeListener change = new ChangeListener() {
      @Override
      public void stateChanged(Date time, Object data) {
        FileChangeWatcher fw = (FileChangeWatcher) data;
        System.out.println("* File State Changed ("+ fw.getFile()+ ")!");
        System.out.println("* Last Modified in "+ fw.lasModified());
      }
    };
    
    FileChangeWatcher w = new FileChangeWatcher("./watchdog.txt");
    FileChangeWatcher w2 = new FileChangeWatcher("./watchdog.txt");
    
    MultiWatchdog dog = new MultiWatchdog(5000);
    dog.addWatcher(w).addWatcher(w2)
        .addListener(change)
        .startWatcher();
    try {
      Thread.sleep(30000);
    } catch(InterruptedException e) {}
    System.out.println("* Stopping Watchdog...");
    dog.stopWatcher();
  }

}
