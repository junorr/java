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

package us.pserver.tmo;

import us.pserver.scron.Job;
import us.pserver.scronv6.hide.Pair;
import us.pserver.scron.Schedule;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/04/2014
 */
public class ScriptPair extends Pair {

  private ScriptJob job;
  
  
  public ScriptPair() {
    super();
  }
  
  
  public ScriptPair(Schedule s, ScriptJob j) {
    super(s, j);
    job = j;
  }
  
  
  public ScriptPair(Pair p) {
    if(p != null) {
      this.schedule(p.schedule())
          .job(p.job());
    }
  }
  
  
  @Override
  public ScriptPair job(Job j) {
    super.job(j);
    if(j != null && j instanceof ScriptJob)
      job = (ScriptJob) j;
    return this;
  }
  
  
  @Override
  public ScriptJob job() {
    return job;
  }
  
}
