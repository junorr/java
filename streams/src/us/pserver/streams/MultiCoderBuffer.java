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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;
import us.pserver.cdr.hex.HexInputStream;
import us.pserver.cdr.hex.HexOutputStream;
import us.pserver.cdr.lzma.LzmaStreamFactory;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullarray;
import static us.pserver.chk.Checker.nullbuffer;
import static us.pserver.chk.Checker.range;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/07/2014
 */
public class MultiCoderBuffer {

  public static final int LARGE_BUFFER = 524_288; // 500KB
  
  
  private ByteBuffer buffer;
  
  private Path temp;
  
  private FileChannel channel;
  
  private boolean readmode;
  
  private Map<CoderType, MultiCoderBuffer> coders;
  
  private CryptKey key;
  
  private long bmark, chmark;
  
  
  public MultiCoderBuffer() {
    this(LARGE_BUFFER);
  }
  
  
  public MultiCoderBuffer(int bufferSize) {
    range(bufferSize, 1, Integer.MAX_VALUE);
    buffer = ByteBuffer.allocateDirect(bufferSize);
    readmode = false;
    key = null;
    bmark = chmark = 0;
    coders = new LinkedHashMap<>();
  }
  
  
  /**
   * Habilita/Desabilita o codificador hexadecimal.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MultiCoderBuffer setHexCoderEnabled(boolean enabled) {
    CoderType cdr = CoderType.HEX;
    cdr.setEnabled(enabled);
    if(enabled)
      coders.put(cdr, new MultiCoderBuffer());
    else
      coders.remove(cdr);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador Base64.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MultiCoderBuffer setBase64CoderEnabled(boolean enabled) {
    CoderType cdr = CoderType.BASE64;
    cdr.setEnabled(enabled);
    if(enabled)
      coders.put(cdr, new MultiCoderBuffer());
    else
      coders.remove(cdr);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador GZip.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MultiCoderBuffer setGZipCoderEnabled(boolean enabled) {
    CoderType cdr = CoderType.GZIP;
    cdr.setEnabled(enabled);
    if(enabled)
      coders.put(cdr, new MultiCoderBuffer());
    else
      coders.remove(cdr);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador Lzma.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MultiCoderBuffer setLzmaCoderEnabled(boolean enabled) {
    CoderType cdr = CoderType.LZMA;
    cdr.setEnabled(enabled);
    if(enabled)
      coders.put(cdr, new MultiCoderBuffer());
    else
      coders.remove(cdr);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador de criptografia.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @param key Chave de criptografia.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MultiCoderBuffer setCryptCoderEnabled(boolean enabled, CryptKey key) {
    CoderType cdr = CoderType.CRYPT;
    cdr.setEnabled(enabled);
    this.key = key;
    if(enabled) {
      nullarg(CryptKey.class, key);
      coders.put(cdr, new MultiCoderBuffer()
          .setCryptCoderEnabled(enabled, key));
    }
    else
      coders.remove(cdr);
    return this;
  }
  
  
  /**
   * Verifica se o codificador hexadecimal está habilitado.
   * @return <code>true</code> se o codificador estiver
   * habilitado, <code>false</code> caso contrário.
   */
  public boolean isHexCoderEnabled() {
    return CoderType.HEX.isEnabled();
  }
  
  
  /**
   * Verifica se o codificador Base64 está habilitado.
   * @return <code>true</code> se o codificador estiver
   * habilitado, <code>false</code> caso contrário.
   */
  public boolean isBase64CoderEnabled() {
    return CoderType.BASE64.isEnabled();
  }
  
  
  /**
   * Verifica se o codificador GZip está habilitado.
   * @return <code>true</code> se o codificador estiver
   * habilitado, <code>false</code> caso contrário.
   */
  public boolean isGZipCoderEnabled() {
    return CoderType.GZIP.isEnabled();
  }
  
  
  /**
   * Verifica se o codificador Lzma está habilitado.
   * @return <code>true</code> se o codificador estiver
   * habilitado, <code>false</code> caso contrário.
   */
  public boolean isLzmaCoderEnabled() {
    return CoderType.LZMA.isEnabled();
  }
  
  
  /**
   * Verifica se o codificador de criptografia está habilitado.
   * @return <code>true</code> se o codificador estiver
   * habilitado, <code>false</code> caso contrário.
   */
  public boolean isCryptCoderEnabled() {
    return CoderType.CRYPT.isEnabled() && key != null;
  }
  
  
  public boolean isAnyCoderEnabled() {
    return !coders.isEmpty();
  }
  
  
  /**
   * Retorna a chave de criptografia.
   * @return chave de criptografia.
   */
  public CryptKey getCryptKey() {
    return key;
  }
  
  
  /**
   * Retorna o índice de classificação de 
   * processamento do tipo de codificador 
   * especificado.
   * @param coder Tipo de codificador.
   * @return índice de classificação.
   */
  public int getCoderOrder(CoderType coder) {
    if(coders.isEmpty() || coder == null) return -1;
    Iterator<CoderType> set = coders.keySet().iterator();
    int count = 0;
    while(set.hasNext()) {
      if(coder == set.next())
        return count;
      count++;
    }
    return -1;
  }
  
  
  public MultiCoderBuffer getBufferFor(CoderType cdr) {
    if(coders.isEmpty() || cdr == null)
      return null;
    return coders.get(cdr);
  }
  
  
  public boolean containsCoder(CoderType ct) {
    return coders.containsKey(ct);
  }
  
  
  public int getMemBufferCapacity() {
    return buffer.capacity();
  }
  
  
  public long size() throws IOException {
    long len = buffer.position();
    if(channel != null) {
      len += channel.size();
    }
    return len;
  }
  
  
  public MultiCoderBuffer clearCoders() {
    if(coders.isEmpty()) return this;
    Iterator<CoderType> set = coders.keySet().iterator();
    while(set.hasNext()) {
      set.next().setEnabled(false);
    }
    coders.clear();
    return this;
  }
  
  
  public MultiCoderBuffer reset() throws IOException {
    close();
    channel = null;
    temp = null;
    readmode = false;
    return clearCoders();
  }
  
  
  public MultiCoderBuffer mark() throws IOException {
    bmark = buffer.position();
    if(channel != null)
      chmark = channel.position();
    return this;
  }
  
  
  public MultiCoderBuffer setMark() throws IOException {
    buffer.position((int) bmark);
    if(channel != null)
      channel.position(chmark);
    return this;
  }
  
  
  public Path getTemp() {
    return temp;
  }
  
  
  private void initChannel() throws IOException {
    temp = Files.createTempFile(null, null);
    channel = FileChannel.open(temp,
        StandardOpenOption.READ, 
        StandardOpenOption.DELETE_ON_CLOSE, 
        StandardOpenOption.WRITE);
  }
  
  
  private InputStream createInputStream() {
    return new InputStream() {
      @Override
      public int read() throws IOException {
        return MultiCoderBuffer.this.read();
      }
      @Override
      public int read(byte[] bs) throws IOException {
        return MultiCoderBuffer.this.read(bs);
      }
      @Override
      public int read(byte[] bs, int o, int l) throws IOException {
        return MultiCoderBuffer.this.read(bs, o, l);
      }
    };//input
  }
  
  
  private OutputStream createOutputStream() {
    return new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        MultiCoderBuffer.this.write(b);
      }
      @Override
      public void write(byte[] bs) throws IOException {
        MultiCoderBuffer.this.write(bs);
      }
      @Override
      public void write(byte[] bs, int o, int l) throws IOException {
        MultiCoderBuffer.this.write(bs, o, l);
      }
    };//output
  }
  
  
  public void flush() throws IOException {
    if(buffer.position() > 0) {
      if(channel == null)
        initChannel();
      buffer.flip();
      channel.write(buffer);
      buffer.clear();
    }
  }
  
  
  public InputStream getInputStream() {
    return createInputStream();
  }
  
  
  public OutputStream getOutputStream() {
    return createOutputStream();
  }
  
  
  public MultiCoderBuffer flip() throws IOException {
    if(!readmode) {
      if(channel != null) {
        flush();
        channel.position(0);
      } else {
        buffer.flip();
        System.out.println("  buf.position="+ buffer.position());
        System.out.println("  buf.limit   ="+ buffer.limit());
      }
      readmode = true;
    }
    else {
      if(channel != null) {
        channel.position(channel.size());
      } else {
        buffer.position(buffer.limit());
        buffer.limit(buffer.capacity());
      }
      readmode = false;
    }
    return this;
  }
  
  
  public void close() throws IOException {
    if(channel != null) {
      channel.close();
    }
    buffer.clear();
  }
  
  
  public MultiCoderBuffer rewind() throws IOException {
    if(!readmode) flip();
    else flip().flip();
    return this;
  }
  
  
  private OutputStream parseOutput(CoderType coder, OutputStream os) throws IOException {
    nullarg(CoderType.class, coder);
    nullarg(OutputStream.class, os);
    
    System.out.println("* Encoder enabled: ["+ coder.name()+ "]");
    switch(coder) {
      case BASE64:
        return new Base64OutputStream(os);
      case HEX:
        return new HexOutputStream(os);
      case GZIP:
        return new GZIPOutputStream(os);
      case LZMA:
        return LzmaStreamFactory.createLzmaOutput(os);
      case CRYPT:
        return CryptUtils.createCipherOutputStream(os, key);
      default:
        return null;
    }
  }
  
  
  private InputStream parseInput(CoderType coder, InputStream is) throws IOException {
    nullarg(CoderType.class, coder);
    nullarg(InputStream.class, is);
    
    System.out.println("* Decoder enabled: ["+ coder.name()+ "]");
    switch(coder) {
      case BASE64:
        return new Base64InputStream(is);
      case HEX:
        return new HexInputStream(is);
      case GZIP:
        return new GZIPInputStream(is);
      case LZMA:
        return LzmaStreamFactory.createLzmaInput(is);
      case CRYPT:
        return CryptUtils.createCipherInputStream(is, key);
      default:
        return null;
    }
  }
  
  
  public MultiCoderBuffer encode() throws IOException {
    if(coders.isEmpty() || size() == 0) return this;
    Iterator<CoderType> it = coders.keySet().iterator();
    while(it.hasNext()) {
      CoderType cd = it.next();
      MultiCoderBuffer buf = coders.get(cd);
      OutputStream os = buf.getOutputStream();
      os = parseOutput(cd, os);
      InputStream is = this.getInputStream();
      StreamUtils.transfer(is, os);
      os.close();
      buffer = buf.buffer;
      temp = buf.temp;
    }
    return this;
  }
  
  
  public MultiCoderBuffer decode() throws IOException {
    if(coders.isEmpty() || size() == 0) return this;
    Iterator<CoderType> it = coders.keySet().iterator();
    while(it.hasNext()) {
      CoderType cd = it.next();
      MultiCoderBuffer buf = coders.get(cd);
      OutputStream os = buf.getOutputStream();
      os = parseOutput(cd, os);
      InputStream is = this.getInputStream();
      StreamUtils.transfer(is, os);
      os.close();
      buffer = buf.buffer;
      temp = buf.temp;
    }
    return this;
  }
  
  
  public void write(int b) throws IOException {
    if(readmode) flip();
    if(buffer.remaining() == 0) flush();
    buffer.put((byte) b);
  }
  
  
  public void write(byte[] bs, int offset, int length) throws IOException {
    nullarray(bs);
    range(offset, -1, bs.length);
    range(length, 1, bs.length - offset);
    if(readmode) flip();
    for(int i = offset; i < length; i++) {
      write(bs[i]);
    }
  }
  
  
  public void write(byte[] bs) throws IOException {
    write(bs, 0, bs.length);
  }
  
  
  public void write(ByteBuffer buf) throws IOException {
    nullbuffer(buf);
    if(readmode) flip();
    while(buf.remaining() > 0) {
      write(buf.get());
    }
  }
  
  
  public int read() throws IOException {
    if(!readmode) flip();
    if(channel != null) {
      flush();
      ByteBuffer b = ByteBuffer.allocate(1);
      channel.read(b);
      b.flip();
      return b.get();
    }
    else {
      if(buffer.remaining() == 0) return -1;
      return buffer.get();
    }
  }
  
  
  public int read(byte[] bs, int offset, int length) throws IOException {
    nullarray(bs);
    range(offset, -1, bs.length);
    range(length, 1, bs.length - offset);
    
    if(!readmode) flip();
    if(channel != null) {
      ByteBuffer b = ByteBuffer.allocate(length);
      int read = channel.read(b);
      if(read <= 0) return read;
      b.flip();
      b.get(bs, offset, read);
      return read;
    }
    else {
      if(buffer.remaining() == 0) return -1;
      if(length > buffer.remaining())
        length = buffer.remaining();
      buffer.get(bs, offset, length);
      return length;
    }
  }
  
  
  public int read(byte[] bs) throws IOException {
    nullarray(bs);
    return read(bs, 0, bs.length);
  }
  
  
  public int read(ByteBuffer buf) throws IOException {
    nullbuffer(buf);
    if(!readmode) flip();
    byte[] bs = new byte[buf.limit()];
    int read = read(bs);
    if(read <= 0) return read;
    buf.put(bs, 0, read);
    return read;
  }
  
}
