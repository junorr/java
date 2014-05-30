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

package com.jpower.lcdpaper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
  
  
  public Config() {
    map = new TreeMap<>();
    file = null;
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
  
  
  public boolean save() {
    if(file == null) return false;
    if(map.isEmpty()) return false;
    String comment = null;
    if(map.containsKey("COMMENT"))
      comment = map.remove("COMMENT").toString();
    try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
      pw.println(comment);
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
      pw.flush();
      pw.close();
    } catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
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
      
      do {
        
        line = br.readLine();
        
        if(line != null && line.contains(EQ)) {
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
    
    List l = new LinkedList();
    String[] keys = this.keysArray();
    for(int i = 0; i < keys.length; i++) {
      if(keys[i].contains(key))
        l.add(this.getObject(keys[i]));
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
  
  
  public InetSocketAddress getSocketAddress(String keyAddress, String keyPort) {
    if(map.isEmpty() || !map.containsKey(keyAddress) 
        || !map.containsKey(keyPort))
      return null;
    
    return new InetSocketAddress(
        this.get(keyAddress), 
        this.getInt(keyPort));
  }
  
  
  public Config put(String key, Object obj) {
    if(obj instanceof List)
      return putList(key, (List) obj);
    if(key != null || !key.isEmpty())
      map.put(key, String.valueOf(obj));
    return this;
  }
  
  
  private Config putList(String key, List l) {
    if(l != null && !l.isEmpty())
      for(int i = 0; i < l.size(); i++) {
        this.put(key.concat("_").concat(
            String.valueOf(i)), l.get(i));
      }
    return this;
  }
  
  
  public boolean contains(String key) {
    if(key.contains("list") || key.contains("LIST")) {
      boolean b = false;
      String[] keys = this.keysArray();
      for(String k : keys) {
        b = k.contains(key);
        if(b) return b;
      }
      return b;
    }
    return map.containsKey(key);
  }
  
  
  public Config clear() {
    map.clear();
    return this;
  }
  
}
