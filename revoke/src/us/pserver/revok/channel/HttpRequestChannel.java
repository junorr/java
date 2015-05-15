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

package us.pserver.revok.channel;

import us.pserver.revok.protocol.Transport;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.revok.HttpConnector;
import us.pserver.revok.http.HttpEntityFactory;
import us.pserver.revok.http.HttpEntityParser;
import us.pserver.revok.http.HttpConsts;
import us.pserver.revok.protocol.JsonSerializer;
import us.pserver.revok.protocol.ObjectSerializer;


/**
 * Canal de transmissão de objetos através do
 * protocolo HTTP. Implementa o lado cliente
 * da comunicação, cujas requisições são efetuadas
 * utilizando o método POST. O objeto é serializado
 * em json e transmitido no corpo
 * da requisição utilizando delimitadores no formato
 * XML.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.1 - 20150422
 */
public class HttpRequestChannel implements Channel {
  
  /**
   * <code>
   *  HTTP_CONN_BUFFER_SIZE = 8*1024
   * </code><br>
   * Tamanho de buffer da conexao HTTP.
   */
  public static final int HTTP_CONN_BUFFER_SIZE = 8*1024;

  
  private Socket sock;
  
  private boolean crypt, gzip;
  
  private boolean valid;
  
  private CryptAlgorithm algo;
  
  private CryptKey key;
  
  private ObjectSerializer serial;
  
  private HttpConnector netc;
  
  private DefaultBHttpClientConnection conn;
  
  private HttpResponse response;
  
  private HttpProcessor processor;
  
