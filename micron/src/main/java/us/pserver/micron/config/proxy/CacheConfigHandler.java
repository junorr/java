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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.cache.expiry.TouchedExpiryPolicy;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import us.pserver.micron.config.CacheConfig;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.ACCESSED;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.CREATED;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.ETERNAL;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.MODIFIED;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.TOUCHED;
import us.pserver.micron.security.Group;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public class CacheConfigHandler implements InvocationHandler, CacheConfig {
  
  private final Config cfg;
  
  public CacheConfigHandler(Config cfg) {
    this.cfg = Match.notNull(cfg).getOrFail("Bad null Config");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    switch(method.getName()) {
      case "getName":
        return getName();
      case "getBackups":
        return getBackups();
      case "getCacheMode":
        return getCacheMode();
      case "getCacheRebalanceMode":
        return getCacheRebalanceMode();
      case "getCacheExpiryPolicy":
        return getCacheExpiryPolicy();
      case "getExpiryDuration":
        return getExpiryDuration();
      case "getExpiryPolicyFactory":
        return getExpiryPolicyFactory();
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
  public String getName() {
    return cfg.get("name").asString().get();
  }
  
  
  @Override
  public int getBackups() {
    return cfg.get("backups").asInt().orElse(0);
  }
  
  
  @Override
  public CacheMode getCacheMode() {
    return CacheMode.valueOf(cfg.get("mode").asString().get());
  }
  
  
  @Override
  public CacheRebalanceMode getCacheRebalanceMode() {
    return CacheRebalanceMode.valueOf(cfg.get("rebalance").asString().get());
  }
  
  
  @Override
  public Optional<CacheExpiryPolicy> getCacheExpiryPolicy() {
    return cfg.get("expiry_policy").asString().map(CacheExpiryPolicy::valueOf);
  }
  
  
  @Override
  public int getExpiryDuration() {
    return cfg.get("expiry_duration").asInt().orElse(0);
  }
  
  
  @Override
  public Optional<javax.cache.configuration.Factory<ExpiryPolicy>> getExpiryPolicyFactory() {
    Optional<CacheExpiryPolicy> opt = getCacheExpiryPolicy();
    if(!opt.isPresent()) return Optional.empty();
    switch(opt.get()) {
      case ACCESSED:
        return Optional.of(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, getExpiryDuration())));
      case CREATED:
        return Optional.of(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, getExpiryDuration())));
      case MODIFIED:
        return Optional.of(ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, getExpiryDuration())));
      case TOUCHED:
        return Optional.of(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, getExpiryDuration())));
      case ETERNAL:
        return Optional.of(EternalExpiryPolicy.factoryOf());
      default:
        return Optional.empty();
    }
  }
  
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + Objects.hashCode(this.getName());
    hash = 47 * hash + Objects.hashCode(this.getCacheMode());
    hash = 47 * hash + Objects.hashCode(this.getExpiryDuration());
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
    if (!Group.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final CacheConfig other = (CacheConfig) obj;
    if (!Objects.equals(this.getName(), other.getName())) {
      return false;
    }
    if (!Objects.equals(this.getCacheMode(), other.getCacheMode())) {
      return false;
    }
    return Objects.equals(this.getExpiryDuration(), other.getExpiryDuration());
  }
  
  @Override
  public String toString() {
    return "CacheConfig{ name=" + getName() + ", mode=" + getCacheMode() + ", backups=" + getBackups() + ", expiryPolicy=" + getCacheExpiryPolicy() + ", expiryDuration=" + getExpiryDuration() + " }";
  }
  
}
