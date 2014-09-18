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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import us.pserver.cdr.Coder;
import us.pserver.cdr.FileUtils;
import static us.pserver.chk.Checker.nullarray;

/**
 * Compactador/Descompactador GZIP para byte array 
 * <code>(byte[])</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 18/06/2014
 */
public class GZipByteCoder implements Coder<byte[]> {
  
  
  private GZIPInputStream createGZipInput(byte[] buf) {
    nullarray(buf);
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
    nullarray(buf);
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
    nullarray(buf);
    try(GZIPInputStream gin = createGZipInput(buf)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      FileUtils.transfer(gin, bos);
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  /**
   * Aplica (des)compactação GZIP na porção do byte 
   * array informado.
   * @param buf Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @param encode <code>true</code> para compactar em 
   * GZIP, <code>false</code> para descompactar.
   * @return Byte array contendo os dados (de)codificados.
   */
  public byte[] apply(byte[] buf, int offset, int length, boolean encode) {
    return (encode 
        ? encode(buf, offset, length) 
        : decode(buf, offset, length));
  }


  /**
   * Compacta em GZIP parte do byte array informado.
   * @param buf Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados codificados.
   */
  public byte[] encode(byte[] buf, int offset, int length) {
    nullarray(buf);
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


  /**
   * Descompacta de GZIP parte do byte array informado.
   * @param buf Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados decodificados.
   */
  public byte[] decode(byte[] buf, int offset, int length) {
    nullarray(buf);
    buf = Arrays.copyOfRange(buf, offset, offset + length);
    try(GZIPInputStream gin = createGZipInput(buf)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      FileUtils.transfer(gin, bos);
      return bos.toByteArray();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
}
