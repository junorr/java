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

package us.pserver.remote;

import java.io.IOException;
import java.net.Socket;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpCryptKey;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.RequestLine;
import us.pserver.http.ResponseLine;
import us.pserver.http.ResponseParser;
import us.pserver.streams.StreamUtils;


/**
 * Canal de transmissão de objetos através do
 * protocolo HTTP. Implementa o lado cliente
 * da comunicação, cujas requisições são efetuadas
 * utilizando o método POST. O objeto é codificado
 * em hexadecimal e transmitido no corpo
 * da requisição utilizando delimitadores no formato
 * XML.<br/><br/>
 * Canais de comunicação no protocolo HTTP
 * permanecem válidos para apenas um ciclo
 * de leitura e escrita. Após um ciclo o canal 
 * se torna inválido e deve ser fechado.
 * Novas requisições deverão ser efetuadas em
 * novas instâncias de <code>HttpRequestChannel</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class HttpRequestChannel implements Channel, HttpConst {

  public static final String 
      
      HTTP_ENCLOSED_OBJECT = 
        HttpEnclosedObject.class.getSimpleName(),
      
      HTTP_CRYPT_KEY = 
        HttpCryptKey.class.getSimpleName(),
      
      HTTP_INPUTSTREAM = 
        HttpInputStream.class.getSimpleName();
  
  
  private NetConnector netconn;
  
  private HttpBuilder builder;
  
  private Socket sock;
  
  private ResponseParser parser;
  
  private boolean crypt, gzip;
  
  private CryptAlgorithm algo;
  
  private ResponseLine resp;
  
  
  /**
   * Construtor padrão que recebe <code>NetConnector</code>
   * para comunicação com o servidor.
   * @param conn <code>NetConnector</code>.
   */
  public HttpRequestChannel(NetConnector conn) {
    if(conn == null)
      throw new IllegalArgumentException(
          "Invalid NetConnector ["+ conn+ "]");
    
    netconn = conn;
    crypt = true;
    gzip = true;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
    parser = new ResponseParser();
    sock = null;
  }
  
  
  /**
   * Retorna o objeto <code>NetConnector</code>.
   * @return <code>NetConnector</code>.
   */
  public NetConnector getNetConnector() {
    return netconn;
  }
  
  
  public HttpRequestChannel setEncryptionEnabled(boolean enabled) {
    crypt = enabled;
    return this;
  }
  
  
  public boolean isEncryptionEnabled() {
    return crypt;
  }
  
  
  public HttpRequestChannel setGZipCompressionEnabled(boolean enabled) {
    gzip = enabled;
    return this;
  }
  
  
  public boolean isGZipCompressionEnabled() {
    return gzip;
  }
  
  
  public HttpRequestChannel setCryptAlgorithm(CryptAlgorithm ca) {
    nullarg(CryptAlgorithm.class, ca);
    algo = ca;
    return this;
  }
  
  
  public CryptAlgorithm getCryptAlgorithm() {
    return algo;
  }
  
  
  public Socket getSocket() {
    return sock;
  }
  
  
  public HttpBuilder getHttpBuilder() {
    if(builder == null)
      builder = HttpBuilder.requestBuilder();
    return builder;
  }
  
  
  public ResponseParser getResponseParser() {
    return parser;
  }
  
  
  public ResponseLine getResponseLine() {
    return resp;
  }
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private void setHeaders(Transport trp) {
    if(trp == null || trp.getObject() == null) 
      throw new IllegalArgumentException(
          "Invalid Transport [trp="+ trp+ "]");
    
    RequestLine req = new RequestLine(Method.POST, 
        netconn.getAddress(), netconn.getPort());
    builder = HttpBuilder.requestBuilder(req);
    
    if(netconn.getProxyAuthorization() != null)
      builder.put(HD_PROXY_AUTHORIZATION, 
          netconn.getProxyAuthorization());
    
    CryptKey key = CryptKey.createRandomKey(algo);
    
    builder.put(new HttpCryptKey(key));
    builder.put(new HttpEnclosedObject(
        trp.getWriteVersion())
        .setCryptKey(key));
    
    if(trp.getInputStream() != null)
      builder.put(new HttpInputStream(
          trp.getInputStream())
          .setGZipCoderEnabled(true)
          .setCryptCoderEnabled(true, key));
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    this.setHeaders(trp);
    if(sock == null)
      sock = netconn.connectSocket();
    
    builder.writeContent(sock.getOutputStream());
    this.verifyResponse();
    //dump();
  }
  
  
  private void verifyResponse() throws IOException {
    readHeaders();
    resp = parser.getResponseLine();
    if(resp == null || resp.getCode() != 200) {
      //parser.headers().forEach(System.out::print);
      throw new IOException(
          "Invalid response from server: "+ resp);
    }
  }
  
  
  public void readHeaders() throws IOException {
    if(sock == null || sock.isClosed())
      return;
    parser.reset().readFrom(sock.getInputStream());
  }
  
  
  /**
   * Imprime todo o conteúdo recebido do canal
   * na saída padrão.
   * @throws IOException Caso ocorra erro na leitura
   * da transmissão.
   *
  */
  public void dump() throws IOException {
    System.out.println("--- Response ---");
    StreamUtils.transfer(sock.getInputStream(), System.out);
  }
  
  
  @Override
  public Transport read() throws IOException {
    if(!parser.containsHeader(HTTP_ENCLOSED_OBJECT))
      return null;
    
    HttpEnclosedObject hob = (HttpEnclosedObject) 
        parser.getHeader(HTTP_ENCLOSED_OBJECT);
    
    Transport tp = hob.getObjectAs();
    
    if(parser.containsHeader(HTTP_INPUTSTREAM)) {
      HttpInputStream his = (HttpInputStream) 
          parser.getHeader(HTTP_INPUTSTREAM);
      tp.setInputStream(his.setGZipCoderEnabled(true)
          .setupInbound().getInputStream());
    }
    return tp;
  }
  
  
  /**
   * Canais de comunicação no protocolo HTTP
   * permanecem válidos para apenas um ciclo
   * de leitura e escrita. Após um ciclo o canal 
   * se torna inválido e deve ser fechado.
   * Novas requisições deverão ser efetuadas em
   * novas instâncias de <code>HttpRequestChannel</code>.
   * @return <code>boolean</code>.
   */
  @Override
  public boolean isValid() {
    return sock != null && sock.isConnected() 
        && !sock.isClosed();
  }
  
  
  @Override
  public void close() {
    try { sock.close(); }
    catch(IOException e) {}
  }
  
}
