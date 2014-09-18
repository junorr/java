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

package us.pserver.scronv6;

import us.pserver.scron.ExecutionContext;
import us.pserver.scronv6.hide.Pair;
import us.pserver.log.Log;
import us.pserver.scron.AbstractCron;


/**
 * Executor de <code>Jobs</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/04/2014
 */
public class Executor implements Runnable {
  
  private final Object O = new Object();

  private Pair pair;
  
  private ExecutionContext context;
  
  private Log log;
  
  
  /**
   * Construtor padrão que recebe um <code>Pair</code>
   * com <code>Schedule e Job</code>, além do
   * contexto de execução <code>ExecutionContext</code>.
   * @param p <code>Pair</code>.
   * @param c <code>ExecutionContext</code>.
   * @see us.pserver.scronv6.hide.Pair
   * @see us.pserver.scron.ExecutionContext
   */
  public Executor(Pair p, ExecutionContext c) {
    if(p == null)
      throw new IllegalArgumentException(
          "Invalid Pair: "+ p);
    if(p.schedule() == null)
      throw new IllegalArgumentException(
          "Invalid Schedule: "+ p.schedule());
    if(p.job() == null)
      throw new IllegalArgumentException(
          "Invalid Job: "+ p.job());
    if(c == null)
      throw new IllegalArgumentException(
          "Invalid ExecutionContext: "+ c);
    
    pair = p;
    context = c;
    log = (Log) c.dataMap()
        .get(AbstractCron.KEY_LOGGER);
  }
  
  
  /**
   * Pausa a <code>Thread</code> corrente
   * pelo tempo especificado em milisegundos.
   * @param millis tempo em milisegundos.
   */
  void delay(long millis) {
    if(millis <= 1) return;
    try {
      synchronized(O) {
        O.wait(millis);
      }
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  
  /**
   * Executa o Job no momento agendado.
   */
  @Override
  public void run() {
    if(!pair.schedule().isValid())
      return;
    
    delay(pair.schedule().getCountdown());
    try {
      log.debug("Executing: "+ pair.toString());
      pair.job().execute(context);
    } catch(Exception e) {
      log.error("Error executing Job: "+ e.toString());
      pair.job().error(e);
    }
  }
  
}
