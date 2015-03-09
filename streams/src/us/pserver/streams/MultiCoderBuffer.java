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
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
 * Buffer misto memória(500KB)/disco
 * com codificadores de conteúdo disponíveis.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/07/2014
 */
public class MultiCoderBuffer {

  /**
   * <code>
   *  LARGE_BUFFER = 524_288; // 500KB
   * </code><br>
   * Tamanho padrão do buffer em memória.
   */
  public static final int LARGE_BUFFER = 524_288; // 500KB
  
  
  private Path temp;
  
  private ByteArrayOutputStream outbuffer;
  
  private ByteArrayInputStream inbuffer;
  
  private RandomAccessFile channel;
  
  private boolean readmode;
  
  private List<CoderType> coders;
  
  private CryptKey key;
  
  private int index, limit;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public MultiCoderBuffer() {
    this(LARGE_BUFFER);
  }
  
  
  /**
   * Construtor que recebe o tamanho do buffer de memória.
   * @param bufferSize Tamanho do buffer de memória.
   */
  public MultiCoderBuffer(int bufferSize) {
    range(bufferSize, 1, Integer.MAX_VALUE);
    readmode = false;
    key = null;
    index = 0;
    limit = bufferSize;
    coders = new LinkedList<>();
    outbuffer = new ByteArrayOutputStream(bufferSize);
  }
  
  
  /**
   * Tamanho disponível em memória.
   */
  private long remaining() {
    return limit - outbuffer.size();
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
      coders.add(cdr);
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
      coders.add(cdr);
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
      coders.add(cdr);
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
      coders.add(cdr);
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
      coders.add(cdr);
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
  
  
  /**
   * Verifica se algum codificador de conteúdo 
   * encontra-se habilitado.
   * @return <code>true</code> se algum codificador
   * de conteúdo estiver habilitado, <code>false</code>
   * caso contrário.
   */
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
    Iterator<CoderType> set = coders.iterator();
    int count = 0;
    while(set.hasNext()) {
      if(coder == set.next())
        return count;
      count++;
    }
    return -1;
  }

  
  /**
   * Verifica se o tipo de codificador está
   * habilitado.
   * @param ct Tipo de codificador.
   * @return <code>true</code> se o tipo de
   * codificador informado estiver habilitado,
   * <code>false</code> caso contrário.
   */
  public boolean containsCoder(CoderType ct) {
    return coders.contains(ct);
  }
  
  
  /**
   * Retorna a capacidade máxima do buffer de memória em bytes.
   * @return capacidade máxima do buffer de memória em bytes.
   */
  public int getMemBufferCapacity() {
    return limit;
  }
  
  
  /**
   * Retorna o tamanho total utilizado do buffer 
   * (memória + disco) em bytes.
   * @return tamanho total utilizado do buffer 
   * (memória + disco) em bytes.
   * @throws IOException Caso ocorra erro ao recuperar
   * o tamanho total.
   */
  public long size() throws IOException {
    long len = outbuffer.size();
    if(channel != null)
      len += channel.getFilePointer();
    return len;
  }
  
  
  /**
   * Desabilita todos os codificadores de conteúdo.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   */
  public MultiCoderBuffer clearCoders() {
    if(coders.isEmpty()) return this;
    Iterator<CoderType> set = coders.iterator();
    while(set.hasNext()) {
      set.next().setEnabled(false);
    }
    coders.clear();
    return this;
  }
  
  
  /**
   * Descarta o conteúdo e reinicia o buffer.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   * @throws IOException Caso ocorra erro.
   */
  public MultiCoderBuffer reset() throws IOException {
    close();
    outbuffer = new ByteArrayOutputStream(limit);
    channel = null;
    temp = null;
    readmode = false;
    return clearCoders();
  }
  
  
  /**
   * Retorna o caminho de arquivo temporário
   * alocado para o buffer, se existir.
   * @return <code>Path</code>.
   */
  public Path getTemp() {
    return temp;
  }
  
  
  /**
   * Inicia o canal de arquivo temporário.
   */
  private void initChannel() throws IOException {
    temp = Files.createTempFile(null, null);
    channel = new RandomAccessFile(temp.toFile(), "rwd");
  }
  
  
  /**
   * Cria um <code>InputStream</code> com o coteúdo do buffer.
   */
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
  
  
  /**
   * Cria um <code>OutputStream</code> para escrever no buffer.
   */
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
  

