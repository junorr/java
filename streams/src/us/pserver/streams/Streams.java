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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;
import us.pserver.cdr.hex.HexInputStream;
import us.pserver.cdr.hex.HexOutputStream;
import static us.pserver.streams.Checker.nullarg;
import static us.pserver.streams.Checker.nullbuffer;
import static us.pserver.streams.Checker.nullstr;
import static us.pserver.streams.Checker.zero;
import static us.pserver.streams.LimitedBuffer.UTF8;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class Streams {
  
  public static final int BUFFER_SIZE = 1024;
  
  public static final String EOF = "EOF";
  
  
  private ByteBufferConverter conv;
  
  private CryptKey key;
  
  private ByteBuffer buffer, last;
  
  private GZIPOutputStream gzo;
  
  private boolean useHex, useCrypt, 
      useGZip, useB64;
  
  
  public Streams() {
    conv = new ByteBufferConverter();
    buffer = null;
    last = null;
    gzo = null;
    useHex = useCrypt = 
        useGZip = useB64 = false;
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
  
  
  public Streams setUseBase64Coder(boolean use) {
    useB64 = use;
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
  
  
  public boolean isUsingBase64Coder() {
    return useB64;
  }
  
  
  private void resetBuffers() {
    if(buffer == null)
      buffer = ByteBuffer.allocate(BUFFER_SIZE);
    if(last == null)
      last = ByteBuffer.allocate(BUFFER_SIZE);
    buffer.clear();
    last.clear();
  }
  
  
  public OutputStream configureOutput(OutputStream os) throws IOException {
    nullarg(OutputStream.class, os);
    OutputStream output = os;
    if(useGZip) {
      gzo = new GZIPOutputStream(output);
      output = gzo;
    }
    if(useHex)
      output = new HexOutputStream(output);
    if(useB64)
      output = new Base64OutputStream(output);
    if(useCrypt)
      output = CryptUtils.createCipherOutputStream(output, key);
    return output;
  }
  
  
  public InputStream configureInput(InputStream is) throws IOException {
    nullarg(InputStream.class, is);
    InputStream input = is;
    if(useGZip)
      input = new GZIPInputStream(input);
    if(useHex)
      input = new HexInputStream(input);
    if(useB64)
      input = new Base64InputStream(input);
    if(useCrypt)
      input = CryptUtils.createCipherInputStream(input, key);
    return input;
  }
  
  
  public void finishGZOutput() throws IOException {
    if(useGZip && gzo != null) {
      gzo.finish();
      gzo.flush();
      gzo = null;
    }
  }
  
  
  public void finishStreams(InputStream is, OutputStream os) throws IOException {
    is.close();
    os.flush();
    os.close();
  }
  
  
  public long transfer(InputStream is, OutputStream os, boolean encode) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    
    InputStream input = is;
    OutputStream output = os;
    
    if(!encode) {
      input = configureInput(input);
    } else {
      output = configureOutput(output);
    }
    
    long total = 0;
    int read = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    
    while((read = input.read(buf)) > 0) {
      total += read;
      output.write(buf, 0, read);
      if(read < buf.length) {
        int len = (read < 50 ? read : 50);
        String str = new String(buf, read -len, len);
        if(str.contains(EOF)) break;
      }
    }
    if(encode) finishGZOutput();
    return total;
  }
  
  
  public long transfer(InputStream is, OutputStream os) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    
    long total = 0;
    int read = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    
    while((read = is.read(buf)) > 0) {
      total += read;
      os.write(buf, 0, read);
      if(read < buf.length) {
        int len = (read < 50 ? read : 50);
        String str = new String(buf, read -len, len);
        if(str.contains(EOF)) break;
      }
    }
    return total;
  }
  
  
  public long transferUntil(InputStream is, OutputStream os, String until, boolean encode) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(until);
    
    resetBuffers();
    InputStream input = is;
    OutputStream output = os;
    
    if(!encode) {
      input = configureInput(input);
    } else {
      output = configureOutput(output);
    }
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    long total = 0;
    int read = -1;
    
    while((read = input.read()) != -1) {
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
          writeBufferTo(last, output);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, output);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, output);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    if(encode) finishGZOutput();
    return total;
  }
  
  
  public long transferUntil(InputStream is, OutputStream os, String until) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(until);
    
    resetBuffers();
    
    LimitedBuffer lbuf = new LimitedBuffer(until.length());
    long total = 0;
    int read = -1;
    
    while((read = is.read()) != -1) {
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
          writeBufferTo(last, os);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, os);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, os);
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
  
  
  public long transferUntilOr(InputStream is, OutputStream os, String until, String orfalse, boolean encode) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(until);
    nullstr(orfalse);
    
    resetBuffers();
    InputStream input = is;
    OutputStream output = os;
    
    if(!encode) {
      input = configureInput(input);
    } else {
      output = configureOutput(output);
    }
    
    LimitedBuffer luntil = new LimitedBuffer(until.length());
    LimitedBuffer lor = new LimitedBuffer(orfalse.length());
    long total = 0;
    int read = -1;
    
    while((read = input.read()) != -1) {
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
          writeBufferTo(last, output);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, output);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, output);
            last.clear();
          }
          buffer.flip();
          writeBufferTo(buffer, last);
          buffer.clear();
        }
        buffer.put((byte) read);
      }
    }
    if(encode) finishGZOutput();
    return total;
  }
  
  
  public long transferUntilOr(InputStream is, OutputStream os, String until, String orfalse) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(until);
    nullstr(orfalse);
    
    resetBuffers();
    
    LimitedBuffer luntil = new LimitedBuffer(until.length());
    LimitedBuffer lor = new LimitedBuffer(orfalse.length());
    long total = 0;
    int read = -1;
    
    while((read = is.read()) != -1) {
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
          writeBufferTo(last, os);
        }
        if(buffer.remaining() > 0) {
          writeBufferTo(buffer, os);
        }
      }
      else {
        if(buffer.remaining() == 0) {
          if(last.remaining() == 0) {
            last.flip();
            writeBufferTo(last, os);
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
  
  
  public void writeBufferTo(ByteBuffer buf, ByteBuffer out) throws IOException {
    nullbuffer(buf);
    nullbuffer(out);
    
    int minsize = Math.min(buf.remaining(), out.remaining());
    byte[] bs = new byte[minsize];
    buf.get(bs);
    out.put(bs);
  }
  
  
  public void writeBufferTo(ByteBuffer buf, OutputStream os) throws IOException {
    nullbuffer(buf);
    nullarg(OutputStream.class, os);
    
    byte[] bs = conv.convert(buf);
    if(bs != null) {
      os.write(bs);
      os.flush();
    }
  }
  
  
  public long transferBetween(InputStream is, OutputStream os, 
      String start, String end, boolean encode) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(start);
    nullstr(end);
    
    InputStream input = is;
    OutputStream output = os;
    
    if(!encode) {
      input = configureInput(input);
    } else {
      output = configureOutput(output);
    }
    
    readUntil(input, start);
    long t = transferUntil(input, output, end);
    if(encode) finishGZOutput();
    return t;
  }
  
  
  public long transferBetween(InputStream is, OutputStream os, String start, String end) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    nullstr(start);
    nullstr(end);
    
    readUntil(is, start);
    return transferUntil(is, os, end);
  }
  
  
  public String readDecodingBetween(InputStream is, String start, String end) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(start);
    nullstr(end);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    transferBetween(is, bos, start, end, false);
    return bos.toString(UTF8);
  }


  public String readBetween(InputStream is, String start, String end) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(start);
    nullstr(end);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    transferBetween(is, bos, start, end);
    return bos.toString(UTF8);
  }


  public boolean readUntil(InputStream is, String str) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(str);
    
    int read = -1;
    LimitedBuffer lbuf = new LimitedBuffer(str.length());
    while((read = is.read()) != -1) {
      lbuf.put(read);
      if(str.equals(lbuf.toUTF8()))
        return true;
    }
    return false;
  }


  public String readUntilOr(InputStream in, String str, String orFalse) throws IOException {
    nullarg(InputStream.class, in);
    nullstr(str);
    nullstr(orFalse);
    
    int read = -1;
    int maxlen = Math.max(str.length(), orFalse.length());
    LimitedBuffer lbuf = new LimitedBuffer(maxlen);
    while((read = in.read()) != -1) {
      lbuf.put(read);
      if(lbuf.toUTF8().contains(str))
        return str;
      if(lbuf.toUTF8().contains(orFalse))
        return orFalse;
    }
    return null;
  }
  

  public boolean readDecodingUntil(InputStream is, String str) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(str);
    
    InputStream input = configureInput(is);
    
    int read = -1;
    LimitedBuffer lbuf = new LimitedBuffer(str.length());
    while((read = input.read()) != -1) {
      lbuf.put(read);
      if(str.equals(lbuf.toUTF8()))
        return true;
    }
    return false;
  }


  public String readDecodingUntilOr(InputStream is, String str, String orFalse) throws IOException {
    nullarg(InputStream.class, is);
    nullstr(str);
    nullstr(orFalse);
    
    InputStream input = configureInput(is);
    
    int read = -1;
    int maxlen = Math.max(str.length(), orFalse.length());
    LimitedBuffer lbuf = new LimitedBuffer(maxlen);
    while((read = input.read()) != -1) {
      lbuf.put(read);
      if(lbuf.toUTF8().contains(str))
        return str;
      if(lbuf.toUTF8().contains(orFalse))
        return orFalse;
    }
    return null;
  }
  
}
