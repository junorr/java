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

package us.pserver.cdr.lzma;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;
import us.pserver.cdr.ByteBufferConverter;
import static us.pserver.cdr.Checker.nullarg;
import static us.pserver.cdr.Checker.nullbuffer;
import us.pserver.cdr.FileCoder;
import us.pserver.cdr.FileUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class LzmaFileCoder implements FileCoder {


  @Override
  public boolean apply(Path p1, Path p2, boolean encode) {
    return (encode ? encode(p1, p2) : decode(p1, p2));
  }


  @Override
  public boolean applyFrom(ByteBuffer buf, Path p, boolean encode) {
    nullbuffer(buf);
    nullarg(Path.class, p);
    
    LzmaByteCoder cdr = new LzmaByteCoder();
    ByteBufferConverter conv = new ByteBufferConverter();
    byte[] bs = conv.convert(buf);
    
    try(OutputStream out = FileUtils.outputStream(p)) {
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
    nullarg(Path.class, p);
    nullarg(PrintStream.class, ps);
    
    try(InputStream in = FileUtils.inputStream(p)) {
      if(encode) {
        LzmaOutputStream lzout = new LzmaOutputStream
            .Builder(ps).build();
        FileUtils.transfer(in, lzout);
        lzout.flush();
        lzout.close();
      }
      else {
        LzmaInputStream lzin = new LzmaInputStream(in, new Decoder());
        FileUtils.transfer(lzin, ps);
        lzin.close();
      }
      return true;
    } 
    catch(IOException e) {
      return false;
    }
  }
  
  
  @Override
  public boolean encode(Path p1, Path p2) {
    nullarg(Path.class, p1);
    nullarg(Path.class, p2);
    try(InputStream in = FileUtils.inputStream(p1);
        OutputStream out = FileUtils.outputStream(p2);
        LzmaOutputStream lzout = new LzmaOutputStream
            .Builder(out).build()) {
      
      FileUtils.transfer(in, lzout);
      lzout.flush();
      return true;
    } catch(IOException e) {
      return false;
    }
  }


  @Override
  public boolean decode(Path p1, Path p2) {
    nullarg(Path.class, p1);
    nullarg(Path.class, p2);
    try(InputStream in = FileUtils.inputStream(p1);
        OutputStream out = FileUtils.outputStream(p2);
        LzmaInputStream lzin = new LzmaInputStream(
            in, new Decoder())) {
      
      FileUtils.transfer(lzin, out);
      out.flush();
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    
    int read = -1;
    int total = 0;
    while((read = in.read()) != -1) {
      total++;
      out.write(read);
    }
    out.flush();
    return total;
  }

  
  public static void main(String[] args) {
    LzmaFileCoder cdr = new LzmaFileCoder();
    cdr.encode(
        FileUtils.path("d:/base.csv"), 
        FileUtils.path("d:/base.lzma"));
    cdr.decode(
        FileUtils.path("d:/base.lzma"), 
        FileUtils.path("d:/base2.csv"));
  }
  
}
