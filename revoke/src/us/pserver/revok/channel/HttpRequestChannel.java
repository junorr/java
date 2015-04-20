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
import us.pserver.revok.http.EntityFactory;
import us.pserver.revok.http.EntityParser;
import us.pserver.revok.http.HttpConsts;
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
    init();
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
  
  
  /**
   * Retorna o objeto <code>HttpConnector</code>.
   * @return <code>HttpConnector</code>.
   */
  public HttpConnector getNetConnector() {
    return netc;
  }
  
  
  public HttpResponse getLastResponse() {
    return response;
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
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private HttpEntityEnclosingRequest createRequest(Transport trp) throws IOException {
    if(trp == null) return null;
    BasicHttpEntityEnclosingRequest request = 
        new BasicHttpEntityEnclosingRequest(HttpConsts.POST, netc.getURIString());
    
    EntityFactory fac = EntityFactory.factory();
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
    request.addHeader(HttpConsts.HD_ACCEPT, 
        HttpConsts.HD_VAL_ACCEPT);
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
    
    System.out.println("[HttpRequestChannel.verifyResponse()] response.status="+ response.getStatusLine());
    Iterator it = response.headerIterator();
    while(it.hasNext()) {
      System.out.println("  - "+ it.next());
    }
    
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
    if(response == null)
      return null;
    try {
      conn.receiveResponseEntity(response);
      HttpEntity content = response.getEntity();
      if(content == null) return null;
      EntityParser par = EntityParser.create();
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
