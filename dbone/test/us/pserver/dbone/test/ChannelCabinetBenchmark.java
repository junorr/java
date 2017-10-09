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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import us.pserver.dbone.volume.DefaultVolume;
import us.pserver.dbone.serial.FSTSerializationService;
import us.pserver.dbone.serial.GsonSerializationService;
import us.pserver.dbone.serial.JavaSerializationService;
import us.pserver.dbone.serial.JsonIoSerializationService;
import us.pserver.dbone.volume.Record;
import us.pserver.dbone.store.Storage;
import us.pserver.dbone.store.StorageFactory;
import us.pserver.dbone.volume.Volume;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectUID;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/09/2017
 */
public class ChannelCabinetBenchmark {

  
  public static List<MappedValue> genValues() {
    Timer tm = new Timer.Nanos().start();
    ArrayList<MappedValue> vals = new ArrayList<>(1_000_000);
    for(int i = 0; i < 1_000_000; i++) {
      vals.add(MappedValue.of(Math.random() * 1_000_000));
    }
    tm.stop();
    System.out.println("-- time to generate "+ vals.size()+ " elements "+ tm+ " --");
    return vals;
  }
  
  
  //public static List<Record> putValues(AsyncVolume2 vol, List<MappedValue> lst) {
  public static List<Record> putValues(Volume vol, List<MappedValue> lst) {
    ArrayList<Record> recs = new ArrayList<>(1_000_000);
    Timer tm = new Timer.Nanos().start();
    while(!lst.isEmpty()) {
      MappedValue val = lst.remove(0);
      ObjectUID uid = ObjectUID.builder().of(val).build();
      recs.add(vol.put(uid, val));
    }
    tm.stop();
    System.out.println("-- time to put "+ recs.size()+ " elements "+ tm+ " --");
    return recs;
  }
  
  
  //public static void getOrdered(AsyncVolume2 vol, List<Record> recs) {
  public static void getOrdered(Volume vol, List<Record> recs) {
    int size = recs.size();
    Timer tm = new Timer.Nanos().start();
    for(Record r : recs) {
      vol.get(r);
    }
    tm.stop();
    System.out.println("-- time to get ordered "+ size+ " elements "+ tm+ " --");
  }
  
  
  //public static void getShuffled(AsyncVolume2 vol, List<Record> recs) {
  public static void getShuffled(Volume vol, List<Record> recs) {
    int size = recs.size();
    Collections.shuffle(recs);
    Timer tm = new Timer.Nanos().start();
    for(Record r : recs) {
      vol.get(r);
    }
    tm.stop();
    System.out.println("-- time to get shuffled"+ size+ " elements "+ tm+ " --");
  }
  
  
  public static void main(String[] args) throws IOException, InterruptedException {
    Path dbpath = Paths.get("/home/juno/dbone-channel.dat");
    Storage stg = StorageFactory.newFactory()
        //.setFile("/storage/dbone-channel.dat")
        .setFile(dbpath)
        .setBlockSize(1024)
        .createNoLock();
    //AsyncVolume2 vol = new AsyncVolume2(stg);
    //Volume vol = new DefaultVolume(stg, new JavaSerializationService());
    Volume vol = new DefaultVolume(stg, new FSTSerializationService());
    //Volume vol = new DefaultVolume(stg, new GsonSerializationService());
    //Volume vol = new DefaultVolume(stg, new JsonIoSerializationService());
    
    List<Record> recs = putValues(vol, genValues());
    //Thread.sleep(2000);
    getOrdered(vol, recs);
    getShuffled(vol, recs);
    //Engine.get().waitShutdown();
    vol.close();
    Files.delete(dbpath);
  }
  
}
