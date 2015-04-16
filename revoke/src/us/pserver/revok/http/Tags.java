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

package us.pserver.revok.http;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/04/2015
 */
public interface Tags {

  /**
   * <code>
   *  START_CRYPT_KEY = "<ckey>"
   * </code><br>
   * XML Tag for CryptKey.
   */
  public static final String START_CRYPT_KEY = "<ckey>";
  
  /**
   * <code>
   *  END_CRYPT_KEY = "</ckey>"
   * </code><br>
   * Close XML Tag for CryptKey.
   */
  public static final String END_CRYPT_KEY = "</ckey>";
  
  /**
   * <code>
   *  START_XML = "<xml>"
   * </code><br>
   * XML Tag.
   */
  public static final String START_XML = "<xml>";
  
  /**
   * <code>
   *  END_XML = "</xml>"
   * </code><br>
   * Close XML Tag.
   */
  public static final String END_XML = "</xml>";
  
  /**
   * <code>
   *  START_CONTENT = "<cnt>"
   * </code><br>
   * XML Tag for Content.
   */
  public static final String START_CONTENT = "<cnt>";
  
  /**
   * <code>
   *  END_CONTENT = "</cnt>"
   * </code><br>
   * Close XML Tag for Content.
   */
  public static final String END_CONTENT = "</cnt>";
  
  /**
   * <code>
   *  START_ROB = "<rob>"
   * </code><br>
   * XML Tag for ROB.
   */
  public static final String START_ROB = "<rob>";
  
  /**
   * <code>
   *  END_ROB = "</rob>"
   * </code><br>
   * Close XML Tag for ROB.
   */
  public static final String END_ROB = "</rob>";
  
  /**
   * <code>
   *  START_STREAM = "<stream>"
   * </code><br>
   * XML Tag for Stream.
   */
  public static final String START_STREAM = "<stream>";
  
  /**
   * <code>
   *  END_STREAM = "</stream>"
   * </code><br>
   * Close XML Tag for Stream.
   */
  public static final String END_STREAM = "</stream>";
  
  /**
   * <code>
   *  GT = ">"
   * </code><br>
   * String for Greater char.
   */
  public static final String GT = ">";
  
}
