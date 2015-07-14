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
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/07/2015
 */
public class XCData extends XTag {

  private static final String 
      CD = "CDATA", 
      EX = "!",
      OB = "[", 
      CB = "]";
  
  
  private final StringBuilder builder;
  
  
  public XCData() {
    super(CD);
    builder = new StringBuilder();
  }
  
  
  public XCData(String str) {
    this();
    builder.append(str);
  }
  
  
  public XCData append(String str) {
    if(str != null)
      builder.append(str);
    return this;
  }
  
  
  public XCData append(Object obj) {
    if(obj != null)
      builder.append(Objects.toString(obj));
    return this;
  }
  
  
  public XCData append(char c) {
    builder.append(c);
    return this;
  }
  
  
  public StringBuilder stringBuilder() {
    return builder;
  }
  
  
  public int length() {
    return builder.length();
  }
  
  
  @Override
  public String toXml() {
    StringBuilder sb = new StringBuilder();
    sb.append(lt)
        .append(EX)
        .append(OB)
        .append(CD)
        .append(OB)
        .append(builder.toString());
    for(XTag x : childs()) {
      sb.append(x.toXml()).append(ln);
    }
    return sb.append(CB)
        .append(CB)
        .append(gt)
        .toString();
  }
  
  
  @Override
  public String toString() {
    return builder.toString();
  }
  
}
