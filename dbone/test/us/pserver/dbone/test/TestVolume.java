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
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import us.pserver.dbone.ObjectUID;
import us.pserver.dbone.serial.JavaSerializationService;
import us.pserver.dbone.volume.DefaultVolume;
import us.pserver.dbone.serial.SerializationService;
import us.pserver.dbone.store.StorageFactory;
import us.pserver.dbone.volume.Volume;
import us.pserver.dbone.volume.Record;
import us.pserver.dbone.store.Storage;
import us.pserver.dbone.store.StorageException;
import us.pserver.dbone.volume.StoreUnit;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/09/2017
 */
public class TestVolume {
  
  public static final PrintStream STDOUT = System.out;
  
  public static final PrintStream DEVNULL = StorageException.rethrow(()->new PrintStream("/dev/null"));
  
  public static final char[] lower = {
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
  };
  
  public static final char[] upper = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  };
  
  public static final char[] lowerVowel = {
    'a', 'e', 'i', 'o', 'u', 'y'
  };
  
  public static final char[] lowerConsonant = {
    'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'
  };
  
  
  public static final char[] upperVowel = {
    'A', 'E', 'I', 'O', 'U', 'Y'
  };
  
  public static final char[] upperConsonant = {
    'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Z'
  };
  
  
  public static void enableStdOut() {
    System.setOut(STDOUT);
  }

  
  public static void disableStdOut() {
    System.setOut(DEVNULL);
  }
  
  
  public static String randomString(int size) {
    StringBuilder str = new StringBuilder();
    boolean vowel = true;
    for(int i = 0; i < size; i++) {
      if(vowel) {
        int v = (int) (Math.random() * lowerVowel.length);
        str.append(lowerVowel[v]);
      }
      else {
        int v = (int) (Math.random() * lowerConsonant.length);
        str.append(lowerConsonant[v]);
      }
      vowel = !vowel;
    }
    return str.toString();
  }
  
  
  public static AObj randomAObj() {
    int[] magic = new int[5];
    for(int i = 0; i < 5; i++) {
      magic[i] = (int)(Math.random() * 100);
    }
    char[] cs = new char[5];
    for(int i = 0; i < 5; i++) {
      int v = (int) (Math.random() * upper.length);
      cs[i] = upper[v];
    }
    int age = (int)(Math.random() * 100);
    return new AObj(randomString(5), age, magic, cs, new Date());
  }
  
  
  public static BObj randomBObj() {
    List<Integer> ls = new ArrayList<>();
    for(int i = 0; i < 5; i++) {
      ls.add((int)(Math.random() * 100) + i);
    }
    BObj obj = new BObj(randomString(5), randomAObj(), ls);
    System.out.println(obj);
    return obj;
  }
  
  
  public static StoreUnit storeUnit(SerializationService serial) {
    BObj obj = randomBObj();
    Timer tm = new Timer.Nanos().start();
    ByteBuffer buf = serial.serialize(obj);
    ObjectUID ouid = ObjectUID.of(buf, obj.getClass().getName());
    System.out.println("-- time to serialize "+ tm.stop()+ " --");
    return StoreUnit.of(ouid, buf);
  }
  
  
  public static void execute(Volume volume, SerializationService serial) {
    StoreUnit unit = storeUnit(serial);
    Timer tm = new Timer.Nanos().start();
    Record rec = volume.put(unit);
    System.out.println("-- time to volume.put "+ tm.stop()+ " --");
    System.out.println(rec);
    tm.clear().start();
    unit = volume.get(rec);
    System.out.println("-- time to volume.get "+ tm.stop()+ " --");
    System.out.println(unit);
    
    unit = storeUnit(serial);
    tm.clear().start();
    rec = volume.put(unit);
    System.out.println("-- time to volume.put "+ tm.stop()+ " --");
    System.out.println(rec);
    tm.clear().start();
    unit = volume.get(rec);
    System.out.println("-- time to volume.get "+ tm.stop()+ " --");
    System.out.println(unit);
    
    unit = storeUnit(serial);
    tm.clear().start();
    rec = volume.put(unit);
    System.out.println("-- time to volume.put "+ tm.stop()+ " --");
    System.out.println(rec);
    tm.clear().start();
    unit = volume.get(rec);
    System.out.println("-- time to volume.get "+ tm.stop()+ " --");
    System.out.println(unit);
  }

  
  public static void main(String[] args) throws IOException, InterruptedException {
    Path dbpath = Paths.get("/home/juno/dbone-channel.dat");
    try {
      //Storage fs = StorageFactory.newFactory().setFile("/storage/channelDBone.bin").create();
      //Storage fs = StorageFactory.newFactory().setFile("/storage/mappedDBone.bin").createMapped();
      Storage fs = StorageFactory.newFactory()
          //.setBlockSize(1024)
          .setFile(dbpath)
          //.concurrent()
          .create();

      SerializationService serial = new JavaSerializationService();
      //SerializationService serial = new FSTSerializationService();
      //SerializationService serial = new GsonSerializationService();
      //SerializationService serial = new JsonIoSerializationService();
      DefaultVolume vol = new DefaultVolume(fs);
      
      System.out.println("* warming up 20x...");
      disableStdOut();
      for(int i = 0; i < 20; i++) {
        execute(vol, serial);
      }
      enableStdOut();
      System.out.println("  Done!");
      execute(vol, serial);

      vol.close();
    }
    finally {
      Files.delete(dbpath);
    }
  }
  
}
