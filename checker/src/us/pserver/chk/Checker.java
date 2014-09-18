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

package us.pserver.chk;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Classe utilitária para verificação de argumentos.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 20/06/2014
 */
public class Checker {

  
  /**
   * Lança uma exceção do tipo <code>IllegalArgumentException</code>
   * com a mensagem <code>"Invalid "+ typ.getName()+" ["+ arg+ "]"</code>.
   * @param typ Tipo do argumento informado.
   * @param arg Argumento inválido.
   */
  public static void throwarg(Class typ, Object arg) {
    throw new IllegalArgumentException(
        "Invalid "+ typ.getName()+" ["+ arg+ "]");
  }
  
  
  /**
   * Verifica se o objeto <code>arg</code> é nulo 
   * <code>(arg == null)</code>, lançando uma exceção 
   * <code>IllegalArgumentException</code> em caso positivo.
   * @param typ Tipo do argumento testado.
   * @param arg Objeto a ser testado contra <code>null</code>.
   */
  public static void nullarg(Class typ, Object arg) {
    if(arg == null) throwarg(typ, arg);
  }
  
  
  public static void nullpath(Path p) {
    if(p == null || !Files.exists(p))
      throwarg(Path.class, p);
  }
  
  
  /**
   * Verifica se a <code>String arg</code> é nula ou vazia
   * <code>(arg == null || arg.isEmpty())</code>, lançando uma exceção 
   * <code>IllegalArgumentException</code> em caso positivo.
   * @param arg String a ser testada.
   */
  public static void nullstr(String arg) {
    if(arg == null || arg.isEmpty())
      throwarg(String.class, arg);
  }
  
  
  /**
   * Verifica se a <code>ByteBuffer buf</code> é nulo ou 
   * possui zero elementos restantes 
   * <code>(buf == null || buf.remaining() == 0)</code>, 
   * lançando uma exceção <code>IllegalArgumentException</code> 
   * em caso positivo.
   * @param buf <code>ByteBuffer</code> a ser testado.
   */
  public static void nullbuffer(ByteBuffer buf) {
    if(buf == null || buf.remaining() == 0)
      throw new IllegalArgumentException(
          "Invalid ByteBuffer [buf="
          + (buf == null ? buf : buf.remaining())+ "]");
  }
  
  
  /**
   * Verifica se o byte array <code>buf</code> é nulo ou 
   * possui tamanho zero <code>(buf == null || buf.length == 0)</code>,
   * lançando uma exceção <code>IllegalArgumentException</code> 
   * em caso positivo.
   * @param buf <code>byte[]</code> a ser testado.
   */
  public static void nullarray(byte[] buf) {
    if(buf == null || buf.length == 0)
      throw new IllegalArgumentException(
          "Invalid byte array [buf="
          + (buf == null ? buf : buf.length)+ "]");
  }
  
  
  /**
   * Verifica se o número <code>num</code> é negativo,
   * lançando uma exceção <code>IllegalArgumentException</code>
   * em caso positivo.
   * @param num Número a ser testado.
   */
  public static void negative(Number num) {
    if(num.intValue() < 0)
      throw new IllegalArgumentException(
          "Invalid negative number ["+ num+ "]");
  }
  
  
  /**
   * Verifica se o número <code>num</code> é igual a zero,
   * lançando uma exceção <code>IllegalArgumentException</code>
   * em caso positivo.
   * @param num Número a ser testado.
   */
  public static void zero(Number num) {
    if(num.intValue() <= 0)
      throw new IllegalArgumentException(
          "Invalid zero/negative number ["+ num+ "]");
  }
  
  
  /**
   * Verifica o número <code>num</code> está entre (exclusivo) 
   * os argumentos <code>(min e max)</code> informados.
   * Ex: se <code>min=0</code> e <code>max=10</code>,
   * o argumento deve ser <code>num &gt; 0 &amp;&amp; num &lt; 10</code>.
   * Este método utiliza <code>Number.doubleValue() : double</code> 
   * para testar os argumentos.
   * @param num Número a ser testado.
   * @param min Valor mínimo (exclusivo).
   * @param max Valor máximo (exclusivo).
   */
  public static void range(Number num, Number min, Number max) {
    if(num.doubleValue() < min.doubleValue() 
        || num.doubleValue() > max.doubleValue())
      throw new IllegalArgumentException(
          "Number out of range ["+ min
          + " > " + num+ " < "+ max+ "]");
  }
  
  
  /**
   * Verifica o número <code>num</code> está entre (exclusivo) 
   * os argumentos <code>(min e max)</code> informados.
   * Ex: se <code>min=0</code> e <code>max=10</code>,
   * o argumento deve ser <code>num &gt; 0 &amp;&amp; num &lt; 10</code>.
   * @param num Número a ser testado.
   * @param min Valor mínimo (exclusivo).
   * @param max Valor máximo (exclusivo).
   */
  public static void range(int num, int min, int max) {
    if(num < min || num > max)
      throw new IllegalArgumentException(
          "Number out of range ["+ min
          + " > " + num+ " < "+ max+ "]");
  }
  
}
