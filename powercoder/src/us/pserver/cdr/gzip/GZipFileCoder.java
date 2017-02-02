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
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileCoder;
import us.pserver.cdr.FileUtils;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 * Compactador/Descompactador de arquivos no formato GZIP.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 18/06/2014
 */
public class GZipFileCoder implements FileCoder {


  @Override
  public boolean apply(Path p1, Path p2, boolean encode) {
    return (encode ? encode(p1, p2) : decode(p1, p2));
  }


  @Override
  public boolean applyFrom(ByteBuffer buf, Path p, boolean encode) {
    Sane.of(buf).check(Checkup.isNotNull());
    Sane.of(buf.remaining())
        .with("No remaining bytes to read")
        .check(Checkup.isGreaterThan(0));
    Sane.of(p).check(Checkup.isNotNull());
    
    GZipByteCoder cdr = new GZipByteCoder();
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
    Sane.of(p).check(Checkup.isNotNull());
    Sane.of(ps).check(Checkup.isNotNull());
    
    try(InputStream in = FileUtils.inputStream(p)) {
      if(encode) {
        GZIPOutputStream gout = new GZIPOutputStream(ps);
        FileUtils.transfer(in, gout);
        gout.finish();
        gout.flush();
        gout.close();
      }
      else {
        GZIPInputStream gin = new GZIPInputStream(in);
        FileUtils.transfer(gin, ps);
        gin.close();
      }
      return true;
    } 
    catch(IOException e) {
      return false;
    }
  }
  
  
  @Override
  public boolean encode(Path p1, Path p2) {
    Sane.of(p1).check(Checkup.isNotNull());
    Sane.of(p2).check(Checkup.isNotNull());
    try(InputStream in = FileUtils.inputStream(p1);
        OutputStream out = FileUtils.outputStream(p2);
        GZIPOutputStream gout = new GZIPOutputStream(out)) {
      
      FileUtils.transfer(in, gout);
      gout.finish();
      gout.flush();
      return true;
    } catch(IOException e) {
      return false;
    }
  }


  @Override
  public boolean decode(Path p1, Path p2) {
    Sane.of(p1).check(Checkup.isNotNull());
    Sane.of(p2).check(Checkup.isNotNull());
    try(InputStream in = FileUtils.inputStream(p1);
        OutputStream out = FileUtils.outputStream(p2);
        GZIPInputStream gin = new GZIPInputStream(in)) {
      
      FileUtils.transfer(gin, out);
      out.flush();
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  public static void main(String[] args) {
    GZipFileCoder cdr = new GZipFileCoder();
    cdr.encode(
        FileUtils.path("d:/base.csv"), 
        FileUtils.path("d:/base.csv.gz"));
    cdr.decode(
        FileUtils.path("d:/base.csv.gz"), 
        FileUtils.path("d:/base.gz.csv"));
  }
  
}
