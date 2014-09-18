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

package com.pserver.isys;

import us.pserver.log.Log;
import us.pserver.log.SLogV2;
import us.pserver.scron.ExecutionContext;
import us.pserver.scron.Job;
import us.pserver.scronv6.SCronV6;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 14/08/2013
 */
public abstract class BasicJob implements Job {

  public static final String DEFAULT_COMMAND = "cmd";
  
  
  protected Log log;
  
  protected EmailParser parser;
  
  protected SCronV6 scron;
  
  
  @Override
  public void execute(ExecutionContext context) throws Exception {
    if(context != null) {
      scron = (SCronV6) context.dataMap().get(
          SCronV6.class.getSimpleName());
      log = (Log) context.dataMap()
          .get(EmailParser.KEY_LOGGER);
      parser = (EmailParser) context.dataMap()
          .get(EmailParser.KEY_EMAILPARSER);
    }
  }


  @Override
  public void error(Throwable th) {
    log.error(th.toString());
  }
  
}
