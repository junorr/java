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

package us.pserver.dbone.serial;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import us.pserver.dbone.index.Index;
import us.pserver.dbone.util.Log;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/06/2018
 */
public enum SerializationService {

  INSTANCE();
  
  private SerializationService() {
    DefaultSerialAdapter ss = new DefaultSerialAdapter();
    serialize = new AtomicReference(ss);
    deserialize = new AtomicReference(ss);
    ObjectMapper mapper = new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    objectMapper = new AtomicReference(mapper
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    );
  }
  
  private final AtomicReference<Serializer> serialize;
  
  private final AtomicReference<Deserializer> deserialize;
  
  private final AtomicReference<ObjectMapper> objectMapper;
  
  
  public SerializationService configureSerializer(Serializer slz) {
    serialize.set(Objects.requireNonNull(slz, "Bad null Serializer"));
    return INSTANCE;
  }
  
  public SerializationService configureDeserializer(Deserializer dsz) {
    deserialize.set(Objects.requireNonNull(dsz, "Bad null Deserializer"));
    return INSTANCE;
  }
  
  public SerializationService configureSerialAdapter(SerialAdapter sra) {
    serialize.set(sra);
    deserialize.set(sra);
    return INSTANCE;
  }
  
  public SerializationService configureObjectMapper(ObjectMapper omp) {
    objectMapper.set(Objects.requireNonNull(omp, "Bad null ObjectMapper"));
    return INSTANCE;
  } 
  
  public Serializer getSerializer() {
    return serialize.get();
  }
  
  public Deserializer getDeserializer() {
    return deserialize.get();
  }
  
  public ObjectMapper getObjectMapper() {
    return objectMapper.get();
  }
  
  
  
  
  
  public static class DefaultSerialAdapter implements SerialAdapter {
    
    @Override
    public ByteBuffer apply(Object o) throws IOException {
      String json = INSTANCE.getObjectMapper()
          .writerFor(o.getClass()).writeValueAsString(o);
      Log.on("json = %s", json);
      return StandardCharsets.UTF_8.encode(json);
    }
    
    @Override
    public <T> T apply(Class<T> cls, ByteBuffer buf) throws IOException {
      String json = StandardCharsets.UTF_8.decode(buf).toString();
      Log.on("cls=%s, json=%s", cls, json);
      //return INSTANCE.getObjectMapper().readerFor(cls).readValue(json);
      return INSTANCE.getObjectMapper().readValue(json, cls);
    }
    
  }
  
}
