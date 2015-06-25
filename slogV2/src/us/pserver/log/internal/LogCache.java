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

package us.pserver.log.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import us.pserver.log.Log;
import us.pserver.log.output.LogOutput;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/06/2015
 */
public class LogCache {

  private final Map<String, Log> cache;
  
  
  public LogCache() {
    cache = Collections.synchronizedMap(
        new HashMap<String, Log>());
  }
  
  
  public LogCache put(String name, Log log) {
    if(name != null && log != null) {
      cache.put(name, log);
    }
    return this;
  }
  
  
  public boolean contains(String name) {
    return cache.containsKey(name);
  }
  
  
  public boolean containsWithSamePrefix(String name) {
    return getAllNames().stream().filter(
        s->name.startsWith(s) || s.startsWith(name))
        .findAny().isPresent();
  }
  
  
  public Map<String, Log> cacheMap() {
    return Collections.unmodifiableMap(cache);
  }
  
  
  public Log get(String name) {
    return cache.get(name);
  }
  
  
  public boolean isEmpty() {
    return cache.isEmpty();
  }
  
  
  public int size() {
    return cache.size();
  }
  
  
  public Log getWithSamePrefix(String name) {
    Optional<String> opt = getAllNames().stream().filter(
        s->name.startsWith(s) || s.startsWith(name)).findFirst();
    return (opt.isPresent() ? cache.get(opt.get()) : null);
  }
  
  
  public Map<String, LogOutput> getOutputsFor(String name) {
    if(!contains(name)) return Collections.EMPTY_MAP;
    return cache.get(name).outputsMap();
  }
  
  
  public Map<String, LogOutput> getOutputsForSamePrefix(String name) {
    if(!containsWithSamePrefix(name)) return Collections.EMPTY_MAP;
    return getWithSamePrefix(name).outputsMap();
  }
  
  
  public Log copyOutputsFor(String name, Log log) {
    if(name == null || !contains(name) || log == null)
      return log;
    Log elog = get(name);
    log.outputsMap().putAll(elog.outputsMap());
    return log;
  }
  
  
  public Log copyOutputsForSamePrefix(String name, Log log) {
    if(name == null || !containsWithSamePrefix(name) || log == null)
      return log;
    Log elog = getWithSamePrefix(name);
    log.outputsMap().putAll(elog.outputsMap());
    return log;
  }
  
  
  public Log remove(String name) {
    return cache.remove(name);
  }
  
  
  public Log removeWithSamePrefix(String name) {
    if(name == null || !containsWithSamePrefix(name))
      return null;
    Optional<String> opt = cache.keySet().stream().filter(
        s->name.startsWith(s) || s.startsWith(name)).findFirst();
    return (opt.isPresent() ? cache.remove(opt.get()) : null);
  }
  
  
  public List<Log> removeAllWithSamePrefix(String name) {
    if(name == null || !containsWithSamePrefix(name))
      return null;
    List<String> nameList = new LinkedList<>();
    List<Log> logList = new LinkedList<>();
    cache.keySet().stream().filter(
        s->name.startsWith(s) || s.startsWith(name))
        .forEach(nameList::add);
    nameList.forEach(s->logList.add(cache.remove(s)));
    return logList;
  }
  
  
  public Collection<Log> getAllLogs() {
    if(cache.isEmpty())
      return Collections.EMPTY_LIST;
    return Collections.unmodifiableCollection(cache.values());
  }
  
  
  public Collection<String> getAllNames() {
    if(cache.isEmpty())
      return Collections.EMPTY_LIST;
    return Collections.unmodifiableCollection(cache.keySet());
  }
  
}
