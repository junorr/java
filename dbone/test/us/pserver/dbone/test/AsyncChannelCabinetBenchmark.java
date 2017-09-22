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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import us.pserver.dbone.store.AsyncFileChannelStorage;
import us.pserver.dbone.store.AsyncVolume;
import us.pserver.dbone.store.Record;
import us.pserver.dbone.store.StorageFactory;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectUID;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/09/2017
 */
public class AsyncChannelCabinetBenchmark {

  
  public static List<MappedValue> genValues() {
    Timer tm = new Timer.Nanos().start();
    List<MappedValue> vals = Collections.synchronizedList(new ArrayList<>(1_000_000));
    for(int i = 0; i < 1_000_000; i++) {
      vals.add(MappedValue.of(Math.random() * 1_000_000));
    }
    tm.stop();
    System.out.println("-- time to generate "+ vals.size()+ " elements "+ tm+ " --");
    return vals;
  }
  
  
  public static List<Record> putValues(AsyncVolume vol, List<MappedValue> lst) {
    List<Record> recs = Collections.synchronizedList(new ArrayList<>(1_000_000));
    Timer tm = new Timer.Nanos().start();
    for(MappedValue v : lst) {
      ObjectUID uid = ObjectUID.builder().of(v).build();
      recs.add(vol.put(uid, v));
      recs.add(vol.put(uid, v));
    }
    tm.stop();
    System.out.println("-- time to put "+ recs.size()+ " elements "+ tm+ " --");
    return recs;
  }
  
  
  public static void getOrdered(AsyncVolume vol, List<Record> recs) {
    int size = recs.size();
    Timer tm = new Timer.Nanos().start();
    for(Record r : recs) {
      vol.get(r, su->{});
    }
    tm.stop();
    System.out.println("-- time to get ordered "+ size+ " elements "+ tm+ " --");
  }
  
  
  public static void getShuffled(AsyncVolume vol, List<Record> recs) {
    int size = recs.size();
    Collections.shuffle(recs);
    Timer tm = new Timer.Nanos().start();
    for(Record r : recs) {
      vol.get(r, su->{});
    }
    tm.stop();
    System.out.println("-- time to get shuffled"+ size+ " elements "+ tm+ " --");
  }
  
  
  public static void main(String[] args) throws IOException {
    AsyncFileChannelStorage stg = (AsyncFileChannelStorage) StorageFactory.newFactory()
        .setFile("/storage/dbone-async-channel.dat")
        .setBlockSize(512)
        .createAsync();
    AsyncVolume vol = new AsyncVolume(stg);
    
    List<Record> recs = putValues(vol, genValues());
    getOrdered(vol, recs);
    getShuffled(vol, recs);
    vol.close();
  }
  
}
