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
import lzma.streams.LzmaOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;
import us.pserver.cdr.hex.HexInputStream;
import us.pserver.cdr.hex.HexOutputStream;
import us.pserver.cdr.lzma.LzmaStreamFactory;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullarray;
import static us.pserver.chk.Checker.nullbuffer;
import static us.pserver.chk.Checker.nullstr;
import static us.pserver.chk.Checker.range;
import static us.pserver.chk.Checker.zero;
import static us.pserver.streams.LimitedBuffer.UTF8;

/**
 * Classe para manipular streams de entrada e saída
 * de dados, combinando capacidade de busca, codificação
 * e compactação de dados durante o processamento.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class Streams {
  
  /**
   * <code>BUFFER_SIZE = 4096</code><br>
   * Tamanho padrão do buffer interno.
   */
  public static final int BUFFER_SIZE = 4096;
  
  /**
   * <code>EOF = "EOF"</code><br>
   * <code>String</code> de representação 
   * de final de arquivo.
   */
  public static final String EOF = "EOF";
  
  /**
   * <code>ORDER_N = {0, 1, 2, 3, 4}</code><br>
   * Índice de ordenação dos tipos
   * de codificadores.
   */
  public static final int
      ORDER_0 = 0,
      ORDER_1 = 1,
      ORDER_2 = 2,
      ORDER_3 = 3,
      ORDER_4 = 4;
  
  
  private ByteBufferConverter conv;
  
  private CryptKey key;
  
  private ByteBuffer buffer, last;
  
  private GZIPOutputStream gzo;
  
  private LzmaOutputStream lzo;
  
  private CoderType[] order;
  
  private InputStream input;
  
  private OutputStream output;
  
  private InputStream rawin;
  
  private BufferOutputStream bout;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public Streams() {
    conv = new ByteBufferConverter();
    buffer = null;
    last = null;
    gzo = null;
    order = new CoderType[5];
    input = null;
    output = null;
    bout = new BufferOutputStream();
    setDefaultCoderOrder();
  }
  
  
  public Streams(InputStream is, OutputStream os, boolean encode) throws IOException {
    this();
    setInputStream(is, !encode);
    setOutputStream(os, encode);
  }
  
  
  public Streams setInputStream(InputStream is, boolean decode) throws IOException {
    nullarg(InputStream.class, is);
    rawin = is;
    if(decode) input = configureInput(is);
    else input = is;
    return this;
  }
  
  
  public Streams setOutputStream(OutputStream os, boolean encode) throws IOException {
    nullarg(OutputStream.class, os);
    bout.setOutputStream(os);
    if(encode) output = configureOutput(bout);
    else output = bout;
    return this;
  }
  
  
  public Streams resetEnclosedOutput() throws IOException {
    output.flush();
    output.close();
    output = configureOutput(bout);
    return this;
  }
  
  
  public InputStream getEnclosedInputStream() {
    return input;
  }
  
  
  public OutputStream getEnclosedOutputStream() {
    return output;
  }
  
  
  public InputStream getRawInputStream() {
    return rawin;
  }
  
  
  public BufferOutputStream getRawOutputStream() {
    return bout;
  }
  
  
  /**
   * Define a ordem padrão de processamento
   * dos codificadores:<br>
   * <pre>
   * order[0] = CoderType.CRYPT;
   * order[1] = CoderType.BASE64;
   * order[2] = CoderType.HEX;
   * order[3] = CoderType.GZIP;
   * order[4] = CoderType.LZMA;
   * </pre>
   * @return Esta instância modificada de <code>Streams</code>.
   */
  public Streams setDefaultCoderOrder() {
    order[0] = CoderType.CRYPT;
    order[1] = CoderType.BASE64;
    order[2] = CoderType.HEX;
    order[3] = CoderType.GZIP;
    order[4] = CoderType.LZMA;
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador hexadecimal.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>Streams</code>.
   */
  public Streams setHexCoderEnabled(boolean enabled) {
    CoderType.HEX.setEnabled(enabled);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador Base64.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>Streams</code>.
   */
  public Streams setBase64CoderEnabled(boolean enabled) {
    CoderType.BASE64.setEnabled(enabled);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador GZip.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>Streams</code>.
   */
  public Streams setGZipCoderEnabled(boolean enabled) {
    CoderType.GZIP.setEnabled(enabled);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador Lzma.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @return Esta instância modificada de <code>Streams</code>.
   */
  public Streams setLzmaCoderEnabled(boolean enabled) {
    CoderType.LZMA.setEnabled(enabled);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o codificador de criptografia.
   * @param enabled <code>true</code> para
   * habilitar o codificador, <code>false</code>
   * para desabilitar.
   * @param key Chave de criptografia.
   * @return Esta instância modificada de <code>Streams</code>.
   */
  public Streams setCryptCoderEnabled(boolean enabled, CryptKey key) {
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
  
  
  /**
   * Retorna a chave de criptografia.
   * @return chave de criptografia.
   */
  public CryptKey getCryptKey() {
    return key;
  }
  
  
  /**
   * Define a ordem de processamento do tipo de 
   * codificador especificado.
   * @param index Índice de classificação.
   * @param coder Tipo de codificador a ser ordenado.
   * @return Esta instância modificada de <code>Streams</code>.
   */
  public Streams setCoderOrder(int index, CoderType coder) {
    nullarg(CoderType.class, coder);
    range(index, -1, 5);
    int iold = getCoderOrder(coder);
    if(index != iold) {
      order[iold] = order[index];
      order[index] = coder;
    }
    return this;
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
  
  
  private void resetBuffers() {
    if(buffer == null)
      buffer = ByteBuffer.allocate(BUFFER_SIZE);
    if(last == null)
      last = ByteBuffer.allocate(BUFFER_SIZE);
    buffer.clear();
    last.clear();
  }
  
  
  /**
   * Encapsula o stream de saída especificado nos 
   * streams de codificação habilitados de acordo 
   * com a ordem de processamento pré definida.
   * @param os Stream de saída a ser encapsulado.
   * @return Stream de saída de codificação.
   * @throws IOException caso ocorra erro na operação.
   */
  public OutputStream configureOutput(OutputStream os) throws IOException {
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
        gzo = new GZIPOutputStream(os);
        return gzo;
      case LZMA:
        lzo = LzmaStreamFactory.createLzmaOutput(os);
        return lzo;
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
  public InputStream configureInput(InputStream is) throws IOException {
    nullarg(InputStream.class, is);
    InputStream input = is;
    for(int i = 4; i >= 0; i--) {
      if(order[i].isEnabled())
        input = parseInput(order[i], input);
    }
    return input;
  }
  
  
  /**
   * Finaliza os streams de compactação de dados,
   * caso estejam habilitados.
   * @throws IOException Caso ocorra erro na operação.
   */
  public void finishCompressorsOutput() {
    try {
      if(CoderType.GZIP.isEnabled() && gzo != null) {
        gzo.finish();
        gzo.flush();
        gzo.close();
        gzo = null;
      }
      if(CoderType.LZMA.isEnabled() && lzo != null) {
        lzo.flush();
        lzo.close();
        lzo = null;
      }
    } catch(IOException e) {}
  }
  
  
  /**
   * Finaliza os streams especificados.
   * @throws IOException Caso ocorra erro na operação.
   */
  public void finishStreams() throws IOException {
    if(output != null) {
      output.flush();
      output.close();
    }
    if(input != null) input.close();
    finishCompressorsOutput();
    bout.finish();
  }
  
  
  /**
   * Transfere os dados do stream de entrada para o 
   * stream de saída, (de)codificando os dados no 
   * processo, de acordo com os codificadores habilitados.
   * @param encode <code>true</code> para 
   * codificar os dados durante a transferência,
   * <code>false</code> para decodificar.
   * @return a quantidade de bytes processados.
   * @throws IOException Caso ocorra erro na operação.
   */
  public long transfer() throws IOException {
    long total = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    int read = 0;
    
    while((read = input.read(buf)) > 0) {
      total += read;
      output.write(buf, 0, read);
      if(read < buf.length) {
        int len = (read < 50 ? read : 50);
        String str = new String(buf, read -len, len);
        if(str.contains(EOF)) break;
      }
    }
    
    output.flush();
    return total;
  }
  
  
  /**
   * Transfere os dados do stream de entrada para o 
   * stream de saída, (de)codificando os dados no 
   * processo, até encontrar a <code>String until</code>
   * nos dados processados, ou até o final do stream.
   * @param until token de parada do processamento, a ser
   * procurada nos dados transferidos.
   * @param encode <code>true</code> para 
   * codificar os dados durante a transferência,
   * <code>false</code> para decodificar.
   * @return a quantidade de bytes processados.
   * @throws IOException Caso ocorra erro na operação.
   */
  public long transferUntil(String until) throws IOException {
    nullstr(until);
    resetBuffers();
    
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
    return total;
  }
  
  
  /**
   * Transfere os dados do stream de entrada para o 
   * stream de saída até encontrar a <code>String until</code>
   * ou a <code>String orfalse</code>
   * nos dados processados, ou até o final do stream.
   * @param is Stream de entrada.
   * @param os Stream de saída.
   * @param until primeiro token de parada do processamento, a ser
   * procurada nos dados transferidos.
   * @param orfalse segundo token de parada do processamento, a ser
   * procurada nos dados transferidos.
   * @return a quantidade de bytes processados.
   * @throws IOException Caso ocorra erro na operação.
   */
  public long transferUntilOr(String until, String orfalse) throws IOException {
    nullstr(until);
    nullstr(orfalse);
    
    resetBuffers();
    
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
    return total;
  }
  
  
  /**
   * Retrocede a posição dos buffers na quantidade 
   * indicada por <code>length</code>.
   */
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
  
  
  /**
   * Escreve o conteúdo do primeiro buffer para o segundo.
   * @param buf Buffer cujo conteúdo será transferido.
   * @param out Buffer que irá receber o conteúdo transferido.
   * @throws IOException Caso ocorra erro na operação.
   */
  public void writeBufferTo(ByteBuffer buf, ByteBuffer out) throws IOException {
    nullbuffer(buf);
    nullbuffer(out);
    
    int minsize = Math.min(buf.remaining(), out.remaining());
    byte[] bs = new byte[minsize];
    buf.get(bs);
    out.put(bs);
  }
  
  
  /**
   * Escreve o conteúdo do buffer para o stream de
   * saída especificado.
   * @param buf Buffer cujo conteúdo será transferido.
   * @param os Stream de saída onde serão escritos
   * os dados do buffer.
   * @throws IOException Caso ocorra erro na operação. 
   */
  public void writeBufferTo(ByteBuffer buf, OutputStream os) throws IOException {
    nullbuffer(buf);
    nullarg(OutputStream.class, os);
    
    byte[] bs = conv.convert(buf);
    if(bs != null) {
      os.write(bs);
      os.flush();
    }
  }
  
  
  /**
   * Transfere do stream de entrada para o 
   * stream de saída os dados que estiverem entre os
   * tokens <code>String start</code> e <code>end</code> 
   * contidos nos dados processados, descartando o restante.
   * O processamento só começa após encontrar a 
   * <code>String start</code> e termina ao encontrar 
   * a <code>String end</code> nos dados processados,
   * antes disso os dados são descartados.
   * @param start token de início do processamento, a ser
   * procurada nos dados transferidos.
   * @param end token de parada do processamento, a ser
   * procurada nos dados transferidos.
   * @return a quantidade de bytes processados.
   * @throws IOException Caso ocorra erro na operação.
   */
  public long transferBetween(String start, String end) throws IOException {
    nullstr(start);
    nullstr(end);
    
    readUntil(start);
    return transferUntil(end);
  }
  
  
  /**
   * Lê e decodifica do stream de entrada os dados 
   * que estiverem entre os tokens <code>String start</code>
   * e <code>end</code> contidos nos dados processados,
   * descartando o restante.
   * O processamento só começa após encontrar a 
   * <code>String start</code> e termina ao encontrar 
   * a <code>String end</code> nos dados processados,
   * antes disso os dados são descartados.
   * @param is Stream de entrada.
   * @param start token de início do processamento, a ser
   * procurada nos dados transferidos.
   * @param end token de parada do processamento, a ser
   * procurada nos dados transferidos.
   * @return <code>String</code> contendo os dados
   * lidos e processados.
   * @throws IOException Caso ocorra erro na operação.
   */
  public String readBetween(String start, String end) throws IOException {
    nullstr(start);
    nullstr(end);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    OutputStream oldOut = output;
    setOutputStream(bos, false);
    transferBetween(start, end);
    output = oldOut;
    return bos.toString(UTF8);
  }


  /**
   * Lê e descarta os dados do stream de entrada
   * até encontrar o token <code>String str</code>
   * contido nos dados lidos.
   * @param is Stream de entrada.
   * @param str token de parada de leitura, a ser
   * procurada nos dados transferidos.
   * @return <code>true</code> indicando que o token
   * <code>String str</code> foi encontrado nos dados
   * lidos, <code>false</code> caso contrário.
   * @throws IOException Caso ocorra erro na operação.
   */
  public boolean readUntil(String str) throws IOException {
    nullstr(str);
    
    int read = -1;
    LimitedBuffer lbuf = new LimitedBuffer(str.length());
    while((read = input.read()) != -1) {
      lbuf.put(read);
      if(str.equals(lbuf.toUTF8()))
        return true;
    }
    return false;
  }


  /**
   * Lê e descarta os dados do stream de entrada
   * até encontrar o token <code>String str</code>
   * ou o segundo token <code>orfalse</code>,
   * contidos nos dados lidos.
   * @param in Stream de entrada.
   * @param str primeiro token de parada de leitura, a ser
   * procurada nos dados transferidos.
   * @param orfalse segundo token de parada de leitura, a ser
   * procurada nos dados transferidos.
   * @return o token encontrado nos dados lidos 
   * ou <code>null</code>, caso nenhum dos tokens
   * seja encontrado.
   * @throws IOException Caso ocorra erro na operação.
   */
  public String readUntilOr(String str, String orfalse) throws IOException {
    nullstr(str);
    nullstr(orfalse);
    
    int read = -1;
    int maxlen = Math.max(str.length(), orfalse.length());
    LimitedBuffer lbuf = new LimitedBuffer(maxlen);
    while((read = input.read()) != -1) {
      lbuf.put(read);
      if(lbuf.toUTF8().contains(str))
        return str;
      if(lbuf.toUTF8().contains(orfalse))
        return orfalse;
    }
    return null;
  }
  

  /**
   * Escreve os dados do buffer para o 
   * stream de saída, codificando os dados no 
   * processo, de acordo com os codificadores habilitados.
   * @param buf conteúdo a ser escrito no stream de saída.
   * @param os Stream de saída.
   * @param encode <code>true</code> para 
   * codificar os dados na transferência,
   * <code>false</code> para que não haja codificação.
   * @throws IOException Caso ocorra erro na operação.
   */
  public void write(byte[] buf) throws IOException {
    nullarray(buf);
    output.write(buf);
    output.flush();
  }
  
  
  /**
   * Escreve os dados do buffer para o 
   * stream de saída, codificando os dados no 
   * processo, de acordo com os codificadores habilitados.
   * @param buf conteúdo a ser escrito no stream de saída.
   * @param os Stream de saída.
   * @param encode <code>true</code> para 
   * codificar os dados na transferência,
   * <code>false</code> para que não haja codificação.
   * @throws IOException Caso ocorra erro na operação.
   */
  public void write(byte[] buf, int offset, int length) throws IOException {
    nullarray(buf);
    range(offset, -1, buf.length -2);
    range(length, 1, buf.length - offset);
    output.write(buf, offset, length);
    output.flush();
  }
  
  
}
