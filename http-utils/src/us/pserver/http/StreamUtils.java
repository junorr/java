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

package us.pserver.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.hex.HexByteCoder;


/**
 * Classe com métodos utilitários para manipulação 
 * de streams.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public abstract class StreamUtils {
  
  /**
   * <code>
   *  BYTES_CRLF = {10, 13}
   * </code><br>
   * Bytes de caracteres de quebra de linha.
   */
  public static final byte[] BYTES_CRLF = {10, 13};
  
  /**
   * <code>
   *  BYTES_EOF = {69, 79, 70}
   * </code><br>
   * Bytes de caracteres 'EOF' de fim de transmissão.
   */
  public static final byte[] BYTES_EOF = {69, 79, 70};
  
  /**
   * <code>
   *  EOF = "EOF"
   * </code><br>
   * Representação <code>String</code> de fim de arquivo.
   */
  public static final String EOF = "EOF";
  
  /**
   * <code>
   *  UTF8 = "UTF-8"
   * </code><br>
   * Codificação de caracteres.
   */
  public static final String UTF8 = "UTF-8";
  
  /**
   * <code>
   *  BUFFER_SIZE = 1024
   * </code><br>
   * Tamanho do buffer em bytes.
   */
  public static int BUFFER_SIZE = 1024;
  
  
  /**
   * Transfere o conteúdo entre dois streams até final
   * ou até encontrar os bytes relativos ao fim de transmissão 
   * <code>EOF</code>.
   * @param in <code>InputStream</code>
   * @param out <code>OutputStream</code>
   * @return Número total de bytes transferidos.
   * @throws IOException caso ocorra erro na transferência.
   */
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    if(in == null) 
      throw new IOException("Invalid InputStream [in="+ in+ "]");
    if(out == null) 
      throw new IOException("Invalid OutputStream [out="+ out+ "]");
    
    long total = 0;
    int read = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    
    while((read = in.read(buf)) > 0) {
      total += read;
      out.write(buf, 0, read);
      int len = (read < 30 ? read : 30);
      String str = new String(buf, read -len, len);
      if(read < buf.length && str.contains(EOF))
        break;
    }
    return total;
  }
  
  
  /**
   * Transfere o conteúdo do stream de entrada para o 
   * stream de saída, codificando os dados em Hexadecimal.
   * @param in stream de entrada a ser codificado e transferido.
   * @param out stream de saída.
   * @return quantidade de bytes transmitidos.
   * @throws IOException caso ocorra erro na transferência/codificação 
   * dos dados.
   */
  public static long transferHexEncoding(InputStream in, OutputStream out) throws IOException {
    return transferHexCoder(in, out, true);
  }
  
  
  /**
   * Transfere o conteúdo do stream de entrada para o 
   * stream de saída, decodificando os dados do formato hexadecimal.
   * @param in stream de entrada a ser decodificado e transferido.
   * @param out stream de saída.
   * @return quantidade de bytes transmitidos.
   * @throws IOException caso ocorra erro na transferência/decodificação 
   * dos dados.
   */
  public static long transferHexDecoding(InputStream in, OutputStream out) throws IOException {
    return transferHexCoder(in, out, false);
  }
  
  
  /**
   * Transfere o conteúdo do stream de entrada para o 
   * stream de saída, codificando/decodificando 
   * <code>(encode boolean)</code> os dados em Hexadecimal.
   * @param in stream de entrada a ser codificado/decodificado e transferido.
   * @param out stream de saída.
   * @param encode <code>true</code> para codificar os dados em hexadecimal,
   * <code>false</code> para decodificar.
   * @return quantidade de bytes transmitidos.
   * @throws IOException caso ocorra erro na 
   * transferência/codificação/decodificação dos dados.
   */
  public static long transferHexCoder(InputStream in, OutputStream out, boolean encode) throws IOException {
    if(in == null) 
      throw new IOException("Invalid InputStream [in="+ in+ "]");
    if(out == null) 
      throw new IOException("Invalid OutputStream [out="+ out+ "]");
    
    HexByteCoder cdr = new HexByteCoder();
    int read = 0;
    long total = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    
    while((read = in.read(buf)) > 0) {
      total += read;
      out.write(cdr.apply(buf, 0, read, encode));
      int len = (read < 30 ? read : 30);
      String str = new String(buf, read -len, len);
      if(read < buf.length && str.contains(EOF))
        break;
    }
    return total;
  }
  
  
  /**
   * Transfere o conteúdo entre dois streams até que seja
   * encontrada a <code>String</code> fornecida no conteúdo da 
   * transmissão, ou até o final da transmissão.
   * @param in <code>InputStream</code>
   * @param out <code>OutputStream</code>
   * @param until <code>String</code> de condição de 
   * parada de transferência de conteúdo.
   * @return Número total de bytes transferidos.
   * @throws IOException caso ocorra erro na transferência.
   */
  public static long transferUntil(InputStream in, OutputStream out, String until) throws IOException {
    if(in == null) 
      throw new IOException("Invalid InputStream [in="+ in+ "]");
    if(out == null) 
      throw new IOException("Invalid OutputStream [out="+ out+ "]");
    if(until == null || until.isEmpty()) 
      throw new IOException("Invalid string token [until="+ until+ "]");
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    long total = 0;
    int read = -1;
    while((read = in.read()) != -1) {
      total++;
      lbuf.put(read);
      if(lbuf.size() == until.length()) {
        if(until.equals(lbuf.toUTF8()))
          break;
        else if(total % until.length() == 0)
          lbuf.writeTo(out);
      }
    }
    return total;
  }
  
  
  public static long transferUntil(InputStream in, OutputStream out, String until, String orfalse) throws IOException {
    if(in == null) 
      throw new IOException("Invalid InputStream [in="+ in+ "]");
    if(out == null) 
      throw new IOException("Invalid OutputStream [out="+ out+ "]");
    if(until == null || until.isEmpty()) 
      throw new IOException("Invalid string token [until="+ until+ "]");
    if(orfalse == null || orfalse.isEmpty()) 
      throw new IOException("Invalid string token [orfalse="+ orfalse+ "]");
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    long total = 0;
    int read = -1;
    while((read = in.read()) != -1) {
      total++;
      lbuf.put(read);
      if(lbuf.size() == until.length()) {
        if(until.equals(lbuf.toUTF8()))
          break;
        else if(total % until.length() == 0)
          lbuf.writeTo(out);
      }
    }
    return total;
  }
  
  
  public static long transferBetween(InputStream in, OutputStream out, 
      String start, String end) throws IOException {
    if(in == null) 
      throw new IOException("Invalid InputStream [in="+ in+ "]");
    if(out == null) 
      throw new IOException("Invalid OutputStream [out="+ out+ "]");
    if(start == null || start.isEmpty()) 
      throw new IOException("Invalid start token [start="+ start+ "]");
    if(end == null || end.isEmpty()) 
      throw new IOException("Invalid end token [end="+ end+ "]");
    
    readUntil(in, start);
    return transferUntil(in, out, end);
  }
  
  
  /**
   * Escreve uma <code>String</code> codificada em <code>UTF-8</code>
   * no stream de saída.
   * @param str <code>String</code> a ser escrita.
   * @param out <code>OuputStream</code>.
   * @throws IOException caso ocorra erro na escrita.
   */
  public static void write(String str, OutputStream out) throws IOException {
    if(out == null) 
      throw new IOException("Invalid OutputStream [out="+ out+ "]");
    if(str == null || str.isEmpty()) 
      throw new IOException("Invalid string token [str="+ str+ "]");
    
    out.write(bytes(str));
    out.flush();
  }
  
  
  /**
   * Retorna os bytes relativos ao conteúdo
   * da <code>String</code> codificada em <code>UTF-8</code>.
   * @param str <code>String</code>.
   * @return <code>byte[]</code>;
   */
  public static byte[] bytes(String str) {
    if(str == null || str.isEmpty())
      return new byte[0];
    try {
      return str.getBytes(UTF8);
    } 
    catch(UnsupportedEncodingException e) {
      return new byte[0];
    }
  }
  
  
  /**
   * Lê e retorna o conteúdo do stream que esteja
   * delimitado entre duas <code>Strings</code>, 
   * <code>start</code> e <code>end</code>, descartando o resto.
   * @param in <code>InputStream</code>
   * @param start <code>String</code> demarcando início do conteúdo.
   * @param end <code>String</code> demarcando o final do conteúdo.
   * @return O conteúdo <code>String</code> lido.
   * @throws IOException caso ocorra erro na leitura.
   */
  public static String readBetween(InputStream in, String start, String end) throws IOException {
    if(in == null)
      throw new IllegalArgumentException(
          "Invalid InputStream [in="+ in+ "]");
    if(start == null || start.isEmpty())
      throw new IllegalArgumentException(
          "Invalid String [start="+ start+ "]");
    if(end == null || end.isEmpty())
      throw new IllegalArgumentException(
          "Invalid String [end="+ end+ "]");
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    transferBetween(in, bos, start, end);
    return bos.toString(UTF8);
  }


  /**
   * Lê e descarta o conteúdo do stream, até que 
   * seja encontrada a <code>String</code> fornecida.
   * @param in <code>InputStream</code>
   * @param str <code>String</code> delimitadora.
   * @return <code>true</code> se a <code>String</code>
   * delimitadora for encontrada, <code>false</code>
   * caso contrário.
   * @throws IOException Caso ocorra erro na leitura do stream.
   */
  public static boolean readUntil(InputStream in, String str) throws IOException {
    if(in == null)
      throw new IllegalArgumentException(
          "Invalid InputStream [in="+ in+ "]");
    if(str == null || str.isEmpty())
      throw new IllegalArgumentException(
          "Invalid String [str="+ str+ "]");
    
    int read = -1;
    LimitedBuffer lbuf = new LimitedBuffer(str.length());
    while((read = in.read()) != -1) {
      lbuf.put(read);
      if(lbuf.size() == str.length() 
          && str.equals(lbuf.toUTF8()))
        return true;
    }
    return false;
  }


  /**
   * Lê e descarta o conteúdo do stream até que 
   * seja encontrada a primeira <code>String</code> 
   * fornecida, ou a segunda <code>String</code> 
   * fornecida, retornando a <code>String</code> 
   * encontrada, ou <code>null</code> no caso de 
   * nenhum argumento encontrado.
   * @param in <code>InputStream</code>
   * @param str primeira <code>String</code> delimitadora.
   * @param orFalse segunda <code>String</code> delimitadora.
   * @return a <code>String</code> encontrada, ou 
   * <code>null</code> no caso de nenhum argumento encontrado.
   * @throws IOException Caso ocorra erro na leitura do stream.
   */
  public static String readUntil(InputStream in, String str, String orFalse) throws IOException {
    if(in == null)
      throw new IllegalArgumentException(
          "Invalid InputStream [in="+ in+ "]");
    if(str == null || str.isEmpty())
      throw new IllegalArgumentException(
          "Invalid String [str="+ str+ "]");
    if(orFalse == null || orFalse.isEmpty())
      throw new IllegalArgumentException(
          "Invalid String [orFalse="+ orFalse+ "]");
    
    int read = -1;
    int maxlen = Math.max(str.length(), orFalse.length());
    LimitedBuffer lbuf = new LimitedBuffer(maxlen);
    while((read = in.read()) != -1) {
      lbuf.put(read);
      if(lbuf.size() == maxlen) {
        if(lbuf.toUTF8().contains(str))
          return str;
        if(lbuf.toUTF8().contains(orFalse))
          return orFalse;
      }
    }
    return null;
  }
  
}