  /**
   * Força escrita do conteúdo da memória para um 
   * arquivo temporário.
   * @throws IOException Caso ocorra erro.
   */
  public void flush() throws IOException {
    if(outbuffer.size() > 0) {
      if(channel == null)
        initChannel();
      channel.write(outbuffer.toByteArray());
      outbuffer.reset();
    }
  }
  
  
  /**
   * Retorna um <code>InputStream</code> com o coteúdo do buffer.
   * @return <code>InputStream</code> com o coteúdo do buffer.
   */
  public InputStream getInputStream() {
    return createInputStream();
  }
  
  
  /**
   * Retorna um <code>OutputStream</code> para escrever no buffer.
   * @return <code>OutputStream</code> para escrever no buffer.
   */
  public OutputStream getOutputStream() {
    return createOutputStream();
  }
  
  
  /**
   * Alterna o modo do buffer entre escrita (default) e leitura.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   * @throws IOException Caso ocorra erro.
   */
  public MultiCoderBuffer flip() throws IOException {
    if(!readmode) {
      if(channel != null) {
        flush();
        channel.seek(0);
        outbuffer.reset();
      } else if(outbuffer.size() > 0) {
        inbuffer = new ByteArrayInputStream(outbuffer.toByteArray());
        outbuffer = null;
      }
      readmode = true;
    }
    else {
      if(channel != null) {
        channel.seek(channel.length());
        outbuffer.reset();
      } else {
        inbuffer.reset();
        outbuffer = new ByteArrayOutputStream(limit);
        StreamUtils.transfer(inbuffer, outbuffer);
      }
      inbuffer = null;
      readmode = false;
    }
    return this;
  }
  
  
  /**
   * Fecha o buffer e descarta todo o conteúdo.
   * @throws IOException Caso ocorra erro.
   */
  public void close() throws IOException {
    //System.out.println("MultiCoderBuffer.close()");
    //print(Thread.currentThread().getStackTrace());
    if(channel != null) {
      channel.close();
      Files.deleteIfExists(temp);
    }
    outbuffer = null;
    inbuffer = null;
  }
  
  
  private void print(StackTraceElement[] stack) {
    System.out.println("* StackTrace: "+ stack.length+ " {");
    for(StackTraceElement el : stack) {
      System.out.println("  - "+ el.getLineNumber()+ ": "+ el.getClassName()+ "."+ el.getMethodName());
    }
    System.out.println("}");
  }
  
  
  /**
   * Redefine a posição de leitura do buffer para o início do conteúdo.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   * @throws IOException Caso ocorra erro.
   */
  public MultiCoderBuffer rewind() throws IOException {
    if(readmode) {
      if(channel != null)
        channel.seek(0);
      else
        inbuffer.reset();
    }
    return this;
  }
  
  
  /**
   * Cria up <code>OutputStream</code> apropriado 
   * para o tipo de codificador informado.
   */
  private OutputStream createOutput(CoderType coder, OutputStream os) throws IOException {
    nullarg(CoderType.class, coder);
    nullarg(OutputStream.class, os);
    
    //System.out.println("* Encoder enabled: ["+ coder.name()+ "]");
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
  
  
  /**
   * Cria up <code>InputStream</code> apropriado 
   * para o tipo de codificador informado.
   */
  private InputStream createInput(CoderType coder, InputStream is) throws IOException {
    nullarg(CoderType.class, coder);
    nullarg(InputStream.class, is);
    
    //System.out.println("* Decoder enabled: ["+ coder.name()+ "]");
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
   * Codifica o conteúdo do buffer de acordo 
   * com os codificadores habilitados.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   * @throws IOException Caso ocorra erro.
   */
  public MultiCoderBuffer encode() throws IOException {
    if(coders.isEmpty() || size() == 0) return this;
    Iterator<CoderType> it = coders.iterator();
    while(it.hasNext()) {
      if(!readmode) flip();
      CoderType cd = it.next();
      MultiCoderBuffer buf = new MultiCoderBuffer();
      OutputStream os = buf.getOutputStream();
      os = createOutput(cd, os);
      InputStream is = this.getInputStream();
      long l = IO.tr(is, os);
      System.out.println("MultiCoderBuffer.encode["+ cd+ "]="+ l);
      os.close();
      if(this.channel != null) {
        channel.close();
        Files.deleteIfExists(temp);
      }
      outbuffer = buf.outbuffer;
      channel = buf.channel;
      inbuffer = buf.inbuffer;
      temp = buf.temp;
      readmode = false;
    }
    return this;
  }
  
  
  /**
   * Decodifica o conteúdo do buffer de acordo 
   * com os codificadores habilitados.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   * @throws IOException Caso ocorra erro.
   */
  public MultiCoderBuffer decode() throws IOException {
    if(coders.isEmpty() || size() == 0) return this;
    Iterator<CoderType> it = coders.iterator();
    while(it.hasNext()) {
      if(!readmode) flip();
      CoderType cd = it.next();
      MultiCoderBuffer buf = new MultiCoderBuffer();
      OutputStream os = buf.getOutputStream();
      InputStream is = createInput(cd, this.getInputStream());
      long l = IO.tr(is, os);
      System.out.println("MultiCoderBuffer.decode["+ cd+ "]="+ l);
      os.close();
      if(this.channel != null) {
        channel.close();
        Files.deleteIfExists(temp);
      }
      outbuffer = buf.outbuffer;
      channel = buf.channel;
      inbuffer = buf.inbuffer;
      temp = buf.temp;
      readmode = buf.readmode;
    }
    return this;
  }
  
  
  /**
   * Escreve um byte no buffer.
   * @param b byte.
   * @throws IOException Caso ocorra erro na escrita.
   */
  public void write(int b) throws IOException {
    if(readmode) flip();
    if(remaining() == 0) flush();
    outbuffer.write(b);
  }
  
  
  /**
   * Escreve o conteúdo do byte array no buffer.
   * @param bs byte array.
   * @param offset Índice de início no byte array.
   * @param length Tamanho do conteúdo no byte array.
   * @throws IOException Caso ocorra erro na escrita.
   */
  public void write(byte[] bs, int offset, int length) throws IOException {
    if(bs == null) return;
    if(bs.length == 0) {
      outbuffer.write(bs);
      return;
    }
    range(offset, -1, bs.length);
    range(length, 1, bs.length - offset);
    if(readmode) flip();
    for(int i = offset; i < length; i++) {
      write(bs[i]);
    }
  }
  
  
  /**
   * Escreve o conteúdo do byte array no buffer.
   * @param bs byte array.
   * @throws IOException Caso ocorra erro na escrita.
   */
  public void write(byte[] bs) throws IOException {
    write(bs, 0, bs.length);
  }
  
  
  /**
   * Escreve o conteúdo do <code>ByteBuffer</code> no buffer.
   * @param buf <code>ByteBuffer</code>.
   * @throws IOException Caso ocorra erro na escrita.
   */
  public void write(ByteBuffer buf) throws IOException {
    nullbuffer(buf);
    if(readmode) flip();
    while(buf.remaining() > 0) {
      write(buf.get());
    }
  }
  
  
  /**
   * Lê um byte do buffer.
   * @return byte lido.
   * @throws IOException Caso ocorra erro na leitura.
   */
  public int read() throws IOException {
    //System.out.println("* remining() = "+ remaining());
    if(!readmode) flip();
    if(channel != null) {
      flush();
      return channel.read();
    }
    else if(inbuffer != null) {
      return inbuffer.read();
    }
    else {
      return -1;
    }
  }
  
  
  /**
   * Le o conteúdo do buffer no byte array informado.
   * @param bs byte array.
   * @param offset Índice de início no byte array.
   * @param length Tamanho do conteúdo no byte array.
   * @return O número de bytes lidos.
   * @throws IOException Caso ocorra erro na leitura.
   */
  public int read(byte[] bs, int offset, int length) throws IOException {
    nullarray(bs);
    range(offset, -1, bs.length);
    range(length, 1, bs.length - offset);
    //System.out.println("* remining() = "+ remaining());
    if(!readmode) flip();
    if(channel != null) {
      flush();
      return channel.read(bs, offset, length);
    }
    else if(inbuffer != null) {
      return inbuffer.read(bs, offset, length);
    } 
    else {
      return -1;
    }
  }
  
  
  /**
   * Le o conteúdo do buffer no byte array informado.
   * @param bs byte array.
   * @return O número de bytes lidos.
   * @throws IOException Caso ocorra erro na leitura.
   */
  public int read(byte[] bs) throws IOException {
    nullarray(bs);
    return read(bs, 0, bs.length);
  }
  
  
  /**
   * Le o conteúdo do buffer no <code>ByteBuffer</code> informado.
   * @param buf <code>ByteBuffer</code>.
   * @return O número de bytes lidos.
   * @throws IOException Caso ocorra erro na leitura.
   */
  public int read(ByteBuffer buf) throws IOException {
    nullbuffer(buf);
    if(!readmode) flip();
    byte[] bs = new byte[buf.limit()];
    int read = read(bs);
    if(read <= 0) return read;
    buf.put(bs, 0, read);
    return read;
  }
  
  
  /**
   * Salva (escreve) o conteúdo do buffer no arquivo 
   * especificado pelo caminho informado.
   * @param p Caminho do arquivo onde será salvo o 
   * conteúdo do buffer.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   * @throws IOException Caso ocorra erro.
   */
  public MultiCoderBuffer save(Path p) throws IOException {
    nullarg(Path.class, p);
    if(!readmode) flip();
    OutputStream os = Files.newOutputStream(p, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.WRITE);
    InputStream is = this.getInputStream();
    StreamUtils.transfer(is, os);
    os.flush();
    os.close();
    is.close();
    return this;
  }
  
  
  /**
   * Carrega o conteúdo do arquivo especificado
   * para o buffer.
   * @param p Caminho do arquivo do qual será carregado o conteúdo.
   * @return Esta instância modificada de <code>MultiCoderBuffer</code>.
   * @throws IOException Caso ocorra erro.
   */
  public MultiCoderBuffer load(Path p) throws IOException {
    nullarg(Path.class, p);
    reset();
    InputStream is = Files.newInputStream(p, 
        StandardOpenOption.READ);
    OutputStream os = this.getOutputStream();
    StreamUtils.transfer(is, os);
    os.flush();
    os.close();
    is.close();
    return this;
  }
  
}
