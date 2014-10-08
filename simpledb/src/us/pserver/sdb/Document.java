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

package us.pserver.sdb;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class Document {

  public static final String 
      SSTR = "str",
      SINT = "int",
      SDOUBLE = "double",
      SLONG = "long",
      SBOOLEAN = "boolean",
      SOJS = "ojs.";
  
  
  private transient long block;
  
  private String label;
  
  private final Map<String, Object> map;
  
  
  protected Document(String lbl, long blk) {
    block = blk;
    label = lbl;
    map = new LinkedHashMap<>();
  }
  
  
  public Document() {
    block = -1;
    label = null;
    map = new LinkedHashMap<>();
  }
  
  
  public Document(String label) {
    this();
    label(label);
  }

  
  public String label() {
    return label;
  }
  
  
  public Document label(String lbl) {
    if(lbl == null || lbl.isEmpty())
      throw new IllegalArgumentException(
          "Invalid index label: "+ lbl);
    if(lbl.length() > 50)
      lbl = lbl.substring(0, 50);
    this.label = lbl;
    return this;
  }
  
  
  public long block() {
    return block;
  }
  
  
  public Document block(long blk) {
    block = blk;
    return this;
  }
  
  
  public Map<String, Object> map() {
    return map;
  }
  
  
  public Object get(String key) {
    if(key != null && !key.isEmpty() 
        && map.containsKey(key)) {
      return map.get(key);
    }
    return null;
  }
  
  
  public <T> T getAs(String key) {
    try {
      return (T) this.get(key);
    } catch(Exception e) {
      return null;
    }
  }
  
  
  public String getString(String key) {
    if(key != null && !key.isEmpty() 
        && map.containsKey(key)) {
      return map.get(key).toString();
    }
    return null;
  }
  
  
  public int getInt(String key) {
    if(key != null && !key.isEmpty() 
        && map.containsKey(key)) {
      Object val = map.get(key);
      if(val != null && Number.class
          .isAssignableFrom(val.getClass()))
        return ((Number)val).intValue();
    }
    return -1;
  }
  
  
  public long getLong(String key) {
    if(key != null && !key.isEmpty() 
        && map.containsKey(key)) {
      Object val = map.get(key);
      if(val != null && Number.class
          .isAssignableFrom(val.getClass()))
        return ((Number)val).longValue();
    }
    return -1;
  }
  
  
  public double getDouble(String key) {
    if(key != null && !key.isEmpty() 
        && map.containsKey(key)) {
      Object val = map.get(key);
      if(val != null && Number.class
          .isAssignableFrom(val.getClass()))
        return ((Number)val).doubleValue();
    }
    return -1;
  }
  
  
  public boolean getBoolean(String key) {
    if(key != null && !key.isEmpty() 
        && map.containsKey(key)) {
      Object val = map.get(key);
      if(Boolean.class.isAssignableFrom(val.getClass()))
        return ((Boolean)val);
    }
    return false;
  }
  
  
  public Document put(String key, String value) {
    if(key != null && !key.isEmpty()) {
      map.put(key, value);
    }
    return this;
  }
  
  
  public Document put(String key, double value) {
    if(key != null && !key.isEmpty()) {
      map.put(key, value);
    }
    return this;
  }
  
  
  public Document put(String key, long value) {
    if(key != null && !key.isEmpty()) {
      map.put(key, value);
    }
    return this;
  }
  
  
  public Document put(String key, boolean value) {
    if(key != null && !key.isEmpty()) {
      map.put(key, value);
    }
    return this;
  }
  
  
  public Document put(String key, int value) {
    if(key != null && !key.isEmpty()) {
      map.put(key, value);
    }
    return this;
  }
  
  
  public Document put(String key, Object value) {
    if(key != null && !key.isEmpty() && value != null) {
      map.put(key, value);
    }
    return this;
  }
  
  
  public Document putLong(String key, String val) {
    try {
      this.put(key, Long.parseLong(val));
    } catch(NumberFormatException e) {}
    return this;
  }
  
  
  public Document putInt(String key, String val) {
    try {
      this.put(key, Integer.parseInt(val));
    } catch(NumberFormatException e) {}
    return this;
  }
  
  
  public Document putDouble(String key, String val) {
    try {
      this.put(key, Double.parseDouble(val));
    } catch(NumberFormatException e) {}
    return this;
  }
  
  
  public Document putBoolean(String key, String val) {
    return this.put(key, Boolean.parseBoolean(val));
  }
  
  
  public static Document fromXml(String xml) {
    XStream x = new XStream();
    return (Document) x.fromXML(xml);
  }
  
  
  public String toXml() {
    XStream x = new XStream();
    StringWriter sw = new StringWriter();
    x.marshal(this, new CompactWriter(sw));
    return sw.toString();
  }
  
  
  @Override
  public String toString() {
    return toXml();
  }
  
  
  public static void main(String[] args) {
    Document doc = new Document("server");
    doc.put("ip", "172.29.14.102")
        .put("port", 22)
        .put("active", true)
        .put("cred", new Document("credentials")
            .put("user", "juno")
            .put("pass", "12345678"))
        .put("latency", 0.84);
    System.out.println("* xml=" + doc);
    System.out.println("* doc="+ Document.fromXml(doc.toXml()));
  }
  
}
