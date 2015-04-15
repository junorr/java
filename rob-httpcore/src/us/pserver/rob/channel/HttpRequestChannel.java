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

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.rob.NetConnector;
import us.pserver.rob.http.EntityFactory;
import us.pserver.rob.http.EntityParser;
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
public class HttpRequestChannel implements Channel {
  
  public static final int BUFFER_SIZE = 8*1024;

  
  private Socket sock;
  
  private boolean crypt, gzip;
  
  private boolean valid;
  
  private CryptAlgorithm algo;
  
  private CryptKey key;
  
  private NetConnector netc;
  
  private DefaultBHttpClientConnection conn;
  
  private HttpResponse response;
  
  
  /**
   * Construtor padrão que recebe <code>NetConnector</code>
   * para comunicação com o servidor.
   * @param conn <code>NetConnector</code>.
   */
  public HttpRequestChannel(NetConnector conn) {
    if(conn == null)
      throw new IllegalArgumentException(
          "Invalid NetConnector ["+ conn+ "]");
    
    netc = conn;
    crypt = true;
    gzip = true;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
    sock = null;
    valid = true;
    this.conn = null;
    key = null;
    response = null;
  }
  
  
  /**
   * Retorna o objeto <code>NetConnector</code>.
   * @return <code>NetConnector</code>.
   */
  public NetConnector getNetConnector() {
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
        new BasicHttpEntityEnclosingRequest("POST", netc.getURIString());
    
    EntityFactory fac = EntityFactory.factory();
    if(gzip) {
      fac.enableGZipCoder();
    }
    if(crypt) {
      key = CryptKey.createRandomKey(algo);
      fac.enableCryptCoder(key);
    }
    fac.put(trp.getWriteVersion());
    if(trp.hasContentEmbedded())
      fac.put(trp.getInputStream());
    
    request.addHeader(HttpConsts.HD_USER_AGENT, 
        HttpConsts.VAL_USER_AGENT);
    request.addHeader(HttpConsts.HD_ENCODING, 
        HttpConsts.VAL_ENCODING);
    request.addHeader(HttpConsts.HD_ACCEPT, 
        HttpConsts.VAL_ACCEPT);
    request.addHeader(HttpConsts.HD_CONNECTION, 
        HttpConsts.VAL_CONNECTION);
    request.setEntity(fac.create());
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
      HttpEntityEnclosingRequest request = createRequest(trp);
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