  private HttpCoreContext context;
  
  
  /**
   * Construtor padrão que recebe <code>HttpConnector</code>
   * para comunicação com o servidor.
   * @param conn <code>HttpConnector</code>.
   */
  public HttpRequestChannel(HttpConnector conn) {
    if(conn == null)
      throw new IllegalArgumentException(
          "Invalid NetConnector ["+ conn+ "]");
    
    netc = conn;
    crypt = true;
    gzip = true;
    sock = null;
    valid = true;
    this.conn = null;
    key = null;
    response = null;
    serial = new JsonSerializer();
    init();
  }
  
  
  public HttpRequestChannel(HttpConnector conn, ObjectSerializer serializer) {
    this(conn);
    if(serializer == null)
      serializer = new JsonSerializer();
    serial = serializer;
  }
  
  
  private void init() {
    algo = CryptAlgorithm.AES_CBC_PKCS5;
    context = HttpCoreContext.create();
    context.setTargetHost(new HttpHost(
        (netc.getAddress() == null ? "localhost" : netc.getAddress()), netc.getPort()));
    processor = HttpProcessorBuilder.create()
        .add(new RequestContent())
        .add(new RequestTargetHost())
        .add(new RequestUserAgent(HttpConsts.HD_VAL_USER_AGENT))
        .add(new RequestConnControl())
        .build();
  }
  
  
  public ObjectSerializer getObjectSerializer() {
    return serial;
  }
  
  
  public HttpRequestChannel setObjectSerializer(ObjectSerializer serializer) {
    if(serializer != null) {
      serial = serializer;
    }
    return this;
  }
  
  
  /**
   * Retorna o objeto <code>HttpConnector</code>.
   * @return <code>HttpConnector</code>.
   */
  public HttpConnector getHttpConnector() {
    return netc;
  }
  
  
  /**
   * Return the last response received from the server.
   * @return HttpResponse of the last response received from the server.
   */
  public HttpResponse getLastResponse() {
    return response;
  }
  
  
  /**
   * Enable cryptography of data transmitted on the channel.
   * The default cryptography algorithm is AES CBC PKCS5 padded.
   * @param enabled <code>true</code> for enable criptography, <code>false</code> to disable it.
   * @return This instance of HttpRequestChannel.
   */
  public HttpRequestChannel setEncryptionEnabled(boolean enabled) {
    crypt = enabled;
    return this;
  }
  
  
  /**
   * Verifies if cryptography is enalbed.
   * @return <code>true</code> if cryptography is enabled, <code>false</code> otherwise.
   */
  public boolean isEncryptionEnabled() {
    return crypt;
  }
  
  
  /**
   * Enable GZIP compression of the data transmitted on the channel.
   * @param enabled <code>true</code> for enable GZIP compression, <code>false</code> to disable it.
   * @return This instance of HttpRequestChannel.
   */
  public HttpRequestChannel setGZipCompressionEnabled(boolean enabled) {
    gzip = enabled;
    return this;
  }
  
  
  /**
   * Verifies if GZIP compression is enalbed.
   * @return <code>true</code> if GZIP compression is enabled, <code>false</code> otherwise.
   */
  public boolean isGZipCompressionEnabled() {
    return gzip;
  }
  
  
  /**
   * Define the cryptography algorithm utilized.
   * The default cryptography algorithm is AES CBC PKCS5 padded.
   * @param ca CryptAlgorithm
   * @return This instance of HttpRequestChannel
   */
  public HttpRequestChannel setCryptAlgorithm(CryptAlgorithm ca) {
    nullarg(CryptAlgorithm.class, ca);
    algo = ca;
    return this;
  }
  
  
  /**
   * Return the cryptography algorithm utilized.
   * The default cryptography algorithm is AES CBC PKCS5 padded.
   * @return The cryptography algorithm utilized.
   */
  public CryptAlgorithm getCryptAlgorithm() {
    return algo;
  }
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private HttpEntityEnclosingRequest createRequest(Transport trp) throws IOException {
    if(trp == null) return null;
    BasicHttpEntityEnclosingRequest request = 
        new BasicHttpEntityEnclosingRequest(HttpConsts.POST, netc.getURIString());
    
    HttpEntityFactory fac = HttpEntityFactory.instance(serial);
    String contenc = HttpConsts.HD_VAL_DEF_ENCODING;
    if(gzip) {
      contenc = HttpConsts.HD_VAL_GZIP_ENCODING;
      fac.enableGZipCoder();
    }
    if(crypt) {
      key = CryptKey.createRandomKey(algo);
      fac.enableCryptCoder(key);
    }
    fac.put(trp.getWriteVersion());
    if(trp.hasContentEmbedded())
      fac.put(trp.getInputStream());
    
    request.addHeader(HttpConsts.HD_CONT_ENCODING, contenc);
    request.addHeader(HttpConsts.HD_ACCEPT, HttpConsts.HD_VAL_ACCEPT);
    if(netc.getProxyAuthorization() != null) {
      request.addHeader(HttpConsts.HD_PROXY_AUTH,
          netc.getProxyAuthorization());
    }
    
    request.setEntity(fac.create());
    return request;
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    if(conn == null) {
      conn = new DefaultBHttpClientConnection(HTTP_CONN_BUFFER_SIZE);
      if(sock == null)
        sock = netc.connectSocket();
      conn.bind(sock);
    }
    try {
      HttpEntityEnclosingRequest request = createRequest(trp);
      processor.process(request, context);
      conn.sendRequestHeader(request);
      conn.sendRequestEntity(request);
      this.verifyResponse();
    }
    catch(HttpException e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  private void verifyResponse() throws IOException, HttpException {
    response = conn.receiveResponseHeader();
    if(response == null || response
        .getStatusLine().getStatusCode() != 200) {
      throw new IOException(
          "Invalid response from server: "+ response.getStatusLine());
    }
    processor.process(response, context);
    
    //System.out.println("[HttpRequestChannel.verifyResponse()] response.status="+ response.getStatusLine());
    /*
    Iterator it = response.headerIterator();
    while(it.hasNext()) {
      System.out.println("  - "+ it.next());
    }
    */
  }
  
  
  @Override
  public Transport read() throws IOException {
    if(response == null) return null;
    try {
      conn.receiveResponseEntity(response);
      HttpEntity content = response.getEntity();
      if(content == null) return null;
      HttpEntityParser par = HttpEntityParser.instance(serial);
      if(gzip) par.enableGZipCoder();
      
      par.parse(content);
      Transport t = (Transport) par.getObject();
      if(par.getInputStream() != null)
        t.setInputStream(par.getInputStream());
      return t;
    }
    catch(HttpException e) {
      throw new IOException(e.toString(), e);
    }
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
    return valid && sock != null && sock.isConnected() 
        && !sock.isClosed() && !sock.isOutputShutdown();
  }
  
  
  @Override
  public void close() {
    try {
      if(conn != null)
        conn.close();
      if(sock != null)
        sock.close(); 
    }
    catch(IOException e) {}
  }
  
}
