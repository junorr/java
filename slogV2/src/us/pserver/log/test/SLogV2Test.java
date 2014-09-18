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

package us.pserver.log.test;

import us.pserver.log.SLogV2;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2014
 */
public class SLogV2Test {

  public static void runFor(int times, Runnable r) {
    for(int i = 0; i < times; i++) {
      r.run();
    }
  }
   
  public static void main(String[] args) {
    SLogV2 log = new SLogV2()
        .allocFileOutputExecutor();
    
    runFor(5, ()-> log.debug("This is a Debug Message."));
    runFor(5, ()-> log.info("This is a Info Message."));
    runFor(5, ()-> log.warning("This is a Warning Message."));
    runFor(5, ()-> log.error("This is a Error Message."));
    runFor(5, ()-> log.fatal("This is a Fatal Message."));
    log.stop();
  }
  
}