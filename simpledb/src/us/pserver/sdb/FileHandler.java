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
import static us.pserver.sdb.SimpleDB.BYTE_END;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/09/2014
 */
public class FileHandler {
  
  public static final int BLOCK_SIZE = 256;
  

  private RandomAccessFile raf;
  
  private String file;
  
  private long block;
  
  
  public FileHandler(String file) throws IOException {
    init(file);
    block = 0;
  }
  
  
  private void init(String file) throws IOException {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException("Invalid file: "+ file);
    this.file = file;
    Path p = Paths.get(file);
    if(Files.exists(p)) {
      readFile(p);
    } else {
      createFile(p);
    }
  }
  
  
  private void createFile(Path file) throws IOException {
    if(file == null) return;
    if(file.getParent() != null 
        && !Files.exists(file.getParent()))
      Files.createDirectories(file.getParent());
    
    Files.createFile(file);
    raf = new RandomAccessFile(file.toFile(), "rw");
    this.writeLine(String.valueOf(Long.MAX_VALUE));
    nextBlock();
  }
  
  
  private void readFile(Path file) throws IOException {
    if(file == null) return;
    raf = new RandomAccessFile(file.toFile(), "rw");
    String str = this.readLine();
    if(str == null)
      throw new IOException(
          "Invalid file format: readLine()="+ str);
    try {
      long l = Long.parseLong(str);
      if(l != Long.MAX_VALUE)
        throw new IOException(
          "Invalid file format: "+ l+ " != "+ Long.MAX_VALUE);
    } catch(NumberFormatException e) {
      throw new IOException("Invalid file format: wrong header '"+ str+ "'");
    }
    nextBlock();
  }
  
  
  public FileHandler seekBlock(long blk) throws IOException {
    if(blk >= 0) {
      if(raf.length() < blk * BLOCK_SIZE)
        raf.setLength((blk + 1) * BLOCK_SIZE);
      block = blk;
      seek(block * BLOCK_SIZE);
    }
    return this;
  }
  
  
  public FileHandler seekBlockFromPos(long pos) throws IOException {
    return seekBlock(blockFromPos(pos));
  }
  
  
  public static long blockFromPos(long pos) {
    if(pos <= 0) return 0;
    long block = pos / BLOCK_SIZE;
    if(pos % BLOCK_SIZE != 0)
      block++;
    return block;
  }
  
  
  public long getBlock() {
    return block;
  }
  
  
  public FileHandler nextBlock() throws IOException {
    seek(position());
    return this.seekBlock(++block);
  }
  
  
  public FileHandler prevBlock() throws IOException {
    if(block -1 >= 0) {
      seek(position());
      seekBlock(--block);
    }
    return this;
  }
  
  
  public boolean hasNextBlock() throws IOException {
    return raf.length() > (position() + BLOCK_SIZE);
  }
  
  
  public long seekByte(int b) throws IOException {
    long ini = position();
    long pos = ini;
    try {
      byte[] bs = new byte[1];
      int read = 0;
      while(true) {
        read = raf.read(bs);
        if(read <= 0) break;
        if(bs[0] == b) return pos;
        pos++;
      }
    } catch(EOFException e) {}
    seek(ini);
    return -1;
  }
  
  
  public FileHandler seek(long pos) throws IOException {
    if(pos >= 0 && pos <= raf.length()) {
      seek(pos);
      block = pos / BLOCK_SIZE;
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
      seek(ini);
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
      seek(ini);
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
    seek(pos);
    return this;
  }
  
  
  public FileHandler moveToEnd() throws IOException {
    if(!isEOF())
      seek(raf.length());
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
      return raf.readLine();
    } catch(EOFException e) {
      return null;
    }
  }
  
  
  public FileHandler writeLine(String ln) throws IOException {
    if(ln != null && !ln.isEmpty()) {
      raf.writeBytes(ln);
      raf.writeByte(BYTE_END);
    }
    return this;
  }
  
  
  public FileHandler write(String str) throws IOException {
    if(str != null && !str.isEmpty()) {
      raf.writeBytes(str);
    }
    return this;
  }
  
  
  public FileHandler writeByte(int b) throws IOException {
    raf.writeByte(b);
    return this;
  }
  
  
  public byte readByte() throws IOException {
    return raf.readByte();
  }
  
  
  protected RandomAccessFile internal() {
    return raf;
  }
  
  
  public void close() throws IOException {
    raf.close();
  }
  
  
  public String deleteCurrentLine() throws IOException {
    long le = seekByte(BYTE_END);
    if(le < 0) le = raf.length();
    long pos = position();
    String str = readString((int)pos, (int)(le - pos));
    seek(pos);
    
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
      seek(pos);
      Files.delete(p);
    }
    return str;
  }
  
  
  public String deleteLineRegex(String rgx) throws IOException {
    if(rgx == null || rgx.isEmpty())
      return null;
    long pos = seekRegex(rgx);
    if(pos < 0) return null;
    seek(pos);
    return deleteCurrentLine();
  }
  
  
  public String deleteLineWith(String str) throws IOException {
    if(str == null || str.isEmpty())
      return null;
    long pos = seek(str);
    if(pos < 0) return null;
    seek(pos);
    return deleteCurrentLine();
  }
  
  
  public String readString(int start, int length) throws IOException {
    if(start < 0 || length < 1 
        || (start + length) > raf.length())
      return null;
    seek(start);
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < length; i++) {
      sb.append((char)raf.read());
    }
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
