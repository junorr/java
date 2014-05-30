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
import java.util.Date;
import us.pserver.cdr.hex.HexStringCoder;
import static us.pserver.remote.StreamUtils.bytes;
import us.pserver.remote.http.HttpBuilder;
import us.pserver.remote.http.HttpHexObject;
import us.pserver.remote.http.ResponseLine;
import us.pserver.remote.http.HttpConst;
import static us.pserver.remote.http.HttpConst.BOUNDARY_CONTENT_END;
import static us.pserver.remote.http.HttpConst.BOUNDARY_CONTENT_START;
import static us.pserver.remote.http.HttpConst.BOUNDARY_XML_END;


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
  
  private final XStream xst;
  
  private final Socket sock;
  
  private final HexStringCoder coder;
  
  private boolean isvalid;
  
  
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
    coder = new HexStringCoder();
    xst = new XStream();
    isvalid = false;
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
  private HttpBuilder setHeaders(Transport trp) throws IOException {
    HttpBuilder build = new HttpBuilder();
    build.add(new ResponseLine(200, VALUE_OK))
        .add(HD_CONTENT_TYPE, VALUE_CONTENT_XML)
        .add(HD_CONTENT_ENCODING, VALUE_ENCODING)
        .add(HD_SERVER, VALUE_SERVER)
        .add(HD_DATE, new Date().toString());
    
    HttpHexObject hob = new HttpHexObject(
        trp.getWriteVersion());
    
    int length = FIXED_LENGTH
        + hob.getLength()
        + FIXED_OBJ_LENGTH;
    
    if(trp.getInputStream() != null)
      length += FIXED_CONT_LENGTH 
          + trp.getInputStream().available();
    
    build.add(HD_CONTENT_LENGTH, String.valueOf(length));
    build.openXmlBoundary().add(hob);
    return build;
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    if(trp == null) return;
    
    InputStream input = trp.getInputStream();
    OutputStream out = sock.getOutputStream();
    HttpBuilder build = this.setHeaders(trp);
    build.writeTo(out);
    
    if(input != null) {
      out.write(bytes(BOUNDARY_CONTENT_START));
      StreamUtils.transfer(input, out);
      out.write(StreamUtils.BYTES_EOF);
      out.write(bytes(BOUNDARY_CONTENT_END));
    }
    else out.write(StreamUtils.BYTES_EOF);
    
    out.write(bytes(BOUNDARY_XML_END));
    out.write(StreamUtils.LNR);
    out.write(StreamUtils.LNR);
    out.flush();
    isvalid = false;
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
    if(StreamUtils.readUntil(in, 
        BOUNDARY_CONTENT_START, StreamUtils.EOF)) {
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
    return isvalid;
  }
  
  
  @Override
  public void close() {
    try {
      sock.shutdownInput();
      sock.shutdownOutput();
      sock.close();
      isvalid = false;
    } catch(IOException e) {}
  }
  
}
