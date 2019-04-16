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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.ArrayBox;
import us.pserver.bitbox.MapBox;
import us.pserver.bitbox.impl.ArrayBoxImpl;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.impl.MapBoxImpl;
import us.pserver.bitbox.transform.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

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
    bt.boxing().accept(b, buffer);
    Assertions.assertTrue(bt.unboxing().apply(buffer.position(0)));
  }
  
  @Test
  public void byte_transform() {
    byte b = 55;
    BitBuffer buffer = BitBuffer.of(2, false);
    ByteTransform bt = new ByteTransform();
    bt.boxing().accept(b, buffer);
    Assertions.assertEquals(b, bt.unboxing().apply(buffer.position(0)));
  }
  
  @Test
  public void char_sequence_transform() {
    String str = "Hello World!!";
    CharSequenceTransform ct = new CharSequenceTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    ct.boxing().accept(str, buffer);
    Assertions.assertEquals(str, ct.unboxing().apply(buffer.position(0)));
  }
  
  @Test
  public void double_transform() {
    double d = 55.6789;
    DoubleTransform dt = new DoubleTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    dt.doubleBoxing().accept(d, buffer);
    Assertions.assertEquals(d, dt.doubleUnboxing().applyAsDouble(buffer.position(0)));
  }
  
  @Test
  public void int_transform() {
    int i = 55;
    IntTransform dt = new IntTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    dt.intBoxing().accept(i, buffer);
    Assertions.assertEquals(i, dt.intUnboxing().applyAsInt(buffer.position(0)));
  }
  
  @Test
  public void long_transform() {
    long l = Long.MAX_VALUE;
    LongTransform dt = new LongTransform();
    BitBuffer buffer = BitBuffer.of(30, false);
    dt.longBoxing().accept(l, buffer);
    Assertions.assertEquals(l, dt.longUnboxing().applyAsLong(buffer.position(0)));
  }
  
  @Test
  public void instant_transform() {
    Instant now = Instant.now();
    long time = now.toEpochMilli();
    InstantTransform dt = new InstantTransform();
    BitBuffer buffer = BitBuffer.of(100, false);
    dt.boxing().accept(now, buffer);
    Assertions.assertEquals(Instant.ofEpochMilli(time), dt.unboxing().apply(buffer.position(0)));
  }
  
  @Test
  public void zoned_date_time_transform() {
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTimeTransform zt = new ZonedDateTimeTransform();
    BitBuffer buffer = BitBuffer.of(100, false);
    zt.boxing().accept(now, buffer);
    Assertions.assertEquals(now, zt.unboxing().apply(buffer.position(0)));
  }
  
  @Test
  public void local_date_time_transform() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTimeTransform lt = new LocalDateTimeTransform();
    BitBuffer buffer = BitBuffer.of(100, false);
    lt.boxing().accept(now, buffer);
    Assertions.assertEquals(now, lt.unboxing().apply(buffer.position(0)));
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
    trans.boxing().accept(dbs, out);
    Assertions.assertArrayEquals(dbs, trans.unboxing().apply(out.position(0)));
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
    trans.boxing().accept(dbs, out);
    Assertions.assertArrayEquals(dbs, trans.unboxing().apply(out.position(0)));
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
    trans.boxing().accept(dbs, out);
    Assertions.assertArrayEquals(dbs, trans.unboxing().apply(out.position(0)));
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
    trans.boxing().accept(ss, out);
    Assertions.assertArrayEquals(ss, trans.unboxing().apply(out.position(0)));
  }
  
  @Test
  public void inetaddress_collection_transform() throws UnknownHostException {
    List<InetAddress> ins = new ArrayList<>(5);
    ins.add(InetAddress.getByName("172.16.12.166"));
    ins.add(InetAddress.getByName("172.16.12.167"));
    ins.add(InetAddress.getByName("172.16.12.168"));
    ins.add(InetAddress.getByName("172.16.12.169"));
    ins.add(InetAddress.getByName("172.16.12.170"));
    CollectionTransform<InetAddress> trans = new CollectionTransform<>();
    BitBuffer out = BitBuffer.of(100, false);
    trans.boxing().accept(ins, out);
    Assertions.assertEquals(ins, trans.unboxing().apply(out.position(0)));
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
    trans.boxing().accept(m,out);
    Assertions.assertEquals(m, trans.unboxing().apply(out.position(0)));
    MapBox box = new MapBoxImpl(out.position(0));
    Assertions.assertEquals(m.get(0), box.get(0));
    Assertions.assertEquals(m.get(4), box.get(4));
  }
  
}
