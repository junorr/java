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

import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/07/2015
 */
public class XAttr extends XTag {

  
  public XAttr(String name) {
    super(name);
  }
  
  
  public XAttr(String name, String value) {
    super(name);
    this.childs().add(new XValue(
        Valid.off(value).forEmpty()
            .getOrFail("Invalid Attribute Value: "))
    );
  }
  
  
  public String attrName() {
    return value;
  }
  
  
  public XValue attrValue() {
    return (childs().isEmpty() 
        ? null : firstChild().xvalue()
    );
  }
  
  
  public XValue attrValue(String value) {
    XValue v = new XValue(Valid.off(value)
        .forEmpty().getOrFail("Invalid Attr Value: ")
    );
    childs().clear();
    childs().add(v);
    return v;
  }
  
  
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
