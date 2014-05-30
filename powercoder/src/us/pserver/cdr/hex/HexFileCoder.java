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

package us.pserver.cdr.hex;

import us.pserver.cdr.b64.*;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileCoder;
import us.pserver.cdr.StringByteConverter;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/08/2013
 */
public class HexFileCoder implements FileCoder {

  public static final int BUFFER_SIZE = 8192;
  
  
  private HexBufferCoder hcd;
  
  private ByteBufferConverter bcv;
  
  
  public HexFileCoder() {
    hcd = new HexBufferCoder();
    bcv = new ByteBufferConverter();
  }
  
  
  public Path path(String strPath) {
    if(strPath == null 
        || strPath.trim().isEmpty())
      return null;
    
    Path p = Paths.get(strPath);
    this.createIfNotExists(p, true);
    return p;
  }
  
  
  public boolean createIfNotExists(Path p, boolean isFile) {
    if(p == null) return false;
    try {
      if(Files.exists(p)) return true;
      if(isFile)
        Files.createFile(p);
      else
        Files.createDirectory(p);
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  @Override
  public boolean apply(Path src, Path dst, boolean encode) {
    if(src == null || dst == null
        || !Files.exists(src))
      return false;

    try {
      this.createIfNotExists(dst, true);
      FileChannel rc = FileChannel.open(
          src, StandardOpenOption.READ);
      FileChannel wc = FileChannel.open(
          dst, StandardOpenOption.WRITE);
      
      ByteBuffer buf = ByteBuffer
          .allocateDirect(BUFFER_SIZE);
      
      while(rc.read(buf) > 0) {
        buf.flip();
        wc.write(hcd.apply(buf, encode));
        buf = ByteBuffer
            .allocateDirect(BUFFER_SIZE);
      }
      
      rc.close();
      wc.close();
      return true;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  @Override
  public boolean applyTo(Path src, PrintStream ps, boolean encode) {
    if(src == null || !Files.exists(src)
        || ps == null)
      return false;
    
    try {
      FileChannel rc = FileChannel.open(
          src, StandardOpenOption.READ);
      
      ByteBuffer buf = ByteBuffer
          .allocateDirect(BUFFER_SIZE);
      StringByteConverter scv = new StringByteConverter();
      
      while(rc.read(buf) > 0) {
        buf.flip();
        buf = hcd.apply(buf, encode);
        byte[] bs = bcv.convert(buf);
        ps.println(scv.reverse(bs));
        buf = ByteBuffer
            .allocateDirect(BUFFER_SIZE);
      }
      
      rc.close();
      return true;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  @Override
  public boolean applyFrom(ByteBuffer buf, Path path, boolean encode) {
    if(buf == null || buf.limit() < 1
        || path == null) return false;
    
    if(!this.createIfNotExists(path, true))
      return false;
    
    buf = hcd.apply(buf, encode);
    try (FileChannel fc = FileChannel.open(
        path, StandardOpenOption.WRITE)) {
      fc.write(buf);
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  @Override
  public boolean encode(Path src, Path dst) {
    return this.apply(src, dst, true);
  }
  
  
  @Override
  public boolean decode(Path src, Path dst) {
    return this.apply(src, dst, false);
  }
  
  
  public boolean encodeTo(Path src, PrintStream ps) {
    return this.applyTo(src, ps, true);
  }
  
  
  public boolean decodeTo(Path src, PrintStream ps) {
    return this.applyTo(src, ps, false);
  }
  
  
  public boolean encodeFrom(ByteBuffer buf, Path dst) {
    return this.applyFrom(buf, dst, true);
  }
  
  
  public boolean decodeFrom(ByteBuffer buf, Path dst) {
    return this.applyFrom(buf, dst, false);
  }
  
  
  public static void main(String[] args) {
    HexFileCoder fc = new HexFileCoder();
    fc.encode(
        fc.path("file.txt"), 
        fc.path("file.bce"));
    fc.decode(
        fc.path("file.bce"), 
        fc.path("file2.txt"));
  }
  
}
