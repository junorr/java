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

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import us.pserver.orb.*;
import us.pserver.orb.annotation.Alias;
import us.pserver.orb.annotation.Annotations;
import us.pserver.orb.annotation.ClasspathSource;
import us.pserver.orb.annotation.EnvironmentSource;
import us.pserver.orb.annotation.FileSource;
import us.pserver.orb.annotation.SystemPropertySource;
import us.pserver.orb.annotation.UrlSource;
import us.pserver.orb.bind.MethodBind;
import us.pserver.orb.ds.DataSource;
import us.pserver.orb.parse.OrbParser;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/02/2019
 */
public class OrbSourceBuilder {
  
  private DataSource ds;
  
  private OrbParser parser;
  
  private MethodBind bind;
  
  private int priority = 0;
  
  
  public OrbSourceBuilder() { }

  public DataSource getDataSource() {
    return ds;
  }
  
  public OrbSourceBuilder setDataSource(DataSource ds) {
    this.ds = ds;
    return this;
  }
  
  public OrbParser getOrbParser() {
    return parser;
  }
  
  public OrbSourceBuilder setOrbParser(OrbParser parser) {
    this.parser = parser;
    return this;
  }
  
  public MethodBind getMethodBind() {
    return bind;
  }
  
  public OrbSourceBuilder setMethodBind(MethodBind bind) {
    this.bind = bind;
    return this;
  }
  
  public int getPriority() {
    return priority;
  }
  
  public OrbSourceBuilder setPriority(int priority) {
    this.priority = priority;
    return this;
  }
  
  private OrbParser createOrbParser(Class parser, Class cls) {
    if(!OrbParser.class.isAssignableFrom(parser)) {
      throw new OrbException("Bad OrbParser type: %s", parser);
    }
    Reflector ref = Reflector.of(parser);
    Optional<String> alias = Annotations.getAnnotationValue(Alias.class, cls);
    if(alias.isPresent() && ref.selectConstructor(String.class).isConstructorPresent()) {
      return (OrbParser) ref.create(alias.get());
    }
    else if(ref.selectConstructor().isConstructorPresent()) {
      return (OrbParser) ref.create();
    }
    else {
      throw new OrbException("No default constructor found for %s", parser);
    }
  }
  
  private MethodBind createMethodBind(Class cls) {
    if(!MethodBind.class.isAssignableFrom(cls)) {
      throw new OrbException("Bad MethodBind type: %s", cls);
    }
    Reflector ref = Reflector.of(cls);
    if(!ref.selectConstructor().isConstructorPresent()) {
      throw new OrbException("No default constructor found for %s", cls);
    }
    return (MethodBind) ref.create();
  }
  
  public OrbSourceBuilder from(ClasspathSource src, Class cls) throws OrbException {
    ds = () -> cls.getResourceAsStream(src.value());
    parser = createOrbParser(src.parser(), cls);
    bind = createMethodBind(src.methodBind());
    priority = src.priority();
    return this;
  }
  
  public OrbSourceBuilder from(EnvironmentSource src, Class cls) throws OrbException {
    ds = () -> System.getenv();
    parser = createOrbParser(src.parser(), cls);
    bind = createMethodBind(src.methodBind());
    priority = src.priority();
    return this;
  }
  
  public OrbSourceBuilder from(FileSource src, Class cls) throws OrbException {
    ds = () -> OrbException.compute(() -> 
        Files.newInputStream(Paths.get(src.value()), StandardOpenOption.READ)
    );
    parser = createOrbParser(src.parser(), cls);
    bind = createMethodBind(src.methodBind());
    priority = src.priority();
    return this;
  }
  
  public OrbSourceBuilder from(SystemPropertySource src, Class cls) throws OrbException {
    ds = () -> System.getProperties();
    parser = createOrbParser(src.parser(), cls);
    bind = createMethodBind(src.methodBind());
    priority = src.priority();
    return this;
  }
  
  public OrbSourceBuilder from(UrlSource src, Class cls) throws OrbException {
    ds = () -> OrbException.compute(() -> new URL(src.value()).openStream());
    parser = createOrbParser(src.parser(), cls);
    bind = createMethodBind(src.methodBind());
    priority = src.priority();
    return this;
  }
  
  public <T> OrbSource<T> build() {
    return new OrbSourceImpl(ds, parser, bind, priority);
  }
  
}
