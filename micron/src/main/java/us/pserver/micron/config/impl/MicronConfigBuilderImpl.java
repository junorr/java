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

package us.pserver.micron.config.impl;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.config.IgniteConfig.IgniteConfigBuilder;
import us.pserver.micron.config.MicronConfig;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.config.SecurityConfig.SecurityConfigBuilder;
import us.pserver.micron.config.ServerConfig;
import us.pserver.micron.config.proxy.IgniteConfigHandler;
import us.pserver.micron.config.proxy.SecurityConfigHandler;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class MicronConfigBuilderImpl implements MicronConfig.MicronConfigBuilder {
  
  private ServerConfig server;
  
  private SecurityConfig security;
  
  private IgniteConfig ignite;
  
  
  public MicronConfigBuilderImpl() {
    this.server = null;
    this.security = null;
    this.ignite = null;
  }
  

  @Override
  public ServerConfig getServerConfig() {
    return server;
  }


  @Override
  public SecurityConfig getSecurityConfig() {
    return security;
  }


  @Override
  public IgniteConfig getIgniteConfig() {
    return ignite;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 67 * hash + Objects.hashCode(this.server);
    hash = 67 * hash + Objects.hashCode(this.security);
    hash = 67 * hash + Objects.hashCode(this.ignite);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final MicronConfigBuilderImpl other = (MicronConfigBuilderImpl) obj;
    if(!Objects.equals(this.server, other.server)) {
      return false;
    }
    if(!Objects.equals(this.security, other.security)) {
      return false;
    }
    if(!Objects.equals(this.ignite, other.ignite)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "MicronConfig{" + "server=" + server + ", security=" + security + ", ignite=" + ignite + '}';
  }


  @Override
  public MicronConfig.MicronConfigBuilder setServerConfig(String addr, int port) {
    this.server = new ServerConfigImpl(addr, port);
    return this;
  }


  @Override
  public MicronConfig.MicronConfigBuilder setServerConfig(ServerConfig cfg) {
    this.server = cfg;
    return this;
  }


  @Override
  public MicronConfig.MicronConfigBuilder setSecurityConfig(SecurityConfig cfg) {
    this.security = cfg;
    return this;
  }


  @Override
  public MicronConfig.MicronConfigBuilder setIgniteConfig(IgniteConfig cfg) {
    this.ignite = cfg;
    return this;
  }


  @Override
  public MicronConfig build() {
    return new MicronConfigImpl(server, security, ignite);
  }


  private MicronConfig buildFromAppYaml(Config config) {
    if(config.get("server.address").hasValue()
        && config.get("server.port").hasValue()) {
      this.setServerConfig(
          config.get("server.address").asString().get(), 
          config.get("server.port").asInt().get()
      );
    }
    this.setIgniteConfig((IgniteConfig) Proxy.newProxyInstance(
        IgniteConfig.class.getClassLoader(), 
        new Class[]{IgniteConfig.class}, 
        new IgniteConfigHandler(config.get("ignite")))
    );
    this.setSecurityConfig((SecurityConfig) Proxy.newProxyInstance(
        SecurityConfig.class.getClassLoader(), 
        new Class[]{SecurityConfig.class}, 
        new SecurityConfigHandler(config.get("security")))
    );
    return build();
  }
  
  
  @Override
  public MicronConfig buildFromAppYaml() {
    return buildFromAppYaml(Config.builder()
        .addMapper(Instant.class, this::toInstant)
        .addMapper(LocalDate.class, this::toLocalDate)
        .build());
  }


  @Override
  public MicronConfig buildFromAppYaml(Path appYaml) {
    return buildFromAppYaml(Config.builder()
        .sources(ConfigSources.file(appYaml.toString()).build())
        .addMapper(Instant.class, this::toInstant)
        .addMapper(LocalDate.class, this::toLocalDate)
        .build());
  }


  private LocalDate toLocalDate(Config c) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern( "EEE MMM d HH:mm:ss zzz uuuu" , Locale.US);
    ZonedDateTime zdt = ZonedDateTime.parse(c.asString().get(), fmt);
    return LocalDateTime.ofInstant(zdt.toInstant(), ZoneId.of("GMT")).toLocalDate();
  }
  
  
  private Instant toInstant(Config c) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern( "EEE MMM d HH:mm:ss zzz uuuu" , Locale.US);
    ZonedDateTime zdt = ZonedDateTime.parse(c.asString().get(), fmt);
    return zdt.toInstant();
  }
  
  
  @Override
  public SecurityConfig.SecurityConfigBuilder buildSecurityConfig() {
    SecurityConfigBuilder bld = new SecurityConfigBuilderImpl(this);
    if(security != null) {
      bld.addGroups(security.getGroups())
          .addResources(security.getResources())
          .addRoles(security.getRoles())
          .addUsers(security.getUsers());
    }
    return bld;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder buildIgniteConfig() {
    IgniteConfigBuilder bld = new IgniteConfigBuilderImpl(this);
    if(ignite != null) {
      bld.addAll(ignite.getCacheConfigSet())
          .setIgniteServerConfig(ignite.getIgniteServerConfig())
          .setJoinTimeout(ignite.getJoinTimeout())
          .setStorage(ignite.getStorage());
    }
    return bld;
  }
  
}
