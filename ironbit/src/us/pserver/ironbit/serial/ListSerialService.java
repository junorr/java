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

package us.pserver.ironbit.serial;

import java.util.List;
import java.util.Optional;
import org.objenesis.ObjenesisStd;
import us.pserver.ironbit.ClassID;
import us.pserver.ironbit.IronbitConfiguration;
import us.pserver.ironbit.IronbitException;
import us.pserver.ironbit.SerialCommons;
import us.pserver.ironbit.SerialService;
import us.pserver.ironbit.record.DefaultSerialRecord;
import us.pserver.ironbit.record.SerialRecord;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/10/2017
 */
public class ListSerialService implements SerialService<List<?>> {

  @Override
  public SerialRecord serialize(String name, List<?> obj) {
    ByteBufferOutputStream bos = new ByteBufferOutputStream();
    for(Object o : obj) {
      bos.write(getSerialService(o.getClass()).serialize(o).toByteArray());
    }
    return new DefaultSerialRecord(IronbitConfiguration.get().registerClassID(obj.getClass()), name, bos.toByteArray());
  }
  
  private SerialService getSerialService(Class cls) {
    Optional<SerialService<Object>> ss = IronbitConfiguration.get().<Object>findSerialService(cls);
    if(!ss.isPresent()) {
      throw new IronbitException("SerialService not found for class "+ cls.getName());
    }
    return ss.get();
  }

  @Override
  public List<?> deserialize(SerialRecord rec) {
    byte[] val = rec.getValue();
    ClassID cid = rec.getClassID();
    //ObjenesisStd ost = new ObjenesisStd();
    //List lst = (List) ost.getInstantiatorOf(cid.getClazz()).newInstance();
    List lst = (List) IronbitException.rethrow(()->rec.getClassID().getClazz().newInstance());
    int idx = 0;
    int len = 0;
    while(idx + 1 < val.length) {
      //System.out.println("* List.deserialize: idx="+ idx+ ", val.len="+ val.length);
      len = SerialCommons.readInt(val, idx + Integer.BYTES);
      //System.out.println("len="+ len);
      SerialRecord r = new DefaultSerialRecord(val, idx, len);
      cid = r.getClassID();
      lst.add(getSerialService(cid.getClazz()).deserialize(r));
      idx += len;
    }
    return lst;
  }

}
