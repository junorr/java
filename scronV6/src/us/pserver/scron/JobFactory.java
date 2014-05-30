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

package us.pserver.scron;

import us.pserver.scron.Job;

/**
 * Fábrica de <code>Jobs</code> a partir de objetos <code>Runnable</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/04/2014
 * @see java.lang.Runnable
 */
public abstract class JobFactory {

  
  /**
   * <code>Job</code> implementado a partir de <code>Runnable</code>.
   */
  static class RunnableJob implements Job {
    private Runnable run;
    public RunnableJob(Runnable r) {
      run = r;
    }
    @Override
    public void execute(ExecutionContext context) {
      run.run();
    }
    @Override
    public void error(Throwable th) {
      th.printStackTrace();
    }
  }
  
  
  /**
   * Cria um <code>Job</code> a partir do
   * objeto <code>Runnable</code> informado.
   * @param r Objeto <code>Runnable</code>.
   * @return <code>Job</code> criado.
   */
  public static Job create(Runnable r) {
    if(r == null)
      throw new IllegalArgumentException(
          "Invalid Runnable: "+ r);
    return new RunnableJob(r);
  }
  
}
