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

package us.pserver.dbone.serial.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import us.pserver.dbone.serial.Deserializer;
import us.pserver.dbone.serial.SerializationService;
import us.pserver.dbone.serial.Serializer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/06/2018
 */
public class JacksonSerializationService implements SerializationService {
  
  private final IntFunction<ByteBuffer> alloc;
  
  private final ObjectMapper omp;
  
  private final JacksonSerializer serializer;
  
  private final JacksonDeserializer deserializer;
  
  
  public JacksonSerializationService(ObjectMapper mpr, IntFunction<ByteBuffer> allocpolicy) {
    this.omp = Objects.requireNonNull(mpr, "Bad null ObjectMapper");
    this.alloc = Objects.requireNonNull(allocpolicy, "Bad null ObjectMapper");
    serializer = new JacksonSerializer(mpr);
    deserializer = new JacksonDeserializer(mpr);
  }
  
  public JacksonSerializationService(IntFunction<ByteBuffer> allocpolicy) {
    this(createOM(), allocpolicy);
  }
  
  public JacksonSerializationService() {
    this(ByteBuffer::allocate);
  }
  
  
  private static ObjectMapper createOM() {
    ObjectMapper mapper = new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    return mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }
  
  
  @Override
  public <T> Serializer<T> getSerializer(Class<T> cls) {
    return (Serializer<T>) serializer;
  }
  
  
  @Override
  public <T> Deserializer<T> getDeserializer(Class<T> cls) {
    return (Deserializer<T>) deserializer;
  }
  
  
  @Override
  public <T> ByteBuffer serialize(T value) throws IOException {
    return serializer.apply(value, this);
  }
  
  
  @Override
  public <T> T deserialize(Class<T> cls, ByteBuffer buf) throws IOException {
    return (T) deserializer.apply(cls, buf, this);
  }
  
  
  @Override
  public IntFunction<ByteBuffer> getByteBufferAllocPolicy() {
    return alloc;
  }
  
}
