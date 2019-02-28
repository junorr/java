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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import us.pserver.orb.OrbConfiguration;
import us.pserver.orb.OrbSource;
import us.pserver.orb.TypeStrings;
import us.pserver.orb.invoke.MethodTransform;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/02/2019
 */
public class OrbConfigurationImpl implements OrbConfiguration {
  
  private final TypeStrings types;
  
  private final List<OrbSource<?>> srcs;
  
  private final List<MethodTransform<?>> trans;
  
  
  public OrbConfigurationImpl(TypeStrings types, Collection<OrbSource<?>> srcs, Collection<MethodTransform<?>> trans) {
    this.types = Objects.requireNonNull(types);
    this.srcs = Collections.unmodifiableList(new ArrayList<>(srcs));
    this.trans = Collections.unmodifiableList(new ArrayList<>(trans));
  }
  
  
  @Override
  public TypeStrings getSupportedTypes() {
    return types;
  }


  @Override
  public List<OrbSource<?>> getOrbSources() {
    return srcs;
  }


  @Override
  public List<MethodTransform<?>> getMethodTransforms() {
    return trans;
  }
  
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 67 * hash + Objects.hashCode(this.types);
    hash = 67 * hash + Objects.hashCode(this.srcs);
    hash = 67 * hash + Objects.hashCode(this.trans);
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
    if (getClass() != obj.getClass()) {
      return false;
    }
    final OrbConfiguration other = (OrbConfiguration) obj;
    if (!Objects.equals(this.types, other.getSupportedTypes())) {
      return false;
    }
    if (!Objects.equals(this.srcs, other.getOrbSources())) {
      return false;
    }
    return Objects.equals(this.trans, other.getMethodTransforms());
  }


  @Override
  public String toString() {
    return "OrbConfiguration{" + "types=" + types + ", srcs=" + srcs + ", trans=" + trans + '}';
  }

}
