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

package us.pserver.streams;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import static us.pserver.chk.Checker.nullarg;

/**
 * Handy shortcut class for IO streams.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/08/2014
 */
public abstract class IO {
  
  public static int BUFFER_SIZE = 4096;
  
  
  public static void buffer_size(int size) {
    if(size > 0)
      BUFFER_SIZE = size;
  }

  
  /**
   * Create a <code>Path</code> from <code>String</code>.
   * @param p String path
   * @return <code>Path</code>.
   */
  public static Path p(String p) {
    return Paths.get(p);
  }
  
  
  /**
   * Create an <code>OutputStream</code> class from 
   * <code>Path</code> whit options
   * <code>StandardOpenOption.CREATE</code> and
   * <code>StandardOpenOption.WRITE</code>.
   * @param p File <code>Path</code>.
   * @return <code>OutputStream from Path</code>.
   * @throws IOException In case of IO error creating <code>OutputStream</code>.
   */
  public static OutputStream os(Path p) throws IOException {
    return Files.newOutputStream(p, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.WRITE);
  }
  
  
  /**
   * Create an <code>InputStream</code> class from 
   * <code>Path</code> whit options
   * <code>StandardOpenOption.READ</code>.
   * @param p File <code>Path</code>.
   * @return <code>InputStream from Path</code>.
   * @throws IOException In case of IO error creating <code>OutputStream</code>.
   */
  public static InputStream is(Path p) throws IOException {
    return Files.newInputStream(p, 
        StandardOpenOption.READ);
  }
  
  
  /**
   * Unchecked (Exception free) version of 
   * {@link us.pserver.streams.IO#os(java.nio.file.Path)}
   * @param p File <code>Path</code>.
   * @return <code>OutputStream from Path or null in caso of error</code>.
   */
  public static OutputStream uos(Path p) {
    try {
      return os(p);
    } catch(IOException e) {
      return null;
    }
  }
  
  
  /**
   * Unchecked (Exception free) version of 
   * {@link us.pserver.streams.IO#is(java.nio.file.Path)}
   * @param p File <code>Path</code>.
   * @return <code>InputStream from Path or null in caso of error</code>.
   */
  public static InputStream uis(Path p) {
    try {
      return is(p);
    } catch(IOException e) {
      return null;
    }
  }
  
  
  public static File f(String fileName) {
    if(fileName == null || fileName.trim().isEmpty())
      return null;
    return new File(fileName);
  }
  
  
  public static FileOutputStream fo(String fileName) throws IOException {
    if(fileName == null || fileName.trim().isEmpty())
      throw new IOException("Invalid file name ["+ fileName+ "]");
    return new FileOutputStream(fileName);
  }
  
  
  public static FileOutputStream ufo(String fileName) {
    if(fileName == null || fileName.trim().isEmpty())
      throw new IllegalArgumentException("Invalid file name ["+ fileName+ "]");
    try {
      return new FileOutputStream(fileName);
    } catch(FileNotFoundException e) {
      return null;
    }
  }
  
  
  public static FileOutputStream fo(File file) throws IOException {
    if(file == null) return null;
    return new FileOutputStream(file);
  }
  
  
  public static FileOutputStream ufo(File file) {
    if(file == null) return null;
    try {
      return new FileOutputStream(file);
    } catch(FileNotFoundException e) {
      return null;
    }
  }
  
  
  public static FileOutputStream af(String fileName) throws IOException {
    if(fileName == null || fileName.trim().isEmpty())
      throw new IOException("Invalid file name ["+ fileName+ "]");
    return new FileOutputStream(fileName, true);
  }
  
  
  public static FileOutputStream uaf(String fileName) {
    if(fileName == null || fileName.trim().isEmpty())
      throw new IllegalArgumentException("Invalid file name ["+ fileName+ "]");
    try {
      return new FileOutputStream(fileName, true);
    } catch(FileNotFoundException e) {
      return null;
    }
  }
  
  
  public static FileOutputStream af(File file) throws IOException {
    if(file == null) return null;
    return new FileOutputStream(file, true);
  }
  
  
  public static FileOutputStream uaf(File file) {
    if(file == null) return null;
    try {
      return new FileOutputStream(file, true);
    } catch(FileNotFoundException e) {
      return null;
    }
  }
  
  
  /**
   * Create a Buffered version of the <code>OutputStream</code> informed.
   * @param os <code>OutputStream</code> to buffer.
   * @return <code>BufferedOutputStream</code>.
   */
  public static BufferedOutputStream bf(OutputStream os) {
    nullarg(OutputStream.class, os);
    return new BufferedOutputStream(os);
  }
  
  
  /**
   * Create a Buffered version of the <code>InputStream</code> informed.
   * @param is <code>InputStream</code> to buffer.
   * @return <code>BufferedInputStream</code>.
   */
  public static BufferedInputStream bf(InputStream is) {
    nullarg(InputStream.class, is);
    return new BufferedInputStream(is);
  }
  
  
  /**
   * Return {@link java.io.InputStream#available() }
   * @param is <code>InputStream</code>.
   * @return {@link java.io.InputStream#available() }
   * @throws IOException {@link java.io.InputStream#available() }
   * @see java.io.InputStream#available() 
   */
  public static int sz(InputStream is) throws IOException {
    nullarg(InputStream.class, is);
    return is.available();
  }
  
  
  /**
   * Unchecked (Exception free) version of
   * {@link us.pserver.streams.IO#sz(java.io.InputStream) }
   * @param is <code>InputStream</code>.
   * @return {@link java.io.InputStream#available() }
   * @see us.pserver.streams.IO#sz(java.io.InputStream) 
   */
  public static int usz(InputStream is) {
    try { return sz(is); }
    catch(IOException e) {
      return -1;
    }
  }
  
  
  /**
   * Close the Streams whitout throwing any IOException.
   * <code>OutputStream</code> is closed first.
   * @param is <code>InputStream</code> to close.
   * @param os <code>OutputStream</code> to close.
   */
  public static void cl(InputStream is, OutputStream os) {
    try { os.flush(); os.close(); } catch(Exception e) {}
    try { is.close(); } catch(Exception e) {}
  }
  
  
  /**
   * Transfer the content of <code>InputStream</code> to
   * <code>OutputStream</code> whit (default) <code>1024</code> buffer size.
   * @param is source <code>InputStream</code>.
   * @param os target <code>OutputStream</code>.
   * @return Bytes transfered.
   * @throws IOException In case of error transfering.
   */
  public static long tr(InputStream is, OutputStream os) throws IOException {
    if(is == null || os == null) return -1;
    byte[] buf = new byte[BUFFER_SIZE];
    int read = -1;
    long total = 0;
    while(true) {
      read = is.read(buf);
      if(read < 1) break;
      total += read;
      os.write(buf, 0, read);
      os.flush();
    }
    return total;
  }
  
  
  /**
   * Unchecked (Exception free) version of 
   * {@link us.pserver.streams.IO#tr(java.io.InputStream, java.io.OutputStream)}
   * @param is source <code>InputStream</code>.
   * @param os target <code>OutputStream</code>.
   * @return Bytes transfered or <code>-1</code> in case of error.
   */
  public static long utr(InputStream is, OutputStream os) {
    try {
      return tr(is, os);
    } catch(IOException e) {
      return -1;
    }
  }
  
  
  /**
   * Transfer the content between streams and close them on finish,
   * using (default) <code>1024</code> buffer size.
   * @param is source <code>InputStream</code>.
   * @param os target <code>OutputStream</code>.
   * @return Bytes transfered.
   * @throws IOException In case of error transfering.
   */
  public static long tc(InputStream is, OutputStream os) throws IOException {
    if(is == null || os == null) return -1;
    long l = tr(is, os);
    cl(is, os);
    return l;
  }
  
  
  /**
   * Unchecked (Exception free) version of 
   * {@link us.pserver.streams.IO#tc(java.io.InputStream, java.io.OutputStream)}
   * @param is source <code>InputStream</code>.
   * @param os target <code>OutputStream</code>.
   * @return Bytes transfered or <code>-1</code> in case of error.
   */
  public static long utc(InputStream is, OutputStream os) {
    try {
      return tc(is, os);
    } catch(IOException e) {
      return -1;
    }
  }
  
}
