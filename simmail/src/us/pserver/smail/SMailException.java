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

package us.pserver.smail;

import us.pserver.smail.internal.InfiniteBuffer;
import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * <p style="font-size: medium;">
 * Exceção a ser lançada pelas classes do pacote
 * em caso de erros.
 * </p>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class SMailException extends Exception {

  
  /**
   * Construtor padrão sem argumentos.
   */
  public SMailException() {
    super();
  }
  
  
  /**
   * Construtor que recebe a mensagem de erro especifica.
   * @param message Mensagem de erro.
   */
  public SMailException(String message) {
    super(message);
  }
  
  
  /**
   * Construtor que recebe a causa da exceção lançada.
   * @param cause Causa da exceção.
   */
  public SMailException(Throwable cause) {
    super(cause);
  }
  
  
  /**
   * Construtor que recebe a mensagem de erro específica e 
   * a causa da exceção lançada.
   * @param message Mensagem de erro específica.
   * @param cause Causa da exceção lançada.
   */
  public SMailException(String message, Throwable cause) {
    super(message, cause);
  }
  
  
  /**
   * Retorna uma String com o rastreamento de
   * pilha de erro.
   * @return Rastreamento de pilha de erro.
   */
  public String getStringTrace() {
    InfiniteBuffer buf = new InfiniteBuffer();
    PrintStream st = new PrintStream(buf.getOutput());
    this.printStackTrace(st);
    return new String(buf.toArray());
  }
  
  
  public static void main(String[] args) {
    InfiniteBuffer buf = new InfiniteBuffer();
    new PrintWriter(buf.getOutput());
    SMailException ex = new SMailException(
        "Blow Up!", new Exception("Blow cause"));
    try {
      throw ex;
    } catch(SMailException ex1) {
      System.out.println(ex.getStringTrace());
    }
  }
  
}
