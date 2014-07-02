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

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.http.StreamUtils.EOF;
import us.pserver.streams.Streams;


/**
 * Objeto construtor de uma requisição ou resposta HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class HttpBuilder implements HttpConst {
  
  /**
   * <code>STATIC_SIZE = 40</code><br>
   * Tamanho estático da saída gerada por <code>HttpBuilder</code>, sem
   * considrar o tamanho dos cabeçalhos adicionados.
   */
  public static final int STATIC_SIZE = 42;
  
  
  private final List<Header> hds;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public HttpBuilder() {
    hds = new LinkedList<>();
  }
  
  
  /**
   * Cria e retorna um <code>HttpBuilder</code> pré configurado 
   * para requisições HTTP.<br>
   * Cabeçalhos pré configurados:<br>
   * <code>
   *  &bull; User-Agent: Mozilla/5.0<br>
   *  &bull; Accept: text/html, text/xml, application/octet-stream<br>
   *  &bull; Accept-Encoding: deflate<br>
   *  &bull; Content-Type: multipart/form-data; boundary=9051914041544843365972754266<br>
   * </code>
   * @return <code>HttpBuilder</code> pré configurado 
   * para requisições HTTP.
   */
  public static HttpBuilder requestBuilder() {
    return new HttpBuilder()
      .put(HD_USER_AGENT, VALUE_USER_AGENT)
      .put(HD_ACCEPT, VALUE_ACCEPT)
      .put(HD_ACCEPT_ENCODING, VALUE_ENCODING)
      .put(HD_CONTENT_TYPE, VALUE_CONTENT_MULTIPART 
          + HD_BOUNDARY + BOUNDARY);
  }
  
  
  /**
   * Cria e retorna um <code>HttpBuilder</code> com a linha de 
   * requisição <code>RequestLine</code> pré configurado
   * para requisições HTTP.<br>
   * Cabeçalhos pré configurados:<br>
   * <code>
   *  &bull; User-Agent: Mozilla/5.0<br>
   *  &bull; Accept: text/html, text/xml, application/octet-stream<br>
   *  &bull; Accept-Encoding: deflate<br>
   *  &bull; Content-Type: multipart/form-data; boundary=9051914041544843365972754266<br>
   * </code>
   * @return <code>HttpBuilder</code> pré configurado 
   * para requisições HTTP.
   */
  public static HttpBuilder requestBuilder(RequestLine req) {
    return requestBuilder().put(req);
  }
  
  
  /**
   * Cria e retorna um <code>HttpBuilder</code> pré configurado 
   * para respostas HTTP.<br>
   * Cabeçalhos pré configurados:<br>
   * <code>
   *  &bull; Server: HttpUtils/0.1<br>
   *  &bull; Accept: text/html, text/xml, application/octet-stream<br>
   *  &bull; Accept-Encoding: deflate<br>
   *  &bull; Date: new Date().toString()<br>
   *  &bull; Content-Type: multipart/form-data; boundary=9051914041544843365972754266<br>
   * </code>
   * @return <code>HttpBuilder</code> pré configurado 
   * para requisições HTTP.
   */
  public static HttpBuilder responseBuilder() {
    return new HttpBuilder()
      .put(HD_SERVER, VALUE_SERVER)
      .put(HD_ACCEPT, VALUE_ACCEPT)
      .put(HD_ACCEPT_ENCODING, VALUE_ENCODING)
      .putDateHeader()
      .put(HD_CONTENT_TYPE, VALUE_CONTENT_MULTIPART 
          + HD_BOUNDARY + BOUNDARY);
  }
  
  
  /**
   * Cria e retorna um <code>HttpBuilder</code> com a linha de 
   * resposta <code>ResponseLine</code> pré configurado
   * para respostas HTTP.<br>
   * Cabeçalhos pré configurados:<br>
   * <code>
   *  &bull; User-Agent: Mozilla/5.0<br>
   *  &bull; Accept: text/html, text/xml, application/octet-stream<br>
   *  &bull; Accept-Encoding: deflate<br>
   *  &bull; Content-Type: multipart/form-data; boundary=9051914041544843365972754266<br>
   * </code>
   * @return <code>HttpBuilder</code> pré configurado 
   * para requisições HTTP.
   */
  public static HttpBuilder responseBuilder(ResponseLine rsp) {
    return responseBuilder().put(rsp);
  }
  
  
  /**
   * Adiciona um cabeçalho HTTP.
   * @param hd <code>Header</code>.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder put(Header hd) {
    if(hd != null) hds.add(hd);
    return this;
  }
  
  
  /**
   * Adiciona o cabeçalho de requisição HTTP.
   * @param req cabeçalho de requisição <code>RequestLine</code>.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder put(RequestLine req) {
    if(req != null) {
      putFirst(req).put(req.getHostHeader());
    }
    return this;
  }
  
  
  /**
   * Adiciona o cabeçalho de resposta HTTP.
   * @param rsp cabeçalho de resposta <code>ResponseLine</code>.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder put(ResponseLine rsp) {
    if(rsp != null) {
      putFirst(rsp);
    }
    return this;
  }
  
  
  /**
   * Adiciona o cabeçalho na primeira posição da lista de cabeçalhos.
   * @param hd cabeçalho a ser adicionado na primeira posição.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder putFirst(Header hd) {
    if(hd != null) {
      hds.add(hd);
      for(int i = hds.size()-1; i > 0; i--) {
        Header h = hds.get(i-1);
        hds.set(i-1, hds.get(i));
        hds.set(i, h);
      }
    }
    return this;
  }
  
  
  /**
   * Adiciona um cabeçalho HTTP com o nome e valor fornecidos.
   * @param name Nome do cabeçalho.
   * @param value Valor do cabeçalho.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder put(String name, String value) {
    if(name != null) hds.add(new Header(name, value));
    return this;
  }
  
  /**
   * Adiciona cabeçalho de data na mensagem HTTP.
   * @return Esta instância modificada de <code>HttpBuilder</code>.
   */
  public HttpBuilder putDateHeader() {
    return this.put(HD_DATE, new Date().toString());
  }
  
  
  /**
   * Remove e retorna uma cabeçalho HTTP com o nome fornecido 
   * (<code>headerName</code>).
   * @param headerName Nome do cabeçalho a ser removido.
   * @return O cabeçalho removido ou <code>null</code> caso
   * não seja encontrado.
   */
  public Header remove(String headerName) {
    if(headerName == null) return null;
    int idx = -1;
    for(int i = 0; i < hds.size(); i++) {
      if(hds.get(i).getName()
          .equals(headerName)) {
        idx = i;
        break;
      }
    }
    if(idx >= 0 && idx < hds.size()) 
      return hds.remove(idx);
    
    return null;
  }
  
  
  /**
   * Verifica se existe um cabeçalho HTTP com o nome fornecido 
   * (<code>headerName</code>).
   * @param headerName Nome do cabeçalho a ser verificado.
   * @return <code>true</code> se o cabeçalho existir na
   * lista de cabeçalhos, <code>false</code> caso contrário.
   */
  public boolean contains(String headerName) {
    if(headerName == null) return false;
    return hds.stream().anyMatch(
        hd->hd.getName().equals(headerName));
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
   * @param str
   * @throws IOException Caso ocorra erro na escrita.
   */
  public void writeContent(Streams str) throws IOException {
    nullarg(Streams.class, str);
    if(hds.isEmpty()) return;
    
    int idx = 0;
    for(int i = 0; i < hds.size(); i++) {
      Header hd = hds.get(i);
      idx = i;
      if(!hd.isContentHeader())
        hd.writeContent(str);
      else break;
    }
    new Header(HD_CONTENT_LENGTH, String.valueOf(getLength()))
        .writeContent(str);
    
    for(int i = idx; i < hds.size(); i++) {
      hds.get(i).writeContent(str);
    }
    
    StringBuilder end = new StringBuilder();
    end.append(CRLF).append(HYFENS)
        .append(BOUNDARY).append(HYFENS)
        .append(CRLF).append(EOF)
            .append(CRLF).append(CRLF);
    
    StringByteConverter cv = new StringByteConverter();
    str.getRawOutputStream()
        .write(cv.convert(end.toString()));
    str.getRawOutputStream().flush();
  }
  
  
  public static void main(String[] args) throws IOException {
    HttpBuilder hb = new HttpBuilder();
    Base64StringCoder cdr = new Base64StringCoder();
    RequestLine req = new RequestLine(Method.POST, "172.24.75.2", 8000);
    hb.put(req)
        .put(HD_USER_AGENT, VALUE_USER_AGENT)
        .put(HD_ACCEPT_ENCODING, VALUE_ENCODING)
        .put(HD_ACCEPT, VALUE_ACCEPT)
        .put(HD_PROXY_AUTHORIZATION, "Basic "+ cdr.encode("f6036477:00000000"))
        .put(HD_CONTENT_TYPE, VALUE_CONTENT_MULTIPART + HD_BOUNDARY + BOUNDARY)
        .put(new HttpHexObject("hello world!!"));
    Streams str = new Streams();
    str.setCryptCoderEnabled(true, new CryptKey("123456", CryptAlgorithm.AES_CBC))
        .setBase64CoderEnabled(true);
    //str.setGZipCoderEnabled(true);
    str.setOutputStream(System.out, true);
    hb.writeContent(str);
    str.finishStreams();
  }
  
}
