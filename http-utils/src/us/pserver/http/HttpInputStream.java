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

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.chk.Invoke;
import us.pserver.streams.MultiCoderBuffer;
import us.pserver.streams.StreamUtils;
import static us.pserver.streams.StreamUtils.EOF;

/**
 * Cabeçalho HTTP POST Multipart para envio de conteúdo stream.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/06/2014
 */
public class HttpInputStream extends HeaderEncryptable {
  
  /**
   * <code>
   *  Content-Type: application/octet-stream
   * </code><br>
   * Cabeçalho HTTP de conteúdo binário.
   */
  public static final Header 
      HD_CONTENT_TYPE_OCTETSTREAM = 
      new Header(HD_CONTENT_TYPE, 
          VALUE_APP_OCTETSTREAM);
  
  
  /**
   * <code>
   *  name="inputstream"
   * </code><br>
   * Nome do conteúdo stream.
   */
  public static final String 
      NAME_INPUTSTREAM = "name=\"inputstream\"";
  
  
  /**
   * <code>
   *  STATIC_SIZE = 160
   * </code><br>
   * Tamanho estático do cabeçalho sem considerar 
   * o tamanho do stream de dados.
   */
  public static final int STATIC_SIZE = 160;
  

  private InputStream input;
  
  private MultiCoderBuffer buffer;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HttpInputStream() {
    super();
    setName(getClass().getSimpleName());
    input = null;
    buffer = new MultiCoderBuffer();
  }
  
  
  /**
   * Construtor que recebe o stream de entrada 
   * a ser transmitido no cabeçalho HTTP.
   * @param is <code>InputStream</code> a ser transmitido
   * no cabeçalho HTTP.
   */
  public HttpInputStream(InputStream is) {
    this();
    input = is;
  }
  
  
  public HttpInputStream setGZipCoderEnabled(boolean enabled) {
    buffer.setGZipCoderEnabled(enabled);
    return this;
  }
  
  
  public HttpInputStream setLzmaCoderEnabled(boolean enabled) {
    buffer.setLzmaCoderEnabled(enabled);
    return this;
  }
  
  
  public HttpInputStream setBase64CoderEnabled(boolean enabled) {
    buffer.setBase64CoderEnabled(enabled);
    return this;
  }
  
  
  public HttpInputStream setHexCoderEnabled(boolean enabled) {
    buffer.setHexCoderEnabled(enabled);
    return this;
  }
  
  
  public HttpInputStream setCryptCoderEnabled(boolean enabled, CryptKey k) {
    if(enabled) {
      nullarg(CryptKey.class, k);
      key = k;
    }
    buffer.setCryptCoderEnabled(enabled, k);
    return this;
  }
  
  
  @Override
  public HttpInputStream setCryptKey(CryptKey k) {
    super.setCryptKey(k);
    if(k != null)
      this.setCryptCoderEnabled(true, k);
    return this;
  }
  
  
  public boolean isGZipCoderEnabled() {
    return buffer.isGZipCoderEnabled();
  }
  
  
  public boolean isLzmaCoderEnabled() {
    return buffer.isLzmaCoderEnabled();
  }
  
  
  public boolean isBase64CoderEnabled() {
    return buffer.isBase64CoderEnabled();
  }
  
  
  public boolean isHexCoderEnabled() {
    return buffer.isHexCoderEnabled();
  }
  
  
  public boolean isCryptCoderEnabled() {
    return buffer.isCryptCoderEnabled();
  }
  
  
  public boolean isAnyCoderEnabled() {
    return buffer.isAnyCoderEnabled();
  }
  
  
  public HttpInputStream setupOutbound() throws IOException {
    if(input == null)
      throw new IllegalStateException("InputStream not setted");
    StreamUtils.transfer(input, buffer.getOutputStream());
    buffer.encode();
    return this;
  }
  
  
  public HttpInputStream setupInbound() throws IOException {
    if(input == null)
      throw new IllegalStateException("InputStream not setted");
    OutputStream os = buffer.getOutputStream();
    StreamUtils.transferUntilOr(input, os, BOUNDARY_CONTENT_END, EOF);
    buffer.decode();
    input = buffer.getInputStream();
    return this;
  }
  
  
  /**
   * Define o stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   * @param is stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   * @return Esta instância modificada de <code>HttpInputStream</code>.
   */
  public HttpInputStream setInputStream(InputStream is) {
    if(is == null)
      throw new IllegalArgumentException(
          "Invalid InputStream ["+ is+ "]");
    input = is;
    return this;
  }
  
  
  /**
   * Retorna o stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   * @return o stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   */
  public InputStream getInputStream() {
    return input;
  }
  
  
  /**
   * Retorna o tamanho total do cabeçalho em bytes.
   * @return Tamanho total do cabeçalho em bytes
   * incluindo o tamanho do stream de entrada.
   */
  @Override
  public long getLength() {
    if(input != null && bufferSize() == 0) {
      Invoke.unchecked(this::setupOutbound);
    }
    else if(input == null) {
      return 0;
    }
    
    return STATIC_SIZE + bufferSize();
  }
  
  
  public long bufferSize() {
    return (long) Invoke.unchecked(buffer::size);
  }
  
  
  public void checkSetup() {
    if(input == null && bufferSize() == 0)
      throw new IllegalStateException(
          "HttpInputStream content not configured");
    if(bufferSize() == 0)
      Invoke.unchecked(this::setupOutbound);
  }
  
  
  /**
   * Retorna <code>true</code>.
   * @return <code>true</code>.
   */
  @Override
  public boolean isContentHeader() {
    return true;
  }
  
  
  @Override
  public void writeContent(OutputStream out) throws IOException {
    nullarg(OutputStream.class, out);
    checkSetup();
    
    StringBuffer start = new StringBuffer();
    start.append(CRLF).append(HYFENS).append(BOUNDARY)
        .append(CRLF).append(HD_CONTENT_DISPOSITION)
        .append(": ").append(VALUE_DISPOSITION_FORM_DATA)
        .append("; ").append(NAME_INPUTSTREAM)
        .append(CRLF).append(HD_CONTENT_TYPE_OCTETSTREAM)
        .append(CRLF);
    
    StringByteConverter cv = new StringByteConverter();
    out.write(cv.convert(start.toString()));
    out.write(cv.convert(BOUNDARY_XML_START));
    out.write(cv.convert(BOUNDARY_CONTENT_START));
    StreamUtils.transfer(buffer.getInputStream(), out);
    out.write(cv.convert(BOUNDARY_CONTENT_END));
    out.write(cv.convert(BOUNDARY_XML_END));
    out.flush();
  }
  
  
  public static void main(String[] args) throws IOException {
    StringBuffer start = new StringBuffer();
    start.append(CRLF).append(HYFENS).append(BOUNDARY)
        .append(CRLF).append(HD_CONTENT_DISPOSITION)
        .append(": ").append(VALUE_DISPOSITION_FORM_DATA)
        .append("; ").append(NAME_INPUTSTREAM)
        .append(CRLF).append(HD_CONTENT_TYPE_OCTETSTREAM)
        .append(CRLF);
    
    start.append(BOUNDARY_XML_START)
        .append(BOUNDARY_CONTENT_START)
        //content goes here
        .append(BOUNDARY_CONTENT_END)
        .append(BOUNDARY_XML_END);
    
    System.out.println("* static content:");
    System.out.println(start);
    System.out.println("* STATIC_SIZE = "+ start.length());
    System.out.println();
    
    
    HttpInputStream hin = new HttpInputStream();
    
    ByteArrayInputStream bin = new ByteArrayInputStream("Hello!".getBytes());
    hin.setInputStream(bin);
    /*
    InputStream in = Files.newInputStream(
        Paths.get("d:/pic.jpg"), 
        StandardOpenOption.READ);
    hin.setInputStream(in);
    */
    hin.setCryptCoderEnabled(true, new CryptKey("123456", 
        CryptAlgorithm.DESede_ECB_PKCS5));
    hin.setGZipCoderEnabled(true);
    hin.setupOutbound();
    
    System.out.println("* size = "+ hin.getLength());
    OutputStream out = new FileOutputStream("d:/http-inputstream.txt");
    hin.writeContent(System.out);
  }
  
}
