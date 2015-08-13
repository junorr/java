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

/**
 * Render a xml comment.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class XComment extends XCData {

  private static final String 
      dashes = "--";
  
  
  /**
   * Create an empty xml comment.
   */
  public XComment() {
    super(dashes);
  }
  
  
  /**
   * Create a xml commente with the specified content.
   * @param str comment
   */
  public XComment(String str) {
    super(str);
  }
  
  
  @Override
  public String toXml() {
    StringBuilder sb = new StringBuilder();
    sb.append(lt)
        .append(ex)
        .append(dashes)
        .append(sp)
        .append(super.toString());
    for(XTag x : childs()) {
      sb.append(x.toXml()).append(ln);
    }
    return sb.append(sp)
        .append(dashes)
        .append(gt)
        .toString();
  }
  
}
