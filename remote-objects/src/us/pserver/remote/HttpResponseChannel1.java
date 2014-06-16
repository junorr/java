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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import us.pserver.cdr.hex.HexStringCoder;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpHexObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.RequestParser;
import us.pserver.http.ResponseLine;
import us.pserver.http.StreamUtils;
import static us.pserver.http.StreamUtils.bytes;


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
public class HttpResponseChannel1 implements Channel, HttpConst {
  
  private final XStream xst;
  
  private final Socket sock;
  
  private final HexStringCoder coder;
  
  private HttpBuilder builder;
  
  private RequestParser parser;
  
  
  /**
   * Construtor padrão, recebe um <code>Socket</code>
   * para comunicação na rede.
   * @param sc <code>Socket</code>, possivelmente obtido
   * através do método <code>ServerSocket.accept() : Socket</code>.
   */
  public HttpResponseChannel1(Socket sc) {
    if(sc == null || sc.isClosed())
      throw new IllegalArgumentException(
          "Invalid Socket ["+ sc+ "]");
    
    sock = sc;
    coder = new HexStringCoder();
    xst = new XStream();
    parser = new RequestParser();
    builder = new HttpBuilder();
  }
  
  
  public Socket getSocket() {
    return sock;
  }
  
  
  public HttpBuilder getHttpBuilder() {
    return builder;
  }
  
  
  public RequestParser getRequestParser() {
    return parser;
  }
  
  
  /**
   * Define alguns cabeçalhos HTTP da resposta,
   * como tipo de servidor, conteúdo, codificação,
   * data e tamanho da mensagem.
   * @param trp <code>Transport</code>
   * @return <code>HttpBuilder</code>.
   * @throws IOException Caso ocorra erro
   * construindo os cabeçalhos.
   * @see us.pserver.remote.Transport
   * @see us.pserver.remote.http.HttpBuilder
   */
  private void setHeaders(Transport trp) throws IOException {
    if(trp == null || trp.getObject() == null) 
      throw new IllegalArgumentException(
          "Invalid Transport [trp="+ trp+ "]");
    
    builder = HttpBuilder.responseBuilder(
        new ResponseLine(200, VALUE_OK))
        .put(new HttpHexObject(trp.getWriteVersion()));
    
    if(trp.getInputStream() != null)
      builder.put(new HttpInputStream(
          trp.getInputStream()));
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    this.setHeaders(trp);
    
    OutputStream out = sock.getOutputStream();
    builder.writeTo(out);
    if(trp.hasContentEmbedded())
      trp.getInputStream().close();
    sock.shutdownOutput();
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
    if(StreamUtils.readUntil(in, VALUE_APP_OCTETSTREAM,
        StreamUtils.EOF) == VALUE_APP_OCTETSTREAM) {
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
   * novas instâncias de <code>HttpResponseChannel</code>.
   * @return <code>boolean</code>.
   */
  @Override
  public boolean isValid() {
    return sock != null && sock.isConnected() 
        && !sock.isClosed();
  }
  
  
  @Override
  public void close() {
    try {
      sock.shutdownInput();
      sock.shutdownOutput();
      sock.close();
    } catch(IOException e) {}
  }
  
}
