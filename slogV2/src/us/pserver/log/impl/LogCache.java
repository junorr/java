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

package us.pserver.log.impl;

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
 * @version 1.0 - 26/06/2015
 */
public class LogCache {

  private final Map<String, Log> cache;
  
  
  public LogCache() {
    cache = Collections.synchronizedMap(
        new HashMap<String, Log>());
  }
  
  
  public Map<String, Log> cacheMap() {
    return cache;
  }
  
  
  public boolean isEmpty() {
    return cache.isEmpty();
  }
  
  
  public int size() {
    return cache.size();
  }
  
  
  public LogCache put(String name, Log log) {
    if(name != null && log != null) {
      cache.put(name, log);
    }
    return this;
  }
  
  
  public Log get(String name) {
    if(name == null) return null;
    return cache.get(name);
  }
  
  
  public Log remove(String name) {
    if(name == null) return null;
    return cache.remove(name);
  }
  
  
  public boolean contains(String name) {
    if(name == null) return false;
    return cache.containsKey(name);
  }
  
  
  public Log getWithSamePrefix(String name) {
    if(name == null) return null;
    Optional<Map.Entry<String, Log>> opt = 
        cache.entrySet().stream().filter(
            e->e.getKey().startsWith(name) 
                || name.startsWith(e.getKey()))
            .findFirst();
    return (opt.isPresent() ? opt.get().getValue() : null);
  }
  
  
  public Log removeWithSamePrefix(String name) {
    if(name == null) return null;
    Optional<String> opt = cache.keySet().stream().filter(
        s->s.startsWith(name) || name.startsWith(s)).findFirst();
    if(!opt.isPresent()) return null;
    return cache.remove(opt.get());
  }
  
  
  public Collection<Log> removeAllWithSamePrefix(String name) {
    if(name == null) return Collections.EMPTY_LIST;
    List<Log> ls = new LinkedList<>();
    cache.entrySet().stream().filter(
        e->e.getKey().startsWith(name) || name.startsWith(e.getKey()))
        .forEach(e->ls.add(e.getValue()));
    return ls;
  }
  
  
  public boolean containsWithSamePrefix(String name) {
    if(name == null) return false;
    return cache.keySet().stream().filter(
        s->s.startsWith(name) || name.startsWith(s))
        .findFirst().isPresent();
  }
  
  
  public Map<String, LogOutput> getOutputsFor(String name) {
    if(!contains(name)) return Collections.EMPTY_MAP;
    return get(name).outputsMap();
  }
  
  
  public Map<String, LogOutput> getOutputsForSamePrefix(String name) {
    if(!containsWithSamePrefix(name)) return Collections.EMPTY_MAP;
    return getWithSamePrefix(name).outputsMap();
  }
  
  
  public Collection<String> getAllNames() {
    if(cache.isEmpty()) return Collections.EMPTY_LIST;
    return Collections.unmodifiableCollection(cache.keySet());
  }
  
  
  public Collection<Log> getAll() {
    if(cache.isEmpty()) return Collections.EMPTY_LIST;
    return Collections.unmodifiableCollection(cache.values());
  }
  
  
  public Log copyOutputsFor(String name, Log log) {
    if(!contains(name) || log == null) 
      return log;
    Log orig = get(name);
    log.clearOutputs();
    orig.outputsMap().entrySet().forEach(
        e->log.put(e.getKey(), e.getValue()));
    return log;
  }
  
  
  public Log copyOutputsForSamePrefix(String name, Log log) {
    if(!containsWithSamePrefix(name) || log == null) 
      return log;
    Log orig = getWithSamePrefix(name);
    log.clearOutputs();
    orig.outputsMap().entrySet().forEach(
        e->log.put(e.getKey(), e.getValue()));
    return log;
  }
  
}
