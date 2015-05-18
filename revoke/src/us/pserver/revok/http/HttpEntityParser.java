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

package us.pserver.revok.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.revok.protocol.JsonSerializer;
import us.pserver.revok.protocol.ObjectSerializer;
import us.pserver.streams.IO;
import us.pserver.streams.MixedWriteBuffer;
import us.pserver.streams.StreamResult;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/04/2015
 */
public class HttpEntityParser {

  private MixedWriteBuffer buffer;
  
  private InputStream input;
  
  private Object obj;
  
  private CryptKey key;
  
  private ObjectSerializer serial;
  
  
  public HttpEntityParser() {
    buffer = new MixedWriteBuffer();
    input = null;
    obj = null;
    key = null;
    serial = new JsonSerializer();
  }
  
  
  public HttpEntityParser(ObjectSerializer os) {
    buffer = new MixedWriteBuffer();
    buffer = new MixedWriteBuffer();
    input = null;
    obj = null;
    key = null;
    if(os == null) os = new JsonSerializer();
    serial = os;
  }
  
  
  public static HttpEntityParser instance() {
    return new HttpEntityParser();
  }
  
  
  public static HttpEntityParser instance(ObjectSerializer os) {
    return new HttpEntityParser(os);
  }
  
  
  public ObjectSerializer getObjectSerializer() {
    return serial;
  }
  
  
  public HttpEntityParser setObjectSerializer(ObjectSerializer serializer) {
    if(serializer != null) {
      serial = serializer;
    }
    return this;
  }
  
  
  public HttpEntityParser enableCryptCoder(CryptKey key) {
    if(key != null) {
      buffer.getCoderFactory().setCryptCoderEnabled(true, key);
    }
    return this;
  }
  
  
  public HttpEntityParser disableAllCoders() {
    buffer.getCoderFactory().clearCoders();
    return this;
  }
  
  
  public HttpEntityParser disableCryptCoder() {
    buffer.getCoderFactory().setCryptCoderEnabled(false, null);
    return this;
  }
  
  
  public HttpEntityParser enableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(true);
    return this;
  }
  
  
  public HttpEntityParser disableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(false);
    return this;
  }
  
  
  public HttpEntityParser enableBase64Coder() {
    buffer.getCoderFactory().setBase64CoderEnabled(true);
    return this;
  }
  
  
  public HttpEntityParser disableBase64Coder() {
    buffer.getCoderFactory().setBase64CoderEnabled(false);
    return this;
  }
  
  
  public Object getObject() {
    return obj;
  }
  
  
  public CryptKey getCryptKey() {
    return key;
  }
  
  
  public InputStream getInputStream() {
    return input;
  }
  
  
  private String readFive(InputStream is) throws IOException {
    if(is == null) return null;
    return StreamUtils.readString(is, 5);
  }
  
  
  public HttpEntityParser parse(HttpEntity entity) throws IOException {
    if(entity == null)
      throw new IllegalArgumentException(
          "[EntityParser.parse( HttpEntity )] "
              + "Invalid HttpEntity {"+ entity+ "}");
    
    buffer.clear();
    //buffer = new MixedWriteBuffer();
    InputStream contstream = entity.getContent();
    
    String five = readFive(contstream);
    if(!XmlConsts.START_XML.equals(five)) {
      throw new IOException("[EntityParser.parse( HttpEntity )] "
          + "Invalid Content to Parse {expected="
          + XmlConsts.START_XML+ ", read="+ five+ "}");
    }
    
    five = readFive(contstream);
    five = tryCryptKey(contstream, five);
    
    if(!XmlConsts.START_CONTENT.equals(five)) {
      throw new IOException("[EntityParser.parse( HttpEntity )] "
          + "Invalid Content to Parse {expected="
          + XmlConsts.START_CONTENT+ ", read="+ five+ "}");
    }
    
    IO.tr(contstream, buffer.getRawOutputStream());
    // get decoding stream
    contstream = buffer.getReadBuffer().getInputStream();
    EntityUtils.consume(entity);
    
    five = readFive(contstream);
    five = tryObject(contstream, five);
    tryStream(contstream, five);
    return this;
  }
  
  
  private String tryCryptKey(InputStream is, String five) throws IOException {
    if(five == null || five.trim().isEmpty() || is == null)
      return five;
    if(XmlConsts.START_CRYPT_KEY.contains(five)) {
      StreamUtils.readUntil(is, XmlConsts.GT);
      StreamResult sr = StreamUtils.readStringUntil(is, XmlConsts.END_CRYPT_KEY);
      key = CryptKey.fromString(sr.content());
      this.enableCryptCoder(key);
      five = readFive(is);
    }
    return five;
  }
  
  
  private String tryObject(InputStream is, String five) throws IOException {
    if(five == null || five.trim().isEmpty() || is == null)
      return five;
    if(XmlConsts.START_ROB.equals(five)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      StreamUtils.transferUntil(is, bos, XmlConsts.END_ROB);
      obj = serial.fromBytes(bos.toByteArray());
      five = readFive(is);
    }
    return five;
  }
  
  
  private void tryStream(InputStream is, String five) throws IOException {
    if(five == null || five.trim().isEmpty() || is == null)
      return;
    if(XmlConsts.START_STREAM.contains(five)) {
      MixedWriteBuffer inbuf = new MixedWriteBuffer();
      StreamUtils.readUntil(is, XmlConsts.GT);
      StreamUtils.transferUntil(is, inbuf.getRawOutputStream(), XmlConsts.END_STREAM);
      input = inbuf.getReadBuffer().getRawInputStream();
    }
  }
  
}
