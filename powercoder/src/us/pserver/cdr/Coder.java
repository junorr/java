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


/**
 * Codificador/Decodificador de formatos.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 20/06/2014
 * @param <T> Tipo de abjeto a ser codificado.
 */
public interface Coder<T> {
  
  /**
   * Aplica (de)codificação (de acordo 
   * com o argumento <code>encode</code>) no objeto
   * informado.
   * @param t Objeto a ser (de)codificado.
   * @param encode <code>true</code> para codificar
   * o objeto, <code>false</code> para decodifica-lo.
   * @return Outro objeto do mesmo tipo com a (de)codificação 
   * aplicada.
   */
  public T apply(T t, boolean encode);
  
  /**
   * Codifica o objeto <code>t</code>.
   * @param t Objeto a ser codificado.
   * @return Objeto codificado.
   */
  public T encode(T t);
  
  /**
   * Decodifica o objeto <code>t</code>.
   * @param t Objeto a ser decodificado.
   * @return Objeto decodificado.
   */
  public T decode(T t);
  
}
