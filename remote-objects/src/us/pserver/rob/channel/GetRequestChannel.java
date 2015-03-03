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
import java.util.Objects;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptByteCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.gzip.GZipByteCoder;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.http.GetRequest;
import us.pserver.http.Header;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.JsonObjectConverter;
import us.pserver.http.ResponseLine;
import us.pserver.http.ResponseParser;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import static us.pserver.rob.channel.HttpRequestChannel.HTTP_ENCLOSED_OBJECT;
import static us.pserver.rob.channel.HttpRequestChannel.HTTP_INPUTSTREAM;
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
public class GetRequestChannel implements Channel, HttpConst {

  public static final String 
      
      TRANSPORT = "trp",
      CRYPT_KEY = "crk",
      GZIP = "gz",
      METHOD = "mth",
      OBJECT = "obj",
      TYPES = "types",
      ARGS = "args",
      AUTH = "auth";
  
  
  private NetConnector netconn;
  
  private HttpBuilder builder;
  
  private Socket sock;
  
  private ResponseParser parser;
  
  private boolean crypt, gzip;
  
  private CryptAlgorithm algo;
  
  private CryptKey key;
  
  private ResponseLine resp;
  
  
  /**
   * Construtor padrão que recebe <code>NetConnector</code>
   * para comunicação com o servidor.
   * @param conn <code>NetConnector</code>.
   */
  public GetRequestChannel(NetConnector conn) {
    if(conn == null)
      throw new IllegalArgumentException(
          "Invalid NetConnector ["+ conn+ "]");
    
    netconn = conn;
    key = null;
    crypt = false;
    gzip = false;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
    parser = new ResponseParser();
    sock = null;
  }
  
  
  /**
   * Retorna o objeto <code>NetConnector</code>.
   * @return <code>NetConnector</code>.
   */
  public NetConnector getNetConnector() {
    return netconn;
  }
  
  
  public GetRequestChannel setEncryptionEnabled(boolean enabled) {
    crypt = enabled;
    return this;
  }
  
  
  public boolean isEncryptionEnabled() {
    return crypt;
  }
  
  
  public GetRequestChannel setGZipCompressionEnabled(boolean enabled) {
    gzip = enabled;
    return this;
  }
  
  
  public boolean isGZipCompressionEnabled() {
    return gzip;
  }
  
  
  public GetRequestChannel setCryptAlgorithm(CryptAlgorithm ca) {
    nullarg(CryptAlgorithm.class, ca);
    algo = ca;
    return this;
  }
  
  
  public CryptAlgorithm getCryptAlgorithm() {
    return algo;
  }
  
  
  public Socket getSocket() {
    return sock;
  }
  
  
  public HttpBuilder getHttpBuilder() {
    if(builder == null)
      builder = HttpBuilder.requestBuilder();
    return builder;
  }
  
  
  public ResponseParser getResponseParser() {
    return parser;
  }
  
  
  public ResponseLine getResponseLine() {
    return resp;
  }
  
  
  private void encloseRemote(RemoteMethod rmt, GetRequest get, JsonObjectConverter jsc) {
    if(rmt == null || get == null || jsc == null)
      return;
    
    get.query(OBJECT, encode(rmt.objectName(), jsc))
        .query(METHOD, encode(rmt.method(), jsc));
    
    if(rmt.credentials() != null 
        && rmt.credentials().getUser() != null) {
      get.query(AUTH, encode(rmt.credentials(), jsc));
    }
    
    if(rmt.types() != null && !rmt.types().isEmpty()) {
      get.query(TYPES, encodeArray(rmt.typesArray(), jsc));
    }
    
    if(rmt.params() != null && !rmt.params().isEmpty()) {
      get.query(ARGS, encodeArray(rmt.params().toArray(), jsc));
    }
  }
  
  
  private String encode(Object obj, JsonObjectConverter jsc) {
    if(obj == null || jsc == null) return null;
    StringByteConverter sbc = new StringByteConverter();
    
    String cont;
    if(obj instanceof CharSequence || obj instanceof Number)
      cont = Objects.toString(obj);
    else if(obj instanceof Class)
      cont = ((Class)obj).getName();
    else
      cont = jsc.convert(obj);
    
    if(!crypt && !gzip) return cont;
    
    byte[] bs = sbc.convert(cont);
    if(isGZipCompressionEnabled()) {
      GZipByteCoder gz = new GZipByteCoder();
      bs = gz.encode(bs);
    }
    if(isEncryptionEnabled() && key != null) {
      CryptByteCoder cbc = new CryptByteCoder(key);
      bs = cbc.encode(bs);
    }
    if(crypt || gzip) {
      Base64ByteCoder bbc = new Base64ByteCoder();
      bs = bbc.encode(bs);
    }
    return sbc.reverse(bs);
  }
  
  
  private String[] encodeArray(Object[] objs, JsonObjectConverter jsc) {
    if(objs == null || objs.length < 1 || jsc == null) 
      return null;
    
    String[] ss = new String[objs.length];
    for(int i = 0; i < objs.length; i++) {
      if(objs[i] == null) continue;
      ss[i] = encode(objs[i], jsc);
    }
    return ss;
  }
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private void setHeaders(Transport trp) throws IOException {
    if(trp == null || trp.getObject() == null) 
      throw new IllegalArgumentException(
          "Invalid Transport [trp="+ trp+ "]");
    
    GetRequest req = new GetRequest(
        netconn.getAddress(), netconn.getPort());
    
    key = (crypt ? CryptKey.createRandomKey(algo) : null);
    JsonObjectConverter jsc = new JsonObjectConverter();
    
    if(trp.getObject() != null 
        && trp.getObject() instanceof RemoteMethod) {
      encloseRemote(trp.castObject(), req, jsc);
    }
    else {
      req.query(TRANSPORT, encode(trp, jsc));
    }
    if(key != null) req.query(CRYPT_KEY, key);
    if(gzip) req.query(GZIP, 1);
    if(req.build().length() > 2012)
      throw new IOException("Oversized Request Line (length="+ req.build().length()+ ", max=2012)");
    
    builder = HttpBuilder.requestBuilder(req);
    if(netconn.getProxyAuthorization() != null)
      builder.put(HD_PROXY_AUTHORIZATION, 
          netconn.getProxyAuthorization());
    builder.remove(HD_CONTENT_TYPE);
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    this.setHeaders(trp);

    if(sock == null)
      sock = netconn.connectSocket();
    
    builder.writeContent(sock.getOutputStream());
    verifyResponse();
    //dump();
  }
  
  
  private void verifyResponse() throws IOException {
    parser.reset().parseInput(sock.getInputStream());
    resp = parser.getResponseLine();
    if(resp == null || resp.getCode() != 200) {
      //parser.headers().forEach(System.out::print);
      throw new IOException(
          "Invalid response from server: "+ resp);
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
    if(parser.headers().isEmpty())
      throw new IOException("No Content Received");
    Header hd = parser.headers().get(parser.headers().size() -1);
    if(hd == null)
      throw new IOException("No Content Received");
    
    StringByteConverter scv = new StringByteConverter();
    String str = hd.getValue();
    if(str.contains("\n") || str.contains("\r"))
      str = str.replace("\n", "").replace("\r", "");
    byte[] bs = scv.convert(str);
    
    if(gzip || crypt) {
      Base64ByteCoder bbc = new Base64ByteCoder();
      bs = bbc.decode(bs);
    }
    if(crypt && key != null) {
      CryptByteCoder cbc = new CryptByteCoder(key);
      bs = cbc.decode(bs);
    }
    if(gzip) {
      GZipByteCoder gbc = new GZipByteCoder();
      bs = gbc.decode(bs);
    }
    
    str = scv.reverse(bs);
    JsonObjectConverter jsc = new JsonObjectConverter();
    return (Transport) jsc.convert(str);
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
        && !sock.isClosed() && !sock.isOutputShutdown();
  }
  
  
  @Override
  public void close() {
    try {
      if(sock != null) sock.close(); 
    }
    catch(IOException e) {}
  }
  
}
