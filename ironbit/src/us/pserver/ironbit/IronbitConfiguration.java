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

package us.pserver.ironbit;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.ironbit.serial.BooleanSerializer;
import us.pserver.ironbit.serial.CharacterSerializer;
import us.pserver.ironbit.serial.ClassSerializer;
import us.pserver.ironbit.serial.DateSerializer;
import us.pserver.ironbit.serial.DoubleSerializer;
import us.pserver.ironbit.serial.FloatSerializer;
import us.pserver.ironbit.serial.InstantSerializer;
import us.pserver.ironbit.serial.IntegerSerializer;
import us.pserver.ironbit.serial.LocalDateTimeSerializer;
import us.pserver.ironbit.serial.LongSerializer;
import us.pserver.ironbit.serial.PathSerializer;
import us.pserver.ironbit.serial.ShortSerializer;
import us.pserver.ironbit.serial.StringSerializer;
import us.pserver.ironbit.serial.ZonedDateTimeSerializer;
import us.pserver.tools.ConcurrentList;
import us.pserver.tools.NotNull;
import us.pserver.tools.SortedList;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/10/2017
 */
public abstract class IronbitConfiguration {

  private static final IronbitConfiguration instance = new IronbitConfiguration() {};
  
  private final AtomicInteger curid;
  
  private final SortedList<ClassID> classes;
  
  private final Map<Integer,Serializer> serials;
  
  
  private IronbitConfiguration() {
    this.classes = new SortedList<>(new ConcurrentList<>(), ClassID.idComparator());
    this.serials = Collections.synchronizedMap(new TreeMap<>());
    this.curid = new AtomicInteger(0);
    this.registerDefaults();
  }
  
  
  private void registerDefaults() {
    ClassID cid = this.registerClassID(int.class);
    Serializer<?> ser = new IntegerSerializer();
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(Integer.class);
    serials.put(cid.getID(), ser);
    ser = new LongSerializer();
    cid = this.registerClassID(Long.class);
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(long.class);
    serials.put(cid.getID(), ser);
    ser = new FloatSerializer();
    cid = this.registerClassID(Float.class);
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(float.class);
    serials.put(cid.getID(), ser);
    ser = new DoubleSerializer();
    cid = this.registerClassID(Double.class);
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(double.class);
    serials.put(cid.getID(), ser);
    ser = new ShortSerializer();
    cid = this.registerClassID(Short.class);
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(short.class);
    serials.put(cid.getID(), ser);
    ser = new BooleanSerializer();
    cid = this.registerClassID(Boolean.class);
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(boolean.class);
    serials.put(cid.getID(), ser);
    ser = new CharacterSerializer();
    cid = this.registerClassID(Character.class);
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(char.class);
    serials.put(cid.getID(), ser);
    cid = this.registerClassID(String.class);
    serials.put(cid.getID(), new StringSerializer());
    cid = this.registerClassID(Date.class);
    serials.put(cid.getID(), new DateSerializer());
    cid = this.registerClassID(LocalDateTime.class);
    serials.put(cid.getID(), new LocalDateTimeSerializer());
    cid = this.registerClassID(ZonedDateTime.class);
    serials.put(cid.getID(), new ZonedDateTimeSerializer());
    cid = this.registerClassID(Instant.class);
    serials.put(cid.getID(), new InstantSerializer());
    cid = this.registerClassID(Class.class);
    serials.put(cid.getID(), new ClassSerializer());
    cid = this.registerClassID(Path.class);
    serials.put(cid.getID(), new PathSerializer());
  }
  
  
  public static IronbitConfiguration get() {
    return instance;
  }
  
  
  private int nextID() {
    return curid.getAndIncrement();
  }
  
  
  public ClassID registerClassID(Class cls) {
    NotNull.of(cls).failIfNull("Bad null Class");
    Optional<ClassID> opt = this.findClassID(cls);
    if(opt.isPresent()) return opt.get();
    ClassID cid = ClassID.of(nextID(), cls);
    classes.put(cid);
    return cid;
  }
  
  
  public ClassID registerClassID(String cls) {
    NotNull.of(cls).failIfNull("Bad null Class name");
    Optional<ClassID> opt = this.findClassID(cls);
    if(opt.isPresent()) return opt.get();
    ClassID cid = ClassID.of(nextID(), cls);
    classes.put(cid);
    return cid;
  }
  
  
  public ClassID registerSerializer(Class cls, Serializer serial) {
    ClassID cid = this.registerClassID(NotNull.of(cls).getOrFail("Bad null Class"));
    serials.put(cid.getID(), NotNull.of(serial).getOrFail("Bad null Serializer"));
    return cid;
  }
  
  
  public Optional<Serializer> findSerializer(Class cls) {
    Optional<ClassID> opt = findClassID(cls);
    Optional<Serializer> ser = Optional.empty();
    if(opt.isPresent()) {
      ser = Optional.of(serials.get(opt.get().getID()));
    }
    return ser;
  }
  
  
  public Map<Integer,Serializer> serializers() {
    return serials;
  }
  
  
  public boolean containsClassID(Class cls) {
    return containsClassID(NotNull.of(cls).getOrFail("Bad null Class").getName());
  }
  
  
  public boolean containsClassID(String cls) {
    NotNull.of(cls).failIfNull("Bad null Class");
    return classes.parallelStream()
        .anyMatch(c->c.getClassName().equals(cls));
  }
  
  
  public boolean containsClassID(int id) {
    return classes.parallelStream()
        .anyMatch(c->c.getID() == id);
  }
  
  
  public Optional<ClassID> findClassID(Class cls) {
    return findClassID(NotNull.of(cls).getOrFail("Bad null Class").getName());
  }
  
  
  public Optional<ClassID> findClassID(String cls) {
    NotNull.of(cls).failIfNull("Bad null Class name");
    return classes.parallelStream()
        .filter(c->c.getClassName().equals(cls)).findAny();
  }
  
  
  public Optional<ClassID> findClassID(int id) {
    return Optional.ofNullable(classes.get(id));
  }
  
}
