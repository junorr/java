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

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/09/2014
 */
public class FileHandler {

  private RandomAccessFile raf;
  
  private String file;
  
  private String line;
  
  
  public FileHandler(String file) throws IOException {
    init(file);
  }
  
  
  public FileHandler(String file, long seek) throws IOException {
    init(file);
    this.seek(seek);
  }
  
  
  private void init(String file) throws IOException {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException("Invalid file: "+ file);
    this.file = file;
    raf = new RandomAccessFile(file, "rw");
  }
  
  
  public FileHandler seek(long pos) throws IOException {
    if(pos >= 0 && pos <= raf.length()) {
      raf.seek(pos);
    }
    return this;
  }
  
  
  public long seek(String str) throws IOException {
    if(str == null || str.isEmpty())
      return -1;
    
    long ini = position();
    
    try {
      long pos = position();
      StringBuffer buf = new StringBuffer();
      byte[] b = new byte[1];
      
      while(true) {
        int r = raf.read(b);
        if(r <= 0) break;
        buf.append((char)b[0]);
        pos++;
        if(buf.length() > str.length()) {
          buf.deleteCharAt(0);
        }
        if(str.equals(buf.toString())) {
          pos -= str.length();
          seek(pos);
          return pos;
        }
      }
    }
    catch(EOFException e) {
      raf.seek(ini);
    }
    return -1;
  }
  
  
  public long seekRegex(String rgx) throws IOException {
    if(rgx == null || rgx.isEmpty())
      return -1;
    
    long ini = position();
    Pattern pt = Pattern.compile(rgx);
    
    try {
      long pos = position();
      String ln = null;
      
      while(true) {
        ln = raf.readLine();
        if(ln == null) throw new EOFException();
        Matcher m = pt.matcher(ln);
        if(m.find()) {
          pos += m.start();
          seek(pos);
          return pos;
        }
        pos += ln.length()+1;
      }
    }
    catch(EOFException e) {
      raf.seek(ini);
      return -1;
    }
  }
  
  
  public List<Long> seekAllRegex(String rgx) throws IOException {
    if(rgx == null || rgx.isEmpty())
      return null;
    
    LinkedList<Long> ls = new LinkedList<>();
    long pos = 0;
    while(true) {
      pos = seekRegex(rgx);
      if(pos < 0) break;
      ls.add(pos++);
      seek(pos);
    }
    return ls;
  }
  
  
  public List<Long> seekAll(String str) throws IOException {
    if(str == null || str.isEmpty())
      return null;
    
    LinkedList<Long> ls = new LinkedList<>();
    long pos = 0;
    while(true) {
      pos = seek(str);
      if(pos < 0) break;
      ls.add(pos++);
      seek(pos);
    }
    return ls;
  }
  
  
  public FileHandler moveToNexLine() throws IOException {
    long pos = seek("\n");
    if(pos < 1) pos = raf.length();
    else pos++;
    raf.seek(pos);
    return this;
  }
  
  
  public FileHandler moveToEnd() throws IOException {
    if(!isEOF())
      raf.seek(raf.length());
    return this;
  }
  
  
  public long position() throws IOException {
    return raf.getFilePointer();
  }
  
  
  public boolean isEOF() throws IOException {
    return position() >= raf.length() -1;
  }
  
  
  public String readLine() throws IOException {
    try {
      line = raf.readLine();
      return line;
    } catch(EOFException e) {
      return null;
    }
  }
  
  
  public String lastLine() {
    return line;
  }
  
  
  public FileHandler writeLine(String ln) throws IOException {
    if(ln != null && !ln.isEmpty()) {
      raf.writeBytes(ln + "\n");
    }
    return this;
  }
  
  
  public FileHandler write(String str) throws IOException {
    if(str != null && !str.isEmpty()) {
      raf.writeBytes(str);
    }
    return this;
  }
  
  
  protected RandomAccessFile internal() {
    return raf;
  }
  
  
  public void close() throws IOException {
    raf.close();
  }
  
  
  public FileHandler update(String str) throws IOException {
    long pos = position();
    if(!isEOF()) {
      Path p = Files.createTempFile(null, null);
      Files.copy(Paths.get(file), p, StandardCopyOption.REPLACE_EXISTING);
      raf.writeBytes(str + "\n");
      FileHandler fh = new FileHandler(p.toString());
      fh.seek(pos).moveToNexLine();
      pos = position();
      if(!fh.isEOF()) {
        transfer(fh.internal(), raf);
      }
      fh.close();
      raf.setLength(raf.getFilePointer());
      raf.seek(pos);
      Files.delete(p);
    }
    else {
      raf.writeBytes(str + "\n");
    }
    return this;
  }
  
  
  public String deleteLineRegex(String rgx) throws IOException {
    if(rgx == null || rgx.isEmpty())
      return null;
    long pos = seekRegex(rgx);
    if(pos < 0) return null;
    long le = seek("\n");
    if(le < 0) le = raf.length();
    String str = readString((int)pos, (int)(le - pos));
    seek(pos);//.write("\n");
    //pos = position();
    
    if(!isEOF()) {
      Path p = Files.createTempFile(null, null);
      Files.copy(Paths.get(file), p, StandardCopyOption.REPLACE_EXISTING);
      FileHandler fh = new FileHandler(p.toString());
      fh.seek(pos).moveToNexLine();
      if(!fh.isEOF()) {
        transfer(fh.internal(), raf);
      }
      fh.close();
      raf.setLength(raf.getFilePointer());
      raf.seek(pos);
      Files.delete(p);
    }
    return str;
  }
  
  
  public String deleteLineWith(String str) throws IOException {
    if(str == null || str.isEmpty())
      return null;
    long pos = seek(str);
    if(pos < 0) return null;
    long le = seek("\n");
    if(le < 0) le = raf.length();
    String sln = readString((int)pos, (int)(le - pos));
    seek(pos);//.write("\n");
    //pos = position();
    
    if(!isEOF()) {
      Path p = Files.createTempFile(null, null);
      Files.copy(Paths.get(file), p, StandardCopyOption.REPLACE_EXISTING);
      FileHandler fh = new FileHandler(p.toString());
      fh.seek(pos).moveToNexLine();
      if(!fh.isEOF()) {
        transfer(fh.internal(), raf);
      }
      fh.close();
      raf.setLength(raf.getFilePointer());
      raf.seek(pos);
      Files.delete(p);
    }
    return sln;
  }
  
  
  public String readString(int start, int length) throws IOException {
    if(start < 0 || length < 1 
        || (start + length) > raf.length())
      return null;
    long pos = position();
    seek(start);
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < length; i++) {
      sb.append((char)raf.read());
    }
    seek(pos);
    return sb.toString();
  }
  
  
  protected static void transfer(RandomAccessFile r, RandomAccessFile w) throws IOException {
    if(r == null || r.getFilePointer() >= r.length() -1)
      return;
    if(w == null) return;
    byte[] buf = new byte[1024];
    int read = 0;
    while(true) {
      read = r.read(buf);
      if(read <= 0) break;
      w.write(buf, 0, read);
    }
  }
  
  
  protected static void transfer(RandomAccessFile r, OutputStream w) throws IOException {
    if(r == null || r.getFilePointer() >= r.length() -1)
      return;
    if(w == null) return;
    byte[] buf = new byte[1024];
    int read = 0;
    while(true) {
      read = r.read(buf);
      if(read <= 0) break;
      w.write(buf, 0, read);
    }
    w.flush();
  }
  
  
  public String cat(long start) throws IOException {
    long pos = position();
    seek(start);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    transfer(raf, bos);
    seek(pos);
    return bos.toString();
  }
  
  
  public static void main(String[] args) throws IOException {
    FileHandler fh = new FileHandler("/home/juno/file.txt");
    System.out.println("----- cat -----");
    System.out.println(fh.cat(0));
    System.out.println("----- cat -----");
    List<Long> ls = fh.seekAll("vm");
    System.out.println("* seekAll(vm): "+ ls.size());
    for(long l : ls)
      System.out.println("  - "+ l);
    
    //System.out.println("* seek(vm): "+ fh.seek(0).seek("vm"));
    System.out.println("* readString(31, 2): '"+ fh.readString(31, 2)+ "'");
    System.out.println("* readString(56, 2): '"+ fh.readString(56, 2)+ "'");
    
    //System.out.println("* deleteLineRegex(($|^)sys): "+ fh.seek(0).deleteLineRegex("($|^)sys"));
    System.out.println("* deleteLineWith(sys): "+ fh.seek(0).deleteLineWith("sys"));
    System.out.println("----- cat -----");
    System.out.println(fh.cat(0));
    System.out.println("----- cat -----");
    
    /*
    fh.moveToNexLine().moveToNexLine();
    fh.update("Some new line");
    System.out.println("----- cat -----");
    System.out.println(fh.cat(0));
    System.out.println("----- cat -----");
    fh.moveToEnd().write("?");
    System.out.println("----- cat -----");
    System.out.println(fh.cat(0));
    System.out.println("----- cat -----");
    */
    fh.close();
  }
  
}
