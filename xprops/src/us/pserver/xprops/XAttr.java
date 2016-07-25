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

import us.pserver.valid.Valid;


/**
 * XAttr is a xml tag attribute.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class XAttr extends XTag {

  
  /**
   * Create an attribute with the specified 
   * name and no value.
   * @param name Attribute name
   */
  public XAttr(String name) {
    super(name);
  }
  
  
  /**
   * Default Constructor create an attribute with 
   * the specified name and value.
   * @param name Attribute name
   * @param value Attribute value.
   */
  public XAttr(String name, String value) {
    super(name);
    this.childs().add(new XValue(
        Valid.off(value).forEmpty()
            .getOrFail("Invalid Attribute Value: "))
    );
  }
  
  
  /**
   * Get the attribute name.
   * @return attribute name
   */
  public String attrName() {
    return value;
  }
  
  
  /**
   * Get the attribute value.
   * @return attribute value
   */
  public XValue attrValue() {
    return (childs().isEmpty() 
        ? null : firstChild().xvalue()
    );
  }
  
  
  /**
   * Set the attribute value.
   * @param value attribute value.
   * @return Setted value.
   */
  public XValue attrValue(String value) {
    XValue v = new XValue(Valid.off(value)
        .forEmpty().getOrFail("Invalid Attr Value: ")
    );
    childs().clear();
    childs().add(v);
    return v;
  }
  
  
  /**
   * Get the attribute value.
   * @return attribute value
   */
  @Override
  public XValue xvalue() {
    return attrValue();
  }
  
  
  @Override
  public String toXml() {
    return new StringBuilder()
        .append(value)
        .append(eq).append(qt)
        .append(childs().isEmpty() 
            ? "" : firstChild().value())
        .append(qt)
        .toString();
  }
  
}
