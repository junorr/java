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

package us.pserver.tools.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import us.pserver.tools.io.ByteBufferInputStream;
import us.pserver.tools.io.ByteBufferOutputStream;
import us.pserver.tools.io.DynamicBuffer;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectMapper;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/09/2017
 */
public class TestObjectMapper {
  
  public static AObj a() {
    return new AObj("hello", 5, new int[] {5,4,3,2,1,0}, new char[] {'a', 'b', 'c', 'd'}, new Date());
  }
  
  
  public static BObj b() {
    LinkedList<Integer> ls = new LinkedList<>();
    ls.add(10);
    ls.add(9);
    ls.add(8);
    ls.add(7);
    ls.add(6);
    ls.add(5);
    return new BObj("world", a(), ls);
  }
  
  
  public static void exec(BObj b) throws IOException, ClassNotFoundException {
    Timer tm = new Timer.Nanos().start();
    ObjectMapper mapper = new ObjectMapper();
    System.out.println("-- time to create ObjectMapper -- "+ tm.stop());
    AObj a = b.getA();
    tm.clear().start();
    MappedValue omp = mapper.map(a);
    System.out.println("-- time to map 'a' -- "+ tm.stop());
    System.out.println("* a.mapped  : "+ omp);
    tm.clear().start();
    a = (AObj) mapper.unmap(AObj.class, omp);
    System.out.println("-- time to UNmap 'a' -- "+ tm.stop());
    System.out.println("* a.unmapped: "+ a);
    tm.clear().start();
    omp = mapper.map(b);
    System.out.println("-- time to map 'b' -- "+ tm.stop());
    System.out.println("* b.mapped  : "+ omp);
    tm.clear().start();
    b = (BObj) mapper.unmap(BObj.class, omp);
    System.out.println("-- time to UNmap 'b' -- "+ tm.stop());
    System.out.println("* b.unmapped: "+ b);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    tm.clear().start();
    out.writeObject(omp);
    System.out.println("-- time to serialize MappedValue -- "+ tm.stop());
    out.close();
    
    tm.clear().start();
    byte[] bs = bos.toByteArray();
    //System.out.println("-- time ByteArrayOut.toByteArray() -- "+ tm.stop());
    
    ByteArrayInputStream bis = new ByteArrayInputStream(bs);
    ObjectInputStream oin = new ObjectInputStream(bis);
    tm.clear().start();
    omp = (MappedValue) oin.readObject();
    System.out.println("-- time to DEserialize MappedValue -- "+ tm.stop());
    System.out.println("* MappedValue.deserialized: "+ omp);
    oin.close();
    
    
    DynamicBuffer dyn = new DynamicBuffer();
    dyn.setWriting();
    out = new ObjectOutputStream(dyn.getOutputStream());
    tm.clear().start();
    out.writeObject(omp);
    System.out.println("-- time to serialize DynamicBuffer MappedValue -- "+ tm.stop());
    out.close();
    
    dyn.setReading();
    oin = new ObjectInputStream(dyn.getInputStream());
    tm.clear().start();
    omp = (MappedValue) oin.readObject();
    System.out.println("-- time to DEserialize DynamicBuffer MappedValue -- "+ tm.stop());
    System.out.println("* MappedValue.deserialized: "+ omp);
    oin.close();
    
    ByteBufferOutputStream bbo = new ByteBufferOutputStream(ByteBufferOutputStream.ALLOC_POLICY_HEAP);
    out = new ObjectOutputStream(bbo);
    tm.clear().start();
    out.writeObject(omp);
    System.out.println("-- time to serialize ByteBufferOutputStream MappedValue -- "+ tm.stop());
    System.out.println("* MappedValue.serialized: "+ bbo.size()+ ", pages: "+ bbo.pages());
    out.close();
    
    tm.clear().start();
    ByteBuffer buf = bbo.toByteBuffer();
    //System.out.println("-- time ByteBufferOut.toByteBuffer() -- "+ tm.stop());
    
    oin = new ObjectInputStream(new ByteBufferInputStream(buf));
    tm.clear().start();
    omp = (MappedValue) oin.readObject();
    System.out.println("-- time to DEserialize ByteBufferInputStream MappedValue -- "+ tm.stop());
    System.out.println("* MappedValue.deserialized: "+ omp);
    oin.close();
  }
  
  
  public static final PrintStream stdout = System.out;
  
  public static final PrintStream nullout = createNullOut();
  
  
  public static PrintStream createNullOut() {
    try {
      return new PrintStream(new File("/dev/null"));
    } catch(FileNotFoundException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public static void disableOut() {
    System.out.flush();
    System.setOut(nullout);
  }

  
  public static void enableOut() {
    System.out.flush();
    System.setOut(stdout);
  }

  
  public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
    System.out.println("* warming up 10x...");
    disableOut();
    BObj b = b();
    for(int i = 0; i < 10; i++) {
      exec(b);
    }
    enableOut();
    exec(b);
  }
  
}
