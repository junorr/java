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

package us.pserver.test;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/01/2017
 */
public class CustomWrite {
  
  
  static class MyValue {
    private final String value;
    private final int magic;
    
    public MyValue() {
      value = null;
      magic = 0;
    }
    
    public MyValue(String value, int magic) {
      this.value = value;
      this.magic = magic;
    }

    public String getValue() {
      return value;
    }

    public int getMagic() {
      return magic;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 67 * hash + Objects.hashCode(this.value);
      hash = 67 * hash + this.magic;
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final MyValue other = (MyValue) obj;
      if (this.magic != other.magic) {
        return false;
      }
      if (!Objects.equals(this.value, other.value)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "MyValue{" + "value=" + value + ", magic=" + magic + '}';
    }
    
  }
  
  
  
  
  
  static class RefObject {
    private final String name;
    private final long position;
    private final long length;
    
    public RefObject() {
      name = null;
      position = 0;
      length = 0;
    }

    public RefObject(String name, long pos, long len) {
      this.name = name;
      this.position = pos;
      this.length = len;
    }

    public String getName() {
      return name;
    }

    public long getPosition() {
      return position;
    }

    public long getLength() {
      return length;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 97 * hash + Objects.hashCode(this.name);
      hash = 97 * hash + (int) (this.position ^ (this.position >>> 32));
      hash = 97 * hash + (int) (this.length ^ (this.length >>> 32));
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final RefObject other = (RefObject) obj;
      if (this.position != other.position) {
        return false;
      }
      if (this.length != other.length) {
        return false;
      }
      if (!Objects.equals(this.name, other.name)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "RefObject{" + "name=" + name + ", pos=" + position + ", len=" + length + '}';
    }
    
  }
  
  
  
  
  
  static class MyObject {
    private final String name;
    private final int code;
    private final List<MyValue> values;

    public MyObject() {
      name = null;
      code = 0;
      values = null;
    }

    public MyObject(String name, int code, List<MyValue> values) {
      this.name = name;
      this.code = code;
      this.values = values;
    }

    public MyObject(String name, int code) {
      this.name = name;
      this.code = code;
      this.values = new ArrayList<>();
    }

    public MyObject(String name) {
      this.name = name;
      this.code = (int) (Math.random() * Integer.MAX_VALUE);
      this.values = new ArrayList<>();
    }
    
    public MyObject add(MyValue val) {
      if(val != null) {
        values.add(val);
      }
      return this;
    }

    public String getName() {
      return name;
    }

    public int getCode() {
      return code;
    }

    public List<MyValue> getValues() {
      return values;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 37 * hash + Objects.hashCode(this.name);
      hash = 37 * hash + this.code;
      hash = 37 * hash + Objects.hashCode(this.values);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final MyObject other = (MyObject) obj;
      if (this.code != other.code) {
        return false;
      }
      if (!Objects.equals(this.name, other.name)) {
        return false;
      }
      if (!Objects.equals(this.values, other.values)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "MyObject{" + "name=" + name + ", code=" + code + ", values=" + values + '}';
    }
    
  }
  
  
  
  
  
  public static void main(String[] args) {
    MyObject obj = new MyObject("object");
    obj.add(new MyValue("v1", 1))
        .add(new MyValue("v2", 2))
        .add(new MyValue("v3", 3));
    System.out.println("* obj: "+ obj);
    
    Any any = JsonStream.wrap(obj);
    String json = any.toString();
    System.out.println("* json: "+ json);
    
    Map<String,Any> map = any.asMap();
    map.put("@type", Any.wrap(obj.getClass().getName()));
    map.put("values", Any.wrap(new RefObject("values", 500, 200)));
    json = Any.wrapAnyMap(map).toString();
    System.out.println("* json: "+ json);
    
    any = JsonIterator.deserialize(json);
    System.out.println("* any: "+ any);
    List<MyValue> vals = new ArrayList<>();
    vals.add(new MyValue("v1", 1));
    vals.add(new MyValue("v2", 2));
    vals.add(new MyValue("v3", 3));
    map = any.asMap();
    map.put("values", Any.wrap(vals));
    
    any = Any.wrapAnyMap(map);
    System.out.println("* obj: "+ JsonIterator.deserialize(any.toString(), MyObject.class));
  }
  
  
}
