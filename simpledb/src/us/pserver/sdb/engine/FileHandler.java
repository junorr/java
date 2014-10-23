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

package us.pserver.sdb.engine;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  
  public static final int BLOCK_SIZE = 256;
  
  public static final int BYTE_END = 10;
  

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
  
  
  private void init2(String file) throws IOException {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException("Invalid file: "+ file);
    this.file = file;
    Path p = Paths.get(file);
    raf = new RandomAccessFile(p.toFile(), "rw");
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
  
  
  public boolean isBlockBoundary() throws IOException {
    return position() % BLOCK_SIZE == 0;
  }
  
  
  public FileHandler seekBlock(long blk) throws IOException {
    if(blk >= 0) {
      if(raf.length() < blk * BLOCK_SIZE)
        raf.setLength(blk * BLOCK_SIZE);
      block = blk;
      seek(block * BLOCK_SIZE);
    }
    return this;
  }
  
  
  public FileHandler seekBlock(int blk) throws IOException {
    if(blk >= 0) {
      if(raf.length() < blk * BLOCK_SIZE)
        raf.setLength(blk * BLOCK_SIZE);
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
  
  
  public long block() {
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
  
  
  public long reverseSeekByte(int b) throws IOException {
    long ini = position();
    long pos = ini;
    try {
      byte[] bs = new byte[1];
      int read = 0;
      while(pos >= 0) {
        raf.seek(pos);
        read = raf.read(bs);
        if(read <= 0) break;
        if(bs[0] == b) return pos;
        pos -= 2;
      }
    } catch(EOFException e) {}
    seek(ini);
    return -1;
  }
  
  
  public FileHandler seek(long pos) throws IOException {
    if(pos >= 0 && pos <= raf.length()) {
      raf.seek(pos);
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
  
  
  public byte[] readLineBytes() throws IOException {
    try {
      long ini = position();
      long p = seekByte(BYTE_END);
      if(p < 0) p = position();
      int size = (int) (p - ini);
      if(size < 1) return null;
      byte[] bs = new byte[size];
      seek(ini);
      size = raf.read(bs);
      if(size < 1) return null;
      return bs;
    } catch(EOFException e) {
      return null;
    }
  }
  
  
  public FileHandler writeLine(byte[] bts) throws IOException {
    if(bts != null && bts.length > 0) {
      raf.write(bts);
      raf.writeByte(BYTE_END);
    }
    return this;
  }
  
  
  public FileHandler write(byte[] bts) throws IOException {
    if(bts != null && bts.length > 0) {
      raf.write(bts);
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
  
  
  public String clearLine() throws IOException {
    long ini = position();
    long le = seekByte(BYTE_END);
    if(le < 0) le = raf.length();
    String old = readString((int)ini, (int)(le - ini));
    
    raf.seek(ini);
    for(int i = 0; i < old.length(); i++) {
      writeByte('0');
    }
    return old;
  }
  
  
  public FileHandler clearBlocks(int size) throws IOException {
    for(int i = 0; i < (size * BLOCK_SIZE); i++) {
      writeByte('0');
    }
    return this;
  }
  
  
  public String updateLine(String ln) throws IOException {
    if(ln == null || ln.isEmpty())
      return null;
    long ini = position();
    long le = seekByte(BYTE_END);
    if(le < 0) le = raf.length();
    String old = readString((int)ini, (int)(le - ini));
    
    if(ln.length() <= old.length()) {
      raf.seek(ini);
      write(ln);
      deleteLine();
    }
    else if(!isEOF()) {
      int dif = ln.length() - old.length();
      byte[] rw = new byte[dif];
      byte[] buf = new byte[dif];
      long from = le;
      long to = from + dif;
      int read = 0;
      int rbuf = 0;
      
      raf.seek(to);
      if(!isEOF()) {
        rbuf = raf.read(buf);
      }
      raf.seek(from);
      read = raf.read(rw);
      
      while(true) {
        raf.seek(to); 
        raf.write(rw, 0, read);
        rw = buf;
        read = rbuf;
        to += dif;
        dif = BLOCK_SIZE;
        buf = new byte[dif];
        
        raf.seek(to);
        if(isEOF()) {
          if(read > 0)
            raf.write(rw, 0, read);
          seek(ini).writeLine(ln);
          return old;
        }
        rbuf = raf.read(buf);
      }
    }
    return old;
  } 
  
  
  public FileHandler insertBlocks(int blocks) throws IOException {
    if(blocks < 1) return this;
    long ini = position();
    int insb = blocks * BLOCK_SIZE;
    
    if(!isEOF()) {
      byte[] rw = new byte[insb];
      byte[] buf = new byte[insb];
      long from = ini;
      long to = from + insb;
      int read = 0;
      int rbuf = 0;
      
      raf.seek(to);
      if(!isEOF()) {
        rbuf = raf.read(buf);
      }
      raf.seek(from);
      read = raf.read(rw);
      
      while(true) {
        if(read < 1) {
          break;
        }
        raf.seek(to); 
        raf.write(rw, 0, read);
        rw = buf;
        read = rbuf;
        to += insb;
        buf = new byte[insb];
        
        raf.seek(to);
        if(isEOF()) {
          if(read > 0)
            raf.write(rw, 0, read);
        }
        rbuf = raf.read(buf);
      }
    }
    return this;
  } 
  
  
  public String deleteLine() throws IOException {
    long ini = position();
    long le = seekByte(BYTE_END);
    if(le < 0) le = raf.length() - ini;
    String str = readString((int)ini, (int)(le - ini));
    
    if(isEOF()) {
      raf.setLength(ini);
    }
    else {
      byte[] buf = new byte[BLOCK_SIZE];
      long to = ini;
      long from = to + (le - ini);
      int read = 0;
      while(true) {
        raf.seek(from);
        if(isEOF()) {
          raf.setLength(to - BLOCK_SIZE + read);
          break;
        }
        read = raf.read(buf);
        if(read <= 0) break;
        raf.seek(to);
        raf.write(buf, 0, read);
        to += BLOCK_SIZE;
        from += BLOCK_SIZE;
      }
    }
    return str;
  }
  
  
  public String deleteLineRegex(String rgx) throws IOException {
    if(rgx == null || rgx.isEmpty())
      return null;
    long pos = seekRegex(rgx);
    if(pos < 0) return null;
    seek(pos);
    return deleteLine();
  }
  
  
  public String deleteLineWith(String str) throws IOException {
    if(str == null || str.isEmpty())
      return null;
    long pos = seek(str);
    if(pos < 0) return null;
    seek(pos);
    return deleteLine();
  }
  
  
  public String readString(long start, int length) throws IOException {
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
  
  
  public void rawCat() throws IOException {
    long pos = position();
    seek(0);
    byte[] bs = new byte[1];
    int read = 0;
    int count = 1;
    while(true) {
      read = raf.read(bs);
      if(read < 1) break;
      System.out.print("\t"+(bs[0] == 0 || bs[0] == 10 ? "0" : (char)bs[0]));
      if(count++ % 20 == 0)
        System.out.println();
    }
    seek(pos);
  }
  
  
  public static void main(String[] args) throws IOException {
    FileHandler fh = new FileHandler("/home/juno/file.txt");
    System.out.println("----- cat -----");
    fh.rawCat();
    System.out.println("----- cat -----");
    //List<Long> ls = fh.seekAll("vm");
    //System.out.println("* seekAll(vm): "+ ls.size());
    //for(long l : ls)
      //System.out.println("  - "+ l);
    
    //System.out.println("* seek(vm): "+ fh.seek(0).seek("vm"));
    //System.out.println("* readString(31, 2): '"+ fh.readString(31, 2)+ "'");
    //System.out.println("* readString(56, 2): '"+ fh.readString(56, 2)+ "'");
    
    //System.out.println("* deleteLineRegex(($|^)sys): "+ fh.seek(0).deleteLineRegex("($|^)sys"));
    //System.out.println("* deleteLineWith(sys): "+ fh.seek(0).deleteLineWith("sys"));
    System.out.println("* fh.seek(0).seek(\"sysctl\"): " + fh.seek(0).seek("sysctl"));
    //System.out.println("* fh.updateLine(\"some other text 1234567890 abc some other text 1234567890 abc\"): "+ fh.updateLine("some other text 1234567890 abc some other text 1234567890 abc"));
    //System.out.println("* fh.updateLine(\"some text\"): "+ fh.updateLine("some text"));
    System.out.println("* fh.insertBlocks(1)");
    fh.insertBlocks(1);
    
    /*
    fh.seek(0);
    System.out.println("* fh.seek(\"sys\"): "+ fh.seek("sys"));
    System.out.println("* fh.seek(\"\\n\"): "+ fh.seek("\n"));
    System.out.println("* fh.readString(27, 45-27): "+ fh.readString(27, 45-27));
    fh.seek(0).seek("sys");
    System.out.println("* fh.deleteLine(): "+ fh.deleteLine());
    
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
    System.out.println("----- cat -----");
    fh.rawCat();
    System.out.println("----- cat -----");
    fh.close();
  }
  
}
