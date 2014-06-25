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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;
import static us.pserver.cdr.Checker.nullarg;
import static us.pserver.cdr.Checker.nullarray;
import us.pserver.cdr.Coder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class LzmaByteCoder implements Coder<byte[]> {
  
  
  private LzmaInputStream createLzmaInput(byte[] buf) {
    nullarray(buf);
    try {
      return new LzmaInputStream(new ByteArrayInputStream(buf), new Decoder());
    } catch(IOException e) {
      throw new IllegalStateException(e);
    }
  }
  
  
  private LzmaOutputStream createLzmaOutput(OutputStream out) {
    nullarg(OutputStream.class, out);
    try {
      return new LzmaOutputStream.Builder(out).build();
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
    nullarray(buf);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      LzmaOutputStream lzout = createLzmaOutput(bos);
      lzout.write(buf);
      lzout.flush();
      lzout.close();
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public byte[] decode(byte[] buf) {
    nullarray(buf);
    try(LzmaInputStream lzin = createLzmaInput(buf)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      transfer(lzin, bos);
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
    nullarray(buf);
    buf = Arrays.copyOfRange(buf, offset, offset + length);
    return encode(buf);
  }


  public byte[] decode(byte[] buf, int offset, int length) {
    nullarray(buf);
    buf = Arrays.copyOfRange(buf, offset, offset + length);
    return decode(buf);
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
    LzmaByteCoder lzc = new LzmaByteCoder();
    String str = "Implements an input stream filter for compressing";
    System.out.println("* str='"+ str+ "'");
    System.out.println("* str.length="+ str.length());
    byte[] bs = str.getBytes();
    System.out.println("* str.bytes="+ bs.length);
    
    System.out.println("* compressing...");
    bs = lzc.encode(bs);
    str = new String(bs);
    System.out.println("* str.bytes="+ bs.length);
    System.out.println("* str.length="+ str.length());
    System.out.println("* str='"+ str+ "'");
    
    System.out.println("* decompressing...");
    bs = lzc.decode(bs);
    str = new String(bs);
    System.out.println("* str.bytes="+ bs.length);
    System.out.println("* str.length="+ str.length());
    System.out.println("* str='"+ str+ "'");
  }
  
}
