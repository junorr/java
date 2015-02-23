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
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.gzip.GZipByteCoder;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpCryptKey;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.JsonObjectConverter;
import us.pserver.http.RequestLine;
import us.pserver.http.ResponseLine;
import us.pserver.http.ResponseParser;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
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
      
      HTTP_ENCLOSED_OBJECT = 
        HttpEnclosedObject.class.getSimpleName(),
      
      HTTP_CRYPT_KEY = 
        HttpCryptKey.class.getSimpleName(),
      
      HTTP_INPUTSTREAM = 
        HttpInputStream.class.getSimpleName();
  
  
  private NetConnector netconn;
  
  private HttpBuilder builder;
  
  private Socket sock;
  
  private ResponseParser parser;
  
  private boolean crypt, gzip;
  
  private CryptAlgorithm algo;
  
  private ResponseLine resp;
  
  private HttpInputStream his;
  
  
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
    crypt = true;
    gzip = true;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
    parser = new ResponseParser();
    sock = null;
    his = null;
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
  
  
  /**
   * Define alguns cabeçalhos da requisição HTTP,
   * como tipo de conteúdo, codificação, conteúdo
   * aceito e agente da requisição.
   */
  private void setHeaders(Transport trp) throws IOException {
    if(trp == null || trp.getObject() == null) 
      throw new IllegalArgumentException(
          "Invalid Transport [trp="+ trp+ "]");
    
    RequestLine req = new RequestLine(Method.POST, 
        netconn.getAddress(), netconn.getPort());
    builder = HttpBuilder.requestBuilder(req);
    
    if(netconn.getProxyAuthorization() != null)
      builder.put(HD_PROXY_AUTHORIZATION, 
          netconn.getProxyAuthorization());
    
    CryptKey key = CryptKey.createRandomKey(algo);
    
    builder.put(new HttpCryptKey(key));
    builder.put(new HttpEnclosedObject(
        trp.getWriteVersion())
        .setCryptKey(key));
    
    if(trp.getInputStream() != null) {
      his = new HttpInputStream(
          trp.getInputStream())
          .setGZipCoderEnabled(true)
          .setCryptCoderEnabled(true, key);
      builder.put(his);
    }
  }
  
  
  public String packRemote(RemoteMethod rmt) {
    if(rmt == null) return null;
    StringBuffer sb = new StringBuffer();
    sb.append("obj=").append(rmt.objectName())
        .append("&mth=").append(rmt.method())
        .append("&");
    Class[] tps = rmt.typesArray();
    if(tps != null || tps.length > 0) {
      sb.append("types=");
      StringBuffer st = new StringBuffer();
      for(int i = 0; i < tps.length; i++) {
        st.append(tps[i].getName());
        if(i < tps.length -1)
          st.append(",");
      }
      sb.append(urlEncode(st.toString())).append("&");
    }
    List args = rmt.params();
    if(args != null && !args.isEmpty()) {
      sb.append("args=");
      StringBuffer sa = new StringBuffer();
      for(int i = 0; i < args.size(); i++) {
        sa.append(encode(args.get(i)));
        if(i < args.size() -1)
          sa.append(",");
      }
      sb.append(urlEncode(sa.toString()));
    }
    return sb.toString();
  }
  
  
  public String urlEncode(String str) {
    try {
      return URLEncoder.encode(str, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
  
  public String encode(Object obj) {
    if(obj == null) return null;
    if(obj instanceof String || obj instanceof Date) {
      return urlEncode(obj.toString());
    }
    else if(obj instanceof Number) {
      return obj.toString();
    }
    
    StringByteConverter sbc = new StringByteConverter();
    JsonObjectConverter joc = new JsonObjectConverter();
    String str = joc.convert(obj);
    if(str == null || str.trim().isEmpty())
      return null;
    
    if(isGZipCompressionEnabled()) {
      byte[] bytes = sbc.convert(str);
      GZipByteCoder cdr = new GZipByteCoder();
      bytes = cdr.encode(bytes);
      Base64ByteCoder b64 = new Base64ByteCoder();
      bytes = b64.encode(bytes);
      str = sbc.reverse(bytes);
    }
    else {
      
    }
    return str;
  }
  
  
  public String createURL(Transport trp) {
    if(trp == null || trp.getObject() == null) 
      throw new IllegalArgumentException(
          "Invalid Transport [trp="+ trp+ "]");
    StringBuffer sb = new StringBuffer();
    RequestLine req = new RequestLine(Method.POST, 
        netconn.getAddress(), netconn.getPort());
    sb.append(req.getFullAddress()).append("?");
    if(trp.getObject() instanceof RemoteMethod) {
      sb.append(packRemote((RemoteMethod)trp.getObject()));
    }
    else {
      sb.append("trp=").append(encode(trp));
    }
    return sb.toString();
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    if(his != null) his.closeBuffer();
    this.setHeaders(trp);
    if(sock == null)
      sock = netconn.connectSocket();
    
    builder.writeContent(sock.getOutputStream());
    this.verifyResponse();
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
    if(his != null) his.closeBuffer();
    if(!parser.containsHeader(HTTP_ENCLOSED_OBJECT))
      return null;
    
    HttpEnclosedObject hob = (HttpEnclosedObject) 
        parser.getHeader(HTTP_ENCLOSED_OBJECT);
    
    Transport tp = hob.getObjectAs();
    
    if(parser.containsHeader(HTTP_INPUTSTREAM)) {
      if(his != null) his.closeBuffer();
      his = (HttpInputStream) 
          parser.getHeader(HTTP_INPUTSTREAM);
      tp.setInputStream(his.setGZipCoderEnabled(true)
          .setupInbound().getInputStream());
    }
    return tp;
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
      if(sock != null)
        sock.close(); 
      if(his != null)
        his.closeBuffer();
    }
    catch(IOException e) {}
  }
  
  
  public static void main(String[] args) {
    class A {
      String s;
      int x, z;
      float f;
      boolean b;
      byte[] y;
      int compute(int a, int b) {
        x = a;
        z = b;
        f = a/b;
        return (int) f;
      }
    }
    A a = new A();
    a.b = true;
    a.s = "Some String";
    a.y = new byte[] {1,2,3};
    GetRequestChannel get = new GetRequestChannel(new NetConnector("localhost", 25000));
    System.out.println("* encode="+ get.encode(a));
    get.setGZipCompressionEnabled(false);
    System.out.println("* get="+ get.createURL(new Transport(a)));
    RemoteMethod rmt = new RemoteMethod()
        .forObject("a")
        .method("compute")
        .types(int.class, int.class)
        .params(5, 2);
    System.out.println("* get="+ get.createURL(new Transport(rmt)));
  }
  
}
