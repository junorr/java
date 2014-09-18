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

package com.jpower.conf;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 20/09/2012
 */
public class Config implements Serializable {
  
  public static final String COMMENT = "COMMENT";
  
  public static final String EQ = ":=";
  
  
  private Map<String, Object> map;
  
  private File file;
  
  private final Base64ByteCoder cdr;
  
  private final StringByteConverter cv;
  
  
  public Config() {
    map = new TreeMap<>();
    file = null;
    cdr = new Base64ByteCoder();
    cv = new StringByteConverter();
  }
  
  
  public Config(String filePath) {
    this();
    this.setFile(filePath);
    this.load();
  }
  
  
  public Config(File f) {
    this();
    this.setFile(f);
    this.load();
  }
  
  
  public Base64ByteCoder getBase64Coder() {
    return cdr;
  }
  
  
  public boolean save() {
    if(file == null) return false;
    Object comment = null;
    if(map.containsKey("COMMENT"))
      comment = map.remove("COMMENT");

    try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
      if(comment != null)
        pw.println(comment.toString());
      
      pw.println("Configuration File");
      pw.println(new Date());
      pw.println("------------------");
      
      Iterator<String> it = map.keySet().iterator();
      while(it.hasNext()) {
        String s = it.next();
        pw.print(s);
        pw.print(EQ);
        pw.println(map.get(s));
      }
      if(comment != null) 
        map.put(COMMENT, comment);
      pw.flush();
      pw.close();
    } catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }
  
  
  public boolean overwrite() {
    if(file != null && file.exists())
      if(!file.delete()) return false;
    return this.save();
  }
  
  
  public Config setFile(String filePath) {
    if(filePath != null && !filePath.isEmpty())
      file = new File(filePath);
    return this;
  }
  
  
  public Config setFile(File f) {
    if(f != null)
      file = f;
    return this;
  }
  
  
  public Config setComment(String c) {
    if(c != null && !c.isEmpty())
      map.put(COMMENT, c);
    return this;
  }
  
  
  public boolean load() {
    if(file == null || !file.exists())
      return false;
    
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line = null;
      String[] split = null;
      
      line = br.readLine();
      if(line != null && !line.equals("null"))
        map.put(COMMENT, line);
      
      boolean afterComment = false;
      do {
        
        line = br.readLine();
        if(line == null) break;
        
        if(line.contains("------------------"))
          afterComment = true;
        
        if(afterComment && line.contains(EQ)) {
          split = line.split(EQ);
          
          if(split.length > 1) {
            map.put(split[0], split[1]);
          }
        }
        
      } while(line != null);
      
    } catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }
  
  
  public File getFile() {
    return file;
  }
  
  
  public int size() {
    return map.size();
  }
  
  
  public boolean isEmpty() {
    return map.isEmpty();
  }
  
  
  public Iterator<String> keys() {
    if(map.isEmpty()) return null;
    return map.keySet().iterator();
  }
  
  
  public String[] keysArray() {
    if(map.isEmpty()) return null;
    String[] ss = new String[map.size()];
    return map.keySet().toArray(ss);
  }
  
  
  public Iterator<Object> values() {
    if(map.isEmpty()) return null;
    return map.values().iterator();
  }
  
  
  public Object[] valuesArray() {
    if(map.isEmpty()) return null;
    return map.values().toArray();
  }
  
  
  public String get(String key) {
    if(key == null || key.isEmpty())
      return null;
    if(map.containsKey(key))
      return String.valueOf(map.get(key));
    else
      return null;
  }
  
  
  public Object getObject(String key) {
    if(key == null || key.isEmpty())
      return null;
    if(map.containsKey(key))
      return map.get(key);
    else
      return null;
  }
  
  
  public List getList(String key) {
    if(key == null || key.isEmpty()
        || !this.contains(key))
      return null;
    
    String val = String.valueOf(map.get(key));
    if(!val.contains(",")) return null;
    val = val.replace("[", "");
    val = val.replace("]", "");
    
    List l = new LinkedList();
    String[] array = val.split(", ");
    for(String s : array) {
      l.add(s);
    }
    return l;
  }
  
  
  public double getDouble(String key) {
    String s = this.get(key);
    if(s != null)
      try { return Double.parseDouble(s); }
      catch(NumberFormatException ex) {}
    return -1;
  }
  
  
  public long getLong(String key) {
    return (int) this.getDouble(key);
  }

  
  public int getInt(String key) {
    return (int) this.getDouble(key);
  }
  
  
  public InetAddress getAddress(String key) {
    if(map.isEmpty() || key == null 
        || key.isEmpty() || !map.containsKey(key)) 
      return null;
    try {
      return InetAddress.getByName(this.get(key));
    } catch(UnknownHostException ex) {}
    return null;
  }
  
  
  public Date getDate(String key) {
    if(!map.containsKey(key)) return null;
    String val = String.valueOf(map.get(key));
    return SimpleDate.parseDate(val);
  }
  
  
  public InetSocketAddress getSocketAddress(String keyAddress, String keyPort) {
    if(map.isEmpty() || !map.containsKey(keyAddress) 
        || !map.containsKey(keyPort))
      return null;
    
    return new InetSocketAddress(
        this.get(keyAddress), 
        this.getInt(keyPort));
  }
  
  
  public byte[] getBytes(String key) {
    String s = this.get(key);
    if(s != null && !s.isEmpty())
      return cdr.decode(cv.convert(s));
    return null;
  }
  
  
  public Object getSerializable(String key) {
    byte[] bs = this.getBytes(key);
    if(bs != null && bs.length > 0) {
      try (ByteArrayInputStream bis = new ByteArrayInputStream(bs);
          ObjectInputStream ois = new ObjectInputStream(bis)) {
        return ois.readObject();
      } catch(ClassNotFoundException | IOException e) {}
    }
    return null;
  }
  
  
  public Config put(String key, Object obj) {
    if(obj instanceof List)
      return putList(key, (List) obj);
    if(key != null || !key.isEmpty())
      map.put(key, String.valueOf(obj));
    return this;
  }
  
  
  public Config put(String key, String val) {
    if(key != null && !key.isEmpty() 
        && val != null && !val.isEmpty()) {
      map.put(key, val);
    }
    return this;
  }
  
  
  private Config putList(String key, List l) {
    if(l != null && !l.isEmpty()
        && key != null && !key.isEmpty()) {
      String val = "[";
      for(int i = 0; i < l.size(); i++) {
        val += String.valueOf(l.get(i));
        if(i < l.size()-1) val += ", ";
      }
      val += "]";
      map.put(key, val);
    }
    return this;
  }
  
  
  public Config put(String key, byte[] array, int off, int len) {
    if(key != null && !key.isEmpty()
        && array != null && array.length > 0) {
      this.put(key, cv.reverse(cdr.encode(array, off, len)));
    }
    return this;
  }
  
  
  public Config put(String key, byte[] array) {
    if(key != null && !key.isEmpty()
        && array != null && array.length > 0) {
      this.put(key, cv.reverse(cdr.encode(array)));
    }
    return this;
  }
  
  
  public Config put(String key, Date date) {
    if(key != null && date != null) {
      map.put(key, SimpleDate.from(date).toString());
    }
    return this;
  }
  
  
  public Config putSerializable(String key, Serializable srz) {
    if(key != null && !key.isEmpty()
        && srz != null) {
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(bos)) {
        oos.writeObject(srz);
        oos.flush();
        this.put(key, bos.toByteArray());
      } catch(IOException ex) {}
    }
    return this;
  }
  
  
  public boolean contains(String key) {
    return map.containsKey(key);
  }
  
  
  public Config clear() {
    map.clear();
    return this;
  }
  
  
  public static void main(String[] args) {
    Config c = new Config("./test.conf");
    c.clear();
    c.setComment("A Comment");
    c.save();
    
    class A implements Serializable {
      String s;
      Byte[] bs;
      int i;
    }
    
    class B implements Serializable {
      A a;
    }
    
    A a = new A();
    a.bs = new Byte[] {0, 5, 10};
    a.i = 11;
    a.s = A.class.getName();
    
    B b = new B();
    b.a = a;
    
    System.out.println("b.a : "+ b.a);
    System.out.println("a.i : "+ b.a.i);
    System.out.println("a.s : "+ b.a.s);
    System.out.println("a.bs: "+ b.a.bs.length);
    System.out.println("bs[0]: "+ b.a.bs[0]);
    System.out.println("bs[1]: "+ b.a.bs[1]);
    System.out.println("bs[2]: "+ b.a.bs[2]);
    
    c.putSerializable("B", b);
    c.putSerializable("A", a);
    c.put("date", new Date());
    c.put("a.bs", Arrays.asList(a.bs));
    System.out.println("c.contains( 'a.bs' ): "+ c.contains("a.bs"));
    c.save();
    
    c = new Config("./test.conf");
    c.load();
    b = (B) c.getSerializable("B");
    System.out.println("* b="+ b);
    
    List<String> lb = c.getList("a.bs");
    Byte[] bs = new Byte[lb.size()];
    for(int i = 0; i < lb.size(); i++) {
      bs[i] = Byte.parseByte(lb.get(i));
    }
    b.a.bs = bs;
    
    System.out.println("-------------------------");
    System.out.println("b.a : "+ b.a);
    System.out.println("a.i : "+ b.a.i);
    System.out.println("a.s : "+ b.a.s);
    System.out.println("a.bs: "+ b.a.bs.length);
    System.out.println("date: "+ c.getDate("date"));
    System.out.println("bs[0]: "+ b.a.bs[0]);
    System.out.println("bs[1]: "+ b.a.bs[1]);
    System.out.println("bs[2]: "+ b.a.bs[2]);
  }
  
}
