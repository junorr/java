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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import us.pserver.dyna.ResourceLoader;
import us.pserver.ironbit.serial.BooleanSerialService;
import us.pserver.ironbit.serial.CharSerialService;
import us.pserver.ironbit.serial.ClassSerialService;
import us.pserver.ironbit.serial.DateSerialService;
import us.pserver.ironbit.serial.DoubleSerialService;
import us.pserver.ironbit.serial.FloatSerialService;
import us.pserver.ironbit.serial.InstantSerialService;
import us.pserver.ironbit.serial.IntegerSerialService;
import us.pserver.ironbit.serial.ListSerialService;
import us.pserver.ironbit.serial.LocalDateTimeSerialService;
import us.pserver.ironbit.serial.LongSerialService;
import us.pserver.ironbit.serial.PathSerialService;
import us.pserver.ironbit.serial.ShortSerialService;
import us.pserver.ironbit.serial.StringSerialService;
import us.pserver.ironbit.serial.ZonedDateTimeSerialService;
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
  
  private final Map<ClassID,SerialService> serials;
  
  private final AtomicReference<ResourceLoader> refload;
  
  
  private IronbitConfiguration() {
    this.classes = new SortedList<>(new ConcurrentList<>(), ClassID.idComparator());
    this.serials = Collections.synchronizedMap(new TreeMap<>());
    this.curid = new AtomicInteger(0);
    this.refload = new AtomicReference(ResourceLoader.caller());
    this.registerDefaults();
  }
  
  
  private void registerDefaults() {
    ClassID cid = this.registerClassID(int.class);
    SerialService ser = new IntegerSerialService();
    serials.put(cid, ser);
    cid = this.registerClassID(Integer.class);
    serials.put(cid, ser);
    ser = new LongSerialService();
    cid = this.registerClassID(Long.class);
    serials.put(cid, ser);
    cid = this.registerClassID(long.class);
    serials.put(cid, ser);
    ser = new FloatSerialService();
    cid = this.registerClassID(Float.class);
    serials.put(cid, ser);
    cid = this.registerClassID(float.class);
    serials.put(cid, ser);
    ser = new DoubleSerialService();
    cid = this.registerClassID(Double.class);
    serials.put(cid, ser);
    cid = this.registerClassID(double.class);
    serials.put(cid, ser);
    ser = new ShortSerialService();
    cid = this.registerClassID(Short.class);
    serials.put(cid, ser);
    cid = this.registerClassID(short.class);
    serials.put(cid, ser);
    ser = new BooleanSerialService();
    cid = this.registerClassID(Boolean.class);
    serials.put(cid, ser);
    cid = this.registerClassID(boolean.class);
    serials.put(cid, ser);
    ser = new CharSerialService();
    cid = this.registerClassID(Character.class);
    serials.put(cid, ser);
    cid = this.registerClassID(char.class);
    serials.put(cid, ser);
    cid = this.registerClassID(String.class);
    serials.put(cid, new StringSerialService());
    cid = this.registerClassID(Date.class);
    serials.put(cid, new DateSerialService());
    cid = this.registerClassID(LocalDateTime.class);
    serials.put(cid, new LocalDateTimeSerialService());
    cid = this.registerClassID(ZonedDateTime.class);
    serials.put(cid, new ZonedDateTimeSerialService());
    cid = this.registerClassID(Instant.class);
    serials.put(cid, new InstantSerialService());
    cid = this.registerClassID(Class.class);
    serials.put(cid, new ClassSerialService());
    cid = this.registerClassID(Path.class);
    serials.put(cid, new PathSerialService());
    cid = this.registerClassID(List.class);
    serials.put(cid, new ListSerialService());
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
  
  
  public ClassID registerSerialService(ClassID cid, SerialService serial) {
    serials.put(cid, NotNull.of(serial).getOrFail("Bad null Serializer"));
    return cid;
  }
  
  
  public IronbitConfiguration setResourceLoader(ResourceLoader ldr) {
    refload.set(NotNull.of(ldr).getOrFail("Bad null ResourceLoader"));
    return this;
  }
  
  
  public ResourceLoader loader() {
    return refload.get();
  }
  
  
  public Class loadClass(ClassID cid) {
    return IronbitException.rethrow(()->
        refload.get().loadClass(cid.getClassName())
    );
  }
  
  
  public <T> Optional<SerialService<T>> findSerialService(Class cls) {
    Optional<ClassID> opt = serials.keySet()
        .parallelStream()
        .filter(c->c.getClazz().isAssignableFrom(cls))
        .findAny();
    return Optional.ofNullable(opt.isPresent() ? serials.get(opt.get()) : null);
  }
  
  
  public <T> Optional<SerialService<T>> findSerialService(ClassID cls) {
    Optional<ClassID> opt = serials.keySet()
        .parallelStream()
        .filter(c->c.getClazz().isAssignableFrom(cls.getClazz()))
        .findAny();
    return Optional.ofNullable(opt.isPresent() ? serials.get(opt.get()) : null);
  }
  
  
  public Map<ClassID,SerialService> serialServices() {
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
