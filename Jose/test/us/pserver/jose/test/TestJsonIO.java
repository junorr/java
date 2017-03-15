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

package us.pserver.jose.test;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/03/2017
 */
public class TestJsonIO {

  public static final String JSON = ""
      + ("{"
      + "  'hello': 'world', "
      + "  'num': 500.52, "
      + "  'bool': true, "
      + "  'array': [1, 2, 3, 4, 5], "
      + "  'child': {"
      + "    'str': 'hello world',"
      + "    'date': '2017-03-15T15:17:45Z',"
      + "    'lucky': 3.14,"
      + "    'nest': {"
      + "      'array': ['a', 'b', 'c', 'd', 'e'],"
      + "      'magic': 77713.25"
      + "    },"
      + "    'superior': [{'n': 300}, {'n': 400}, {'n': 500}]"
      + "  }"
      + "}").replace("'", "\"");
      ;
  
  
  public static class JavaJson {
    private final String hello;
    private final double num;
    private final boolean bool;
    private final List<Integer> array;
    private final JavaChild child;
    private final List<JavaN> superior;
    
    public JavaJson() {
      hello = null;
      num = 0;
      array = new ArrayList<>();
      child = null;
      superior = new ArrayList<>();
      bool = false;
    }

    public JavaJson(String hello, double num, boolean bool, JavaChild child) {
      this.hello = hello;
      this.num = num;
      this.bool = bool;
      this.array = new ArrayList<>();
      this.child = child;
      this.superior = new ArrayList<>();
    }

    @Override
    public String toString() {
      return "JavaJson{" + "hello=" + hello + ", num=" + num + ", bool=" + bool + ", array=" + array + ", child=" + child + ", superior=" + superior + '}';
    }
    
  }
  
  
  public static class JavaChild {
    private final String str;
    private final Date date;
    private final double lucky;
    private final JavaNest nest;
    
    public JavaChild() {
      str = null;
      date = new Date();
      lucky = 0;
      nest = null;
    }

    public JavaChild(String str, double lucky, JavaNest nest) {
      this.str = str;
      this.date = new Date();
      this.lucky = lucky;
      this.nest = nest;
    }
    
    @Override
    public String toString() {
      return "JavaChild{" + "str=" + str + ", date=" + date + ", lucky=" + lucky + ", nest=" + nest + '}';
    }
    
  }
  
  
  public static class JavaNest {
    private final List<String> array;
    private final double magic;
    
    public JavaNest() {
      array = new ArrayList<>();
      magic = 0;
    }

    public JavaNest(double magic) {
      this.array = new ArrayList<>();
      this.magic = magic;
    }

    @Override
    public String toString() {
      return "JavaNest{" + "array=" + array + ", magic=" + magic + '}';
    }
    
  }
  
  
  public static class JavaN {
    private final long n;
    
    public JavaN() {
      n = 0;
    }

    public JavaN(long n) {
      this.n = n;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 71 * hash + (int) (this.n ^ (this.n >>> 32));
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
      final JavaN other = (JavaN) obj;
      if (this.n != other.n) {
        return false;
      }
      return true;
    }
    
    @Override
    public String toString() {
      return "n="+ n;
    }
  }
  
  
  public static void main(String[] args) {
    JavaNest jn = new JavaNest(182.111);
    jn.array.add("f");
    jn.array.add("g");
    jn.array.add("h");
    jn.array.add("i");
    jn.array.add("j");
    JavaChild jc = new JavaChild("json", 7.333, jn);
    JavaJson jj = new JavaJson("wwii", 2.222, false, jc);
    jj.array.add(900);
    jj.array.add(880);
    jj.array.add(777);
    jj.array.add(600);
    jj.array.add(550);
    jj.superior.add(new JavaN(9));
    jj.superior.add(new JavaN(8));
    jj.superior.add(new JavaN(7));
    jj.superior.add(new JavaN(6));
    jj.superior.add(new JavaN(5));
    
    System.out.println("---- json-io ----");
    System.out.println("* java ==>> json");
    System.out.println(jj);
    Timer tm = new Timer.Nanos().start();
    String json = JsonWriter.objectToJson(jj);
    tm.stop();
    System.out.println(json);
    System.out.println(tm);
    
    System.out.println();
    System.out.println("* java ==>> json");
    System.out.println(jj);
    tm = new Timer.Nanos().start();
    Map<String,Object> map = JsonReader.jsonToMaps(json);
    json = JsonWriter.objectToJson(jj);
    tm.stop();
    System.out.println(json);
    System.out.println(tm);
    
    System.out.println();
    System.out.println("* java <<== json");
    tm.clear().start();
    jj = (JavaJson) JsonReader.jsonToJava(json);
    tm.stop();
    System.out.println(jj);
    System.out.println(tm);
    
    //System.out.println();
    //System.out.println("* map <<== json");
    //tm.clear().start();
    //JsonObject job = (JsonObject) JsonReader.jsonToJava(JSON);
    //tm.stop();
    //System.out.println(job);
    //System.out.println(JsonWriter.objectToJson(job));
    //System.out.println(tm);
    
    System.out.println();
    System.out.println("* java <<== custon json");
    try {
      tm.clear().start();
      jj = (JavaJson) JsonReader.jsonToJava(JSON);
      tm.stop();
      System.out.println(jj);
      System.out.println(tm);
    } catch(Exception e) {
      System.out.println(e.toString());
    }
    
    System.out.println();
    System.out.println("---- jsoniter ----");
    System.out.println("* java ==>> json");
    tm.clear().start();
    json = JsonStream.serialize(jj);
    tm.stop();
    System.out.println(json);
    System.out.println(tm);
    
    System.out.println();
    System.out.println("* java <<== json");
    tm.clear().start();
    jj = JsonIterator.deserialize(json, JavaJson.class);
    tm.stop();
    System.out.println(jj);
    System.out.println(tm);
    
    System.out.println();
    System.out.println("* java <<== custon json");
    tm.clear().start();
    jj = JsonIterator.deserialize(JSON, JavaJson.class);
    tm.stop();
    System.out.println(jj);
    System.out.println(tm);

    
    System.out.println("---- gson ----");
    System.out.println("* java ==>> json");
    System.out.println(jj);
    tm = new Timer.Nanos().start();
    json = new Gson().toJson(jj);
    tm.stop();
    System.out.println(json);
    System.out.println(tm);
    
    System.out.println();
    System.out.println("* java <<== json");
    tm.clear().start();
    jj = new Gson().fromJson(json, JavaJson.class);
    tm.stop();
    System.out.println(jj);
    System.out.println(tm);
    
    System.out.println();
    System.out.println("* map <<== json");
    tm.clear().start();
    JsonObject gob = (JsonObject) new Gson().toJsonTree(jj);
    tm.stop();
    System.out.println(gob);
    System.out.println(JsonWriter.objectToJson(gob));
    System.out.println(tm);
    
  }
  
}
