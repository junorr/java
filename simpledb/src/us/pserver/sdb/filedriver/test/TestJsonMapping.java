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

import com.cedarsoftware.util.io.JsonReader;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Decoder;
import com.jsoniter.spi.Encoder;
import com.jsoniter.spi.JsoniterSpi;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import us.pserver.job.json.adapter.JsoniterReader;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2017
 */
public class TestJsonMapping {
  
  
  static class Host {
    private final String name;
    private final String ip;
    private final int port;
    
    public Host() {
      name = null;
      ip = null;
      port = 0;
    }
    
    public Host(String name, String ip, int port) {
      this.name = name;
      this.ip = ip;
      this.port = port;
    }
    
    public Host(String ip, int port) {
      this.ip = name = ip;
      this.port = port;
    }

    public String getName() {
      return name;
    }

    public String getIp() {
      return ip;
    }

    public int getPort() {
      return port;
    }

    @Override
    public String toString() {
      return "Host{" + "name=" + name + ", ip=" + ip + ", port=" + port + '}';
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 83 * hash + Objects.hashCode(this.name);
      hash = 83 * hash + Objects.hashCode(this.ip);
      hash = 83 * hash + this.port;
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Host other = (Host) obj;
      if (this.port != other.port) {
        return false;
      }
      if (!Objects.equals(this.name, other.name)) {
        return false;
      }
      if (!Objects.equals(this.ip, other.ip)) {
        return false;
      }
      return true;
    }
    
  }
  
  
  static class Conf {
    private final List<Host> hosts;
    private final long block;
    private final Class cls;
    
    public Conf() {
      this.hosts = null;
      block = 1024;
      cls = null;
    }
    
    public Conf(Host host, long block, Class cls) {
      this.hosts = new ArrayList<>();
      hosts.add(host);
      this.block = block;
      this.cls = cls;
    }

    public List<Host> getHosts() {
      return hosts;
    }

    public long getBlock() {
      return block;
    }

    @Override
    public String toString() {
      return "Conf{" + "block=" + block + ", class=" + cls + ", hosts=" + hosts + '}';
    }

  }
  
  
  static class ClassCoder implements Encoder, Decoder {

    @Override
    public void encode(Object o, JsonStream stream) throws IOException {
      if(o == null || !Class.class.isAssignableFrom(o.getClass())) {
        throw new IOException("Bad Object: "+ (o == null ? o : o.getClass()));
      }
      Class c = (Class) o;
      stream.writeVal(c.getName());
    }

    @Override
    public Any wrap(Object o) {
      if(o == null || !Class.class.isAssignableFrom(o.getClass())) {
        throw new IllegalArgumentException("Bad Object: "+ (o == null ? o : o.getClass()));
      }
      Class c = (Class) o;
      return Any.wrap(c.getName());
    }

    @Override
    public Object decode(JsonIterator ji) throws IOException {
      String cname = ji.readString();
      try {
        return Class.forName(cname);
      } catch(ClassNotFoundException e) {
        throw new IOException(e.toString(), e);
      }
    }
    
  }
  
  
  public static void jsoniter() throws IOException {
    ClassCoder cc = new ClassCoder();
    JsoniterSpi.registerTypeEncoder(Class.class, cc);
    JsoniterSpi.registerTypeDecoder(Class.class, cc);
    Conf conf = new Conf(new Host("127.0.0.1", 80), 512, String.class);
    String json = JsonStream.serialize(conf);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    JsonStream.serialize(conf, bos);
    System.out.println("* array: "+ Arrays.toString(bos.toByteArray()));
    System.out.println("* isString: '"+ UTF8String.from(bos.toByteArray())+ "'");
    
    //ByteBuffer buf = ByteBuffer.allocate(512);
    //buf.put(UTF8String.from(json).getBytes());
    //byte[] bs = new byte[128];
    //buf.put(bs).flip();
    //JsonIterator iter = JsonIterator.parse(buf.array());
    //Conf conf;
    //String json = "{\"hosts\":[{\"ip\":\"*\",\"name\":\"*\",\"port\":80}, {\"ip\":\"127.0.0.1\",\"name\":\"127.0.0.1\",\"port\":8080}],\"block\":512,\"cls\":\"java.lang.String\"}";
    System.out.println("* json(len="+ json.length()+"): "+ json);
    Timer tm = new Timer.Nanos().start();
    
    conf = JsonIterator.deserialize(json, Conf.class);
    //conf = iter.read(Conf.class);
    tm.stop();
    System.out.println(tm.stop());
    System.out.println(conf);
  }
  
  
  public static void jsoniterReader() {
    System.out.println("- step 0");
    JsoniterReader jr = new JsoniterReader();
    ClassCoder cc = new ClassCoder();
    JsoniterSpi.registerTypeEncoder(Class.class, cc);
    JsoniterSpi.registerTypeDecoder(Class.class, cc);
    System.out.println("- step 2");
    Conf conf = new Conf(new Host("127.0.0.1", 80), 512, String.class);
    System.out.println("- step 3");
    Timer tm = new Timer.Nanos().start();
    String json = JsonStream.serialize(conf);
    tm.stop();
    System.out.println("* json="+ json);
    System.out.println("* "+ tm);
    byte[] bs = new UTF8String(json).getBytes();
    ByteBuffer buf = ByteBuffer.allocate(bs.length);
    buf.put(bs);
    buf.flip();
    tm.clear().start();
    Map<String,Object> map = jr.read(buf);
    tm.stop();
    System.out.println("* map="+ map);
    System.out.println("* "+ tm);
  }

  
  public static void jsonio() throws IOException {
    //Conf conf = new Conf(new Host("127.0.0.1", 80), 512);
    //String json = JsonWriter.objectToJson(conf);
    Conf conf;
    String json = "{\"@type\":\"us.pserver.sdb.filedriver.test.TestJsonMapping$Conf\",\"hosts\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"us.pserver.sdb.filedriver.test.TestJsonMapping$Host\",\"name\":\"127.0.0.1\",\"ip\":\"127.0.0.1\",\"port\":80}]},\"block\":512}";
    System.out.println("* json = "+ json);
    Timer tm = new Timer.Nanos().start();
    conf = (Conf) JsonReader.jsonToJava(json);
    tm.stop();
    System.out.println(tm.stop());
    System.out.println(conf);
  }
  
  
  public static void main(String[] args) throws IOException {
    jsonio();
    System.out.println("--------------------------------");
    jsoniter();
    System.out.println("--------------------------------");
    jsoniterReader();
  }
  
}
