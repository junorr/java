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
import java.util.Optional;
import us.pserver.ironbit.ClassID;
import us.pserver.ironbit.IronbitConfiguration;
import us.pserver.ironbit.IronbitException;
import us.pserver.ironbit.SerialCommons;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/09/2017
 */
public class DefaultSerialRecord implements SerialRecord {
  
  protected final byte[] bytes;
  
  
  public DefaultSerialRecord(byte[] bts) {
    this.bytes = bts;
  }
  
  
  public DefaultSerialRecord(byte[] bts, int off, int len) {
    this.bytes = new byte[len];
    System.arraycopy(bts, off, bytes, 0, len);
    //System.out.println("* DefaultSerialRecord.bytes.len="+ bytes.length);
  }
  
  
  public DefaultSerialRecord(ClassID cid, String name, byte[] val) {
    short nmlen = (short) (name == null ? 0 : name.length());
    bytes = new byte[HEADER_SIZE + nmlen + val.length];
    int idx = 0;
    SerialCommons.writeInt(cid.getID(), bytes, idx);
    idx += Integer.BYTES;
    SerialCommons.writeInt(bytes.length, bytes, idx);
    idx += Integer.BYTES;
    SerialCommons.writeShort(nmlen, bytes, idx);
    idx += Short.BYTES;
    if(nmlen > 0) {
      SerialCommons.writeString(name, bytes, idx);
    }
    idx += nmlen;
    System.arraycopy(val, 0, bytes, idx, val.length);
  }
  
  
  /* Serialized Objects format
   * classID : int | length : int | nameSize : short | name : String | [value : bytes]
   */
  
  @Override
  public ClassID getClassID() {
    int id = SerialCommons.readInt(bytes, 0);
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
  public String getName() {
    String str = "";
    int nameSize = nameSize();
    if(nameSize > 0) {
      str = SerialCommons.readString(bytes, offsetName(), nameSize);
    }
    return str;
  }
  
  
  @Override
  public byte[] getValue() {
    byte[] bs = new byte[valueSize()];
    System.arraycopy(bytes, offsetValue(), bs, 0, bs.length);
    return bs;
  }
  
  
  protected int offsetNameSize() {
    return Integer.BYTES * 2;
  }
  
  
  protected int nameSize() {
    return SerialCommons.readShort(bytes, offsetNameSize());
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
  public byte[] toByteArray() {
    return bytes;
  }
  
  
  @Override
  public ByteBuffer toByteBuffer() {
    return ByteBuffer.wrap(bytes);
  }
  
  
  @Override
  public String toString() {
    return String.format("SerialRecord{cid=%d, len=%d, name=%s}", this.getClassID().getID(), this.length(), this.getName());
  }
  
}
