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

package us.pserver.streams;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static us.pserver.chk.Checker.nullarg;

/**
 * <code>FilterOutputStream</code> que sobrescreve o método
 * {@link java.io.OutputStream#close() } para que não
 * faça nada, impedindo que o stream encapsulado seja fechado.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/08/2014
 */
public class ProtectedOutputStream extends FilterOutputStream {

  private OutputStream output;
  
  
  /**
   * Construtor padrão que recebe o <code>OutputStream</code>
   * encapsulado.
   * @param os <code>OutputStream</code> encapsulado.
   */
  public ProtectedOutputStream(OutputStream os) {
    super(os);
    setOutputStream(os);
  }
  
  
  /**
   * Define o <code>OutputStream</code> encapsulado.
   * @param os <code>OutputStream</code> encapsulado.
   * @return Esta instância modificada de <code>ProtectedInputStream</code>.
   */
  public ProtectedOutputStream setOutputStream(OutputStream os) {
    nullarg(OutputStream.class, os);
    output = os;
    return this;
  }
  
  
  /**
   * Retorna o <code>OutputStream</code> encapsulado.
   * @return <code>OutputStream</code> encapsulado.
   */
  public OutputStream getOutputStream() {
    return output;
  }
  
  
  /**
   * Não faz nada.
   * @throws IOException Nunca é lançado.
   */
  @Override
  public void close() throws IOException {
    output.flush();
  }
  
  
  /**
   * Fecha o <code>OutputStream</code> encapsulado,
   * invocando {@link java.io.OutputStream#close() }.
   * @return Esta instância modificada de <code>ProtectedOutputStream</code>.
   * @throws IOException Caso ocorra erro ao fechar.
   */
  public ProtectedOutputStream forceClose() throws IOException {
    super.flush();
    super.close();
    output.flush();
    output.close();
    return this;
  }
  
}
