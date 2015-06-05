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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

/**
 * Fábrica de saídas de stream
 * <code>OutputStream</code> e 
 * <code>PrintStream</code>,
 * além de método utilitário para
 * criação de <code>LogOutput</code>
 * com saída de stream personalizada.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 19/04/2014
 */
public class StreamFactory {

  
  /**
   * Cria um <code>OutputStream</code> cuja
   * saída de log se dará pelo objeto
   * <code>Consumer&lt;Character&gt;</code>
   * informado.
   * @param cs <code>Consumer&lt;Character&gt;</code>.
   * @return Objeto <code>OutputStream</code>
   * com saída de dados personalizada por
   * <code>Consumer&lt;Character&gt;</code>.
   */
  public static OutputStream createOutputStream(Consumer<Character> cs) {
    if(cs == null)
      throw new IllegalArgumentException(
          "Invalid Consumer: "+ cs);
    return new OutputStream() {
      @Override 
      public void write(int b) throws IOException {
        cs.accept((char) b);
      }
    };
  }
  
  
  /**
   * Cria um <code>PrintStream</code> cuja
   * saída de log se dará pelo objeto
   * <code>Consumer&lt;Character&gt;</code>
   * informado.
   * @param cs <code>Consumer&lt;Character&gt;</code>.
   * @return Objeto <code>PrintStream</code>
   * com saída de dados personalizada por
   * <code>Consumer&lt;Character&gt;</code>.
   */
  public static PrintStream createPrintStream(Consumer<Character> cs) {
    return new PrintStream(createOutputStream(cs));
  }
  
  
  /**
   * Cria um <code>LogOutput</code> cuja
   * saída de log se dará pelo objeto
   * <code>Consumer&lt;Character&gt;</code>
   * informado.
   * @param cs <code>Consumer&lt;Character&gt;</code>.
   * @return Objeto <code>LogOutput</code>
   * com saída de log personalizada por
   * <code>Consumer&lt;Character&gt;</code>.
   */
  public static BasicLogOutput createLogOutput(Consumer<Character> cs) {
    return new BasicLogOutput().setOutputStream(
        createOutputStream(cs));
  }
  
}
