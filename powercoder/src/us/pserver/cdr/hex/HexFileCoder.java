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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileCoder;
import us.pserver.cdr.FileUtils;
import us.pserver.valid.Valid;

/**
 * Codificador/Decodificador de arquivos no 
 * formato hexadecimal.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class HexFileCoder implements FileCoder {

  private ByteBufferConverter conv;

  
  /**
   * Construtor padrão sem argumentos.
   */
  public HexFileCoder() {
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
    Valid.off(src).forNull().fail(Path.class);
    Valid.off(dst).forNull().fail(Path.class);
    try(InputStream in = FileUtils.inputStream(src);
        OutputStream out = new HexOutputStream(
            FileUtils.outputStream(dst))) {
      FileUtils.transfer(in, out);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  @Override
  public boolean decode(Path src, Path dst) {
    Valid.off(src).forNull().fail(Path.class);
    Valid.off(dst).forNull().fail(Path.class);
    try(InputStream in = new HexInputStream(
            FileUtils.inputStream(src));
        OutputStream out = FileUtils.outputStream(dst)) {
      FileUtils.transfer(in, out);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean encodeTo(Path src, PrintStream ps) {
    Valid.off(src).forNull().fail(Path.class);
    Valid.off(ps).forNull().fail(PrintStream.class);
    try(InputStream in = FileUtils.inputStream(src);
        OutputStream out = new HexOutputStream(ps)) {
      FileUtils.transfer(in, out);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean decodeTo(Path src, PrintStream ps) {
    Valid.off(src).forNull().fail(Path.class);
    Valid.off(ps).forNull().fail(PrintStream.class);
    try(InputStream in = new HexInputStream(
        FileUtils.inputStream(src))) {
      FileUtils.transfer(in, ps);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean encodeFrom(ByteBuffer buf, Path dst) {
    Valid.off(buf).forNull().fail(ByteBuffer.class);
    Valid.off(buf.remaining()).forLesserThan(1)
        .fail("No remaining bytes to read");
    Valid.off(dst).forNull().fail(Path.class);
    
    try(HexOutputStream out = new HexOutputStream(
        FileUtils.outputStream(dst));) {
      out.write(conv.convert(buf));
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  private boolean decodeFrom(ByteBuffer buf, Path dst) {
    Valid.off(buf).forNull().fail(ByteBuffer.class);
    Valid.off(buf.remaining()).forLesserThan(1)
        .fail("No remaining bytes to read");
    Valid.off(dst).forNull().fail(Path.class);
    
    ByteArrayInputStream bin = 
        new ByteArrayInputStream(conv.convert(buf));
    
    try(OutputStream out = FileUtils.outputStream(dst);
        HexInputStream in = new HexInputStream(bin)) {
      FileUtils.transfer(in, out);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
  
  
  public static void main(String[] args) {
    HexFileCoder fc = new HexFileCoder();
    fc.encode(
        FileUtils.path("d:/pic.jpg"), 
        FileUtils.path("d:/pic.hex"));
    fc.decode(
        FileUtils.path("d:/pic.hex"), 
        FileUtils.path("d:/pic2.jpg"));
  }
  
}
