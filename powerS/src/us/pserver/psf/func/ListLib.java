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

package us.pserver.psf.func;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSObject;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/02/2015
 */
public class ListLib implements FSFunctionExtension {
  
  public static final String
      LIST = "list",
      LSADD = "lsadd",
      LSADDALL = "lsaddall",
      LSSET = "lsset",
      LSGET = "lsget",
      LSSIZE = "lssize",
      LSISEMPTY = "lsisempty",
      LSCLEAR = "lsclear",
      LSREMOVE = "lsremove",
      LSINDEXOF = "lsindexof",
      LSLINDEXOF = "lslindexof",
      LSCONTAINS = "lscontains",
      LSFOR = "lsfor",
      LSSORT = "lssort",
      LSSUBLIST = "lssublist";
  
  
  private BasicIO bio;

  
  public ListLib(BasicIO bas) {
    if(bas == null)
      throw new IllegalArgumentException("Invalid NULL BasicIO");
    bio = bas;
  }
  
  
  public List list() {
    return new ArrayList();
  }
  
  
  public List get(Object list) throws FSException {
    if(list == null)
      throw new FSException("Invalid NULL Object");
    if(list instanceof FSObject)
      list = ((FSObject)list).getObject();
    if(!(list instanceof List))
      throw new FSException("Object is not a List type");
    return (List) list;
  }
  
  
  public List lsadd(Object list, Object value) throws FSException {
    List ls = get(list);
    ls.add(value);
    return ls;
  }
  

  public List lsaddall(Object list, Object list2) throws FSException {
    List ls = get(list);
    List l2 = get(list2);
    ls.addAll(l2);
    return ls;
  }
  

  public List lsset(Object list, int idx, Object value) throws FSException {
    List ls = get(list);
    if(idx < 0 || idx >= ls.size())
      throw new FSException("Index ("+ idx+ ") out of bounds ([0-"+ ls.size()+"])");
    ls.set(idx, value);
    return ls;
  }
  

  public Object lsget(Object list, int idx) throws FSException {
    List ls = get(list);
    if(idx < 0 || idx >= ls.size())
      throw new FSException("Index ("+ idx+ ") out of bounds ([0-"+ ls.size()+"])");
    return ls.get(idx);
  }
  

  public int lssize(Object list) throws FSException {
    List ls = get(list);
    return ls.size();
  }
  

  public int lsisempty(Object list) throws FSException {
    List ls = get(list);
    return (ls.isEmpty() ? 1 : 0);
  }
  
  
  public List lsclear(Object list) throws FSException {
    List ls = get(list);
    ls.clear();
    return ls;
  }
  
  
  public Object lsremove(Object list, int idx) throws FSException {
    List ls = get(list);
    if(idx < 0 || idx >= ls.size())
      throw new FSException("Index ("+ idx+ ") out of bounds ([0-"+ ls.size()+"])");
    return ls.remove(idx);
  }
  
  
  public int lsindexof(Object list, Object value) throws FSException {
    List ls = get(list);
    return ls.indexOf(value);
  }
  

  public int lscontains(Object list, Object value) throws FSException {
    List ls = get(list);
    return (ls.contains(value) ? 1 : 0);
  }
  
  
  public void lsfor(Object list, final String func) throws FSException {
    List ls = get(list);
    if(func == null || func.trim().isEmpty()) {
      throw new FSException("lsfor( "
          + list+ ", >" + func+ "< ): Invalid Callback Function");
    }
    ls.forEach(new Consumer() {
      @Override
      public void accept(Object v) {
        ArrayList al = new ArrayList(1);
        al.add(v);
        try {
          bio.callScriptFunction(func, al);
        } catch(IOException | FSException fe) {
          throw new RuntimeException(fe.toString());
        }
      }
    });
  }
  

