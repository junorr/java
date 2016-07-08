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

package gson.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public class SerialObject {

  private byte[] serial;
  
  private transient Object obj;
  
  
  private SerialObject() {}
  
  
  public SerialObject(Object s) {
    Sane.of(s).get(Checkup.isNotNull());
    obj = s;
  }
  
  
  public SerialObject(byte[] serial) {
    this.serial = Sane.of(serial).get(Checkup.isNotNull());
  }
  
  
  public byte[] serial() {
    return serial;
  }
  
  
  public Object object() {
    return obj;
  }
  
  
  public SerialObject serialize() throws IOException {
    if(obj != null) {
      try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(obj);
        serial = bos.toByteArray();
      } catch(InvalidClassException | NotSerializableException ex) {
        throw new IOException(ex);
      }
    }
    return this;
  }
  
  
  public SerialObject deserialize() throws IOException {
    if(serial != null && serial.length > 0) {
      try {
        obj = (Serializable) new ObjectInputStream(
            new ByteArrayInputStream(serial)
        ).readObject();
      } catch(ClassNotFoundException 
          | InvalidClassException 
          | StreamCorruptedException 
          | OptionalDataException ex) {
        throw new IOException(ex);
      }
    }
    return this;
  }
  
}
