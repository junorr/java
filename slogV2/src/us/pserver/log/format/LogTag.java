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

package us.pserver.log.format;

/**
 * Represents a mark on the log string pattern format, 
 * that will be interpreted as a log property like 
 * date, level, name and message.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public enum LogTag {

  MESSAGE("{MESSAGE}"),
  
  DATE("{DATE}"),
  
  LEVEL("{LEVEL}"),
  
  NAME("{NAME}");
  
  
  /**
   * Constructor with the string mark.
   * @param mark The string mark pattern.
   */
  LogTag(String mark) {
    if(mark == null || mark.trim().isEmpty())
      throw new IllegalArgumentException("Invalid mark: '"+ mark+ "'");
    this.mark = mark;
  }
  
  /**
   * Get the string mark.
   * @return The string mark.
   */
  public String getMark() {
    return this.mark;
  }
  
  /**
   * Verify if the given string match with the mark of 
   * this <code>LogMark</code> enum.
   * @param str The string to be verified.
   * @return <code>true</code> if the string matches
   * with this <code>LogMark</code> enum.
   */
  public boolean match(String str) {
    return mark.equalsIgnoreCase(str);
  }
  
  /**
   * The string mark.
   */
  private String mark;
  
}
