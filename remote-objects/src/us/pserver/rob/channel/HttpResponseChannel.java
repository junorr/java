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
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpCryptKey;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.RequestParser;
import us.pserver.http.ResponseLine;
import static us.pserver.rob.channel.HttpRequestChannel.HTTP_ENCLOSED_OBJECT;
import static us.pserver.rob.channel.HttpRequestChannel.HTTP_INPUTSTREAM;


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
public class HttpResponseChannel implements Channel, HttpConst {
  
  private final Socket sock;
  
  private HttpBuilder builder;
  
  private RequestParser parser;
  
  private CryptKey key;
  
  private boolean valid;
  
  
  /**
   * Construtor padrão, recebe um <code>Socket</code>
   * para comunicação na rede.
   * @param sc <code>Socket</code>, possivelmente obtido
   * através do método <code>ServerSocket.accept() : Socket</code>.
   */
  public HttpResponseChannel(Socket sc) {
    if(sc == null || sc.isClosed())
      throw new IllegalArgumentException(
          "Invalid Socket ["+ sc+ "]");
    
    sock = sc;
    key = null;
    valid = true;
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
  
  
  public CryptKey getCryptKey() {
    return key;
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
        new ResponseLine(200, "OK"));
    
    HttpEnclosedObject hob = new HttpEnclosedObject(
        trp.getWriteVersion());
    
    HttpInputStream his = (trp.getInputStream() != null 
        ? new HttpInputStream(trp.getInputStream())
            .setGZipCoderEnabled(true) : null);
    
    if(key != null) {
      builder.put(new HttpCryptKey(key));
      hob.setCryptKey(key);
      if(his != null) 
        his.setCryptCoderEnabled(true, key);
    }
    
    builder.put(hob).put(his);
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    this.setHeaders(trp);
    builder.writeContent(sock.getOutputStream());
    sock.getOutputStream().flush();
    valid = false;
  }
  
  
  @Override
  public Transport read() throws IOException {
    parser.reset().parseInput(sock.getInputStream());
    key = parser.getCryptKey();
    if(!parser.containsHeader(HTTP_ENCLOSED_OBJECT))
      return null;
    
    HttpEnclosedObject hob = (HttpEnclosedObject)
        parser.getHeader(HTTP_ENCLOSED_OBJECT);
    
    Transport tp = hob.getObjectAs();
    
    if(parser.containsHeader(HTTP_INPUTSTREAM)) {
      HttpInputStream his = (HttpInputStream)
          parser.getHeader(HTTP_INPUTSTREAM);
      //crypt coder already enabled by HttpParser
      his.setGZipCoderEnabled(true);
      tp.setInputStream(his.setupInbound()
          .getInputStream());
    }
    return tp;
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
      sock.shutdownInput();
      sock.shutdownOutput();
      sock.close();
    } catch(IOException e) {}
  }
  
}
