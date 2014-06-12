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

package us.pserver.remote.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import us.pserver.remote.StreamUtils;
import static us.pserver.remote.http.HttpConst.BOUNDARY;
import static us.pserver.remote.http.HttpConst.BOUNDARY_XML_END;
import static us.pserver.remote.http.HttpConst.BOUNDARY_XML_START;
import static us.pserver.remote.http.HttpConst.CRLF;
import static us.pserver.remote.http.HttpConst.HYFENS;
import static us.pserver.remote.http.HttpHexObject.HD_CONTENT_XML;


/**
 * Objeto construtor de uma requisição ou resposta HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class HttpBuilder implements HttpConst {
  
  public static final int STATIC_SIZE = 40;
  
  
  private final List<Header> hds;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public HttpBuilder() {
    hds = new LinkedList<>();
  }
  
  
  /**
   * Adiciona um cabeçalho HTTP.
   * @param hd <code>Header</code>.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder add(Header hd) {
    if(hd != null) hds.add(hd);
    return this;
  }
  
  
  /**
   * Adiciona um cabeçalho HTTP com o nome e valor fornecidos.
   * @param name Nome do cabeçalho.
   * @param value Valor do cabeçalho.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder add(String name, String value) {
    if(name != null) hds.add(new Header(name, value));
    return this;
  }
  
  
  public HttpBuilder addContentLength() {
    return this.add(HD_CONTENT_LENGTH, String.valueOf(getLength()));
  }
  
  
  /**
   * Adiciona uma <code>String</code> delimitadora.
   * @param boundary <code>String</code> delimitadora.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder appendBoundary(String boundary) {
    if(boundary != null) 
      this.add(Header.boundary(boundary));
    return this;
  }
  
  
  /**
   * Insere um cabeçalho delimitador de abertura de conteúdo XML.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder openXmlBoundary() {
    return this.add(Header.boundary(CRLF+CRLF+BOUNDARY_XML_START));
  }
  
  
  /**
   * Insere um cabeçalho delimitador de encerramento de conteúdo XML.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder closeXmlBoundary() {
    return this.add(Header.boundary(BOUNDARY_XML_END));
  }
  

  /**
   * Retorna uma <code>List</code> com todos os cabeçalhos.
   * @return <code>java.util.List</code>.
   */
  public List<Header> headers() {
    return hds;
  }
  
  
  public long getLength() {
    long len = 0;
    for(Header hd : hds) {
      len += hd.getLength();
    }
    return len + STATIC_SIZE;
  }
  
  
  private void writeHeader(Header hd, OutputStream out) throws IOException {
    hd.writeTo(out);
    if(!hd.isContentHeader())
      StreamUtils.write(CRLF, out);
  }
  
  
  /**
   * Escreve o conteúdo de todos os cabeçalho no 
   * stream de saída fornecido.
   * @param out <code>OutputStream</code>.
   * @throws IOException Caso ocorra erro na escrita.
   */
  public void writeTo(OutputStream out) throws IOException {
    if(out == null) 
      throw new IllegalArgumentException(
          "Invalid OutputStream ["+ out+ "]");
    if(hds.isEmpty()) return;
    
    for(int i = 0; i < hds.size(); i++) {
      Header hd = hds.get(i);
      if(hd.isContentHeader())
        writeHeader(new Header(HD_CONTENT_LENGTH, 
            String.valueOf(getLength())), out);
      
      writeHeader(hd, out);
    }
    
    StringBuilder end = new StringBuilder();
    end.append(CRLF).append(HYFENS)
        .append(BOUNDARY).append(HYFENS)
        .append(CRLF).append(CRLF);
    StreamUtils.write(end.toString(), out);
    out.flush();
  }
  
  
  public static void main(String[] args) throws IOException {
    StringBuilder start = new StringBuilder();
    start.append(CRLF).append(HYFENS)
        .append(BOUNDARY).append(HYFENS)
        .append(CRLF).append(CRLF);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamUtils.write(start.toString(), bos);
    System.out.println("* size="+ bos.size());
  }
  
}
