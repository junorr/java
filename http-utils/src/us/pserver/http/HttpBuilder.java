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

package us.pserver.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import us.pserver.cdr.b64.Base64StringCoder;
import static us.pserver.http.StreamUtils.EOF;


/**
 * Objeto construtor de uma requisição ou resposta HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class HttpBuilder implements HttpConst {
  
  /**
   * <code>
   *   STATIC_SIZE = 40
   * </code><br>
   * Tamanho estático da saída gerada por <code>HttpBuilder</code>, sem
   * considrar o tamanho dos cabeçalhos adicionados.
   */
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
  
  
  /**
   * Retorna uma lista com todos os cabeçalhos.
   * @return <code>java.util.List</code>.
   */
  public List<Header> headers() {
    return hds;
  }
  
  
  /**
   * Retorna o tamanho total em bytes do 
   * conteúdo da mensagem HTTP.
   * @return Tamanho total em bytes do 
   * conteúdo da mensagem HTTP.
   */
  public long getLength() {
    long len = 0;
    for(Header hd : hds) {
      len += hd.getLength();
    }
    return len + STATIC_SIZE;
  }
  
  
  /**
   * Escreve o conteúdo de todos os cabeçalhos no 
   * stream de saída fornecido.
   * @param out <code>OutputStream</code>.
   * @throws IOException Caso ocorra erro na escrita.
   */
  public void writeTo(OutputStream out) throws IOException {
    if(out == null) 
      throw new IllegalArgumentException(
          "Invalid OutputStream ["+ out+ "]");
    if(hds.isEmpty()) return;
    
    int idx = 0;
    for(int i = 0; i < hds.size(); i++) {
      Header hd = hds.get(i);
      idx = i;
      if(!hd.isContentHeader()) {
        hd.writeTo(out);
      } else break;
    }
    new Header(HD_CONTENT_LENGTH, 
            String.valueOf(getLength())).writeTo(out);
    for(int i = idx; i < hds.size(); i++) {
      hds.get(i).writeTo(out);
    }
    
    StringBuilder end = new StringBuilder();
    end.append(CRLF).append(HYFENS)
        .append(BOUNDARY).append(HYFENS).append(EOF)
        .append(CRLF).append(CRLF);
    StreamUtils.write(end.toString(), out);
    out.flush();
  }
  
  
  public static void main(String[] args) throws IOException {
    HttpBuilder hb = new HttpBuilder();
    Base64StringCoder cdr = new Base64StringCoder();
    RequestLine req = new RequestLine(Method.POST, "172.24.75.2", 8000);
    hb.add(req).add(req.getHost())
        .add(HD_USER_AGENT, VALUE_USER_AGENT)
        .add(HD_ACCEPT_ENCODING, VALUE_ENCODING)
        .add(HD_ACCEPT, VALUE_ACCEPT)
        .add(HD_PROXY_AUTHORIZATION, "Basic "+ cdr.encode("f6036477:00000000"))
        .add(HD_CONTENT_TYPE, VALUE_CONTENT_MULTIPART + HD_BOUNDARY + BOUNDARY)
        .add(new HttpHexObject("hello world!!"));
    hb.writeTo(System.out);
  }
  
}
