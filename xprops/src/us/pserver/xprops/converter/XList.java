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

import java.awt.Color;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import us.pserver.xprops.XAttr;
import us.pserver.xprops.XTag;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/07/2015
 */
public class XList<T> extends XTag implements XConverter<List<T>> {

  private final List<T> list;
  
  private Class<T> type;
  
  
  public XList(List<T> ls) {
    super("list");
    list = Valid.off(ls).getOrFail(List.class);
    if(!ls.isEmpty())
      this.type = (Class<T>) ls.get(0).getClass();
  }
  
  
  protected XList(Class<T> type, List<T> ls) {
    super("list");
    list = Valid.off(ls).getOrFail(List.class);
    this.type = Valid.off(type).getOrFail(Class.class);
  }
  
  
  public Class<T> getType() {
    return type;
  }
  
  
  public List<T> getList() {
    return list;
  }
  
  
  public XList populateXmlTags() {
    childs().clear();
    XClass xc = new XClass();
    XConverter conv = XConverterFactory.getXConverter(type);
    this.addChild(xc.toXml(type));
    if(list.isEmpty()) return this;
    for(int i = 0; i < list.size(); i++) {
      this.addNewChild(String.valueOf(i))
          .addChild(conv.toXml(list.get(i)));
    }
    return this;
  }
  

  @Override
  public XTag toXml(List<T> obj) {
    this.list.clear();
    this.list.addAll(obj);
    System.out.println("* XList.toXml( list.get(0) ) = "+ list.get(0));
    return this.populateXmlTags();
  }


  @Override
  public List<T> fromXml(XTag tag) {
    Valid.off(tag).testNull(XTag.class);
    XAttr cattr = tag.findAttr("class");
    Valid.off(cattr).testNull("XTag does not contains a 'class' attribute");
    type = cattr.attrValue().asClass();
    XConverter<T> xc = XConverterFactory.getXConverter(type);
    for(int i = 0; i < Integer.MAX_VALUE; i++) {
      XTag x = tag.findOne(String.valueOf(i), false);
      if(x == null) break;
      list.add(xc.fromXml(x.firstChild()));
    }
    return list;
  }

  
  @Override
  public String toXml() {
    this.populateXmlTags();
    return super.toXml();
  }
  
}
