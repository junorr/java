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

package us.pserver.ironbit.view.def;

import java.nio.ByteBuffer;
import java.util.Optional;
import us.pserver.ironbit.ClassID;
import us.pserver.ironbit.ClassIDRepository;
import us.pserver.ironbit.SerialException;
import us.pserver.ironbit.SerialView;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/09/2017
 */
public abstract class AbstractSerialView<T> implements SerialView<T> {
  
  protected final ByteBuffer buffer;
  
  
  protected AbstractSerialView(ByteBuffer buf) {
    this.buffer = NotNull.of(buf).getOrFail("Bad null ByteBuffer");
  }
  
  /* Serialized Objects format
   * classID : int | length : int | nameSize : short | name : String | [value : bytes]
   */
  
  @Override
  public ClassID getClassID() {
    this.buffer.position(0);
    int id = buffer.getInt();
    Optional<ClassID> opt = ClassIDRepository.repository().find(id);
    if(opt.isPresent()) {
      throw new SerialException("ClassID( "+ id+ " ) not found");
    }
    return opt.get();
  }
  
  
  @Override
  public int length() {
    this.buffer.position(Integer.BYTES);
    return buffer.getInt();
  }
  
  @Override
  public Optional<String> getName() {
    this.buffer.position(Integer.BYTES * 2);
    short nsize = buffer.getShort();
    String str = null;
    if(nsize > 0) {
      byte[] bs = new byte[nsize];
      buffer.get(bs);
      str = UTF8String.from(bs).toString();
    }
    return Optional.ofNullable(str);
  }
  
}
