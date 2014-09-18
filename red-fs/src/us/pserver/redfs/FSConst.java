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

package us.pserver.redfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.CRC32;
import static us.pserver.cdr.FileUtils.path;
import us.pserver.cdr.StringByteConverter;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.streams.StreamUtils.EOF;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/12/2013
 */
public abstract class FSConst {

  public static final int BUFFER_SIZE = 4096;
  
  
  public static long transfer(InputStream is, OutputStream os, IOData dt) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullarg(IOData.class, dt);
    
    StringByteConverter cv = new StringByteConverter();
    byte[] buf = new byte[BUFFER_SIZE];
    int read = -1;
    long total = 0;
    int len = buf.length;
    while(true) {
      if(dt.getLength() > 0)
        len = (int) Math.min(buf.length, 
            (dt.getLength() - total));
      if(len <= 0) break;
      read = is.read(buf, 0, len);
      if(read <= 0) break;
      os.write(buf, 0, read);
      total += read;
      dt.update(read);
      if(cv.reverse(buf, 0, read).contains(EOF))
        break;
    }
    return total;
  }
  
  
  public static long transfer(ProgressInputStream is, OutputStream os) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullarg(IOData.class, is.ioData());
    
    StringByteConverter cv = new StringByteConverter();
    byte[] buf = new byte[BUFFER_SIZE];
    int read = -1;
    long total = 0;
    int len = buf.length;
    while(true) {
      if(is.ioData().getLength() > 0)
        len = (int) Math.min(buf.length, 
            (is.ioData().getLength() - total));
      if(len <= 0) break;
      read = is.read(buf, 0, len);
      if(read <= 0) break;
      os.write(buf, 0, read);
      total += read;
      if(cv.reverse(buf, 0, read).contains(EOF))
        break;
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
        FSConst.BUFFER_SIZE);
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
