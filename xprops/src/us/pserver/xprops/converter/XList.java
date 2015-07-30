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

package us.pserver.xprops.converter;

import java.util.LinkedList;
import java.util.List;
import us.pserver.xprops.XTag;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/07/2015
 */
public class XList extends XTag {

  private final List list;
  
  private final Class type;
  
  
  public XList(List ls) {
    super(Valid.off(ls)
        .testNull(List.class)
        .test(ls.isEmpty(), "Invalid Empty List ")
        .get().get(0).getClass().getName()
    );
    list = ls;
    this.type = ls.get(0).getClass();
  }
  
  
  public Class getType() {
    return type;
  }
  
  
  public List getList() {
    return list;
  }
  
  
  public void populateXmlTags() {
    childs().clear();
    XClass xc = new XClass();
    XConverter conv = XConverterFactory.getXConverter(type);
    this.addChild(xc.toXml(type));
    if(list.isEmpty()) return;
    for(int i = 0; i < list.size(); i++) {
      this.addNewChild(String.valueOf(i))
          .addChild(conv.toXml(list.get(i)));
    }
  }

}
