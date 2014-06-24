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

package us.pserver.cdr.lz4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import us.pserver.cdr.Coder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class LZ4ByteCoder implements Coder<byte[]> {
  
  
  private void checkBuffer(byte[] buf) {
    if(buf == null || buf.length < 1)
      throw new IllegalArgumentException(
          "Invalid byte array [buf="
          + (buf == null ? buf : buf.length)+ "]");
  }
  
  
  private GZIPInputStream createLZ4Input(byte[] buf) {
    checkBuffer(buf);
    try {
      return new GZIPInputStream(new ByteArrayInputStream(buf));
    } catch(IOException e) {
      throw new IllegalStateException(e);
    }
  }


  @Override
  public byte[] apply(byte[] buf, boolean encode) {
    return (encode ? encode(buf) : decode(buf));
  }


  @Override
  public byte[] encode(byte[] buf) {
    checkBuffer(buf);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try(GZIPOutputStream gout = new GZIPOutputStream(bos)) {
      gout.write(buf);
      gout.finish();
      gout.flush();
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public byte[] decode(byte[] buf) {
    checkBuffer(buf);
    try(GZIPInputStream gin = createLZ4Input(buf)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      transfer(gin, bos);
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public byte[] apply(byte[] buf, int offset, int length, boolean encode) {
    return (encode 
        ? encode(buf, offset, length) 
        : decode(buf, offset, length));
  }


  public byte[] encode(byte[] buf, int offset, int length) {
    checkBuffer(buf);
    buf = Arrays.copyOfRange(buf, offset, offset + length);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try(GZIPOutputStream gout = new GZIPOutputStream(bos)) {
      gout.write(buf);
      gout.flush();
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }


  public byte[] decode(byte[] buf, int offset, int length) {
    checkBuffer(buf);
    buf = Arrays.copyOfRange(buf, offset, offset + length);
    try(GZIPInputStream gin = createLZ4Input(buf)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      transfer(gin, bos);
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    if(in == null)
      throw new IllegalArgumentException(
          "Invalid InputStream [in="+ in+ "]");
    if(out == null)
      throw new IllegalArgumentException(
          "Invalid OutputStream [out="+ out+ "]");
    
    int read = -1;
    int total = 0;
    while((read = in.read()) != -1) {
      total++;
      out.write(read);
    }
    out.flush();
    return total;
  }
  
}
