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

package us.pserver.orb.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import us.pserver.orb.Orb;
import us.pserver.orb.OrbConfiguration;
import us.pserver.orb.OrbException;
import us.pserver.orb.OrbSource;
import us.pserver.orb.annotation.Annotations;
import us.pserver.orb.annotation.ClasspathSource;
import us.pserver.orb.annotation.EnvironmentSource;
import us.pserver.orb.annotation.FileSource;
import us.pserver.orb.annotation.SystemPropertySource;
import us.pserver.orb.annotation.UrlSource;
import us.pserver.orb.bind.MethodBind;
import us.pserver.orb.ds.DataSource;
import us.pserver.orb.invoke.MappedInvocable;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/02/2019
 */
public class OrbImpl implements Orb {
  
  private final OrbConfiguration config;
  
  public OrbImpl(OrbConfiguration config) {
    this.config = Objects.requireNonNull(config);
  }

  @Override
  public OrbConfiguration getConfiguration() {
    return config;
  }


  @Override
  public Object implementMultiple(Class... cls) {
    return implementMultiple(getConfigFor(cls[0]), cls);
  }


  @Override
  public <T> T implement(Class<T> cls) {
    return implement(getConfigFor(cls), cls);
  }
  
  
  private OrbConfiguration getConfigFor(Class cls) {
    OrbConfigurationBuilder ocb = new OrbConfigurationBuilder();
    ocb.setTypeStrings(config.getSupportedTypes());
    config.getMethodTransforms().forEach(t -> ocb.addMethodTransform(t));
    Optional<ClasspathSource> cso = Annotations.getAnnotation(ClasspathSource.class, cls);
    if(cso.isPresent()) {
      ocb.addOrbSource(OrbSource.builder().from(cso.get(), cls).build());
    }
    Optional<EnvironmentSource> eso = Annotations.getAnnotation(EnvironmentSource.class, cls);
    if(eso.isPresent()) {
      ocb.addOrbSource(OrbSource.builder().from(eso.get(), cls).build());
    }
    Optional<FileSource> fso = Annotations.getAnnotation(FileSource.class, cls);
    if(fso.isPresent()) {
      ocb.addOrbSource(OrbSource.builder().from(fso.get(), cls).build());
    }
    Optional<SystemPropertySource> sso = Annotations.getAnnotation(SystemPropertySource.class, cls);
    if(sso.isPresent()) {
      ocb.addOrbSource(OrbSource.builder().from(sso.get(), cls).build());
    }
    Optional<UrlSource> uso = Annotations.getAnnotation(UrlSource.class, cls);
    if(uso.isPresent()) {
      ocb.addOrbSource(OrbSource.builder().from(uso.get(), cls).build());
    }
    return ocb.build();
  }
  
  
  private <T> T implement(OrbConfiguration conf, Class<T> cls) throws OrbException {
    Map<String,String> map = new TreeMap<>();
    conf.getOrbSources()
        .stream()
        .sorted(Collections.reverseOrder(OrbSource::compareTo))
        .forEach(s -> map.putAll(s.parser().apply((DataSource) s.dataSource())));
    List<String> prefix = MethodBind.createNameList(Collections.EMPTY_LIST, cls);
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new MappedInvocable(prefix, map, conf));
  }


  private Object implementMultiple(OrbConfiguration conf, Class... cls) throws OrbException {
    Map<String,String> map = new TreeMap<>();
    conf.getOrbSources()
        .stream()
        .sorted(Collections.reverseOrder(OrbSource::compareTo))
        .forEach(s -> map.putAll(s.parser().apply((DataSource) s.dataSource())));
    return Proxy.newProxyInstance(cls[0].getClassLoader(), cls, new MappedInvocable(map, conf));
  }
  
}
