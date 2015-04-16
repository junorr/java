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

import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.streams.IO;
import us.pserver.streams.MixedWriteBuffer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 08/04/2015
 */
public class EntityFactory {

  public static final ContentType 
      TYPE_X_JAVA_ROB = ContentType.create(
          "application/x-java-rob", Consts.UTF_8);
  
  
  private static EntityFactory instance;
  
  private MixedWriteBuffer buffer;
  
  private final StringByteConverter scv;
  
  private ContentType type;
  
  private CryptKey key;
  
  private Object obj;
  
  private InputStream input;
  
  
  public EntityFactory(ContentType type) {
    if(type == null)
      type = TYPE_X_JAVA_ROB;
    this.type = type;
    buffer = new MixedWriteBuffer();
    scv = new StringByteConverter();
    key = null;
    obj = null;
    input = null;
  }
  
  
  public EntityFactory(String stype) {
    this(ContentType.create(stype));
  }
  
  
  public EntityFactory() {
    this(TYPE_X_JAVA_ROB);
  }
  
  
  public static EntityFactory factory(ContentType type) {
    if(instance == null) 
      instance = new EntityFactory(type);
    return instance;
  }
  
  
  public static EntityFactory factory(String stype) {
    if(instance == null) 
      instance = new EntityFactory(stype);
    return instance;
  }
  
  
  public static EntityFactory factory() {
    if(instance == null) 
      instance = new EntityFactory();
    return instance;
  }
  
  
  public EntityFactory enableCryptCoder(CryptKey key) {
    if(key != null) {
      buffer.getCoderFactory().setCryptCoderEnabled(true, key);
      this.key = key;
    }
    return this;
  }
  
  
  public EntityFactory disableAllCoders() {
    buffer.getCoderFactory().clearCoders();
    return this;
  }
  
  
  public EntityFactory disableCryptCoder() {
    buffer.getCoderFactory().setCryptCoderEnabled(false, null);
    return this;
  }
  
  
  public EntityFactory enableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(true);
    return this;
  }
  
  
  public EntityFactory disableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(false);
    return this;
  }
  
  
  public EntityFactory enableBase64Coder() {
    buffer.getCoderFactory().setBase64CoderEnabled(true);
    return this;
  }
  
  
  public EntityFactory disableBase64Coder() {
    buffer.getCoderFactory().setBase64CoderEnabled(false);
    return this;
  }
  
  
  public EntityFactory put(CryptKey key) {
    if(key != null) {
      this.key = key;
    }
    return this;
  }
  
  
  public EntityFactory put(Object obj) {
    if(obj != null) {
      this.obj = obj;
    }
    return this;
  }
  
  
  public EntityFactory put(InputStream is) {
    if(is != null) {
      this.input = is;
    }
    return this;
  }
  
  
  public HttpEntity create() throws IOException {
    if(key == null && obj == null && input == null)
      return null;
    
    buffer.clear();
    buffer.write(scv.convert(Tags.START_XML));
    OutputStream os = buffer.getOutputStream();
    
    if(key != null) {
      buffer.write(scv.convert(Tags.START_CRYPT_KEY));
      buffer.write(scv.convert(key.toString()));
      buffer.write(scv.convert(Tags.END_CRYPT_KEY));
    }
    if(obj != null || input != null) {
      buffer.write(scv.convert(Tags.START_CONTENT));
    }
    if(obj != null) {
      os.write(scv.convert(Tags.START_ROB));
      String js = JsonWriter.objectToJson(obj);
      os.write(scv.convert(js));
      os.write(scv.convert(Tags.END_ROB));
      os.flush();
    }
    if(input != null) {
      os.write(scv.convert(Tags.START_STREAM));
      IO.tr(input, os);
      os.write(scv.convert(Tags.END_STREAM));
      os.flush();
    }
    if(obj != null || input != null) {
      os.write(scv.convert(Tags.END_CONTENT));
      os.flush();
    }
    os.write(scv.convert(Tags.END_XML));
    os.flush();
    os.close();
    
    InputStream istream = buffer.getReadBuffer().getRawInputStream();
    return new InputStreamEntity(istream, istream.available(), type);
  }
  
  
  public static void main(String[] args) throws IOException {
    EntityFactory fac = EntityFactory.factory()
        //.enableGZipCoder()
        .enableCryptCoder(
            CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5));
    class MSG {
      String str;
      public MSG(String s) { str = s; }
      public String toString() { return "MSG{str="+ str+ "}"; }
    }
    fac.put(new MSG("Hello EntityFactory!"));
    HttpEntity ent = fac.create();
    ent.writeTo(System.out);
    System.out.println();
    
    ent = fac.create();
    EntityParser ep = EntityParser.create();//.enableGZipCoder();
    ep.parse(ent);
    System.out.println("* key: "+ ep.getCryptKey());
    System.out.println("* rob: "+ ep.getObject());
    EntityUtils.consume(ent);
  }
  
}
