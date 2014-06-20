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
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.b64.Base64BufferCoder;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.hex.HexBufferCoder;
import static us.pserver.streams.Checker.nullarg;
import static us.pserver.streams.Checker.nullbuffer;
import static us.pserver.streams.Checker.nullstr;
import static us.pserver.streams.Checker.zero;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class Streams {
  
  public static final int 
      BUFFER_SIZE = 1024,
      BUF_HEX_SIZE = 2048;
  
  public static final String EOF = "EOF";
  
  
  private HexBufferCoder hex;
  
  private ByteBufferConverter conv;
  
  private CryptKey key;
  
  private CryptBufferCoder crypt;
  
  private GZIPOutputStream gzout;
  
  private GZIPInputStream gzin;
  
  private ByteBuffer buffer, last;
  
  private boolean useHex, useCrypt, 
      useGZip, decOnRead;
  
  
  public Streams() {
    hex = new HexBufferCoder();
    conv = new ByteBufferConverter();
    crypt = null;
    gzout = null;
    gzin = null;
    buffer = null;
    last = null;
    useHex = useCrypt = 
        useGZip = decOnRead = false;
  }
  
  
  public Streams setUseHexCoder(boolean use) {
    useHex = use;
    return this;
  }
  
  
  public Streams setUseCryptCoder(boolean use, CryptKey key) {
    if(use) {
      nullarg(CryptKey.class, key);
      CryptBufferCoder crypt = new CryptBufferCoder(key);
    }
    useCrypt = use;
    this.key = key;
    return this;
  }
  
  
  public Streams setUseGZipCoder(boolean use) {
    useGZip = use;
    return this;
  }
  
  
  public Streams setDecodeOnRead(boolean onread) {
    decOnRead = onread;
    return this;
  }
  
  
  public boolean isUsingHexCoder() {
    return useHex;
  }
  
  
  public boolean isUsingCryptCoder() {
    return useCrypt;
  }
  
  
  public boolean isUsingGZipCoder() {
    return useGZip;
  }
  
  
  public boolean isDecodingOnRead() {
    return decOnRead;
  }
  
  
  private void resetBuffers(int size) {
    if(buffer == null)
      buffer = ByteBuffer.allocate(size);
    if(last == null)
      last = ByteBuffer.allocate(size);
    buffer.clear();
    last.clear();
  }
  
  
  public long transfer(InputStream in, OutputStream out, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    
    if(!encode) {
      gzin = new GZIPInputStream(in);
      in = gzin;
    }
    
    long total = 0;
    int read = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    
    while((read = in.read(buf)) > 0) {
      total += read;
      writeBufferTo(conv.reverse(buf, 0, read), out, encode);
      if(read < buf.length) {
        int len = (read < 100 ? read : 100);
        String str = new String(buf, read -len, len);
        if(str.contains(EOF)) break;
      }
    }
    return total;
  }
  
  
  public long transferUntil(InputStream in, OutputStream out, String until, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(until);
    
    resetBuffers();
    
    if(!encode) {
      gzin = new GZIPInputStream(in);
      in = gzin;
    }
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
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
          writeBufferTo(last, out, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, out, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, out, encode);
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
  
  
  public long transferUntilOr(InputStream in, OutputStream out, String until, String orfalse, boolean encode) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(until);
    nullstr(orfalse);
    
    resetBuffers();
    
    LimitedBuffer luntil = new LimitedBuffer(until.length());
    LimitedBuffer lor = new LimitedBuffer(orfalse.length());
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
          writeBufferTo(last, out, encode);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, out, encode);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, out, encode);
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
  
  
  private void rewindBuffers(ByteBuffer buf, ByteBuffer last, int length) {
    nullbuffer(buf);
    nullbuffer(last);
    zero(length);
    
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
  
  
  public void writeBufferTo(ByteBuffer buf, OutputStream out, boolean encode) throws IOException {
    nullbuffer(buf);
    nullarg(OutputStream.class, out);
    
    if(encode) {
      writeEncoding(buf, out);
    }
    else {
      writeDecoding(buf, out);
    }
  }
  
  
  public void writeBufferTo(ByteBuffer buf, ByteBuffer out) throws IOException {
    nullbuffer(buf);
    nullbuffer(out);
    
    int minsize = Math.min(buf.remaining(), out.remaining());
    byte[] bs = new byte[minsize];
    buf.get(bs);
    out.put(bs);
  }
  
  
  private void writeEncoding(ByteBuffer buf, OutputStream out) throws IOException {
    nullbuffer(buf);
    nullarg(OutputStream.class, out);
    
    if(useCrypt) {
      nullarg(CryptKey.class, key);
      nullarg(CryptBufferCoder.class, crypt);
      buf = crypt.encode(buf);
    }
    if(useGZip) {
      encodeGZip(buf, out);
    }
    else {
      out.write(conv.convert(buf));
      out.flush();
    }
  }
  
  
  private void writeDecoding(ByteBuffer buf, OutputStream out) throws IOException {
    nullbuffer(buf);
    nullarg(OutputStream.class, out);
    
    if(useHex) {
      buf = hex.decode(buf);
    }
    if(useBase64) {
      buf = base64.decode(buf);
    }
    if(useCrypt) {
      nullarg(CryptKey.class, key);
      nullarg(CryptBufferCoder.class, crypt);
      buf = crypt.decode(buf);
    }
  }
  
  
  private void encodeGZip(ByteBuffer buf, OutputStream out) throws IOException {
    nullbuffer(buf);
    nullarg(OutputStream.class, out);
    
    if(gzout == null)
      gzout = new GZIPOutputStream(out);
    gzout.write(conv.convert(buf));
    gzout.flush();
  }
  
  
  private InputStream decodeGZip(InputStream in) throws IOException {
    nullarg(InputStream.class, in);
    
    byte[] bs = new byte[BUFFER_SIZE];
    int read = 0;
    Path temp = Files.createTempFile(null, null);
    OutputStream out = Files.newOutputStream(temp, 
        StandardOpenOption.WRITE, 
        StandardOpenOption.CREATE);
    gzin = new GZIPInputStream(in);
    
    while((read = gzin.read(bs)) > 0) {
      out.write(bs, 0, read);
    }
    out.flush();
    out.close();
    in.close();
    return Files.newInputStream(temp, StandardOpenOption.READ);
  }
  
  
  public void close() {
    try {
      gzout.finish();
      gzout.flush();
      gzout.close();
      gzin.close();
    } catch(Exception e) {}
  }
  
  
  public long transferBetween(InputStream in, OutputStream out, 
      String start, String end, boolean encode) throws IOException {
    
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    nullstr(start);
    nullstr(end);
    
    readUntil(in, start);
    return transferUntil(in, out, end, encode);
  }
  
  
  public String readBetween(InputStream in, String start, String end) throws IOException {
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
    testNull(InputStream.class, "in", in);
    testNull(String.class, "str", str);
    
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
    testNull(InputStream.class, "in", in);
    testNull(String.class, "str", str);
    testNull(String.class, "orFalse", orFalse);
    
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
