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

package us.pserver.xprops.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import us.pserver.xprops.XAttr;
import us.pserver.xprops.XTag;
import us.pserver.xprops.XInputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/07/2015
 */
public class ListTransformer implements XmlTransformer<List> {
  
  @Override
  public List transform(String str) throws IllegalArgumentException {
    XInputStream xs = new XInputStream(
        new ByteArrayInputStream(
            new UTF8String(
                Valid.off(str).getOrFail("Invalid Xml String: ")
            ).getBytes()
        )
    );
    
    XTag xlist = null;
    try { xlist = xs.read(); }
    catch(IOException e) {
      throw new IllegalArgumentException(e.getLocalizedMessage(), e);
    }
    
    if(!xlist.value().equals("list")) {
      throw new IllegalArgumentException("Invalid Root Tag: "+ xlist.value());
    }
    
    XAttr xclass = xlist.findAttr("class");
    Valid.off(xclass)
        .testNull("Root Tag does not contains a 'class' attr: ");
    Class ltype = xclass.attrValue().asClass();
    
    ObjectTransformer to = new ObjectTransformer(ltype);
    List ls = new ArrayList(xlist.childs().size());
    for(int i = 0; i < xlist.childs().size(); i++) {
      XTag x = xlist.findOne(String.valueOf(i), false);
      if(x != null) {
        ls.add(i, to.transform(str));
      }
    }
    return ls;
  }
  
  
  @Override
  public String reverse(List ls) throws IllegalArgumentException {
    Valid.off(ls).testNull(List.class)
        .test(ls.isEmpty(), "Invalid Empty List: ");
    Class ltype = ls.get(0).getClass();
    XTag xlist = new XTag("list");
    xlist.addNewAttr("class", ltype.getName());
    ObjectTransformer to = new ObjectTransformer(ltype);
    for(int i = 0; i < ls.size(); i++) {
      XTag xnum = new XTag(String.valueOf(i));
      String xml = to.reverse(ls.get(i));
      if(xml.startsWith("<")) {
        xnum.addNewChild(xml);
      } else {
        xnum.addNewValue(xml);
      }
      xlist.addChild(xnum);
    }
    return xlist.toXml();
  }
  
}
