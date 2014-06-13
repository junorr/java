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

import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import us.pserver.cdr.hex.HexStringCoder;
import us.pserver.http.Header;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpHexObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.RequestLine;
import us.pserver.http.StreamUtils;
import static us.pserver.http.StreamUtils.UTF8;


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
public class HttpRequestChannel1 implements Channel, HttpConst {

  private final XStream xst;
  
  private final NetConnector netconn;
  
  private final HexStringCoder coder;
  
  private final HttpBuilder builder;
  
  private Socket sock;
  
  
  /**
   * Construtor padrão que recebe <code>HttpURLConnection</code>
   * para comunicação com o servidor.
   * @param conn <code>NetConnector</code>.
   */
  public HttpRequestChannel1(NetConnector conn) {
    if(conn == null)
      throw new IllegalArgumentException(
          "Invalid NetConnector ["+ conn+ "]");
    
    netconn = conn;
    builder = new HttpBuilder();
    coder = new HexStringCoder();
    xst = new XStream();
    sock = null;
  }
  
  
  /**
   * Retorna o objeto <code>NetConnector</code>.
   * @return <code>NetConnector</code>.
   */
  public NetConnector getNetConnector() {
    return netconn;
  }
  
  
  public Socket getSocket() {
    return sock;
  }
  
  
  public HttpBuilder getHttpBuilder() {
    return builder;
  }
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private void setHeaders() {
    RequestLine req = new RequestLine(Method.POST, 
        netconn.getAddress(), netconn.getPort());
    builder.add(req).add(req.getHost());
    builder.add(
        HD_USER_AGENT, VALUE_USER_AGENT);
    builder.add(
        HD_ACCEPT_ENCODING, VALUE_ENCODING);
    builder.add(
        HD_ACCEPT, VALUE_ACCEPT);
    if(netconn.getProxyAuthorization() != null)
      builder.add(HD_PROXY_AUTHORIZATION, 
          netconn.getProxyAuthorization());
    builder.add(HD_CONTENT_TYPE, 
        VALUE_CONTENT_MULTIPART + HD_BOUNDARY + BOUNDARY);
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    if(trp == null) return;
    builder.headers().clear();
    this.setHeaders();
    
    if(sock == null)
      sock = netconn.connectSocket();
    
    InputStream input = trp.getInputStream();
    OutputStream out = sock.getOutputStream();
    
    builder.add(new HttpHexObject(trp.getWriteVersion()));
    if(input != null)
      builder.add(new HttpInputStream(input));
    
    builder.writeTo(out);
    
    String line = readLine(sock.getInputStream());
    if(line == null || !line.contains("200"))
      throw new IOException("Invalid response ["+ line+ "]");
  }
  
  
  public Header readHeader(InputStream in) {
    if(in == null) return null;
    String str = readLine(in);
    if(str.contains(":")) {
      int id = str.indexOf(":");
      return new Header(str.substring(0, id), str.substring(id+1));
    }
    return new Header(null, str);
  }
  
  
  public String readLine(InputStream in) {
    if(in == null) return null;
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      int read;
      while((read = in.read()) != -1) {
        bos.write(read);
        if(bos.toString(UTF8).contains(CRLF))
          break;
      }
      return bos.toString(UTF8);
    } 
    catch(IOException e) {
      return null;
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
    InputStream in = sock.getInputStream();
    String strp = StreamUtils.readBetween(in, 
        BOUNDARY_OBJECT_START, BOUNDARY_OBJECT_END);
    
    if(strp == null) return null;
    
    Object obj = xst.fromXML(coder.decode(strp));
    
    if(obj == null || !Transport.class
        .isAssignableFrom(obj.getClass())) 
      return null;
    
    Transport trp = (Transport) obj;
    if(StreamUtils.readUntil(in, BOUNDARY_CONTENT_START,
        StreamUtils.EOF) == BOUNDARY_CONTENT_START) {
      trp.setInputStream(in);
    }
    return trp;
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
