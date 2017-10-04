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

package us.pserver.ironbit.record;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import us.pserver.ironbit.ClassID;
import us.pserver.ironbit.IronbitConfiguration;
import us.pserver.ironbit.IronbitException;
import us.pserver.tools.NotNull;
import us.pserver.ironbit.Serializer;
import us.pserver.ironbit.serial.IntegerSerializer;
import us.pserver.ironbit.serial.ShortSerializer;
import us.pserver.ironbit.serial.StringSerializer;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/09/2017
 */
public class ComposeableSerialRecord<T> implements SerialRecord<List<SerialRecord<?>> {
  
  protected final IntegerSerializer ints;
  
  protected final ShortSerializer shorts;
  
  protected final StringSerializer strings;
  
  protected final byte[] bytes;
  
  
  public ComposeableSerialRecord(byte[] bs) {
    ints = new IntegerSerializer();
    shorts = new ShortSerializer();
    strings = new StringSerializer();
    bytes = NotNull.of(bs).getOrFail("Bad null byte[] array");
    serial = IronbitConfiguration.get().serializers().get(ints.deserialize(bytes));
  }
  
  
  public ComposeableSerialRecord(String name, T obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    ints = new IntegerSerializer();
    shorts = new ShortSerializer();
    strings = new StringSerializer();
    
    Optional<Serializer> opt = IronbitConfiguration.get().findSerializer(obj.getClass());
    if(!opt.isPresent()) {
      throw new IronbitException("Serializer not found for: "+ obj.getClass().getName());
    }
    serial = opt.get();
    ByteBufferOutputStream bos = new ByteBufferOutputStream(1024);
    ClassID cid = IronbitConfiguration.get().registerClassID(obj.getClass());
    byte[] bobj = serial.serialize(obj);
    short nameSize = 0;
    byte[] bname = new byte[0];
    if(name != null) {
      bname = strings.serialize(name);
      nameSize = (short) bname.length;
    }
    int length = offsetName() + nameSize + bobj.length;
    bos.write(ints.serialize(cid.getID()));
    bos.write(ints.serialize(length));
    bos.write(shorts.serialize(nameSize));
    bos.write(bname);
    bos.write(bobj);
    ByteBuffer buf = bos.toByteBuffer();
    this.bytes = new byte[buf.remaining()];
    buf.get(this.bytes);
  }
  
  /* Serialized Objects format
   * classID : int | length : int | nameSize : short | name : String | [value : bytes]
   */
  
  @Override
  public ClassID getClassID() {
    int id = ints.deserialize(bytes);
    Optional<ClassID> opt = IronbitConfiguration.get().findClassID(id);
    if(!opt.isPresent()) {
      throw new IronbitException("ClassID( "+ id+ " ) not found");
    }
    return opt.get();
  }
  
  
  @Override
  public int length() {
    return bytes.length;
  }
  
  
  @Override
  public Optional<String> getName() {
    Optional<String> opt = Optional.empty();
    int nameSize = nameSize();
    if(nameSize > 0) {
      byte[] bs = new byte[nameSize];
      System.arraycopy(bytes, offsetName(), bs, 0, bs.length);
      opt = Optional.of(strings.deserialize(bs));
    }
    return opt;
  }
  
  
  @Override
  public T getValue() {
    byte[] bs = new byte[valueSize()];
    System.arraycopy(bytes, offsetValue(), bs, 0, bs.length);
    return serial.deserialize(bs);
  }
  
  
  protected int offsetNameSize() {
    return Integer.BYTES * 2;
  }
  
  
  protected int nameSize() {
    byte[] bs = new byte[Short.BYTES];
    System.arraycopy(bytes, offsetNameSize(), bs, 0, bs.length);
    return shorts.deserialize(bs);
  }
  
  
  protected int offsetName() {
    return offsetNameSize() + Short.BYTES;
  }
  
  
  protected int offsetValue() {
    return offsetName() + nameSize();
  }
  
  
  protected int valueSize() {
    return bytes.length - offsetValue();
  }
  
  
  @Override
  public byte[] getBytes() {
    return bytes;
  }
  
}
