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


/**
 * Interface que define constantes utilizadas 
 * no protocolo HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public interface HttpConst {
  
  /**
   * <code>
   *  HD_COOKIE = "Cookie"
   * </code>
   * Cabeçalho HTTP.
   */
  public static final String HD_COOKIE = "Cookie";

  /**
   * <code>
   *  HD_CONNECTION = "Connection"
   * </code>
   * Cabeçalho HTTP.
   */
  public static final String HD_CONNECTION = "Connection";

  /**
   * <code>
   *  HD_CONTENT_TYPE = "Content-Type"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_CONTENT_TYPE = "Content-Type";

  /**
   * <code>
   *  HD_CONTENT_DISPOSITION = "Content-Disposition"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_CONTENT_DISPOSITION = "Content-Disposition";

  /**
   * <code>
   *  HD_USER_AGENT = "User-Agent"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_USER_AGENT = "User-Agent";

  /**
   * <code>
   *  HD_ACCEPT_ENCODING = "Accept-Encoding"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_ACCEPT_ENCODING = "Accept-Encoding";

  /**
   * <code>
   *  HD_HOST = "Host"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_HOST = "Host";

  /**
   * <code>
   *  HD_ACCEPT = "Accept"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_ACCEPT = "Accept";

  /**
   * <code>
   *  HD_SET_COOKIE = "Set-Cookie"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_SET_COOKIE = "Set-Cookie";

  /**
   * <code>
   *  HD_SERVER = "Server"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_SERVER = "Server";

  /**
   * <code>
   *  HD_CONTENT_ENCODING = "Content-Encoding"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_CONTENT_ENCODING = "Content-Encoding";

  /**
   * <code>
   *  HD_CONTENT_LENGTH = "Content-Length"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_CONTENT_LENGTH = "Content-Length";

  /**
   * <code>
   *  HD_DATE = "Date"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_DATE = "Date";

  /**
   * <code>
   *  HD_DATE = "Date"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_BOUNDARY = "; boundary=";

  /**
   * <code>
   *  HD_X_HTTP_AUTH = "X-Http-Auth"
   * </code><br>
   * Cabeçalho especial de autenticação (se suportado pelo servidor).
   */
  public static final String HD_X_HTTP_AUTH = "X-Http-Auth";

  /**
   * <code>
   *  HD_X_CRYPT_KEY = "X-Crypt-Key"
   * </code><br>
   * Cabeçalho especial para chave de criptografia (se suportado pelo servidor).
   */
  public static final String HD_X_CRYPT_KEY = "X-CKey";

  /**
   * <code>
   *  HD_PROXY_AUTHORIZATION = "Proxy-Authorization"
   * </code><br>
   * Cabeçalho HTTP.
   */
  public static final String HD_PROXY_AUTHORIZATION = "Proxy-Authorization";

  /**
   * <code>
   *  VALUE_DISPOSITION_FORM_DATA = "form-data"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_DISPOSITION_FORM_DATA = "form-data";

  /**
   * <code>
   *  VALUE_CONN_KEEP_ALIVE = "keep-alive"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_CONN_KEEP_ALIVE = "keep-alive";

  /**
   * <code>
   *  VALUE_APP_OCTETSTREAM = "application/octet-stream"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_APP_OCTETSTREAM = "application/octet-stream";

  /**
   * <code>
   *  VALUE_CONTENT_MULTIPART = "multipart/form-data"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_CONTENT_MULTIPART = "multipart/form-data";

  /**
   * <code>
   *  VALUE_CONTENT_XML = "text/xml"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_CONTENT_XML = "text/xml";

  /**
   * <code>
   *  VALUE_USER_AGENT = "Mozilla/5.0"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_USER_AGENT = "Mozilla/5.0";

  /**
   * <code>
   *  VALUE_ENCODING = "deflate"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_ENCODING = "deflate";

  /**
   * <code>
   *  VALUE_ACCEPT = "text/html, text/xml, application/xml"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_ACCEPT = "text/html, text/xml, application/octet-stream";

  /**
   * <code>
   *  VALUE_SERVER = "Java/HttpResponseChannel"
   * </code><br>
   * Valor do cabeçalho HTTP.
   */
  public static final String VALUE_SERVER = "HttpUtils/0.1";

  /**
   * <code>
   *  VALUE_OK = "OK"
   * </code><br>
   * Texto de resposta de requisição HTTP.
   */
  public static final String VALUE_OK = "OK";

  /**
   * <code>
   *  VALUE_CONN_STABLISHED = "Connection stablished"
   * </code><br>
   * Texto de resposta de requisição HTTP.
   */
  public static final String VALUE_CONN_STABLISHED = "Connection established";

  /**
   * <code>
   *  HTTP_VERSION = "HTTP/1.1"
   * </code><br>
   * Versão do protocolo HTTP.
   */
  public static final String HTTP_VERSION = "HTTP/1.0";

  /**
   * <code>
   *  HTTP = "HTTP"
   * </code><br>
   */
  public static final String HTTP = "HTTP";

  /**
   * <code>
   *  BLANK = " "
   * </code><br>
   * Espaço em branco.
   */
  public static final String BLANK = " ";

  /**
   * <code>
   *  SLASH = "/"
   * </code><br>
   * Barra.
   */
  public static final String SLASH = "/";

  /**
   * <code>
   *  LRN = "\r\n"
   * </code><br>
   * Caracteres de retorno de carro e quebra de linha.
   */
  public static final String CRLF = "\r\n";

  /**
   * <code>
   *  HYFENS = "--"
   * </code><br>
   * Two hyfens
   */
  public static final String HYFENS = "--";

  /**
   * <code>
   *  BOUNDARY = "90519140415448433"
   * </code><br>
   * Multipard boundary
   */
  public static final String BOUNDARY = "90519140415448433";//65972754266";

  /**
   * <code>
   *  BOUNDARY_XML_START = "&lt;xml&gt;"
   * </code><br>
   * Cabeçalho delimitador de abertura XML.
   */
  public static final String BOUNDARY_XML_START = "<xml>";

  /**
   * <code>
   *  BOUNDARY_XML_END = "&lt;/xml&gt;"
   * </code><br>
   * Cabeçalho delimitador de encerramento XML.
   */
  public static final String BOUNDARY_XML_END = "</xml>";

  /**
   * <code>
   *  BOUNDARY_CONTENT_START = "&lt;cont enc='octet-stream'&gt;"
   * </code><br>
   * Cabeçalho delimitador de abertura de conteúdo stream.
   */
  public static final String BOUNDARY_CONTENT_START = "<cont enc='octet-stream'>";

  /**
   * <code>
   *  BOUNDARY_CONTENT_END = "&lt;/cont&gt;"
   * </code><br>
   * Cabeçalho delimitador de encerramento de conteúdo stream.
   */
  public static final String BOUNDARY_CONTENT_END = "</cont>";

  /**
   * <code>
   *  BOUNDARY_OBJECT_START = "&lt;rob enc='hex'&gt;"
   * </code><br>
   * Cabeçalho delimitador de abertura de objeto.
   */
  public static final String BOUNDARY_OBJECT_START = "<rob enc='basic'>";

  /**
   * <code>
   *  BOUNDARY_OBJECT_END = "&lt;/rob&gt;"
   * </code><br>
   * Cabeçalho delimitador de encerramento de objeto.
   */
  public static final String BOUNDARY_OBJECT_END = "</rob>";
  
  /**
   * <code>
   *  BOUNDARY_OBJECT_START = "&lt;rob enc='hex'&gt;"
   * </code><br>
   * Cabeçalho delimitador de abertura de objeto.
   */
  public static final String BOUNDARY_CRYPT_KEY_START = "<ckey enc='basic'>";

  /**
   * <code>
   *  BOUNDARY_OBJECT_END = "&lt;/rob&gt;"
   * </code><br>
   * Cabeçalho delimitador de encerramento de objeto.
   */
  public static final String BOUNDARY_CRYPT_KEY_END = "</ckey>";
  
  
  /**
   * Constantes de tipo de método de requisição HTTP.
   */
  public static enum Method {
    CONNECT, GET, POST, PUT, DELETE;
  }

}
