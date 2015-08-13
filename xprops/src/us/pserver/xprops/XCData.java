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

package us.pserver.xprops;

import java.util.Objects;

/**
 * Render a xml CDATA.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class XCData extends XTag {

  protected static final String 
      cdata = "CDATA", 
      ex = "!",
      openbrk = "[", 
      closebrk = "]";
  
  
  private final StringBuilder builder;
  
  
  /**
   * Constructor without arguments.
   */
  public XCData() {
    super(cdata);
    builder = new StringBuilder();
  }
  
  
  /**
   * Default constructor receives the CDATA content.
   * @param str 
   */
  public XCData(String str) {
    this();
    builder.append(str);
  }
  
  
  /**
   * Append the String to the CDATA content.
   * @param str String to append
   * @return This modified XCData instance.
   */
  public XCData append(String str) {
    if(str != null)
      builder.append(str);
    return this;
  }
  
  
  /**
   * Append the object (::toString()) to the CDATA content.
   * @param str object to append
   * @return This modified XCData instance.
   */
  public XCData append(Object obj) {
    if(obj != null)
      builder.append(Objects.toString(obj));
    return this;
  }
  
  
  /**
   * Append the char to the CDATA content.
   * @param str char to append
   * @return This modified XCData instance.
   */
  public XCData append(char c) {
    builder.append(c);
    return this;
  }
  
  
  /**
   * Get the internal StringBuilder.
   * @return StringBuilder.
   */
  public StringBuilder stringBuilder() {
    return builder;
  }
  
  
  /**
   * Get the CDATA content length
   * @return content length
   */
  public int length() {
    return builder.length();
  }
  
  
  @Override
  public String toXml() {
    StringBuilder sb = new StringBuilder();
    sb.append(lt)
        .append(ex)
        .append(openbrk)
        .append(cdata)
        .append(openbrk)
        .append(builder.toString());
    for(XTag x : childs()) {
      sb.append(x.toXml()).append(ln);
    }
    return sb.append(closebrk)
        .append(closebrk)
        .append(gt)
        .toString();
  }
  
  
  @Override
  public String toString() {
    return builder.toString();
  }
  
}
