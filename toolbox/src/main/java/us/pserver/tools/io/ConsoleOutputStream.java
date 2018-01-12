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

package us.pserver.tools.io;

import java.io.IOException;
import java.io.OutputStream;
import us.pserver.tools.Match;

/**
 * <code>OutputStream</code> de descarte de dados.
 * Os dados escritos neste OutputStream são descartados.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2014
 */
public class ConsoleOutputStream extends OutputStream {
  
  private final OutputStream out;
  
  /**
   * Default constructor.
   * @param out Next OutputStream. Cannot be null.
   */
  private ConsoleOutputStream(OutputStream out) {
    this.out = Match.notNull(out).getOrFail("Bad null OutputStream");
  }
  
  
  /**
   * Close the underlying OutputStream.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    out.close();
  }
  
  
  /**
   * Flush the underlying OutputStream.
   * @throws IOException
   */
  @Override
  public void flush() throws IOException {
    out.flush();
  }
  

  /**
   * Descarta o byte informado.
   * @param b byte a ser descartado.
   * @throws IOException Caso ocorra erro na escrita.
   */
  @Override
  public void write(int b) throws IOException {
    System.out.write(b);
    out.write(b);
  }
  
}
