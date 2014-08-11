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

package us.pserver.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullstr;
import static us.pserver.chk.Checker.range;


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
   *  BUFFER_SIZE = 4096
   * </code><br>
   * Tamanho do buffer em bytes.
   */
  public static int BUFFER_SIZE = 4096;
  
  
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
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    
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
    out.flush();
    return total;
  }
  
  
  public static long consume(InputStream is) throws IOException {
    return transfer(is, NullOutput.out);
  }
  
  
  /**
   * Transfere o conteúdo entre dois streams até que seja
   * encontrada a <code>String</code> fornecida no conteúdo da 
   * transmissão, ou até o final da transmissão.
   * @param is <code>InputStream</code>
   * @param os <code>OutputStream</code>
   * @param until <code>String</code> de condição de 
   * parada de transferência de conteúdo.
   * @return Número total de bytes transferidos.
   * @throws IOException caso ocorra erro na transferência.
   */
  public static StreamResult transferUntil(InputStream is, OutputStream os, String until) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(until);
    
    StreamResult res = new StreamResult();
    LimitedBuffer lim = new LimitedBuffer(until.length());
    ByteBuffer buf = ByteBuffer.allocate(until.length() * 2);
    ByteBufferConverter cv = new ByteBufferConverter();
    
    byte[] bs = new byte[1];
    int read = -1;
    
    while(true) {
      read = is.read(bs);
      if(read <= 0) {
        res.eofOn();
        break;
      }
      
      res.increment(read);
      lim.put(bs[0]);
      if(buf.remaining() < 1) {
        buf.flip();
        byte[] b = new byte[lim.length()];
        buf.get(b);
        os.write(b);
        buf.compact();
      }
      buf.put(bs[0]);
      
      if(until.equals(lim.toUTF8())) {
        if(buf.position() > lim.length()) {
          buf.position(buf.position() - lim.length());
          buf.flip();
          os.write(cv.convert(buf));
        }
        res.setToken(until);
        break;
      }
    }
    os.flush();
    return res;
  }
  
  
  public static StreamResult transferUntilOr(InputStream is, OutputStream os, String until, String or) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(until);
    nullstr(or);
    
    StreamResult res = new StreamResult();
    int maxlen = Math.max(until.length(), or.length());
    LimitedBuffer lim = new LimitedBuffer(maxlen);
    ByteBuffer buf = ByteBuffer.allocate(maxlen * 2);
    ByteBufferConverter cv = new ByteBufferConverter();
    
    byte[] bs = new byte[1];
    int read = -1;
    
    while(true) {
      read = is.read(bs);
      if(read <= 0) {
        res.eofOn();
        break;
      }
      
      res.increment(read);
      lim.put(bs[0]);
      if(buf.remaining() < 1) {
        buf.flip();
        byte[] b = new byte[maxlen];
        buf.get(b);
        os.write(b);
        buf.compact();
      }
      buf.put(bs[0]);
      
      if(until.equals(lim.toUTF8()) 
          || lim.toUTF8().contains(until)) {
        if(buf.position() > until.length()) {
          buf.position(buf.position() - until.length());
          buf.flip();
          os.write(cv.convert(buf));
        }
        res.setToken(until);
        break;
      }
      else if(or.equals(lim.toUTF8())
          || lim.toUTF8().contains(or)) {
        if(buf.position() > or.length()) {
          buf.position(buf.position() - or.length());
          buf.flip();
          os.write(cv.convert(buf));
        }
        res.setToken(or);
        break;
      }
    }
    os.flush();
    return res;
  }
  
  
  public static StreamResult transferBetween(InputStream in, OutputStream out, 
      String start, String end) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(start);
    nullstr(end);
    
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
    nullarg(OutputStream.class, out);
    nullarg(String.class, str);
    
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
  public static StreamResult readBetween(InputStream in, String start, String end) throws IOException {
    nullarg(InputStream.class, in);
    nullstr(start);
    nullstr(end);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamResult res = transferBetween(in, bos, start, end);
    return res.setContent(bos.toString(UTF8));
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
  public static StreamResult readUntil(InputStream in, String str) throws IOException {
    nullarg(InputStream.class, in);
    nullstr(str);
    
    StreamResult res = new StreamResult();
    int read = -1;
    LimitedBuffer lbuf = new LimitedBuffer(str.length());
    
    while(true) {
      read = in.read();
      if(read == -1) {
        res.eofOn();
        break;
      }
      
      res.increment();
      lbuf.put(read);
      if(str.equals(lbuf.toUTF8())) {
        res.setToken(str);
        break;
      }
    }
    return res;
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
  public static StreamResult readUntilOr(InputStream in, String str, String orFalse) throws IOException {
    nullarg(InputStream.class, in);
    nullstr(str);
    nullstr(orFalse);
    
    StreamResult res = new StreamResult();
    int read = -1;
    int maxlen = Math.max(str.length(), orFalse.length());
    LimitedBuffer lbuf = new LimitedBuffer(maxlen);
    
    while(true) {
      read = in.read();
      if(read == -1) {
        res.eofOn();
        break;
      }
      
      res.increment();
      lbuf.put(read);
      if(lbuf.size() == maxlen) {
        if(lbuf.toUTF8().contains(str)) {
          res.setToken(str);
          break;
        }
        else if(lbuf.toUTF8().contains(orFalse)) {
          res.setToken(orFalse);
          break;
        }
      }
    }
    return res;
  }
  
  
  public static String readString(InputStream is, int length) throws IOException {
    nullarg(InputStream.class, is);
    range(length, 1, Integer.MAX_VALUE);
    byte[] bs = new byte[length];
    int read = is.read(bs);
    if(read <= 0) return null;
    return new String(bs, 0, read, UTF8);
  }
  
  
  public static StreamResult readStringUntil(InputStream is, String until) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(until);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamResult res = transferUntil(is, bos, until);
    return res.setContent(bos.toString(UTF8));
  }
  
  
  public static StreamResult readStringUntilOr(InputStream is, String until, String or) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(until);
    nullstr(or);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    StreamResult res = transferUntilOr(is, bos, until, or);
    return res.setContent(bos.toString(UTF8));
  }
  
  
  public static void writeEOF(OutputStream os) throws IOException {
    nullarg(OutputStream.class, os);
    os.write(BYTES_EOF);
    os.write(new byte[0]);
    os.flush();
  }
  
}
