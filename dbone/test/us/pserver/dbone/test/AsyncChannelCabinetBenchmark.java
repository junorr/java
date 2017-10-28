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
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import us.pserver.coreone.Core;
import us.pserver.dbone.ObjectUID;
import us.pserver.dbone.serial.JavaSerializationService;
import us.pserver.dbone.serial.SerializationService;
import us.pserver.dbone.volume.Record;
import us.pserver.dbone.store.Storage;
import us.pserver.dbone.store.StorageFactory;
import us.pserver.dbone.volume.AsyncVolume2;
import us.pserver.dbone.volume.StoreUnit;
import us.pserver.dbone.volume.Volume;
import us.pserver.tools.Sleeper;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/09/2017
 */
public class AsyncChannelCabinetBenchmark {

  
  public static List<StoreUnit> genValues(SerializationService serial) {
    Timer tm = new Timer.Nanos().start();
    ArrayList<StoreUnit> vals = new ArrayList<>(1_000_000);
    for(int i = 0; i < 1_000_000; i++) {
      ByteBuffer buf = serial.serialize((Math.random() * 1_000_000));
      vals.add(StoreUnit.of(ObjectUID.of(buf, Double.class.getName()), buf));
    }
    tm.stop();
    System.out.println("-- time to generate "+ vals.size()+ " elements "+ tm+ " --");
    return vals;
  }
  
  
  //public static List<Record> putValues(AsyncVolume2 vol, List<MappedValue> lst) {
  public static List<Record> putValues(AsyncVolume2 vol, List<StoreUnit> lst) {
    List<Record> recs = new ArrayList<>(1_000_000);
    Timer tm = new Timer.Nanos().start();
    Timer t = new Timer.Nanos().start();
    for(StoreUnit su : lst) {
      recs.add(vol.put(su));
      tm.lap();
    }
    tm.stop();
    System.out.println("-- time to put "+ recs.size()+ " elements "+ tm+ " --");
    tm.clear().start();
    Core.INSTANCE.waitRunningCycles();
    System.out.println("-- time waiting running cycles "+ tm.stop()+ " --");
    Sleeper.of(5000).sleep();
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
    try {
      Storage stg = StorageFactory.newFactory()
          //.setFile("/storage/dbone-channel.dat")
          .setFile(dbpath)
          .setBlockSize(1024)
          .create();

      SerializationService serial = new JavaSerializationService();
      //SerializationService serial = new FSTSerializationService();
      //SerializationService serial = new GsonSerializationService();
      //SerializationService serial = new JsonIoSerializationService();

      //AsyncVolume vol = new AsyncVolume(stg);
      AsyncVolume2 vol = new AsyncVolume2(stg);

      List<Record> recs = putValues(vol, genValues(serial));
      //Thread.sleep(10000);
      System.out.println("* reading...");
      getOrdered(vol, recs);
      getShuffled(vol, recs);
      //Engine.get().waitShutdown();
      vol.close();
    }
    finally {
      Files.delete(dbpath);
    }
  }
  
}
