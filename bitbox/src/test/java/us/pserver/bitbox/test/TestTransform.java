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

package us.pserver.bitbox.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.ArrayBox;
import us.pserver.bitbox.MapBox;
import us.pserver.bitbox.impl.ArrayBoxImpl;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.impl.BitBufferImpl;
import us.pserver.bitbox.impl.DynamicMapBox;
import us.pserver.bitbox.impl.MapBoxImpl;
import us.pserver.bitbox.transform.*;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12 de abr de 2019
 */
public class TestTransform {
  
  @Test
  public void boolean_transform() {
    boolean b = true;
    BitBuffer buffer = BitBuffer.of(2, false);
    BooleanTransform bt = new BooleanTransform();
    int len = bt.box(b, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertTrue(bt.unbox(buffer.position(0)));
  }
  
  @Test
  public void byte_transform() {
    byte b = 55;
    BitBuffer buffer = BitBuffer.of(2, false);
    ByteTransform bt = new ByteTransform();
    int len = bt.box(b, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(b, bt.unbox(buffer.position(0)));
  }
  
  @Test
  public void char_sequence_transform() {
    String str = "Hello World!!";
    CharSequenceTransform ct = new CharSequenceTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    int len = ct.box(str, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(str, ct.unbox(buffer.position(0)));
  }
  
  @Test
  public void double_transform() {
    double d = 55.6789;
    DoubleTransform dt = new DoubleTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    int len = dt.doubleBox(d, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(d, dt.doubleUnbox(buffer.position(0)));
  }
  
  @Test
  public void int_transform() {
    int i = 55;
    IntTransform dt = new IntTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    int len = dt.intBox(i, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(i, dt.intUnbox(buffer.position(0)));
  }
  
  @Test
  public void long_transform() {
    long l = Long.MAX_VALUE;
    LongTransform dt = new LongTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    int len = dt.longBox(l, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(l, dt.longUnbox(buffer.position(0)));
  }
  
  @Test
  public void instant_transform() {
    Instant now = Instant.now();
    long time = now.toEpochMilli();
    InstantTransform dt = new InstantTransform();
    BitBuffer buffer = BitBuffer.of(100, false);
    int len = dt.box(now, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(Instant.ofEpochMilli(time), dt.unbox(buffer.position(0)));
  }
  
  @Test
  public void zoned_date_time_transform() {
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTimeTransform zt = new ZonedDateTimeTransform();
    BitBuffer buffer = BitBuffer.of(100, false);
    int len = zt.box(now, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(now, zt.unbox(buffer.position(0)));
  }
  
  @Test
  public void local_date_time_transform() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTimeTransform lt = new LocalDateTimeTransform();
    BitBuffer buffer = BitBuffer.of(100, false);
    int len = lt.box(now, buffer);
    Assertions.assertEquals(len, buffer.position());
    Assertions.assertEquals(now, lt.unbox(buffer.position(0)));
  }
  
  @Test
  public void double_array_transform() {
    double[] dbs = new double[5];
    dbs[0] = 5.0;
    dbs[1] = 6.1;
    dbs[2] = 7.2;
    dbs[3] = 8.3;
    dbs[4] = 9.4;
    System.out.println(Arrays.toString(dbs));
    DoubleArrayTransform trans = new DoubleArrayTransform();
    BitBuffer out = BitBuffer.of(100, false);
    int len = trans.box(dbs, out);
    Assertions.assertEquals(len, out.position());
    Assertions.assertArrayEquals(dbs, trans.unbox(out.position(0)));
  }
  
  @Test
  public void float_array_transform() {
    float[] dbs = new float[5];
    dbs[0] = 5.0f;
    dbs[1] = 6.1f;
    dbs[2] = 7.2f;
    dbs[3] = 8.3f;
    dbs[4] = 9.4f;
    System.out.println(Arrays.toString(dbs));
    FloatArrayTransform trans = new FloatArrayTransform();
    BitBuffer out = BitBuffer.of(100, false);
    int len = trans.box(dbs, out);
    Assertions.assertEquals(len, out.position());
    Assertions.assertArrayEquals(dbs, trans.unbox(out.position(0)));
  }
  
  @Test
  public void char_array_transform() {
    char[] dbs = new char[5];
    dbs[0] = 'a';
    dbs[1] = 'b';
    dbs[2] = 'c';
    dbs[3] = 'd';
    dbs[4] = 'e';
    System.out.println(Arrays.toString(dbs));
    CharArrayTransform trans = new CharArrayTransform();
    BitBuffer out = BitBuffer.of(100, false);
    int len = trans.box(dbs, out);
    Assertions.assertEquals(len, out.position());
    Assertions.assertArrayEquals(dbs, trans.unbox(out.position(0)));
  }
  
  @Test
  public void string_array_transform() {
    String[] ss = new String[5];
    ss[0] = "hello";
    ss[1] = "world";
    ss[2] = "abc";
    ss[3] = "def";
    ss[4] = "ghi";
    System.out.println(Arrays.toString(ss));
    ArrayTransform<String> trans = new ArrayTransform<>();
    BitBuffer out = BitBuffer.of(100, false);
    int len = trans.box(ss, out);
    Assertions.assertEquals(len, out.position());
    Assertions.assertArrayEquals(ss, trans.unbox(out.position(0)));
  }
  
  @Test
  public void inetaddress_collection_transform() throws UnknownHostException {
    List<InetAddress> ins = new ArrayList<>(5);
    ins.add(InetAddress.getByName("172.16.12.166"));
    ins.add(InetAddress.getByName("172.16.12.167"));
    ins.add(InetAddress.getByName("172.16.12.168"));
    ins.add(InetAddress.getByName("172.16.12.169"));
    ins.add(null);
    ins.add(InetAddress.getByName("172.16.12.170"));
    CollectionTransform<InetAddress> trans = new CollectionTransform<>();
    BitBuffer out = BitBuffer.of(100, false);
    int len = trans.box(ins, out);
    Assertions.assertEquals(len, out.position());
    Assertions.assertEquals(ins.size() -1, trans.unbox(out.position(0)).size());
    ArrayBox<InetAddress> box = new ArrayBoxImpl<>(out.position(0));
    Assertions.assertEquals(ins.get(0), box.get(0));
    Assertions.assertEquals(ins.get(4), box.get(4));
  }
  
  @Test
  public void map_transform() throws UnknownHostException {
    Map<Integer,LocalDateTime> m = new HashMap<>();
    m.put(0, LocalDateTime.now());
    m.put(1, LocalDateTime.now());
    m.put(2, LocalDateTime.now());
    m.put(3, LocalDateTime.now());
    m.put(4, LocalDateTime.now());
    MapTransform<Integer,LocalDateTime> trans = new MapTransform<>();
    BitBuffer out = BitBuffer.of(200, false);
    int len = trans.box(m,out);
    Assertions.assertEquals(len, out.position());
    Assertions.assertEquals(m, trans.unbox(out.position(0)));
    MapBox box = new MapBoxImpl(out.position(0));
    Assertions.assertEquals(m.get(0), box.get(0));
    Assertions.assertEquals(m.get(4), box.get(4));
  }
  
  @Test
  public void read_list_with_array_box() {
    List<String> ls = new LinkedList<>();
    ls.add("Hello");
    ls.add("World");
    ls.add("java");
    ls.add("11");
    CollectionTransform<String> tran = new CollectionTransform<>();
    BitBuffer buf = new BitBufferImpl(256, true);
    int len = tran.box(ls, buf);
    Assertions.assertEquals(32 + Integer.BYTES * 10, len);
    Assertions.assertEquals(len, buf.position());
    ArrayTransform<String> at = new ArrayTransform();
    String[] arr = at.unbox(buf.position(0));
    for(int i = 0; i < ls.size(); i++) {
      Assertions.assertEquals(ls.get(i), arr[i]);
    }
    ArrayBox<String> box = new ArrayBoxImpl<>(buf.position(0));
    Assertions.assertEquals(ls.get(0), box.get(0));
    Assertions.assertEquals(ls.get(2), box.get(2));
  }
  
  public static enum Weather {
    SUN, CLOUDS, RAIN, RAINBOW
  }
  
  @Test
  public void enum_transform() {
    BitBuffer buf = new BitBufferImpl(256, true);
    EnumTransform<Weather> tran = new EnumTransform<>();
    int len = tran.box(Weather.RAIN, buf);
    Assertions.assertEquals(len, buf.position());
    Assertions.assertEquals(Weather.RAIN, tran.unbox(buf.position(0)));
    buf.position(0).putInt(10, 10);
    System.out.println("buf.position(0).putInt(10, 10).position() = " + buf.position());
  }
  
  @Test
  public void dynamic_map_entry_transform() {
    Map<String,Integer> m = new TreeMap<>();
    m.put("Integer.MIN_VALUE", Integer.MIN_VALUE);
    m.put("Integer.ZERO", 0);
    m.put("Integer.MAX_VALUE", Integer.MAX_VALUE);
    System.out.println(m);
    DynamicMapTransform tran = new DynamicMapTransform();
    BitBuffer b = BitBuffer.of(256, true);
    int len = tran.box(m, b);
    Assertions.assertEquals(len, b.position());
    Map<String,Integer> f = tran.unbox(b.position(0));
    System.out.println(f);
    Assertions.assertEquals(m.get("Integer.MIN_VALUE"), f.get("Integer.MIN_VALUE"));
    Assertions.assertEquals(m.get("Integer.ZERO"), f.get("Integer.ZERO"));
    Assertions.assertEquals(m.get("Integer.MAX_VALUE"), f.get("Integer.MAX_VALUE"));
    MapBox box = new DynamicMapBox(b.position(0));
    Assertions.assertEquals(m.get("Integer.MIN_VALUE"), box.get("Integer.MIN_VALUE"));
    Assertions.assertEquals(m.get("Integer.ZERO"), box.get("Integer.ZERO"));
    Assertions.assertEquals(m.get("Integer.MAX_VALUE"), box.get("Integer.MAX_VALUE"));
  }
  
}
