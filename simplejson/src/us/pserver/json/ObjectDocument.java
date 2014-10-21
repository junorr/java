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

package us.pserver.json;

import com.jpower.rfl.Reflector;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/10/2014
 */
public class ObjectDocument {

  private Reflector ref;
  
  private ObjectJson ojs;
  
  
  public ObjectDocument() {
    ref = new Reflector();
    ojs = new ObjectJson();
  }
  
  
  public Document toDoc(Object obj) {
    if(obj == null) 
      return null;
    if(obj instanceof Document)
      return (Document) obj;
    
    Document doc = new Document();
    doc.label(obj.getClass().getName());
    Field[] fs = ref.on(obj).fields();
    for(int i = 0; i < fs.length; i++) {
      Object val = ref.on(obj).field(fs[i].getName()).get();
      if(val == null) continue;
      if(ojs.isPrimitive(val) || ojs.isArray(val) || ojs.isList(val)) {
        doc.put(fs[i].getName(), val);
      } 
      else {
        doc.put(fs[i].getName(), toDoc(val));
      }
    }
    return doc;
  }
  
  
  public Object fromDoc(Document doc) {
    if(doc == null) return null;
    ref.onClass(doc.label());
    if(ref.hasError()) {
      throw new UnsupportedOperationException(
          "Invalid class ("+ doc.label()+ "): "
              + ref.getError().getMessage(), ref.getError());
    }
    Object obj = ref.create();
    UnsupportedOperationException uex = null;
    if(ref.hasError()) {
      uex = new UnsupportedOperationException(
          "No empty constructor for class ("+ doc.label()+ "): "
              + ref.getError().getMessage(), ref.getError());
    }
    else if(obj == null) {
      uex = new UnsupportedOperationException(
          "No empty constructor for class ("+ doc.label()+ ")");
    }
    if(uex != null) throw uex;
    
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      Field f = ref.on(obj).field(key).field();
      if(f == null) continue;
      if(String.class.isAssignableFrom(f.getType()))
        ref.set(doc.get(key));
      else if(Double.class.isAssignableFrom(f.getType())
          || double.class.isAssignableFrom(f.getType())) {
        ref.set(doc.getDouble(key));
      }
      else if(Integer.class.isAssignableFrom(f.getType())
          || int.class.isAssignableFrom(f.getType())) {
        ref.set(doc.getInt(key));
      }
      else if(Long.class.isAssignableFrom(f.getType())
          || long.class.isAssignableFrom(f.getType())) {
        ref.set(doc.getLong(key));
      }
      else if(Byte.class.isAssignableFrom(f.getType())
          || byte.class.isAssignableFrom(f.getType())) {
        Double d = doc.getDouble(key);
        ref.set(d.byteValue());
      }
      else if(Short.class.isAssignableFrom(f.getType())
          || short.class.isAssignableFrom(f.getType())) {
        Double d = doc.getDouble(key);
        ref.set(d.shortValue());
      }
      else if(Boolean.class.isAssignableFrom(f.getType())
          || boolean.class.isAssignableFrom(f.getType())) {
        ref.set(doc.getBoolean(key));
      }
      else if(Character.class.isAssignableFrom(f.getType())
          || char.class.isAssignableFrom(f.getType())) {
        Character c = doc.getString(key).charAt(0);
        ref.set(c);
      }
      else if(Double.class.isAssignableFrom(f.getType())
          || double.class.isAssignableFrom(f.getType())) {
        ref.set(doc.getDouble(key));
      }
      else if(Double.class.isAssignableFrom(f.getType())
          || double.class.isAssignableFrom(f.getType())) {
        ref.set(doc.getDouble(key));
      }
      else if(Double.class.isAssignableFrom(f.getType())
          || double.class.isAssignableFrom(f.getType())) {
        ref.set(doc.getDouble(key));
      }
    }
    return null;
  }
  
  
  public static void main(String[] args) {
    double d = 5.0;
    Object obj = d;
    System.out.println("* d="+ obj);
    System.out.println("* d is Double? "+ Double.class.isAssignableFrom(obj.getClass()));
    System.out.println("* d is double? "+ double.class.isAssignableFrom(obj.getClass()));
  }
  
}
