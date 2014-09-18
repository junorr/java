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
 *
*/

package us.pserver.cdr;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;


/**
 * Codificador/Decodificador de arquivos.
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 25/06/2014
 */
public interface FileCoder {
  
  /**
   * Aplica (de)codificação (de acordo com o argumento 
   * <code>encode</code>) no arquivo <code>p1</code> informado,
   * escrevendo os dados modificados no arquivo <code>p2</code>.
   * O arquivo <code>p1</code> não é modificado.
   * Se o arquivo <code>p2</code> existir, será sobrescrito.
   * @param p1 Arquivo a ser (de)codificado.
   * @param p2 Arquivo (de)codificado.
   * @param encode <code>true</code> para codificar
   * o arquivo, <code>false</code> para decodifica-lo.
   * @return <code>true</code> se o método for bem
   * sucedido, <code>false</code> caso contrário.
   */
  public boolean apply(Path p1, Path p2, boolean encode);
  
  /**
   * Aplica (de)codificação (de acordo com o argumento 
   * <code>encode</code>) no buffer <code>buf</code> informado,
   * escrevendo os dados modificados no arquivo <code>p2</code>.
   * Os dados do buffer <code>buf</code> não são modificados.
   * Se o arquivo <code>p2</code> existir, será sobrescrito.
   * @param buf <code>ByteBuffer</code> cujos dados serão 
   * (de)codificados e escritos em arquivo.
   * @param p Arquivo (de)codificado.
   * @param encode <code>true</code> para codificar
   * o arquivo, <code>false</code> para decodifica-lo.
   * @return <code>true</code> se o método for bem
   * sucedido, <code>false</code> caso contrário.
   */
  public boolean applyFrom(ByteBuffer buf, Path p, boolean encode);
  
  /**
   * Aplica (de)codificação (de acordo com o argumento 
   * <code>encode</code>) no arquivo <code>p</code> informado,
   * escrevendo os dados modificados no <code>PrintStream ps</code>.
   * O arquivo <code>p</code> não é modificado.
   * @param p Arquivo a ser (de)codificado.
   * @param ps Saída onde os dados (de)codificados serão escritos.
   * @param encode <code>true</code> para codificar
   * o arquivo, <code>false</code> para decodifica-lo.
   * @return <code>true</code> se o método for bem
   * sucedido, <code>false</code> caso contrário.
   */
  public boolean applyTo(Path p, PrintStream ps, boolean encode);
  
  /**
   * Codifica o arquivo <code>p1</code> informado,
   * escrevendo os dados modificados no arquivo <code>p2</code>.
   * O arquivo <code>p1</code> não é modificado.
   * Se o arquivo <code>p2</code> existir, será sobrescrito.
   * @param p1 Arquivo a ser codificado.
   * @param p2 Arquivo codificado.
   * @return <code>true</code> se o método for bem
   * sucedido, <code>false</code> caso contrário.
   */
  public boolean encode(Path p1, Path p2);
  
  /**
   * Decodifica o arquivo <code>p1</code> informado,
   * escrevendo os dados modificados no arquivo <code>p2</code>.
   * O arquivo <code>p1</code> não é modificado.
   * Se o arquivo <code>p2</code> existir, será sobrescrito.
   * @param p1 Arquivo a ser decodificado.
   * @param p2 Arquivo decodificado.
   * @return <code>true</code> se o método for bem
   * sucedido, <code>false</code> caso contrário.
   */
  public boolean decode(Path p1, Path p2);
  
}
