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


/**
 * Interface que define constantes utilizadas 
 * no protocolo HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public interface HttpConst {
  
  /**
   * Tamanho fixo de conteúdo para os cabeçalhos HTTP 
   * <br/><code>FIXED_LENGTH = 26</code>
   */
  public static final int FIXED_LENGTH = 26;

  /**
   * Tamanho fixo de conteúdo para os cabeçalhos HTTP
   * <br/><code>FIXED_CONT_LENGTH = 36</code>
   */
  public static final int FIXED_CONT_LENGTH = 36;

  /**
   * Tamanho fixo de conteúdo para os cabeçalhos HTTP
   * <br/><code>FIXED_OBJ_LENGTH = 2</code>
   */
  public static final int FIXED_OBJ_LENGTH = 2;

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_COOKIE = "Cookie"
   * </code>
   */
  public static final String HD_COOKIE = "Cookie";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_CONTENT_TYPE = "Content-Type"
   * </code>
   */
  public static final String HD_CONTENT_TYPE = "Content-Type";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_CONTENT_DISPOSITION = "Content-Disposition"
   * </code>
   */
  public static final String HD_CONTENT_DISPOSITION = "Content-Disposition";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_USER_AGENT = "User-Agent"
   * </code>
   */
  public static final String HD_USER_AGENT = "User-Agent";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_ACCEPT_ENCODING = "Accept-Encoding"
   * </code>
   */
  public static final String HD_ACCEPT_ENCODING = "Accept-Encoding";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_HOST = "Host"
   * </code>
   */
  public static final String HD_HOST = "Host";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_ACCEPT = "Accept"
   * </code>
   */
  public static final String HD_ACCEPT = "Accept";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_SET_COOKIE = "Set-Cookie"
   * </code>
   */
  public static final String HD_SET_COOKIE = "Set-Cookie";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_SERVER = "Server"
   * </code>
   */
  public static final String HD_SERVER = "Server";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_CONTENT_ENCODING = "Content-Encoding"
   * </code>
   */
  public static final String HD_CONTENT_ENCODING = "Content-Encoding";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_CONTENT_LENGTH = "Content-Length"
   * </code>
   */
  public static final String HD_CONTENT_LENGTH = "Content-Length";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_DATE = "Date"
   * </code>
   */
  public static final String HD_DATE = "Date";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_DATE = "Date"
   * </code>
   */
  public static final String HD_BOUNDARY = "; boundary=";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HD_PROXY_AUTHORIZATION = "Proxy-Authorization"
   * </code>
   */
  public static final String HD_PROXY_AUTHORIZATION = "Proxy-Authorization";

  /**
   * Content disposition value
   * <br/><code>
   *  VALUE_DISPOSITION_FORM_DATA = "form-data"
   * </code>
   */
  public static final String VALUE_DISPOSITION_FORM_DATA = "form-data";

  /**
   * Content type value
   * <br/><code>
   *  VALUE_APP_OCTETSTREAM = "application/octet-stream"
   * </code>
   */
  public static final String VALUE_APP_OCTETSTREAM = "application/octet-stream";

  /**
   * Valor de cabeçalho  
   * <br/><code>
   *  VALUE_CONTENT_MULTIPART = "multipart/form-data"
   * </code>
   */
  public static final String VALUE_CONTENT_MULTIPART = "multipart/form-data";

  /**
   * Valor de cabeçalho  
   * <br/><code>
   *  VALUE_CONTENT_XML = "text/xml"
   * </code>
   */
  public static final String VALUE_CONTENT_XML = "text/xml";

  /**
   * Valor de cabeçalho  
   * <br/><code>
   *  VALUE_USER_AGENT = "Mozilla/5.0"
   * </code>
   */
  public static final String VALUE_USER_AGENT = "Mozilla/5.0";

  /**
   * Valor de cabeçalho  
   * <br/><code>
   *  VALUE_ENCODING = "deflate"
   * </code>
   */
  public static final String VALUE_ENCODING = "deflate";

  /**
   * Valor de cabeçalho  
   * <br/><code>
   *  VALUE_ACCEPT = "text/html, text/xml, application/xml"
   * </code>
   */
  public static final String VALUE_ACCEPT = "text/html, text/xml, application/xml, application/octet-stream";

  /**
   * Valor de cabeçalho  
   * <br/><code>
   *  VALUE_SERVER = "Java/HttpResponseChannel"
   * </code>
   */
  public static final String VALUE_SERVER = "Java/HttpResponseChannel";

  /**
   * Valor de cabeçalho  
   * <br/><code>
   *  VALUE_OK = "OK"
   * </code>
   */
  public static final String VALUE_OK = "OK";

  /**
   * Cabeçalho 
   * <br/><code>
   *  HTTP_VERSION = "HTTP/1.1"
   * </code>
   */
  public static final String HTTP_VERSION = "HTTP/1.1";

  /**
   * Caractere espaço em branco 
   * <br/><code>
   *  BLANK = " "
   * </code>
   */
  public static final String BLANK = " ";

  /**
   * Caracteres de retorno de carro e quebra de linha 
   * <br/><code>
   *  LRN = "\r\n"
   * </code>
   */
  public static final String CRLF = "\r\n";

  /**
   * Two hyfens
   * <br/><code>
   *  HYFENS = "--"
   * </code>
   */
  public static final String HYFENS = "--";

  /**
   * Multipard boundary
   * <br/><code>
   *  BOUNDARY = "9051914041544843365972754266"
   * </code>
   */
  public static final String BOUNDARY = "9051914041544843365972754266";

  /**
   * Cabeçalho delimitador de abertura XML 
   * <br/><code>
   *  BOUNDARY_XML_START = "&ltxml&gt"
   * </code>
   */
  public static final String BOUNDARY_XML_START = "<xml>";

  /**
   * Cabeçalho delimitador de encerramento XML
   * <br/><code>
   *  BOUNDARY_XML_END = "&lt/xml&gt"
   * </code>
   */
  public static final String BOUNDARY_XML_END = "</xml>";

  /**
   * Cabeçalho delimitador de abertura de conteúdo stream
   * <br/><code>
   *  BOUNDARY_CONTENT_START = "&ltconttt enc='octet-stream'&gt"
   * </code>
   */
  public static final String BOUNDARY_CONTENT_START = "<conttt enc='octet-stream'>";

  /**
   * Cabeçalho delimitador de encerramento de conteúdo stream
   * <br/><code>
   *  BOUNDARY_CONTENT_END = "&lt/conttt&gt"
   * </code>
   */
  public static final String BOUNDARY_CONTENT_END = "</conttt>";

  /**
   * Cabeçalho delimitador de abertura de objeto
   * <br/><code>
   *  BOUNDARY_OBJECT_START = "&ltrobbb enc='hex'&gt"
   * </code>
   */
  public static final String BOUNDARY_OBJECT_START = "<robbb enc='hex'>";

  /**
   * Cabeçalho delimitador de encerramento de objeto
   * <br/><code>
   *  BOUNDARY_OBJECT_END = "&lt/robbb&gt"
   * </code>
   */
  public static final String BOUNDARY_OBJECT_END = "</robbb>";
  
  
  /**
   * Constantes de tipo de método de requisição HTTP.
   */
  public static enum Method {
    CONNECT, GET, POST, PUT, DELETE;
  }

}
