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
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.revok.http.HttpEntityFactory;
import us.pserver.revok.http.HttpEntityParser;
import us.pserver.revok.http.HttpConsts;
import us.pserver.revok.protocol.JsonSerializer;
import us.pserver.revok.protocol.ObjectSerializer;


/**
 * Canal de transmissão de objetos através do
 * protocolo HTTP. Implementa o lado servidor
 * da comunicação. O objeto é codificado
 * em hexadecimal e transmitido no corpo
 * da resposta utilizando delimitadores no formato
 * XML.<br/><br/>
 * Canais de comunicação no protocolo HTTP
 * permanecem válidos para apenas um ciclo
 * de leitura e escrita. Após um ciclo o canal 
 * se torna inválido e deve ser fechado.
 * Novas requisições deverão ser efetuadas em
 * novas instâncias de <code>HttpResponseChannel</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class HttpResponseChannel implements Channel {
  
  private CryptKey key;
  
  private boolean valid;
  
  private boolean gzip;
  
  private HttpServerConnection conn;
  
  private HttpProcessor processor;
  
  private HttpCoreContext context;
  
  private ObjectSerializer serial;
  
  
  /**
   * Construtor padrão, recebe um <code>Socket</code>
   * para comunicação na rede.
   * @param hsc HttpServerConnection.
   */
  public HttpResponseChannel(HttpServerConnection hsc) {
    if(hsc == null || !hsc.isOpen())
      throw new IllegalArgumentException(
          "[HttpResponseChannel( HttpServerConnection )] "
          + "Invalid Connection {"+ hsc+ "}");
    
    conn = hsc;
    key = null;
    valid = true;
    gzip = true;
    serial = new JsonSerializer();
    init();
  }
  
  
  public HttpResponseChannel(HttpServerConnection hsc, ObjectSerializer os) {
    this(hsc);
    if(os == null) os = new JsonSerializer();
    serial = os;
  }
  
  
  private void init() {
    context = HttpCoreContext.create();
    processor = HttpProcessorBuilder.create()
        .add(new ResponseServer(HttpConsts.HD_VAL_SERVER))
        .add(new ResponseDate())
        .add(new ResponseContent())
        .add(new ResponseConnControl())
        .build();
  }
  
  
  public ObjectSerializer getObjectSerializer() {
    return serial;
  }
  
  
  public HttpResponseChannel setObjectSerializer(ObjectSerializer serializer) {
    if(serializer != null) {
      serial = serializer;
    }
    return this;
  }
  
  
  public HttpServerConnection getHttpConnection() {
    return conn;
  }
  
  
  public HttpResponseChannel setGZipCompressionEnabled(boolean bool) {
    gzip = bool;
    return this;
  }
  
  
  public boolean isGZipCompressionEnabled() {
    return gzip;
  }
  
  
  public CryptKey getCryptKey() {
    return key;
  }
  
  
  private HttpResponse createResponse(Transport trp) throws IOException {
    if(trp == null) return null;
    HttpResponse response = new BasicHttpResponse(
        HttpVersion.HTTP_1_1, 
        HttpConsts.STATUS_200, 
        HttpConsts.STATUS_OK);
    
    String contenc = HttpConsts.HD_VAL_DEF_ENCODING;
    if(gzip) contenc = HttpConsts.HD_VAL_GZIP_ENCODING;
    response.addHeader(HttpConsts.HD_CONT_ENCODING, contenc);
    
    HttpEntityFactory fac = HttpEntityFactory.instance(serial);
    if(gzip) fac.enableGZipCoder();
    if(key != null) fac.enableCryptCoder(key);
    fac.put(trp.getWriteVersion());
    if(trp.getInputStream() != null) {
      fac.put(trp.getInputStream());
    }
    response.setEntity(fac.create());
    return response;
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    HttpResponse response = createResponse(trp);
    if(response == null) return;
    try {
      processor.process(response, context);
      conn.sendResponseHeader(response);
      conn.sendResponseEntity(response);
      conn.flush();
    }
    catch(HttpException e) {
      throw new IOException(e.toString(), e);
    }
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
      
      HttpEntityParser par = HttpEntityParser.instance(serial);
      if(gzip) par.enableGZipCoder();
      par.parse(content);
      key = par.getCryptKey();
      Transport t = (Transport) par.getObject();
      if(par.getInputStream() != null)
        t.setInputStream(par.getInputStream());
      return t;
    }
    catch(ConnectionClosedException e) {
      return null;
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
   * novas instâncias de <code>HttpResponseChannel</code>.
   * @return <code>boolean</code>.
   */
  @Override
  public boolean isValid() {
    return valid;
  }
  
  
  @Override
  public void close() {
    try {
      if(conn != null) {
        conn.close();
      }
    } catch(IOException e) {}
  }
  
}
