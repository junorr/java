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

package us.pserver.micron.config.proxy;

import io.helidon.config.Config;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.config.proxy.CacheConfigHandler;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public class IgniteConfigHandler implements InvocationHandler, IgniteConfig {
  
  private final Config cfg;
  
  public IgniteConfigHandler(Config cfg) {
    this.cfg = Match.notNull(cfg).getOrFail("Bad null Config");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    switch(method.getName()) {
      case "getAddress":
        return getAddress();
      case "getPort":
        return getPort();
      case "getStorage":
        return getStorage();
      case "getJoinTimeout":
        return getJoinTimeout();
      case "getCacheConfigSet":
        return getCacheConfigSet();
      case "hashCode":
        return hashCode();
      case "equals":
        return equals(args[0]);
      case "toString":
        return toString();
      default:
        throw new UnsupportedOperationException("Unknown method: " + getMethodString(method, args));
    }
  }
    
    
  private String getMethodString(Method meth, Object[] args) {
    StringBuilder msg = new StringBuilder(meth.getName())
        .append("( ");
    if(args == null || args.length < 1) {
      return msg.append(")").toString();
    }
    for(int i = 0; i < args.length; i++) {
      msg.append(args[i].getClass().getSimpleName()).append(", ");
    }
    return msg.delete(msg.length() - 2, msg.length()).append(" )").toString();
  }
  
  
  @Override
  public String getAddress() {
    return cfg.get("address").asString().orElse("localhost");
  }
  
  @Override
  public int getPort() {
    return cfg.get("port").asInt().orElse(0);
  }
  
  
  @Override
  public Optional<Path> getStorage() {
    return cfg.get("storage").as(Path.class).asOptional();
  }
  
  
  @Override
  public long getJoinTimeout() {
    return cfg.get("join_timeout").asLong().orElse(0L);
  }
  
  
  @Override
  public Set<IgniteConfig.CacheConfig> getCacheConfigSet() {
    List<Config> lst = cfg.get("caches").asNodeList().orElse(Collections.EMPTY_LIST);
    if(lst.isEmpty()) return Collections.EMPTY_SET;
    Set<IgniteConfig.CacheConfig> set = new HashSet();
    for(Config c : lst) {
      set.add((IgniteConfig.CacheConfig) Proxy.newProxyInstance(
          IgniteConfig.class.getClassLoader(), 
          new Class[]{IgniteConfig.CacheConfig.class}, 
          new CacheConfigHandler(c))
      );
    }
    return set;
  }
  

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + Objects.hashCode(getAddress());
    hash = 47 * hash + Objects.hashCode(getPort());
    hash = 47 * hash + Objects.hashCode(getJoinTimeout());
    hash = 47 * hash + Objects.hashCode(getStorage());
    hash = 47 * hash + Objects.hashCode(getCacheConfigSet());
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
    if (!IgniteConfig.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final IgniteConfig other = (IgniteConfig) obj;
    if (!Objects.equals(this.getAddress(), other.getAddress())) {
      return false;
    }
    if (!Objects.equals(this.getPort(), other.getPort())) {
      return false;
    }
    if (!Objects.equals(this.getJoinTimeout(), other.getJoinTimeout())) {
      return false;
    }
    if (!Objects.equals(this.getStorage(), other.getStorage())) {
      return false;
    }
    return Objects.equals(this.getCacheConfigSet(), other.getCacheConfigSet());
  }
  
  @Override
  public String toString() {
    return "IgniteConfig{ address=" + getAddress() + ", port=" + getPort() + ", joinTimeout=" + getJoinTimeout() + ", storage=" + getStorage() + ", caches=" + getCacheConfigSet() + " }";
  }
  
}
