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

package us.pserver.dbone.test;

import us.pserver.dbone.store.DefaultVolume;
import us.pserver.dbone.store.StorageFactory;
import us.pserver.dbone.store.Volume;
import us.pserver.dbone.store.VolumeTransaction;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectUID;
import us.pserver.dbone.store.Record;
import us.pserver.dbone.store.Storage;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/09/2017
 */
public class TestVolume {

  
  public static void main(String[] args) throws Throwable {
    //Storage fs = StorageFactory.newFactory().setFile("/storage/dbone.dat").setOpenForced().create();
    Storage fs = StorageFactory.newFactory().createDirect(32*1024);
    Volume volume = new DefaultVolume(fs);
    MappedValue val = MappedValue.of(5);
    ObjectUID uid = ObjectUID.builder().of(val).build();
    Record rec = volume.put(uid, val);
    System.out.println(rec);
    System.out.println(volume.get(rec));
    
    VolumeTransaction vtx = volume.startTransaction();
    val = MappedValue.of(8);
    uid = ObjectUID.builder().of(val).build();
    rec = vtx.put(uid, val);
    System.out.println(rec);
    System.out.println(vtx.get(rec));
    vtx.rollback();
    
    val = MappedValue.of(2);
    uid = ObjectUID.builder().of(val).build();
    rec = volume.put(uid, val);
    System.out.println(rec);
    System.out.println(volume.get(rec));
    
    volume.close();
  }
  
}
