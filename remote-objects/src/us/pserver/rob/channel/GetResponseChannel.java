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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.crypt.CryptByteCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.gzip.GZipByteCoder;
import us.pserver.http.GetRequest;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpInputStream;
import us.pserver.http.JsonObjectConverter;
import us.pserver.http.PlainContent;
import us.pserver.http.RequestLine;
import us.pserver.http.RequestParser;
import us.pserver.http.ResponseLine;
import us.pserver.rob.RemoteMethod;
import static us.pserver.rob.channel.GetRequestChannel.*;
import us.pserver.rob.container.Credentials;
import us.pserver.streams.IO;
import us.pserver.streams.StreamUtils;


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
public class GetResponseChannel implements Channel, HttpConst {
  
  private final Socket sock;
  
  private HttpBuilder builder;
  
  private RequestParser parser;
  
  private CryptKey key;
  
  private boolean gzip;
  
  private boolean valid;
  
  private HttpInputStream his;
  
  
  /**
   * Construtor padrão, recebe um <code>Socket</code>
   * para comunicação na rede.
   * @param sc <code>Socket</code>, possivelmente obtido
   * através do método <code>ServerSocket.accept() : Socket</code>.
   */
  public GetResponseChannel(Socket sc) {
    if(sc == null || sc.isClosed())
      throw new IllegalArgumentException(
          "Invalid Socket ["+ sc+ "]");
    
    sock = sc;
    gzip = false;
    key = null;
    valid = true;
    parser = new RequestParser();
    builder = new HttpBuilder();
    his = null;
  }
  
  
  private GetResponseChannel() {
    sock = null;
    key = null;
    valid = true;
    parser = new RequestParser();
    builder = new HttpBuilder();
    his = null;
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
    builder.remove(HD_CONTENT_TYPE);
    
    StringByteConverter scv = new StringByteConverter();
    JsonObjectConverter jsc = new JsonObjectConverter();
    String str = jsc.convert(trp);
    
    PlainContent cont = new PlainContent();
    byte[] bs = scv.convert(str);
    
    if(gzip) {
      GZipByteCoder gbc = new GZipByteCoder();
      bs = gbc.encode(bs);
    }
    if(key != null) {
      CryptByteCoder cbc = new CryptByteCoder(key);
      bs = cbc.encode(bs);
    }
    if(gzip || key != null) {
      Base64ByteCoder bbc = new Base64ByteCoder();
      bs = bbc.encode(bs);
    }
    
    cont.setContent(scv.reverse(bs));
    builder.put(cont);
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    this.setHeaders(trp);
    builder.writeContent(sock.getOutputStream());
    sock.getOutputStream().flush();
    valid = false;
  }
  
  
  private RemoteMethod createRemote(GetRequest get) throws IOException {
    if(get == null) return null;
    
    StringByteConverter scv = new StringByteConverter();
    byte[] bobj = scv.convert(get.queryGet(OBJECT));
    byte[] bmth = scv.convert(get.queryGet(METHOD));
    byte[] bauth = (get.queryContains(AUTH) 
        ? scv.convert(get.queryGet(AUTH)) : null);
    List<String> ltypes = get.queryGetList(TYPES);
    byte[][] btypes = new byte[ltypes.size()][1];
    for(int i = 0; i < btypes.length; i++) {
      btypes[i] = scv.convert(ltypes.get(i));
    }
    List<String> largs = get.queryGetList(ARGS);
    byte[][] bargs = new byte[largs.size()][1];
    for(int i = 0; i < bargs.length; i++) {
      bargs[i] = scv.convert(largs.get(i));
    }
    
    if(get.queryContains(CRYPT_KEY)
        || (get.queryContains(GZIP)
        && "1".equals(get.queryGet(GZIP)))) {
      Base64ByteCoder bbc = new Base64ByteCoder();
      bobj = bbc.decode(bobj);
      bmth = bbc.decode(bobj);
      bauth = (bauth != null ? bbc.decode(bauth) : null);
      for(int i = 0; i < btypes.length; i++) {
        btypes[i] = bbc.decode(btypes[i]);
        bargs[i] = bbc.decode(bargs[i]);
      }
    }
    
    if(get.queryContains(CRYPT_KEY)) {
      key = CryptKey.fromString(get.queryGet(CRYPT_KEY));
      CryptByteCoder cbc = new CryptByteCoder(key);
      bobj = cbc.decode(bobj);
      bmth = cbc.decode(bobj);
      bauth = (bauth != null ? cbc.decode(bauth) : null);
      for(int i = 0; i < btypes.length; i++) {
        btypes[i] = cbc.decode(btypes[i]);
        bargs[i] = cbc.decode(bargs[i]);
      }
    }
    
    if(get.queryMap().containsKey(GZIP)
        && get.queryGet(GZIP).equals("1")) {
      gzip = true;
      GZipByteCoder gbc = new GZipByteCoder();
      bobj = gbc.decode(bobj);
      bmth = gbc.decode(bobj);
      bauth = (bauth != null ? gbc.decode(bauth) : null);
      for(int i = 0; i < btypes.length; i++) {
        btypes[i] = gbc.decode(btypes[i]);
        bargs[i] = gbc.decode(bargs[i]);
      }
    }
    
    JsonObjectConverter jsc = new JsonObjectConverter();
    RemoteMethod rmt = new RemoteMethod()
        .forObject(scv.reverse(bobj))
        .method(scv.reverse(bmth));
    if(bauth != null) {
      Credentials cr = (Credentials) jsc.convert(scv.reverse(bauth));
      rmt.credentials(cr);
    }

    for(int i = 0; i < btypes.length; i++) {
      Class cls = null;
      try {
        cls = ClassHelper.getClass(scv.reverse(btypes[i]));
      } catch(ClassNotFoundException e) {
        throw new IOException(e.toString());
      }
      rmt.addType(cls);
      if(ClassHelper.isNumber(cls)) {
        rmt.addParam(convertNumber(Double.parseDouble(largs.get(i)), cls));
      }
      else if(String.class.isAssignableFrom(cls)) {
        rmt.addParam(largs.get(i));
      }
      else {
        rmt.addParam(jsc.convert(largs.get(i)));
      }
    }
    return rmt;
  }
  
  
  private Object convertNumber(Double n, Class cls) {
    if(n == null || cls == null) return n;
    if(int.class.isAssignableFrom(cls)
        || Integer.class.isAssignableFrom(cls)) {
      return new Integer(n.intValue());
    }
    else if(short.class.isAssignableFrom(cls)
        || Short.class.isAssignableFrom(cls)) {
      return new Short(n.shortValue());
    }
    else if(byte.class.isAssignableFrom(cls) 
        || Byte.class.isAssignableFrom(cls)) {
      return new Byte(n.byteValue());
    }
    else if(long.class.isAssignableFrom(cls)
        || Long.class.isAssignableFrom(cls)) {
      return new Long(n.longValue());
    }
    else if(float.class.isAssignableFrom(cls)
        || Float.class.isAssignableFrom(cls)) {
      return new Float(n.floatValue());
    }
    else return n;
  }
  
  
  private Transport createTransport(GetRequest get) {
    if(get == null) return null;
    
    StringByteConverter scv = new StringByteConverter();
    byte[] btrp = scv.convert(get.queryGet(TRANSPORT));
    
    Base64ByteCoder bbc = new Base64ByteCoder();
    btrp = bbc.decode(btrp);
    
    if(get.queryContains(CRYPT_KEY)) {
      key = CryptKey.fromString(get.queryGet(CRYPT_KEY));
      CryptByteCoder cbc = new CryptByteCoder(key);
      btrp = cbc.decode(btrp);
    }
    
    if(get.queryMap().containsKey(GZIP)
        && get.queryGet(GZIP).equals("1")) {
      gzip = true;
      GZipByteCoder gbc = new GZipByteCoder();
      btrp = gbc.decode(btrp);
    }
    
    JsonObjectConverter jsc = new JsonObjectConverter();
    return (Transport) jsc.convert(scv.reverse(btrp));
  }
  
  
  @Override
  public Transport read() throws IOException {
    RequestParser rp = new RequestParser()
        .parseInput(sock.getInputStream());
    RequestLine rl = rp.getRequestLine();
    if(rl == null)
      throw new IOException("Invalid Request (parse to NULL)");
    
    key = null;
    gzip = false;
    
    GetRequest get = GetRequest.parse(rl.toString());
    if(get == null || get.queryMap().isEmpty())
      throw new IOException("Invalid GET Request ["+ rl.toString()+ "]");
    
    Transport trp;
    if(get.queryContains("ui") && "1".equals(get.queryGet("ui"))) {
      sendUI();
      return null;
    }
    if(!get.queryContains("trp")) {
      trp = new Transport(createRemote(get));
    }
    else {
      trp = createTransport(get);
    }
    return trp;
  }
  
  
  private void sendUI() throws IOException {
    System.out.println("* Sending UI...");
    StringByteConverter scv = new StringByteConverter();
    InputStream in = getClass().getResourceAsStream("remote-object.html");
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamUtils.transfer(in, bos);
    IO.cl(in, bos);
    builder = HttpBuilder.responseBuilder(new ResponseLine(200, "OK"));
    builder.remove(HD_CONTENT_TYPE);
    builder.put(HD_CONTENT_TYPE, "text/html");
    builder.put(new PlainContent(scv.reverse(bos.toByteArray())));
    builder.writeContent(sock.getOutputStream());
  }
  
  
  public Transport read(InputStream in) throws IOException {
    RequestParser rp = new RequestParser()
        .parseInput(in);
    RequestLine rl = rp.getRequestLine();
    if(rl == null)
      throw new IOException("Invalid Request (parse to NULL)");
    
    GetRequest get = GetRequest.parse(rl.toString());
    if(get == null || get.queryMap().isEmpty())
      throw new IOException("Invalid GET Request ["+ rl.toString()+ "]");
    
    Transport trp;
    if(!get.queryContains("trp")) {
      trp = new Transport(createRemote(get));
    }
    else {
      trp = createTransport(get);
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
    return valid;
  }
  
  
  @Override
  public void close() {
    try {
      if(sock != null) {
        sock.shutdownInput();
        sock.shutdownOutput();
        sock.close();
      }
      if(his != null) his.closeBuffer();
    } catch(IOException e) {}
  }
  
  
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    GetRequest get = new GetRequest("localhost", 25000);
    get.query("obj", "a")
        .query("mth", "compute")
        .query("types", int.class, int.class)
        .query("args", 5, 3);
    HttpBuilder build = HttpBuilder.requestBuilder(get);
    build.remove(HttpConst.HD_CONTENT_TYPE);
    build.writeContent(System.out);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    build.writeContent(bos);
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    
    System.out.println(new GetResponseChannel().read(bis));
  }
  
}
