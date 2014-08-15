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

package us.pserver.zip.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.listener.ProgressListener;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/12/2013
 */
public abstract class ZipConst {

  public static final int BUFFER_SIZE = 4096;
  
  
  public static long transfer(InputStream is, OutputStream os) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    
    byte[] buf = new byte[BUFFER_SIZE];
    int read = -1;
    long total = 0;
    while(true) {
      read = is.read(buf);
      if(read <= 0) break;
      os.write(buf, 0, read);
      total += read;
    }
    os.flush();
    return total;
  }
  
  
  public static Path excludeEqualsParts(Path base, Path path) {
    if(base == null || path == null)
      return path;
    
    Iterator<Path> ipath = path.iterator();
    Path p = null;
    while(ipath.hasNext()) {
      Path ip = ipath.next();
      boolean contains = false;
      Iterator<Path> ibase = base.iterator();
      while(ibase.hasNext()) {
        Path ib = ibase.next();
        if(ip.equals(ib)) {
          contains = true;
          break;
        }
      }//while
      if(!contains) {
        if(p == null) p = ip;
        else p = p.resolve(ip);
      }
    }
    return p;
  }
  
  
  public static long transfer(InputStream is, OutputStream os, ProgressListener pl) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullarg(ProgressListener.class, pl);
    
    byte[] buf = new byte[BUFFER_SIZE];
    int read = -1;
    long total = 0;
    while(true) {
      read = is.read(buf);
      if(read <= 0) break;
      pl.update(read);
      os.write(buf, 0, read);
      total += read;
    }
    os.flush();
    return total;
  }
  
  
  public static long totalSize(List<Path> ls) throws IOException {
    nullarg(List.class, ls);
    if(ls.isEmpty()) return 0;
    long total = 0;
    for(Path p : ls) {
      total += Files.size(p);
    }
    return total;
  }
  
  
  public static long totalUnzipSize(List<ZipEntry> ls) throws IOException {
    nullarg(List.class, ls);
    if(ls.isEmpty()) return 0;
    long total = 0;
    for(ZipEntry z : ls) {
      total += z.getSize();
    }
    return total;
  }
  
  
  public static long getCRC32(Path pth) throws IOException {
    nullarg(Path.class, pth);
    if(!Files.exists(pth))
      throw new FileNotFoundException("No such file ["+ pth+ "]");
    CRC32 crc = new CRC32();
    FileChannel fc = FileChannel.open(pth, 
        StandardOpenOption.READ);
    ByteBuffer buf = ByteBuffer.allocateDirect(
        ZipConst.BUFFER_SIZE);
    int read = 1;
    while(read > 0) {
      read = fc.read(buf);
      if(read <= 0) break;
      buf.flip();
      byte[] bs = new byte[read];
      buf.get(bs);
      crc.update(bs);
      buf.clear();
    }
    return crc.getValue();
  }
  
}
