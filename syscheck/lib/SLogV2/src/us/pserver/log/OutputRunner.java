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

package us.pserver.log;

/**
 * Implementação de <code>Runnable</code>
 * para gerar a saída de log pelo objeto 
 * <code>LogOutput</code> em <code>Threads</code> específicas.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2014
 */
public class OutputRunner implements Runnable {

  private LogOutput output;
  
  private LogLevel level;
  
  private String message;
  
  
  /**
   * Construtor padrão que recebe um objeto
   * <code>LogOutput</code> onde será gerada a saída de log,
   * além da mensagem e nível de log.
   * @param out <code>LogOutput</code>.
   * @param msg Mensagem de log.
   * @param lvl Nível de log.
   */
  public OutputRunner(LogOutput out, String msg, LogLevel lvl) {
    if(out == null)
      throw new IllegalArgumentException(
          "Invalid LogOutput: "+ out);
    if(msg == null)
      throw new IllegalArgumentException(
          "Invalid Message: "+ msg);
    if(lvl == null)
      throw new IllegalArgumentException(
          "Invalid LogLevel: "+ lvl);
    output = out;
    level = lvl;
    message = msg;
  }
  
  
  @Override
  public void run() {
    output.log(message, level);
  }
  
}