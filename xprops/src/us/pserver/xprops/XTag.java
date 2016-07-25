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
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import us.pserver.valid.Valid;

/**
 * XTag represents a Xml tag.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
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
  

  /**
   * Default constructor which receives the tag name.
   * @param value Tag name.
   */
  public XTag(final String value) {
    super(value);
    childs = new LinkedList<>();
    xmlIdent = null;
    identLevel = 0;
  }
  
  
  /**
   * Create and add a new child tag.
   * @param value The child tag name.
   * @return The created child tag.
   * @throws IllegalArgumentException If the tag name is null.
   */
  public XTag addNewChild(String value) throws IllegalArgumentException {
    XTag tag = new XTag(Valid.off(value)
        .forEmpty()
        .getOrFail("Invalid Tag Name: ")
    );
    tag.id().compose(this.id);
    childs.add(tag);
    return tag;
  }
  
  
  /**
   * Create and add a new child value.
   * @param value The child tag value.
   * @return The created child XValue.
   * @throws IllegalArgumentException If the tag name is null.
   */
  public XValue addNewValue(String value) throws IllegalArgumentException {
    XValue tag = new XValue(Valid.off(value)
        .forEmpty()
        .getOrFail("Invalid Tag Name: ")
    );
    tag.id().compose(this.id);
    childs.add(tag);
    return tag;
  }
  
  
  /**
   * Create and add a new tag attribute.
   * @param name The attribute name.
   * @param value The attribute value.
   * @return The created child XAttr.
   * @throws IllegalArgumentException If the attribute name or value is null.
   */
  public XAttr addNewAttr(String name, String value) throws IllegalArgumentException {
    XAttr tag = new XAttr(
        Valid.off(name).forEmpty()
            .getOrFail("Invalid Tag Name: "),
        Valid.off(value).forEmpty()
            .getOrFail("Invalid Tag Value: ")
    );
    tag.id().compose(this.id);
    childs.add(tag);
    return tag;
  }
  
  
  /**
   * Add a new child tag.
   * @param child The child tag to be added.
   * @return This modified XTag instance.
   */
  public XTag addChild(XTag child) {
    if(child != null) {
      childs.add(child);
    }
    return this;
  }
  
  
  /**
   * Return the child list of this tag.
   * @return List&lt;XTag&gt;
   */
  public List<XTag> childs() {
    return childs;
  }
  
  
  /**
   * Find in this tag childs a tag with the specified value.
   * @param value The child value to be found.
   * @param includeChilds If the search should include grandchilds.
   * @return a List with all results found.
   */
  public List<XTag> find(String value, boolean includeChilds) {
    return find(new XID(Valid.off(value)
        .forEmpty()
        .getOrFail("Invalid String Value: ")), 
        includeChilds
    );
  }
  
  
  /**
   * Find in this tag childs a tag with the specified value.
   * @param value The child value to be found.
   * @param includeChilds If the search should include grandchilds.
   * @return The XTag child found or null.
   */
  public XTag findOne(String value, boolean includeChilds) {
    return findOne(new XID(Valid.off(value)
        .forEmpty()
        .getOrFail("Invalid String Value: ")), 
        includeChilds
    );
  }
  
  
  private boolean match(XID id1, XID id2) {
    return id1.toString().toLowerCase()
        .startsWith(id2.toString().toLowerCase())
        || id1.getStringID().equalsIgnoreCase(id2.getStringID())
        || id1.toString().toLowerCase()
            .endsWith(id2.toString().toLowerCase());
  }
  
  
  /**
   * Find in this tag childs a tag with the specified ID.
   * @param id The child ID to be found.
   * @param includeChilds If the search should include grandchilds.
   * @return a List with all results found.
   */
  public List<XTag> find(XID id, boolean includeChilds) {
    if(id == null 
        || childs.isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    List<XTag> list = new LinkedList<>();
    for(XTag x : childs) {
      if(match(x.id(), id)) {
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
  
  
  /**
   * Find in this tag childs a tag with the specified ID.
   * @param id The child ID to be found.
   * @param includeChilds If the search should include grandchilds.
   * @return The found XTag child or null.
   */
  public XTag findOne(XID id, boolean includeChilds) {
    Valid.off(id).forNull().fail(XID.class);
    for(XTag x : childs) {
      if(match(x.id(), id)) {
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
  
  
  /**
   * Return this tag first child or null.
   * @return The first child tag.
   */
  public XTag firstChild() {
    return (childs.isEmpty() ? null : childs.get(0));
  }
  
  
  /**
   * Set the identation level for formatting this tag xml representation.
   * @param ident A String of while spaces for identing.
   * @param level The identation level.
   * @return Thid modified instance of XTag.
   */
  public XTag setXmlIdentation(String ident, int level) {
    xmlIdent = ident;
    identLevel = level;
    return this;
  }
  
  
  /**
   * Get the identation string for formatting this tag xml representation.
   * @return A String of while spaces for identing.
   */
  public String getXmlIdentation() {
    return xmlIdent;
  }
  
  
  /**
   * Return the string identation adjusted for the identation level.
   * @return The string identation.
   */
  String getIdent() {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < identLevel; i++) {
      sb.append(xmlIdent);
    }
    return sb.toString();
  }
  
  
  /**
   * Find a child attribute with the specified name.
   * @param name The child attribute name.
   * @return The XAttr child found or null.
   */
  public XAttr findAttr(String name) {
    Valid.off(name).forEmpty().fail("Invalid Attr Name: ");
    List<XAttr> ls = this.getAllAttrs();
    for(XAttr a : ls) {
      if(a.attrName().equals(name))
        return a;
    }
    return null;
  }
  
  
  /**
   * Return a list with all child attributes.
   * @return List&lt;XAttr&gt;
   */
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
  
  
  int computeChilds(XTag tag) {
    if(tag == null) return 0;
    int total = tag.childs().size();
    for(XTag x : tag.childs()) {
      total += computeChilds(x);
    }
    return total;
  }
  
  
  void sortChilds() {
    if(childs.isEmpty())
      return;
    Comparator<XTag> comp = new Comparator<XTag>() {
      @Override
      public int compare(XTag o1, XTag o2) {
        Integer i1 = computeChilds(o1);
        Integer i2 = computeChilds(o2);
        if(i1.compareTo(i2) == 0)
          return o1.value().compareTo(o2.value());
        return i1.compareTo(i2);
      }
    };
    Collections.sort(childs, comp);
  }
  
  
  @Override
  public String toXml() {
    StringBuilder sb = new StringBuilder();
    sortChilds();
    if(xmlIdent != null)
      sb.append(getIdent());
    sb.append(lt).append(value());
    List<XAttr> attrs = getAllAttrs();
    for(XAttr at : attrs) {
      sb.append(sp).append(at.toXml());
    }
    if(childs().size() == attrs.size()) {
      return sb.append(sl).append(gt).toString();
    }
    else {
      sb.append(gt);
    }
    return this.appendChilds(sb)
        .append(lt)
        .append(sl)
        .append(value())
        .append(gt).toString();
  }
  
  
  private StringBuilder appendChilds(StringBuilder sb) {
    if(childs.isEmpty() || sb == null) 
      return sb;
    boolean lastValue = false;
    for(XTag x : childs) {
      if(x == null) continue;
      if(XAttr.class.isInstance(x))
        continue;
      lastValue = true;
      if(!XValue.class.isInstance(x)) {
        lastValue = false;
        if(xmlIdent != null) {
          sb.append(ln);
          x.setXmlIdentation(xmlIdent, identLevel+1);
        }
      }
      sb.append(x.toXml());
    }
    if(xmlIdent != null && !lastValue) {
      sb.append(ln)
          .append(getIdent());
    }
    return sb;
  }
  
}
