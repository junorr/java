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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import us.pserver.xprops.util.Validator;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/07/2015
 */
public class XTag extends AbstractUnit {
  
  static final String 
      lt = "<", 
      sp = " ", 
      qt = "\"", 
      gt = ">",
      sl = "/",
      ln = "\n",
      eq = "=";
  
  
  private List<XTag> childs;
  
  private String xmlIdent;
  
  private int identLevel;
  
  private boolean ommitRoot;
  

  public XTag(final String value) {
    super(value);
    childs = new LinkedList<>();
    xmlIdent = null;
    identLevel = 0;
    ommitRoot = false;
  }
  
  
  public XTag addNewChild(String value) throws IllegalArgumentException {
    XTag tag = new XTag(Validator.off(value)
        .forEmpty()
        .getOrFail("Invalid Tag Name: ")
    );
    tag.id().compose(this.id);
    childs.add(tag);
    return tag;
  }
  
  
  public XValue addNewValue(String value) throws IllegalArgumentException {
    XValue tag = new XValue(Validator.off(value)
        .forEmpty()
        .getOrFail("Invalid Tag Name: ")
    );
    tag.id().compose(this.id);
    childs.add(tag);
    return tag;
  }
  
  
  public XAttr addNewAttr(String name, String value) throws IllegalArgumentException {
    XAttr tag = new XAttr(
        Validator.off(name).forEmpty().getOrFail("Invalid Tag Name: "),
        Validator.off(value).forEmpty().getOrFail("Invalid Tag Value: ")
    );
    tag.id().compose(this.id);
    childs.add(tag);
    return tag;
  }
  
  
  public XTag addChild(XTag child) throws IllegalArgumentException {
    if(child != null) {
    //Validator.off(child).forNull().getOrFail(XTag.class)
      //  .id().compose(this.id);
      childs.add(child);
    }
    return this;
  }
  
  
  public List<XTag> childs() {
    return childs;
  }
  
  
  public List<XTag> find(String value, boolean includeChilds) {
    return find(new XID(Validator.off(value)
        .forEmpty()
        .getOrFail("Invalid String Value: ")), 
        includeChilds
    );
  }
  
  
  public XTag findOne(String value, boolean includeChilds) {
    return findOne(new XID(Validator.off(value)
        .forEmpty()
        .getOrFail("Invalid String Value: ")), 
        includeChilds
    );
  }
  
  
  public List<XTag> find(XID id, boolean includeChilds) {
    if(id == null 
        || childs.isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    List<XTag> list = new LinkedList<>();
    for(XTag x : childs) {
      if(x.id().toString().toLowerCase()
          .startsWith(id.toString().toLowerCase())
          || x.value().equalsIgnoreCase(id.getStringID())
          || x.id().toString().toLowerCase()
              .endsWith(id.toString().toLowerCase())) {
        list.add(x);
      }
    }
    if(!includeChilds) return list;
    for(XTag x : childs) {
      List<XTag> fl = x.find(id, includeChilds);
      if(!fl.isEmpty()) list.addAll(fl);
    }
    return list;
  }
  
  
  public XTag findOne(XID id, boolean includeChilds) {
    Validator.off(id).forNull().fail(XID.class);
    for(XTag x : childs) {
      if(x.id().toString().toLowerCase()
          .startsWith(id.toString().toLowerCase())
          || x.value().equalsIgnoreCase(id.getStringID())
          || x.id().toString().toLowerCase()
              .endsWith(id.toString().toLowerCase())) {
        return x;
      }
    }
    if(!includeChilds) return null;
    for(XTag x : childs) {
      XTag f = x.findOne(id, includeChilds);
      if(f != null) return f;
    }
    return null;
  }
  
  
  public XTag firstChild() {
    return (childs.isEmpty() ? null : childs.get(0));
  }
  
  
  public XTag setXmlIdentation(String ident, int level) {
    xmlIdent = ident;
    identLevel = level;
    return this;
  }
  
  
  public String getXmlIdentation() {
    return xmlIdent;
  }
  
  
  String getIdent() {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < identLevel; i++) {
      sb.append(xmlIdent);
    }
    return sb.toString();
  }
  
  
  public XTag setOmmitRoot(boolean ommit) {
    this.ommitRoot = ommit;
    return this;
  }
  
  
  public boolean isOmmitRoot() {
    return this.ommitRoot;
  }
  
  
  public XAttr findAttr(String name) {
    Validator.off(name).forEmpty().fail("Invalid Attr Name: ");
    List<XAttr> ls = this.getAllAttrs();
    for(XAttr a : ls) {
      if(a.attrName().equals(name))
        return a;
    }
    return null;
  }
  
  
  List<XAttr> getAllAttrs() {
    if(childs.isEmpty())
      return Collections.EMPTY_LIST;
    List<XAttr> ls = new LinkedList<>();
    for(XTag x : childs) {
      if(XAttr.class.isInstance(x))
        ls.add((XAttr)x);
    }
    return ls;
  }
  
  
  @Override
  public String toXml() {
    StringBuilder sb = new StringBuilder();
    if(!ommitRoot) {
      sb.append((xmlIdent != null ? getIdent() : ""))
          .append(lt)
          .append(value);
    
      List<XAttr> attrs = getAllAttrs();
      if(!attrs.isEmpty()) {
        for(XAttr a : attrs) {
          sb.append(sp).append(a.toXml());
        }
      }
      sb.append(gt);
    }
    
    if(!childs.isEmpty() 
        && xmlIdent != null 
        && !ommitRoot) {
      sb.append(ln);
    }
    for(XTag x : childs) {
      if(XAttr.class.isInstance(x)) {
        continue;
      }
      if(xmlIdent != null) {
        x.setXmlIdentation(xmlIdent, identLevel + 1);
        sb.append(x.toXml()).append(ln);
      } 
      else {
        sb.append(x.toXml());
      }
    }
    if(!ommitRoot) {
      sb.append((xmlIdent != null ? getIdent() : ""))
        .append(lt)
        .append(sl)
        .append(value)
        .append(gt);
    }
    return sb.toString();
  }

}
