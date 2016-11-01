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

package us.pserver.str;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2014
 */
public class Strings {

  private String str;
  
  
  public Strings() {
    str = null;
  }
  
  
  public Strings(String s) {
    this.setString(s);
  }
  
  
  public String getString() {
    return str;
  }
  
  
  protected void checkEmpty(String id, String s) {
    if(s == null || s.isEmpty())
      throw new IllegalArgumentException("Invalid empty "+ id+ " ["+ s+ "]");
  }
  
  
  public Strings setString(String s) {
    checkEmpty("String", s);
    str = s;
    return this;
  }
  
  
  //a
  public int[] findAll(String s) {
    checkEmpty("Argument", s);
    checkEmpty("String", str);
    int ix = 0;
    List<Integer> ls = new LinkedList<>();
    while(true) {
      ix = str.indexOf(s, ix);
      if(ix < 0) break;
      else ls.add(ix++);
    }
    int[] is = new int[ls.size()];
    for(int i = 0; i < ls.size(); i++) {
      is[i] = ls.get(i);
    }
    return is;
  }
  
  
  //f
  public int find(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return str.indexOf(s);
  }
  
  
  //f
  public int find(String s, int idx) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    if(idx < 0 || idx > str.length())
      throw new IllegalArgumentException("Invalid find index: "+ idx);
    System.out.println("find( "+ s+ ", "+ idx+ " ): "+ str.indexOf(s, idx));
    return str.indexOf(s, idx);
  }
  
  
  //l
  public int last(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return str.lastIndexOf(s);
  }
  
  
  //u
  public String upper() {
    checkEmpty("String", str);
    return str.toUpperCase();
  }
  
  
  //w
  public String lower() {
    checkEmpty("String", str);
    return str.toLowerCase();
  }
  
  
  //e
  public int equals(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.equals(s) ? 1 : 0);
  }
  
  
  //n
  public int notEquals(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.equals(s) ? 0 : 1);
  }
  
  
  //E
  public int equalsics(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.toLowerCase().equals(s.toLowerCase()) ? 1 : 0);
  }
  
  
  //c
  public int contains(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.contains(s) ? 1 : 0);
  }
  
  
  //C
  public int containsics(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.toLowerCase().contains(s.toLowerCase()) ? 1 : 0);
  }
  
  
  //A
  public String charAt(int idx) {
    checkEmpty("String", str);
    if(idx < 0 || idx > str.length())
      throw new IllegalArgumentException("Invalid char index: "+ idx);
    return String.valueOf(str.charAt(idx));
  }
  
  
  //L
  public int length() {
    checkEmpty("String", str);
    return str.length();
  }
  
  
  //s
  public int startsWith(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.startsWith(s) ? 1 : 0);
  }
  
  
  //S
  public int startsWithIcs(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.toLowerCase().startsWith(s.toLowerCase()) ? 1 : 0);
  }
  
  
  //d
  public int endsWith(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.endsWith(s) ? 1 : 0);
  }
  
  
  //D
  public int endsWithIcs(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return (str.toLowerCase().endsWith(s.toLowerCase()) ? 1 : 0);
  }
  
  
  //p
  public String[] split(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return str.split(s);
  }
  
  
  //r
  public String replace(String match, String rep) {
    checkEmpty("String", str);
    checkEmpty("Match", match);
    checkEmpty("Argument", rep);
    return str.replace(match, rep);
  }
  
  
  //b
  public String subs(int off, int length) {
    checkEmpty("String", str);
    if(off < 0 || off >= str.length())
      throw new IllegalArgumentException("Invalid offset: "+ off);
    if(length < 1 || (off + length) > str.length())
      throw new IllegalArgumentException("Invalid length: "+ length);
    return str.substring(off, off + length);
  }
  
  
  //B
  public String subs(int off) {
    checkEmpty("String", str);
    if(off < 0 || off >= str.length())
      throw new IllegalArgumentException("Invalid offset: "+ off);
    return str.substring(off);
  }
  
  
  //t
  public String trim() {
    checkEmpty("String", str);
    str = str.trim();
    return str;
  }
  
  
  //T
  public String concat(String s) {
    checkEmpty("String", str);
    checkEmpty("Argument", s);
    return str.concat(s);
  }
  
  
  //F
  public void load(String file) {
    checkEmpty("File", file);
    File f = new File(file);
    if(!f.exists())
      throw new IllegalArgumentException("File does not exists: "+ file);
    try {
      FileInputStream is = new FileInputStream(f);
      load(new FileInputStream(f));
    } catch(IOException e) {
      throw new IllegalArgumentException("Cant read file: "+ file, e);
    }
  }
  
  
  //|
  public void load(InputStream is) {
    if(is == null)
      throw new IllegalArgumentException("Invalid input: "+ is);
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      byte[] buf = new byte[512];
      int read = -1;
      while(true) {
        read = is.read(buf);
        if(read < 1) break;
        bos.write(buf, 0, read);
      }
      str = bos.toString();
      is.close();
    } catch(IOException e) {
      throw new IllegalArgumentException("Cant read input: "+ is, e);
    }
  }
  
}
