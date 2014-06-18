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

package us.pserver.cdr.gzip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileCoder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class GZipFileCoder implements FileCoder {


  @Override
  public boolean apply(Path p1, Path p2, boolean encode) {
    return (encode ? encode(p1, p2) : decode(p1, p2));
  }


  @Override
  public boolean applyFrom(ByteBuffer buf, Path p, boolean encode) {
    if(buf == null || buf.remaining() == 0)
      throw new IllegalArgumentException(
          "Invalid ByteBuffer [buf="
          + (buf == null ? buf : buf.remaining())+ "]");
    checkPath(p);
    
    GZipByteCoder cdr = new GZipByteCoder();
    ByteBufferConverter conv = new ByteBufferConverter();
    byte[] bs = conv.convert(buf);
    
    try(OutputStream out = Files.newOutputStream(p, 
        StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
      
      out.write(cdr.apply(bs, encode));
      out.flush();
      return true;
    } 
    catch(IOException e) {
      return false;
    }
  }


  @Override
  public boolean applyTo(Path p, PrintStream ps, boolean encode) {
    checkPath(p);
    if(ps == null)
      throw new IllegalArgumentException(
          "Invalid PrintStream [ps="+ ps+ "]");
    
    try(InputStream in = Files.newInputStream(p, 
        StandardOpenOption.READ)) {
      
      if(encode) {
        GZIPOutputStream gout = new GZIPOutputStream(ps);
        GZipByteCoder.transfer(in, gout);
        gout.finish();
        gout.flush();
        gout.close();
      }
      else {
        GZIPInputStream gin = new GZIPInputStream(in);
        GZipByteCoder.transfer(gin, ps);
        gin.close();
      }
      return true;
    } 
    catch(IOException e) {
      return false;
    }
  }
  
  
  private void checkPath(Path p) {
    if(p == null)
      throw new IllegalArgumentException(
          "Invalid Path [p="+ p+ "]");
  }


  @Override
  public boolean encode(Path p1, Path p2) {
    checkPath(p1);
    checkPath(p2);
    try(InputStream in = Files.newInputStream(
        p1, StandardOpenOption.READ);
        OutputStream out = Files.newOutputStream(
            p2, StandardOpenOption.WRITE, 
            StandardOpenOption.CREATE);
        GZIPOutputStream gout = new GZIPOutputStream(out)) {
      
      GZipByteCoder.transfer(in, gout);
      gout.finish();
      gout.flush();
      return true;
    } catch(IOException e) {
      return false;
    }
  }


  @Override
  public boolean decode(Path p1, Path p2) {
    checkPath(p1);
    checkPath(p2);
    try(InputStream in = Files.newInputStream(
        p1, StandardOpenOption.READ);
        OutputStream out = Files.newOutputStream(
            p2, StandardOpenOption.WRITE, 
            StandardOpenOption.CREATE);
        GZIPInputStream gin = new GZIPInputStream(in)) {
      
      GZipByteCoder.transfer(gin, out);
      out.flush();
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  public static Path path(String str) {
    if(str == null || str.isEmpty())
      throw new IllegalArgumentException(
          "Invalid String [str="+ str+ "]");
    return Paths.get(str);
  }

  
  public static void main(String[] args) {
    GZipFileCoder cdr = new GZipFileCoder();
    cdr.encode(path("d:/picture.jpg"), path("d:/picture.gzip"));
    cdr.decode(path("d:/picture.gzip"), path("d:/picture.jpg"));
  }
  
}
