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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;
import us.pserver.cdr.Coder;
import us.pserver.cdr.FileUtils;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 * Compactador/Descompactador LZMA para byte 
 * array <code>(byte[])</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 18/06/2014
 */
public class LzmaByteCoder implements Coder<byte[]> {
  

  @Override
  public byte[] apply(byte[] buf, boolean encode) {
    return (encode ? encode(buf) : decode(buf));
  }


  @Override
  public byte[] encode(byte[] buf) {
    Sane.of(buf).check(Checkup.isNotEmptyArray());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      LzmaOutputStream lzout = LzmaStreamFactory.createLzmaOutput(bos);
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
    Sane.of(buf).check(Checkup.isNotEmptyArray());
    try(LzmaInputStream lzin = LzmaStreamFactory.createLzmaInput(buf)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      FileUtils.transfer(lzin, bos);
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  /**
   * Aplica (des)compactação LZMA na porção do byte 
   * array informado.
   * @param buf Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @param encode <code>true</code> para compactar em 
   * LZMA, <code>false</code> para descompactar.
   * @return Byte array contendo os dados (de)codificados.
   */
  public byte[] apply(byte[] buf, int offset, int length, boolean encode) {
    return (encode 
        ? encode(buf, offset, length) 
        : decode(buf, offset, length));
  }


  /**
   * Compacta parte do byte array informado no formato LZMA.
   * @param buf Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados codificados.
   */
  public byte[] encode(byte[] buf, int offset, int length) {
    Sane.of(buf).check(Checkup.isNotNull());
    Sane.of(offset).check(Checkup.isBetween(0, buf.length -1));
    Sane.of(length).check(Checkup.isBetween(1, buf.length-offset));
    buf = Arrays.copyOfRange(buf, offset, offset + length);
    return encode(buf);
  }


  /**
   * Descompacta parte do byte array informado no formato LZMA.
   * @param buf Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados decodificados.
   */
  public byte[] decode(byte[] buf, int offset, int length) {
    Sane.of(buf).check(Checkup.isNotNull());
    Sane.of(offset).check(Checkup.isBetween(0, buf.length -1));
    Sane.of(length).check(Checkup.isBetween(1, buf.length-offset));
    buf = Arrays.copyOfRange(buf, offset, offset + length);
    return decode(buf);
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
