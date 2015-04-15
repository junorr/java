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

package us.pserver.rob.http;

import com.cedarsoftware.util.io.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.streams.IO;
import us.pserver.streams.MixedWriteBuffer;
import us.pserver.streams.StreamResult;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/04/2015
 */
public class EntityParser {

  private MixedWriteBuffer buffer;
  
  private InputStream input;
  
  private Object obj;
  
  private CryptKey key;
  
  
  public EntityParser() {
    buffer = new MixedWriteBuffer();
  }
  
  
  public static EntityParser create() {
    return new EntityParser();
  }
  
  
  public EntityParser enableCryptCoder(CryptKey key) {
    if(key != null) {
      buffer.getCoderFactory().setCryptCoderEnabled(true, key);
    }
    return this;
  }
  
  
  public EntityParser disableAllCoders() {
    buffer.getCoderFactory().clearCoders();
    return this;
  }
  
  
  public EntityParser disableCryptCoder() {
    buffer.getCoderFactory().setCryptCoderEnabled(false, null);
    return this;
  }
  
  
  public EntityParser enableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(true);
    return this;
  }
  
  
  public EntityParser disableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(false);
    return this;
  }
  
  
  public EntityParser enableBase64Coder() {
    buffer.getCoderFactory().setBase64CoderEnabled(true);
    return this;
  }
  
  
  public EntityParser disableBase64Coder() {
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
  
  
  public EntityParser parse(HttpEntity entity) throws IOException {
    if(entity == null)
      throw new IllegalArgumentException(
          "[EntityParser.parse( HttpEntity )] "
              + "Invalid MixedWriteBuffer {entity="+ entity+ "}");
    
    buffer.clear();
    InputStream contstream = entity.getContent();
    
    String five = readFive(contstream);
    if(!Tags.START_XML.equals(five)) {
      throw new IOException("[EntityParser.parse()] "
          + "Invalid Content to Parse {expected="
          + Tags.START_XML+ ", read="+ five+ "}");
    }
    
    five = readFive(contstream);
    five = tryCryptKey(contstream, five);
    
    if(!Tags.START_CONTENT.equals(five)) {
      throw new IOException("[EntityParser.parse()] "
          + "Invalid Content to Parse {expected="
          + Tags.START_CONTENT+ ", read="+ five+ "}");
    }
    
    IO.tr(contstream, buffer.getRawOutputStream());
    contstream = buffer.getReadBuffer().getInputStream();
    EntityUtils.consume(entity);
    
    five = readFive(contstream);
    five = tryObject(contstream, five);
    tryStream(contstream, five);
    return this;
  }
  
  
  private String tryCryptKey(InputStream is, String five) throws IOException {
    if(five == null || five.trim().isEmpty()
        || is == null)
      return five;
    if(Tags.START_CRYPT_KEY.contains(five)) {
      StreamUtils.readUntil(is, Tags.GT);
      StreamResult sr = StreamUtils.readStringUntil(is, Tags.END_CRYPT_KEY);
      key = CryptKey.fromString(sr.content());
      this.enableCryptCoder(key);
      five = readFive(is);
    }
    return five;
  }
  
  
  private String tryObject(InputStream is, String five) throws IOException {
    if(five == null || five.trim().isEmpty()
        || is == null)
      return five;
    if(Tags.START_ROB.contains(five)) {
      StreamResult sr = StreamUtils.readStringUntil(is, Tags.END_ROB);
      obj = JsonReader.jsonToJava(sr.content());
      five = readFive(is);
    }
    return five;
  }
  
  
  private void tryStream(InputStream is, String five) throws IOException {
    if(five == null || five.trim().isEmpty()
        || is == null)
      return;
    if(Tags.START_STREAM.contains(five)) {
      MixedWriteBuffer inbuf = new MixedWriteBuffer();
      StreamUtils.readUntil(is, Tags.GT);
      StreamUtils.transferUntil(is, inbuf.getRawOutputStream(), Tags.END_STREAM);
      input = inbuf.getReadBuffer().getRawInputStream();
    }
  }
  
}
