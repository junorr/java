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

package us.pserver.rob.channel;

import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptByteCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.gzip.GZipByteCoder;
import us.pserver.chk.Checker;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.rob.MethodChain;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import static us.pserver.rob.channel.HttpRequestChannel.BUFFER_SIZE;
import us.pserver.rob.http.EntityParser;
import us.pserver.rob.http.GetRequest;
import us.pserver.rob.http.GetRequestFactory;
import us.pserver.rob.http.HttpConsts;
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
public class GetRequestChannel implements Channel {

  private NetConnector netc;
  
  private Socket sock;
  
  private GetRequestFactory getfac;
  
  private boolean valid;
  
  private StatusLine resp;
  
  private DefaultBHttpClientConnection conn;

  private HttpResponse response;
  
  private HttpProcessor processor;
  
  private HttpCoreContext context;
  
  /**
   * Construtor padrão que recebe <code>NetConnector</code>
   * para comunicação com o servidor.
   * @param conn <code>NetConnector</code>.
   */
  public GetRequestChannel(NetConnector conn) {
    if(conn == null)
      throw new IllegalArgumentException(
          "Invalid NetConnector ["+ conn+ "]");
    
    netc = conn;
    this.conn = null;
    valid = true;
    sock = null;
    init();
  }
  
  
  private void init() {
    getfac = new GetRequestFactory(netc.getURIString());
    getfac.setCryptAlgorithm(CryptAlgorithm.AES_CBC_PKCS5);
    context = HttpCoreContext.create();
    context.setTargetHost(new HttpHost(netc.getAddress(), netc.getPort()));
    processor = HttpProcessorBuilder.create()
        .add(new RequestContent())
        .add(new RequestTargetHost())
        .add(new RequestUserAgent(HttpConsts.HD_VAL_USER_AGENT))
        .add(new RequestConnControl())
        .build();
  }
  
  
  /**
   * Retorna o objeto <code>NetConnector</code>.
   * @return <code>NetConnector</code>.
   */
  public NetConnector getNetConnector() {
    return netc;
  }
  
  
  public GetRequestChannel setEncryptionEnabled(boolean enabled) {
    getfac.setEncryptionEnabled(enabled);
    return this;
  }
  
  
  public boolean isEncryptionEnabled() {
    return getfac.isEncryptionEnabled();
  }
  
  
  public GetRequestChannel setGZipCompressionEnabled(boolean enabled) {
    getfac.setGZipCompressionEnabled(enabled);
    return this;
  }
  
  
  public boolean isGZipCompressionEnabled() {
    return getfac.isGZipCompressionEnabled();
  }
  
  
  public GetRequestChannel setCryptAlgorithm(CryptAlgorithm ca) {
    getfac.setCryptAlgorithm(ca);
    return this;
  }
  
  
  public CryptAlgorithm getCryptAlgorithm() {
    return getfac.getCryptAlgorithm();
  }
  
  
  public DefaultBHttpClientConnection getHttpClientConnection() {
    return conn;
  }
  
  
  public StatusLine getStatusLine() {
    return resp;
  }
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private HttpRequest createRequest(Transport trp) throws IOException {
    if(trp == null || trp.getObject() == null) 
      throw new IllegalArgumentException(
          "Invalid Transport [trp="+ trp+ "]");
    
    GetRequest get;
    if(trp.isObjectFromType(RemoteMethod.class)) {
      get = getfac.create((RemoteMethod) trp.getObject());
    }
    else if(trp.isObjectFromType(MethodChain.class)) {
      get = getfac.create((MethodChain) trp.getObject());
    }
    else {
      throw new IllegalArgumentException(
          "[GetRequestChannel.createRequest( Transport )] "
              + "Invalid object type for Transport {trp.object="+ trp.getObject()+ "}");
    }
    
    BasicHttpRequest request = new BasicHttpRequest(get);
    
    request.addHeader(HttpConsts.HD_ACCEPT, 
        HttpConsts.HD_VAL_ACCEPT);
    if(netc.getProxyAuthorization() != null) {
      request.addHeader(HttpConsts.HD_PROXY_AUTH,
          netc.getProxyAuthorization());
    }
    return request;
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    if(conn == null) {
      conn = new DefaultBHttpClientConnection(BUFFER_SIZE);
      if(sock == null)
        sock = netc.connectSocket();
      conn.bind(sock);
    }
    try {
      HttpRequest request = createRequest(trp);
      processor.process(request, context);
      conn.sendRequestHeader(request);
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
      throw new IOException("[GetRequestChannel.verifyResponse()] "
          + "Invalid response from server: "+ response.getStatusLine());
    }
    processor.process(response, context);
    
    System.out.println("[GetRequestChannel.verifyResponse()] response.status="+ response.getStatusLine());
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
    if(conn == null || !conn.isOpen())
      return null;
    try {
      HttpRequest basereq = conn.receiveRequestHeader();
      if(basereq == null 
          || !HttpEntityEnclosingRequest.class
              .isAssignableFrom(basereq.getClass())) {
        throw new IOException("[HttpResponseChannel.read()] "
            + "Invalid HttpRequest without content received");
      }
      
      HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) basereq;
      conn.receiveRequestEntity(request);
      HttpEntity content = request.getEntity();
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
      if(sock != null) sock.close(); 
    }
    catch(IOException e) {}
  }
  
  
  public static void main(String[] args) throws IOException {
    GetRequest get = new GetRequest();
    MethodChain chain = new MethodChain();
    chain.add(new RemoteMethod()
          .forObject("a")
          .method("compute")
          .types(int.class, int.class)
          .params(5, 3))
        .add(new RemoteMethod()
          .method("info"))
        .add(new RemoteMethod()
          .method("round")
          .types(double.class, int.class)
          .params(1.66666667, 2));
    System.out.println("* chain: "+ chain);
    new GetRequestChannel().encloseChain(chain, get, new JsonObjectConverter());
    System.out.println(get);
    
    GetResponseChannel rch = new GetResponseChannel();
    chain = rch.createChain(get);
    System.out.println("* chain: "+ chain);
  }
  
}
