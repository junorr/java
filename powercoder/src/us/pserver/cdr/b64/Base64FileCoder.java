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

package us.pserver.cdr.b64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileCoder;
import us.pserver.cdr.FileUtils;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullbuffer;

/**
 * Codificador/Decodificador de arquivos no formato Base64.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class Base64FileCoder implements FileCoder {

  private ByteBufferConverter conv;

  
  public Base64FileCoder() {
    conv = new ByteBufferConverter();
  }
  
  
  @Override
  public boolean apply(Path src, Path dst, boolean encode) {
    return (encode ? encode(src, dst) 
        : decode(src, dst));
  }
  
  
  @Override
  public boolean applyTo(Path src, PrintStream ps, boolean encode) {
    return (encode ? encodeTo(src, ps) 
        : decodeTo(src, ps));
  }
  
  
  @Override
  public boolean applyFrom(ByteBuffer buf, Path path, boolean encode) {
    return (encode 
        ? encodeFrom(buf, path) 
        : decodeFrom(buf, path));
  }
  
  
  @Override
  public boolean encode(Path src, Path dst) {
    nullarg(Path.class, src);
    nullarg(Path.class, dst);
    
    try(InputStream in = FileUtils.inputStream(src);
        OutputStream out = new Base64OutputStream(
            FileUtils.outputStream(dst))) {
      FileUtils.transfer(in, out);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  @Override
  public boolean decode(Path src, Path dst) {
    nullarg(Path.class, src);
    nullarg(Path.class, dst);
    
    try(InputStream in = new Base64InputStream(
            FileUtils.inputStream(src));
        OutputStream out = FileUtils.outputStream(dst)) {
      FileUtils.transfer(in, out);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean encodeTo(Path src, PrintStream ps) {
    nullarg(Path.class, src);
    nullarg(PrintStream.class, ps);
    
    try(InputStream in = FileUtils.inputStream(src);
        OutputStream out = new Base64OutputStream(ps)) {
      FileUtils.transfer(in, out);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean decodeTo(Path src, PrintStream ps) {
    nullarg(Path.class, src);
    nullarg(PrintStream.class, ps);
    
    try(InputStream in = new Base64InputStream(
        FileUtils.inputStream(src))) {
      FileUtils.transfer(in, ps);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean encodeFrom(ByteBuffer buf, Path dst) {
    nullbuffer(buf);
    nullarg(Path.class, dst);
    
    try(Base64OutputStream out = new Base64OutputStream(
        FileUtils.outputStream(dst));) {
      out.write(conv.convert(buf));
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean decodeFrom(ByteBuffer buf, Path dst) {
    nullbuffer(buf);
    nullarg(Path.class, dst);
    
    ByteArrayInputStream bin = 
        new ByteArrayInputStream(conv.convert(buf));
    
    try(OutputStream out = FileUtils.outputStream(dst);
        Base64InputStream in = new Base64InputStream(bin)) {
      FileUtils.transfer(in, out);
      out.flush();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  public static void main(String[] args) {
    Base64FileCoder fc = new Base64FileCoder();
    fc.encode(
        FileUtils.path("d:/pic.jpg"), 
        FileUtils.path("d:/pic.bce"));
    fc.decode(
        FileUtils.path("d:/pic.bce"), 
        FileUtils.path("d:/pic2.jpg"));
  }
  
}
