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

package us.pserver.smail.event;

import java.io.PrintStream;


/**
 * <p style="font-size: medium;">
 * Classe utilitária que implementa 
 * <code>ConnectionListener</code> e imprime 
 * todos os eventos gerados no <code>PrintStream</code>
 * informado no construtor.
 * </p>
 *
 * @see us.pserver.smail.SMail
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class PrinterConnectionListener implements ConnectionListener {

  private PrintStream ps = null;
  
  
  /**
   * Construtor padrão que recebe o <code>PrintStream</code>
   * no qual será impresso todos os eventos recebidos.
   * @param p PrintStrem onde serão impressos os eventos.
   */
  public PrinterConnectionListener(PrintStream p) {
    this.ps = p;
  } 
  

  @Override
  public void connecting(ConnectionEvent event) {
    ps.println(" * Connecting...        ["+event.getWhen()+"]");
  }


  @Override
  public void connected(ConnectionEvent event) {
    ps.println(" * Connected...         ["+event.getWhen()+"]");
  }

}
