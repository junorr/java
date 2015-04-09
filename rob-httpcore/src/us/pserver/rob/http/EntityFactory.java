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

import com.cedarsoftware.util.io.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.streams.IO;
import us.pserver.streams.MultiCoderBuffer;
import us.pserver.streams.StreamCoderFactory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 08/04/2015
 */
public class EntityFactory {

  public static final ContentType 
      TYPE_X_ROB_OBJECT = ContentType.create(
          "application/x-rob-object", Consts.UTF_8),
      TYPE_X_ROB_KEY = ContentType.create(
          "application/x-rob-key", Consts.UTF_8),
      TYPE_X_ROB_STREAM = ContentType.create(
          "application/x-rob-stream", Consts.UTF_8);
  
  
  private static EntityFactory instance;
  
  private final MultiCoderBuffer buffer;
  
  private final StringByteConverter scv;
  
  private ContentType type;
  
  
  public EntityFactory(ContentType type) {
    if(type == null)
      type = TYPE_X_ROB_OBJECT;
    this.type = type;
    buffer = new MultiCoderBuffer();
    scv = new StringByteConverter();
  }
  
  
  public EntityFactory(String stype) {
    this(ContentType.create(stype));
  }
  
  
  public EntityFactory() {
    this(TYPE_X_ROB_OBJECT);
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
      buffer.setCryptCoderEnabled(true, key);
    }
    return this;
  }
  
  
  public EntityFactory disableAllCoders() {
    buffer.clearCoders();
    return this;
  }
  
  
  public EntityFactory disableCryptCoder() {
    buffer.setCryptCoderEnabled(false, null);
    return this;
  }
  
  
  public EntityFactory enableGZipCoder() {
    buffer.setGZipCoderEnabled(true);
    return this;
  }
  
  
  public EntityFactory disableGZipCoder() {
    buffer.setGZipCoderEnabled(false);
    return this;
  }
  
  
  public EntityFactory enableBase64Coder() {
    buffer.setBase64CoderEnabled(true);
    return this;
  }
  
  
  public EntityFactory disableBase64Coder() {
    buffer.setBase64CoderEnabled(false);
    return this;
  }
  
  
  public EntityFactory enableHexCoder() {
    buffer.setHexCoderEnabled(true);
    return this;
  }
  
  
  public EntityFactory disableHexCoder() {
    buffer.setHexCoderEnabled(false);
    return this;
  }
  
  
  public EntityFactory enableLzmaCoder() {
    buffer.setLzmaCoderEnabled(true);
    return this;
  }
  
  
  public EntityFactory disableLzmaCoder() {
    buffer.setLzmaCoderEnabled(false);
    return this;
  }
  
  
  public HttpEntity create(Object obj) throws IOException {
    if(obj == null) 
      throw new IllegalArgumentException(
          "[ObjectEntityFactory.create( Object )] Invalid Object: '"+ obj+ "'");
    
    String js = JsonWriter.objectToJson(obj);
    byte[] bs = scv.convert(js);
    
    if(streamfac.isAnyCoderEnabled()) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ByteArrayInputStream bis = new ByteArrayInputStream(bs);
      OutputStream os = streamfac.create(bos);
      IO.tc(bis, os);
      bs = bos.toByteArray();
      bos.reset();
      bos = null;
      bis.reset();
      bis = null;
    }
    
    return new ByteArrayEntity(bs, type);
  }
  
  
  public HttpEntity create(InputStream is) throws IOException {
    if(obj == null) 
      throw new IllegalArgumentException(
          "[ObjectEntityFactory.create( Object )] Invalid Object: '"+ obj+ "'");
    
    String js = JsonWriter.objectToJson(obj);
    byte[] bs = scv.convert(js);
    
    if(streamfac.isAnyCoderEnabled()) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ByteArrayInputStream bis = new ByteArrayInputStream(bs);
      OutputStream os = streamfac.create(bos);
      IO.tc(bis, os);
      bs = bos.toByteArray();
      bos.reset();
      bos = null;
      bis.reset();
      bis = null;
    }
    
    return new ByteArrayEntity(bs, type);
  }
  
}
