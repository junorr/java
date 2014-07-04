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
public class MegaBuffer {

  public static final int LARGE_BUFFER = 1_048_576;
  
  
  private ByteBuffer buffer;
  
  private Path temp;
  
  private FileChannel channel;
  
  private boolean readmode;
  
  private CoderType[] order;
  
  private CryptKey key;
  
  
  public MegaBuffer() {
    this(LARGE_BUFFER);
  }
  
  
  public MegaBuffer(int bufferSize) {
    range(bufferSize, 1, Integer.MAX_VALUE);
    buffer = ByteBuffer.allocateDirect(bufferSize);
    readmode = false;
    key = null;
    order = new CoderType[5];
    order[0] = CoderType.CRYPT;
    order[1] = CoderType.BASE64;
    order[2] = CoderType.HEX;
    order[3] = CoderType.GZIP;
    order[4] = CoderType.LZMA;
  }
  
  
  /**
   * Habilita/Desabilita o codificador hexadecimal.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MegaBuffer setHexCoderEnabled(boolean enabled) {
    CoderType.HEX.setEnabled(enabled);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador Base64.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MegaBuffer setBase64CoderEnabled(boolean enabled) {
    CoderType.BASE64.setEnabled(enabled);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador GZip.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MegaBuffer setGZipCoderEnabled(boolean enabled) {
    CoderType.GZIP.setEnabled(enabled);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador Lzma.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>MixedBuffer</code>.
   */
  public MegaBuffer setLzmaCoderEnabled(boolean enabled) {
    CoderType.LZMA.setEnabled(enabled);
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
  public MegaBuffer setCryptCoderEnabled(boolean enabled, CryptKey key) {
    if(enabled) nullarg(CryptKey.class, key);
    this.key = key;
    CoderType.CRYPT.setEnabled(enabled);
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
    boolean b = false;
    for(int i = 0; i < order.length; i++) {
      b = (b || order[i].isEnabled());
    }
    return b;
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
    for(int i = 0; i < 5; i++) {
      if(order[i] == coder)
        return i;
    }
    return -1;
  }
  
  
  public int getBufferSize() {
    return buffer.capacity();
  }
  
  
  public long size() throws IOException {
    long len = buffer.position();
    if(channel != null) {
      len += channel.size();
    }
    return len;
  }
  
  
  public MegaBuffer clearCoders() {
    for(int i = 0; i < order.length; i++) {
      order[i].setEnabled(false);
    }
    return this;
  }
  
  
  public MegaBuffer reset() throws IOException {
    close();
    channel = null;
    temp = null;
    readmode = false;
    return clearCoders();
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
  
  
  private InputStream createInputStream() throws IOException {
    return new InputStream() {
      @Override
      public int read() throws IOException {
        return MegaBuffer.this.read();
      }
      @Override
      public int read(byte[] bs) throws IOException {
        return MegaBuffer.this.read(bs);
      }
      @Override
      public int read(byte[] bs, int o, int l) throws IOException {
        return MegaBuffer.this.read(bs, o, l);
      }
    };//input
  }
  
  
  private OutputStream createOutputStream() throws IOException {
    return new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        MegaBuffer.this.write(b);
      }
      @Override
      public void write(byte[] bs) throws IOException {
        MegaBuffer.this.write(bs);
      }
      @Override
      public void write(byte[] bs, int o, int l) throws IOException {
        MegaBuffer.this.write(bs, o, l);
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
  
  
  public InputStream getInputStream() throws IOException {
    return createInputStream();
  }
  
  
  public OutputStream getOutputStream() throws IOException {
    return createOutputStream();
  }
  
  
  public MegaBuffer flip() throws IOException {
    if(!readmode) {
      if(channel != null) {
        flush();
        channel.position(0);
      } else {
        buffer.flip();
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
  
  
  public MegaBuffer rewind() throws IOException {
    if(!readmode)
      this.flip().flip();
    return this;
  }
  
  
  /**
   * Encapsula o stream de saída especificado nos 
   * streams de codificação habilitados de acordo 
   * com a ordem de processamento pré definida.
   * @param os Stream de saída a ser encapsulado.
   * @return Stream de saída de codificação.
   * @throws IOException caso ocorra erro na operação.
   */
  private OutputStream configureOutput(OutputStream os) throws IOException {
    nullarg(OutputStream.class, os);
    OutputStream output = os;
    for(int i = 4; i >= 0; i--) {
      if(order[i].isEnabled())
        output = parseOutput(order[i], output);
    }
    return output;
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
  
  
  /**
   * Encapsula o stream de entrada especificado nos 
   * streams de codificação habilitados de acordo 
   * com a ordem de processamento pré definida.
   * @param is Stream de entrada a ser encapsulado.
   * @return Stream de entrada para codificação.
   * @throws IOException caso ocorra erro na operação.
   */
  private InputStream configureInput(InputStream is) throws IOException {
    nullarg(InputStream.class, is);
    InputStream input = is;
    for(int i = 4; i >= 0; i--) {
      if(order[i].isEnabled())
        input = parseInput(order[i], input);
    }
    return input;
  }
  
  
  public void write(int b) throws IOException {
    if(readmode) flip();
    if(buffer.remaining() == 0) flush();
    buffer.put((byte) b);
  }
  
  
  public void write(byte[] bs, int offset, int length) throws IOException {
    nullarray(bs);
    range(offset, 0, bs.length -2);
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
  
  
  public long read(InputStream in) throws IOException {
    nullarg(InputStream.class, in);
    if(readmode) flip();
    OutputStream out = getOutputStream();
    long total = transfer(in, out);
    out.close();
    in.close();
    return total;
  }
  
  
  public long readDecoding(InputStream in) throws IOException {
    nullarg(InputStream.class, in);
    if(readmode) flip();
    OutputStream out = getOutputStream();
    in = configureInput(in);
    long total = transfer(in, out);
    out.close();
    in.close();
    return total;
  }
  
  
  public long readEncoding(InputStream in) throws IOException {
    nullarg(InputStream.class, in);
    if(readmode) flip();
    OutputStream out = configureOutput(getOutputStream());
    long total = transfer(in, out);
    out.close();
    in.close();
    return total;
  }
  
  
  public long read(Path p) throws IOException {
    nullarg(Path.class, p);
    InputStream in = Files.newInputStream(p,
        StandardOpenOption.READ);
    return read(in);
  }
  
  
  public long readEncoding(Path p) throws IOException {
    nullarg(Path.class, p);
    InputStream in = Files.newInputStream(p,
        StandardOpenOption.READ);
    return readEncoding(in);
  }
  
  
  public long readDecoding(Path p) throws IOException {
    nullarg(Path.class, p);
    InputStream in = Files.newInputStream(p,
        StandardOpenOption.READ);
    return readDecoding(in);
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
      if(buffer.position() == 0) return -1;
      return buffer.get();
    }
  }
  
  
  public int read(byte[] bs, int offset, int length) throws IOException {
    nullarray(bs);
    range(offset, 0, bs.length -2);
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
  
  
  public long write(OutputStream out) throws IOException {
    nullarg(OutputStream.class, out);
    if(!readmode) flip();
    InputStream in = getInputStream();
    long total = transfer(in, out);
    in.close();
    return total;
  }
  
  
  public long writeEncoding(OutputStream out) throws IOException {
    nullarg(OutputStream.class, out);
    if(!readmode) flip();
    InputStream in = getInputStream();
    out = configureOutput(out);
    long total = transfer(in, out);
    in.close();
    out.close();
    return total;
  }
  
  
  public long writeDecoding(OutputStream out) throws IOException {
    nullarg(OutputStream.class, out);
    if(!readmode) flip();
    InputStream in = configureInput(getInputStream());
    long total = transfer(in, out);
    in.close();
    return total;
  }
  
  
  public long write(Path p) throws IOException {
    nullarg(Path.class, p);
    OutputStream out = Files.newOutputStream(p,
        StandardOpenOption.WRITE,
        StandardOpenOption.CREATE);
    long t = write(out);
    out.close();
    return t;
  }
  
  
  public long writeEncoding(Path p) throws IOException {
    nullarg(Path.class, p);
    OutputStream out = Files.newOutputStream(p,
        StandardOpenOption.WRITE,
        StandardOpenOption.CREATE);
    long t = writeEncoding(out);
    out.close();
    return t;
  }
  
  
  public long writeDecoding(Path p) throws IOException {
    nullarg(Path.class, p);
    OutputStream out = Files.newOutputStream(p,
        StandardOpenOption.WRITE,
        StandardOpenOption.CREATE);
    long t = writeDecoding(out);
    out.close();
    return t;
  }
  
  
  public static long transfer(InputStream is, OutputStream os) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(OutputStream.class, os);
    long total = 0;
    byte[] bs = new byte[8192];
    int read = 0;
    while((read = is.read(bs)) > 0) {
      total += read;
      os.write(bs, 0, read);
    }
    os.flush();
    return total;
  }
  
}
