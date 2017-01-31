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

package us.pserver.sdb.filedriver.test;

import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import us.pserver.job.index.Index;
import us.pserver.job.index.Index.DefIndex;
import us.pserver.job.Region.DefRegion;
import us.pserver.tools.StringPad;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2016
 */
public class TestIndex {

  
  public static void main(String[] args) throws IOException {
    ByteBuffer buf = ByteBuffer.allocateDirect(2048);
    Index<Double> idx = new DefIndex("capacity", 52.7);
    long start = 0;
    long len = 512;
    for(int i = 0; i < 10; i++) {
      idx.add(new DefRegion(start, len));
      start += len;
    }
    
    System.out.println("* index: "+ idx);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    System.out.print(StringPad.of("* Serializing Object to Bytes...").rpad(" ", 40));
    Timer tm = new Timer.Nanos().start();
    out.writeObject(idx);
    buf.put(bos.toByteArray());
    System.out.println("[OK]\n  "+ tm.stop());
    System.out.println(StringPad.of("* buffer.position(): ").rpad(" ", 40)+ buf.position());
    System.out.println();
    
    Path pout = Paths.get("/home/juno/index.bin");
    System.out.print(StringPad.of("* Writing to "+ pout+ "...").rpad(" ", 40));
    tm.clear().start();
    try (FileChannel ch = FileChannel.open(pout, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.WRITE
    )) {
      buf.flip();
      ch.write(buf);
    }
    System.out.println("[OK]\n  "+ tm.stop());
    System.out.println();
    
    System.out.print(StringPad.of("* Serializing Object to Json...").rpad(" ", 40));
    buf.clear();
    JsonWriter jw = new JsonWriter(bos);
    tm.clear().start();
    jw.write(idx);
    buf.put(bos.toByteArray());
    System.out.println("[OK]\n  "+ tm.stop());
    System.out.println(StringPad.of("* buffer.position(): ").rpad(" ", 40)+ buf.position());
    String json = JsonWriter.objectToJson(idx);
    System.out.println();
    
    System.out.println(json);
    System.out.println(JsonReader.jsonToJava(json));
    Map<String,Object> map = JsonReader.jsonToMaps(json);
    System.out.println();
    map.forEach((k,v)->{
      System.out.print("("+ k.getClass().getSimpleName()+ ") ");
      System.out.print(k+ "=");
      if(Map.class.isAssignableFrom(v.getClass())) {
        Map m = (Map) v;
        System.out.println();
        m.forEach((kk,vv)->{
          System.out.print("("+ kk.getClass().getSimpleName()+ ") ");
          System.out.println(kk+ "="+ "("+ vv.getClass().getSimpleName()+ ") "+ vv);
        });
      }
      else {
        System.out.println("("+ v.getClass().getSimpleName()+ ") "+ v);
      }
    });
  }
  
}
