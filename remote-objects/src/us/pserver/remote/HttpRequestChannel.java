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
import java.net.HttpURLConnection;
import us.pserver.cdr.hex.HexStringCoder;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.HttpInputStream;
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

  private final XStream xst;
  
  private final HttpURLConnection conn;
  
  private final HexStringCoder coder;
  
  private boolean isvalid;
  
  
  /**
   * Construtor padrão que recebe <code>HttpURLConnection</code>
   * para comunicação com o servidor.
   * @param huc <code>HttpURLConnection</code>.
   */
  public HttpRequestChannel(HttpURLConnection huc) {
    if(huc == null)
      throw new IllegalArgumentException(
          "Invalid HttpURLConnection ["+ huc+ "]");
    
    conn = huc;
    coder = new HexStringCoder();
    xst = new XStream();
    isvalid = true;
  }
  
  
  /**
   * Retorna o objeto <code>HttpURLConnection</code>.
   * @return <code>HttpURLConnection</code>.
   */
  public HttpURLConnection getHttpConnection() {
    return conn;
  }
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private void setHeaders() {
    conn.setRequestProperty(
        HD_USER_AGENT, VALUE_USER_AGENT);
    conn.setRequestProperty(
        HD_ACCEPT_ENCODING, VALUE_ENCODING);
    conn.setRequestProperty(
        HD_ACCEPT, VALUE_ACCEPT);
    conn.setRequestProperty(
        HD_CONTENT_TYPE, VALUE_CONTENT_MULTIPART + HD_BOUNDARY + BOUNDARY);
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    if(trp == null) return;
    
    this.setHeaders();
    InputStream input = trp.getInputStream();
    OutputStream out = conn.getOutputStream();
    
    HttpBuilder build = new HttpBuilder()
        .put(new HttpEnclosedObject(trp.getWriteVersion()));
    if(input != null)
      build.put(new HttpInputStream(input));
    build.writeTo(out);
    
    isvalid = false;
    
    if(conn.getResponseCode() != 200)
      throw new IOException("Invalid response code ["
          + conn.getResponseCode()+ "] "
          + conn.getResponseMessage());
  }
  
  
  /**
   * Imprime todo o conteúdo recebido do canal
   * na saída padrão.
   * @throws IOException Caso ocorra erro na leitura
   * da transmissão.
   *
  */
  public void dump() throws IOException {
    System.out.println("Response: "+ conn.getResponseCode()
        + " "+ conn.getResponseMessage());
    StreamUtils.transfer(conn.getInputStream(), System.out);
  }
  
  
  @Override
  public Transport read() throws IOException {
    InputStream in = conn.getInputStream();
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
    return isvalid;
  }
  
  
  @Override
  public void close() {
    conn.disconnect();
    isvalid = false;
  }
  
}
