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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.hex.HexBufferCoder;
import us.pserver.cdr.hex.HexByteCoder;
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
    return total;
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
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    
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
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(until);
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      lbuf.put(read);
      
      if(until.equals(lbuf.toUTF8())) {
        rewindBuffers(buffer, last, until.length());
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferTo(last, out);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, out);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, out);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  public static long transferUntilHexCoder(InputStream in, OutputStream out, String until, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(until);
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      lbuf.put(read);
      
      if(until.equals(lbuf.toUTF8())) {
        rewindBuffers(buffer, last, until.length());
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferHexCoder(last, out, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferHexCoder(buffer, out, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferHexCoder(last, out, encode);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  public static long transferUntilCryptCoder(InputStream in, OutputStream out, CryptKey key, String until, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullarg(CryptKey.class, key);
    nullstr(until);
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      lbuf.put(read);
      
      if(until.equals(lbuf.toUTF8())) {
        rewindBuffers(buffer, last, until.length());
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferCryptCoder(last, out, key, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferCryptCoder(buffer, out, key, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferCryptCoder(last, out, key, encode);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  public static long transferUntilCryptHexCoder(InputStream in, OutputStream out, CryptKey key, String until, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullarg(CryptKey.class, key);
    nullstr(until);
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      lbuf.put(read);
      
      if(until.equals(lbuf.toUTF8())) {
        rewindBuffers(buffer, last, until.length());
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferCryptHexCoder(last, out, key, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferCryptHexCoder(buffer, out, key, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferCryptHexCoder(last, out, key, encode);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  private static void rewindBuffers(ByteBuffer buf, ByteBuffer last, int length) {
    nullarg(ByteBuffer.class, buf);
    nullarg(ByteBuffer.class, last);
    range(length, 1, Integer.MAX_VALUE);
    
    if(buf.position() >= length) {
      buf.position(buf.position() - (length-1));
    } else {
      int back1 = buf.position();
      int back2 = length - back1;
      back2 = Math.min(back2-1, last.position());
      last.position(last.position() - back2);
      buf.position(buf.position() - back1);
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    String until = "</xml>";
    String str = "A direct byte buffer may be created "
        + "by invoking the allocateDirect factory "
        + "metho" + until;
    System.out.println("* str='"+ str+ "'");
    ByteArrayInputStream bis = 
        new ByteArrayInputStream(str.getBytes(UTF8));
    ByteArrayOutputStream bos = 
        new ByteArrayOutputStream();
    System.out.println("* transferUntil(bis, bos, until);");
    transferUntil(bis, bos, until);
    //transfer(bis, bos);
    System.out.println("* bos='"+ bos.toString(UTF8)+ "'");
  }
  
  
  public static void writeBufferTo(ByteBuffer buf, OutputStream out) throws IOException {
    nullarg(ByteBuffer.class, buf);
    nullarg(OutputStream.class, out);
    if(buf.remaining() < 1) return;
    
    byte[] bs = new byte[buf.remaining()];
    buf.get(bs);
    out.write(bs);
    out.flush();
  }
  
  
  public static void writeBufferHexCoder(ByteBuffer buf, OutputStream out, boolean encode) throws IOException {
    nullarg(ByteBuffer.class, buf);
    nullarg(OutputStream.class, out);
    if(buf.remaining() < 1) return;
    
    HexBufferCoder cdr = new HexBufferCoder();
    ByteBufferConverter conv = new ByteBufferConverter();
    
    cdr.apply(buf, encode);
    out.write(conv.convert(buf));
    out.flush();
  }
  
  
  public static void writeBufferCryptCoder(ByteBuffer buf, OutputStream out, CryptKey key, boolean encode) throws IOException {
    nullarg(ByteBuffer.class, buf);
    nullarg(OutputStream.class, out);
    nullarg(CryptKey.class, key);
    if(buf.remaining() < 1) return;
    
    CryptBufferCoder cdr = new CryptBufferCoder(key);
    ByteBufferConverter conv = new ByteBufferConverter();
    
    cdr.apply(buf, encode);
    out.write(conv.convert(buf));
    out.flush();
  }
  
  
  public static void writeBufferCryptHexCoder(ByteBuffer buf, OutputStream out, CryptKey key, boolean encode) throws IOException {
    nullarg(ByteBuffer.class, buf);
    nullarg(OutputStream.class, out);
    nullarg(CryptKey.class, key);
    if(buf.remaining() < 1) return;
    
    CryptBufferCoder cbc = new CryptBufferCoder(key);
    HexBufferCoder hbc = new HexBufferCoder();
    ByteBufferConverter conv = new ByteBufferConverter();
    
    if(encode) {
      cbc.encode(buf);
      hbc.encode(buf);
    }
    else {
      hbc.decode(buf);
      cbc.decode(buf);
    }
    
    out.write(conv.convert(buf));
    out.flush();
  }
  
  
  public static void writeBufferTo(ByteBuffer buf, ByteBuffer out) throws IOException {
    nullarg(ByteBuffer.class, buf);
    nullarg(OutputStream.class, out);
    
    int minsize = Math.min(buf.remaining(), out.remaining());
    byte[] bs = new byte[minsize];
    buf.get(bs);
    out.put(bs);
  }
  
  
  public static long transferUntilOr(InputStream in, OutputStream out, String until, String orfalse) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(until);
    nullstr(orfalse);
    
    LimitedBuffer luntil = new LimitedBuffer(until.length());
    LimitedBuffer lor = new LimitedBuffer(orfalse.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      luntil.put(read);
      lor.put(read);
      
      int len = 0;
      if(until.equals(luntil.toUTF8()))
        len = until.length();
      else if(orfalse.equals(lor.toUTF8()))
        len = orfalse.length();
      
      if(len > 0) {
        rewindBuffers(buffer, last, len);
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferTo(last, out);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, out);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, out);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  public static long transferUntilOrHexCoder(InputStream in, OutputStream out, String until, String orfalse, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(until);
    nullstr(orfalse);
    
    LimitedBuffer luntil = new LimitedBuffer(until.length());
    LimitedBuffer lor = new LimitedBuffer(orfalse.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      luntil.put(read);
      lor.put(read);
      
      int len = 0;
      if(until.equals(luntil.toUTF8()))
        len = until.length();
      else if(orfalse.equals(lor.toUTF8()))
        len = orfalse.length();
      
      if(len > 0) {
        rewindBuffers(buffer, last, until.length());
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferHexCoder(last, out, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferHexCoder(buffer, out, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferHexCoder(last, out, encode);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  public static long transferUntilOrCryptCoder(InputStream in, OutputStream out, CryptKey key, String until, String orfalse, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullarg(CryptKey.class, key);
    nullstr(until);
    nullstr(orfalse);
    
    LimitedBuffer luntil = new LimitedBuffer(until.length());
    LimitedBuffer lor = new LimitedBuffer(orfalse.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      luntil.put(read);
      lor.put(read);
      
      int len = 0;
      if(until.equals(luntil.toUTF8()))
        len = until.length();
      else if(orfalse.equals(lor.toUTF8()))
        len = orfalse.length();
      
      if(len > 0) {
        rewindBuffers(buffer, last, until.length());
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferCryptCoder(last, out, key, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferCryptCoder(buffer, out, key, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferCryptCoder(last, out, key, encode);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  public static long transferUntilOrCryptHexCoder(InputStream in, OutputStream out, CryptKey key, String until, String orfalse, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullarg(CryptKey.class, key);
    nullstr(until);
    nullstr(orfalse);
    
    LimitedBuffer luntil = new LimitedBuffer(until.length());
    LimitedBuffer lor = new LimitedBuffer(orfalse.length());
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    ByteBuffer last = ByteBuffer.allocate(BUFFER_SIZE);
    long total = 0;
    int read = -1;
    
    while((read = in.read()) != -1) {
      total++;
      luntil.put(read);
      lor.put(read);
      
      int len = 0;
      if(until.equals(luntil.toUTF8()))
        len = until.length();
      else if(orfalse.equals(lor.toUTF8()))
        len = orfalse.length();
      
      if(len > 0) {
        rewindBuffers(buffer, last, until.length());
        buffer.flip();
        if(last.remaining() > 0) {
          writeBufferTo(buffer, last);
        }
        if(last.position() > 0) {
          last.flip();
          writeBufferCryptHexCoder(last, out, key, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferCryptHexCoder(buffer, out, key, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferCryptHexCoder(last, out, key, encode);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    return total;
  }
  
  
  public static long transferBetween(InputStream in, OutputStream out, 
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
  public static String readBetween(InputStream in, String start, String end) throws IOException {
    nullarg(InputStream.class, in);
    nullstr(start);
    nullstr(end);
    
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
    nullarg(InputStream.class, in);
    nullstr(str);
    
    int read = -1;
    LimitedBuffer lbuf = new LimitedBuffer(str.length());
    while((read = in.read()) != -1) {
      lbuf.put(read);
      if(str.equals(lbuf.toUTF8()))
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
  public static String readUntilOr(InputStream in, String str, String orFalse) throws IOException {
    nullarg(InputStream.class, in);
    nullstr(str);
    nullstr(orFalse);
    
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
  
  
  public static String readString(InputStream is, int length) throws IOException {
    nullarg(InputStream.class, is);
    range(length, 1, Integer.MAX_VALUE);
    byte[] bs = new byte[length];
    int read = is.read(bs);
    if(read <= 0) return null;
    return new String(bs, 0, read, UTF8);
  }
  
  
  public static String readStringUntil(InputStream is, String until) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(until);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    transferUntil(is, bos, until);
    return bos.toString(UTF8);
  }
  
}
