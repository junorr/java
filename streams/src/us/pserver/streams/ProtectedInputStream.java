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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import static us.pserver.chk.Checker.nullarg;

/**
 * <code>FilterInputStream</code> que sobrescreve o método
 * {@link java.io.InputStream#close() } para que não
 * faça nada, impedindo que o stream encapsulado seja fechado.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/08/2014
 */
public class ProtectedInputStream extends FilterInputStream {

  private InputStream input;
  
  
  /**
   * Construtor padrão que recebe o <code>InputStream</code>
   * encapsulado.
   * @param is <code>InputStream</code> encapsulado.
   */
  public ProtectedInputStream(InputStream is) {
    super(is);
    setInputStream(is);
  }
  

  /**
   * Define o <code>InputStream</code> encapsulado.
   * @param is <code>InputStream</code> encapsulado.
   * @return Esta instância modificada de <code>ProtectedInputStream</code>.
   */
  public ProtectedInputStream setInputStream(InputStream is) {
    nullarg(InputStream.class, is);
    input = is;
    return this;
  }
  
  
  /**
   * Retorna o <code>InputStream</code> encapsulado.
   * @return <code>InputStream</code> encapsulado.
   */
  public InputStream getInputStream() {
    return input;
  }
  
  
  /**
   * Não faz nada.
   * @throws IOException Nunca é lançado.
   */
  @Override
  public void close() throws IOException {}
  
  
  /**
   * Fecha o <code>InputStream</code> encapsulado,
   * invocando {@link java.io.InputStream#close() }.
   * @return Esta instância modificada de <code>ProtectedInputStream</code>.
   * @throws IOException Caso ocorra erro ao fechar.
   */
  public ProtectedInputStream forceClose() throws IOException {
    super.close();
    input.close();
    return this;
  }
  
}
