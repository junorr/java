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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import us.pserver.orb.OrbConfiguration;
import us.pserver.orb.OrbSource;
import us.pserver.orb.TypeStrings;
import us.pserver.orb.invoke.DefaultMethodTransform;
import us.pserver.orb.invoke.EnumStringTransform;
import us.pserver.orb.invoke.MethodTransform;
import us.pserver.orb.invoke.ProxyReturnTransform;
import us.pserver.orb.invoke.TypeStringTransform;
import us.pserver.orb.types.TypeString;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/02/2019
 */
public class OrbConfigurationBuilder {
    
  private TypeStrings types;

  private List<OrbSource<?>> srcs;

  private List<MethodTransform<?>> trans;

  public OrbConfigurationBuilder() {
    types = new TypeStrings();
    srcs = new ArrayList<>();
    trans = new ArrayList<>();
    initMethodTransforms();
  }
  
  private void initMethodTransforms() {
    trans.add(new DefaultMethodTransform());
    trans.add(new ProxyReturnTransform());
    trans.add(new EnumStringTransform());
    trans.add(new TypeStringTransform(types));
  }
  
  public TypeStrings getTypeStrings() {
    return types;
  }
  
  public OrbConfigurationBuilder setTypeStrings(TypeStrings types) {
    this.types = types;
    return this;
  }

  public OrbConfigurationBuilder addTypeString(Class cls, TypeString type) {
    if(type != null) {
      types.put(cls, type);
    }
    return this;
  }

  public List<OrbSource<?>> getOrbSources() {
    return srcs;
  }

  public OrbConfigurationBuilder addOrbSource(OrbSource<?> src) {
    if(src != null) {
      srcs.add(src);
    }
    return this;
  }

  public List<MethodTransform<?>> getMethodTransforms() {
    return trans;
  }

  public OrbConfigurationBuilder addMethodTransform(MethodTransform<?> tran) {
    if(tran != null) {
      trans.add(tran);
    }
    return this;
  }

  public OrbConfiguration build() {
    return new OrbConfigurationImpl(types, srcs, trans);
  }

}