  public List lssort(Object list) throws FSException {
    List ls = get(list);
    ls.sort(new Comparator() {
      @Override
      public int compare(Object o1, Object o2) {
        if(o1 instanceof FSObject)
          o1 = ((FSObject)o1).getObject();
        if(o2 instanceof FSObject)
          o2 = ((FSObject)o2).getObject();
        if(o1 instanceof Number && o2 instanceof Number) {
          Number n1 = (Number) o1;
          Number n2 = (Number) o2;
          Double d1 = n1.doubleValue();
          Double d2 = n2.doubleValue();
          return d1.compareTo(d2);
        }
        else if(o1 instanceof Date && o2 instanceof Date) {
          Date d1 = (Date) o1;
          Date d2 = (Date) o2;
          return d1.compareTo(d2);
        }
        else {
          String s1 = o1.toString();
          String s2 = o2.toString();
          return s1.compareTo(s2);
        }
      }
    });
    return ls;
  }
  
  
  public int lslindexof(Object list, Object value) throws FSException {
    List ls = get(list);
    return ls.lastIndexOf(value);
  }
  
  
  public List lssublist(Object list, int from, int len) throws FSException {
    List ls = get(list);
    if(from < 0 || from >= ls.size())
      throw new FSException("Index ("+ from+ ") out of bounds ([0-"+ ls.size()+"])");
    if(len < 1 || (len+from) > ls.size())
      throw new FSException("Invalid length ("+ len+ "). list.size()="+ ls.size());
    
    return ls.subList(from, from+len);
  }
  
  
  @Override
  public Object callFunction(String name, ArrayList al) throws FSException {
    switch(name) {
      case LIST:
        return list();
      case LSADD:
        FUtils.checkLen(al, 2);
        return lsadd(al.get(0), al.get(1));
      case LSADDALL:
        FUtils.checkLen(al, 2);
        return lsaddall(al.get(0), al.get(1));
      case LSCLEAR:
        FUtils.checkLen(al, 1);
        return lsclear(al.get(0));
      case LSCONTAINS:
        FUtils.checkLen(al, 2);
        return lscontains(al.get(0), al.get(1));
      case LSFOR:
        FUtils.checkLen(al, 2);
        lsfor(al.get(0), FUtils.str(al, 1));
        return null;
      case LSGET:
        FUtils.checkLen(al, 2);
        return lsget(al.get(0), FUtils._int(al, 1));
      case LSINDEXOF:
        FUtils.checkLen(al, 2);
        return lsindexof(al.get(0), al.get(1));
      case LSISEMPTY:
        FUtils.checkLen(al, 1);
        return lsisempty(al.get(0));
      case LSLINDEXOF:
        FUtils.checkLen(al, 2);
        return lslindexof(al.get(0), al.get(1));
      case LSREMOVE:
        FUtils.checkLen(al, 2);
        return lsremove(al.get(0), FUtils._int(al, 1));
      case LSSET:
        FUtils.checkLen(al, 3);
        return lsset(al.get(0), FUtils._int(al, 1), al.get(2));
      case LSSIZE:
        FUtils.checkLen(al, 1);
        return lssize(al.get(0));
      case LSSORT:
        FUtils.checkLen(al, 1);
        return lssort(al.get(0));
      case LSSUBLIST:
        FUtils.checkLen(al, 3);
        return lssublist(al.get(0), FUtils._int(al, 1), FUtils._int(al, 2));
      default:
        throw new FSUnsupportedException();
    }
  }

  
  public void addTo(FSFastExtension fe) {
    if(fe == null) return;
    fe.addFunctionExtension(LIST, this);
    fe.addFunctionExtension(LSADD, this);
    fe.addFunctionExtension(LSADDALL, this);
    fe.addFunctionExtension(LSCLEAR, this);
    fe.addFunctionExtension(LSCONTAINS, this);
    fe.addFunctionExtension(LSFOR, this);
    fe.addFunctionExtension(LSGET, this);
    fe.addFunctionExtension(LSINDEXOF, this);
    fe.addFunctionExtension(LSISEMPTY, this);
    fe.addFunctionExtension(LSLINDEXOF, this);
    fe.addFunctionExtension(LSREMOVE, this);
    fe.addFunctionExtension(LSSET, this);
    fe.addFunctionExtension(LSSIZE, this);
    fe.addFunctionExtension(LSSORT, this);
    fe.addFunctionExtension(LSSUBLIST, this);
  }
  
}
