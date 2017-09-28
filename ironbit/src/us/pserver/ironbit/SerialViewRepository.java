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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.tools.ConcurrentList;
import us.pserver.tools.NotNull;
import us.pserver.tools.SortedList;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/09/2017
 */
public abstract class SerialViewRepository {

  private static final SerialViewRepository instance = createInstance();
  
  
  private static SerialViewRepository createInstance() {
    return new SerialViewRepository() {};
  }
  
  
  public static SerialViewRepository repository() {
    return instance;
  }
  
  
  private final ConcurrentList<SerialView<?>> views;
  
  
  private SerialViewRepository() {
    this.views = new ConcurrentList<>();
  }
  
  
  public ClassID register(Class cls, SerialView<?> view) {
    NotNull.of(cls).failIfNull("Bad null Class");
    NotNull.of(view).failIfNull("Bad null SerialView");
    if(contains(cls)) return find(cls).get();
    ClassID cid = ClassID.of(nextID(), cls);
    views.put(cid);
    return cid;
  }
  
  
  public ClassID register(String cls) {
    NotNull.of(cls).failIfNull("Bad null Class name");
    if(contains(cls)) return find(cls).get();
    ClassID cid = ClassID.of(nextID(), cls);
    views.put(cid);
    return cid;
  }
  
  
  public boolean contains(Class cls) {
    return contains(NotNull.of(cls).getOrFail("Bad null Class").getName());
  }
  
  
  public boolean contains(String cls) {
    NotNull.of(cls).failIfNull("Bad null Class");
    return views.parallelStream()
        .anyMatch(c->c.getClassName().equals(cls));
  }
  
  
  public boolean contains(int id) {
    return views.parallelStream()
        .anyMatch(c->c.getID() == id);
  }
  
  
  public Optional<ClassID> find(Class cls) {
    return find(NotNull.of(cls).getOrFail("Bad null Class").getName());
  }
  
  
  public Optional<ClassID> find(String cls) {
    NotNull.of(cls).failIfNull("Bad null Class name");
    return views.parallelStream()
        .filter(c->c.getClassName().equals(cls)).findAny();
  }
  
  
  public Optional<ClassID> find(int id) {
    return views.parallelStream()
        .filter(c->c.getID() == id).findAny();
  }
  
}
