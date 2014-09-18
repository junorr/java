/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.fxs.model;

import com.jpower.fxs.table.Extractor;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.19
 */
public class ObjectModel implements TreeNode {
  
  private ObjectModel parent;
  
  private List<ObjectModel> childs;
  
  private List<Extractor> extractors;

  private String name;
  
  private Class type;
  
  private boolean mark;
  
  
  public ObjectModel() {
    childs = new LinkedList<ObjectModel>();
    extractors = new LinkedList<Extractor>();
    name = null;
    type = null;
    mark = false;
  }
  
  
  public ObjectModel addChild(ObjectModel o, Extractor e) {
    if(o == null || e == null) return this;
    childs.add(o);
    extractors.add(e);
    return this;
  }
  
  
  public List<ObjectModel> getChilds() {
    return childs;
  }
  
  
  public Extractor getExtractor(ObjectModel o) {
    if(o == null || extractors.isEmpty()) return null;
    if(childs.contains(o))
      return extractors.get(childs.indexOf(o));
    return null;
  }
  
  
  public Extractor[] getExtractorsArray() {
    List<Extractor> el = new LinkedList();
    ObjectModel om = this, pr = parent;
    while(pr != null) {
      el.add(pr.getExtractor(om));
      om = pr;
      pr = om.getParent();
    }
    Extractor[] es = new Extractor[el.size()];
    return (Extractor[]) this.invertArray(el.toArray(es));
  }
  
  
  @Override
  public ObjectModel getParent() {
    return parent;
  }
  
  
  private Object[] invertArray(Object[] os) {
    if(os == null || os.length == 0) return os;
    int asc = 0, dsc = os.length -1;
    while(asc != dsc) {
      Object a = os[asc];
      os[asc] = os[dsc];
      os[dsc] = a;
      asc++;
      dsc--;
    }
    return os;
  }
  
  
  @Override
  public TreeNode getChildAt(int idx) {
    if(idx < 0 || idx >= childs.size())
      return null;
    return childs.get(idx);
  }

  
  @Override
  public int getChildCount() {
    return childs.size();
  }

  
  @Override
  public int getIndex(TreeNode node) {
    return childs.indexOf(node);
  }

  
  @Override
  public boolean getAllowsChildren() {
    return true;
  }

  
  @Override
  public boolean isLeaf() {
    return childs.isEmpty();
  }

  
  @Override
  public Enumeration children() {
    return childs.iterator();
  }
  
  
  public static void main(String[] args) {
    ObjectModel om = new ObjectModel();
    String[] ss = { "a", "b", "c" };
    System.out.println(Arrays.toString(ss));
    ss = (String[]) om.invertArray(ss);
    System.out.println(Arrays.toString(ss));
  }
  
}
